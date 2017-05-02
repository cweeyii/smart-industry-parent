package com.cweeyii.web.interceptor;

import com.cweeyii.thread.local.ThreadCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wenyi on 16/6/12.
 * Email:caowenyi@meituan.com
 */
public class DefaultInterceptor extends HandlerInterceptorAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadCache.setIp(request.getRemoteHost());
        ThreadCache.setUri(request.getRequestURL().toString());
        Long costTime = System.currentTimeMillis() - ThreadCache.getStartTime();
        ThreadCache.setCostTime(costTime);
        LOGGER.info("visitor=" + ThreadCache.getIp() + " visited_url=" + ThreadCache.getUri() + " cost_time=" + ThreadCache.getCostTime());
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        ThreadCache.setStartTime();
        return super.preHandle(request, response, handler);
    }
}
