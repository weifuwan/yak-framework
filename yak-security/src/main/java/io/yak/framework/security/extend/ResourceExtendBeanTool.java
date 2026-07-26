package io.yak.framework.security.extend;

/** Backward-compatible access wrapper that now resolves extensions by type. */
public class ResourceExtendBeanTool {
  private final ResourceExtend resourceExtend;
  public ResourceExtendBeanTool(ResourceExtend resourceExtend) { this.resourceExtend = resourceExtend; }
  public ResourceExtend getResourceExtendImpl() { return resourceExtend; }
}
