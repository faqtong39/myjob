package myjob.executor.sample.springboot.job;

/**
 * Base Job
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
public interface BaseJob {
    Object execute(Object params);
}
