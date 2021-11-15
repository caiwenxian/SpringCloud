package com.wenxianm.config;

import com.wenxianm.utils.ThreadMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;


@Configuration
@Slf4j
@EnableAsync
public class ThreadPoolConfiguration {

    /**
     * 已加优雅重启
     * <p>
     * 初始化线程池
     *
     * @return
     */
    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return new ThreadPoolExecutor(2, 2, 0,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy()) {

            @Override
            public void execute(Runnable task) {
                super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
            }

            @Override
            public <T> Future<T> submit(Runnable task, T result) {
                return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()), result);
            }

            @Override
            public <T> Future<T> submit(Callable<T> task) {
                return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
            }

            @Override
            public Future<?> submit(Runnable task) {
                return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
            }


            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                if (t == null && r instanceof Future<?>) {
                    Future<?> future = (Future<?>) r;
                    if (future.isDone()) {
                        try {
                            future.get();
                        } catch (InterruptedException e) {
                            log.error("ThreadPoolExecuteError", e);
                        } catch (ExecutionException e) {
                            log.error("ThreadPoolExecuteError", e);
                        }

                    }
                }
                if (t != null) {
                    log.error("threadPooExecutor execute error, message: {}, {}", t.getMessage(), t);
                }
            }
        };
    }

}
