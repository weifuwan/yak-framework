package io.yak.framework.schedule.core;

/** 提供调度管理操作的当前用户。 */
@FunctionalInterface
public interface CurrentOperatorProvider {
    String currentOperator();
}
