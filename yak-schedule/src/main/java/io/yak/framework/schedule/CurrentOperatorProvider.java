package io.yak.framework.schedule;

/** Supplies the authenticated operator for management and manual execution audit. */
@FunctionalInterface
public interface CurrentOperatorProvider {
  String currentOperator();
}
