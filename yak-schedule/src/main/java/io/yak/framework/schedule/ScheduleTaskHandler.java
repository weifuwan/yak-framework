package io.yak.framework.schedule;

import java.util.Map;

/**
 * 调度任务处理器。
 *
 * <p>作为业务系统接入调度框架的扩展点，每个任务处理器需要注册为
 * Spring Bean，任务定义中的 handler 字段对应 Bean 名称。</p>
 *
 * <p>实现类只需要关注具体业务逻辑，任务调度、参数传递、
 * 执行日志以及失败重试由调度框架统一处理。</p>
 *
 * @author weifuwan
 */
@FunctionalInterface
public interface ScheduleTaskHandler {

    /**
     * 执行调度任务。
     *
     * @param parameters 任务运行参数，不应在方法中修改
     * @throws Exception 任务执行失败时抛出的异常
     */
    void execute(Map<String, String> parameters) throws Exception;
}
