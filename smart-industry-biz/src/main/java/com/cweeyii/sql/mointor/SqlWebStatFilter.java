package com.cweeyii.sql.mointor;

import com.alibaba.druid.filter.stat.StatFilterContext;
import com.alibaba.druid.filter.stat.StatFilterContextListener;
import com.alibaba.druid.filter.stat.StatFilterContextListenerAdapter;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.http.stat.WebAppStat;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.http.stat.WebRequestStat;
import com.alibaba.druid.support.http.stat.WebURIStatValue;
import com.alibaba.druid.support.profile.ProfileEntryStatValue;
import com.alibaba.druid.util.DruidWebUtils;
import com.alibaba.druid.util.PatternMatcher;
import com.alibaba.druid.util.ServletPathMatcher;
import com.cweeyii.sql.mointor.vo.SQLStatVo;
import com.cweeyii.thread.local.ThreadCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by cweeyii on 1/7/16 ${EMAIL}.
 */
public class SqlWebStatFilter implements Filter {
    private static Logger LOGGER = LoggerFactory.getLogger(SqlWebStatFilter.class);
    public static final String PARAM_NAME_EXCLUSIONS = "exclusions";
    private Set<String> excludesPattern;
    private String contextPath;
    private PatternMatcher pathMatcher = new ServletPathMatcher();
    private static final String currentSignature = "URLSignature";

    private SQLContextListener<String> contextListener = new SQLWebFilterContextListener();

    @PostConstruct
    private void monitorThread() {
        new Thread(new MonitorThread<>(contextListener)).start();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

        String exclusions = config.getInitParameter(PARAM_NAME_EXCLUSIONS);
        if (exclusions != null && exclusions.trim().length() != 0) {
            excludesPattern = new HashSet<>(Arrays.asList(exclusions.split("\\s*,\\s*")));
        }
        this.contextPath = DruidWebUtils.getContextPath(config.getServletContext());
        StatFilterContext.getInstance().addContextListener(contextListener);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = getRequestURI(httpRequest);
        if (!isExclusion(requestURI)) {
            if (ThreadCache.getObject(currentSignature) == null) {
                ThreadCache.setLocalObject(currentSignature, requestURI);
            }
        }

        filterChain.doFilter(httpRequest, httpResponse);
    }

    private String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    private boolean isExclusion(String requestURI) {
        if (excludesPattern == null) {
            return false;
        }

        if (contextPath != null && requestURI.startsWith(contextPath)) {
            requestURI = requestURI.substring(contextPath.length());
            if (!requestURI.startsWith("/")) {
                requestURI = "/" + requestURI;
            }
        }

        for (String pattern : excludesPattern) {
            if (pathMatcher.matches(pattern, requestURI)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void destroy() {
        StatFilterContext.getInstance().removeContextListener(contextListener);
    }

    public class SQLWebFilterContextListener extends SQLContextListener<String> {

        @Override
        protected String getCurrentSignature() {
            return currentSignature;
        }

    }


}
