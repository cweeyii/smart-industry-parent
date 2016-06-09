package com.cweeyii.web;

import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.quartz.framework.vo.ArgsJobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
@Controller
@RequestMapping(value = "/startJob")
public class StartJobController {
    @Resource
    private List<AbstractQuartzJob> jobs;

    private static final Set<String> runnings = new HashSet<String>();

    private final static Logger logger = LoggerFactory.getLogger(StartJobController.class);

    @RequestMapping(value = "")
    public
    @ResponseBody
    String startJob(final String jobName, final String params) {

        for (final AbstractQuartzJob job : jobs) {
            if (job.getBeanName().toLowerCase().equals(jobName.toLowerCase())) {
                synchronized (this) {
                    if (runnings.contains(jobName)) {
                        logger.info("task is running");
                        return "task is running";
                    }
                    runnings.add(jobName);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            logger.info(Thread.currentThread() + " jobName : " + jobName + "开始运行");
                            try {
                                Map<String,String> args = new HashMap<>();
                                args.put("param", params);
                                ArgsJobExecutionContext ctx = new ArgsJobExecutionContext(args);
                                job.execute(ctx);
                            } catch (JobExecutionException e) {
                                logger.error("运行任务出错", e);
                            } finally {
                                runnings.remove(jobName);
                            }
                        }
                    }).start();
                    return "task/begin";
                }
            }

        }
        return "task fail......";
    }
}
