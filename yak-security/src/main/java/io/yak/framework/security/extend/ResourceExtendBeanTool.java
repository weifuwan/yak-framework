package io.yak.framework.security.extend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 资源扩展兼容访问工具。
 *
 * <p>新代码建议直接注入 {@link ResourceExtend}。</p>
 *
 * @author weifuwan
 * @deprecated 请直接注入 {@link ResourceExtend}
 */
@Getter
@RequiredArgsConstructor
public class ResourceExtendBeanTool {

  /**
   * 资源扩展实现。
   */
  private final ResourceExtend resourceExtend;

  /**
   * 获取资源扩展实现。
   *
   * @return 资源扩展实现
   * @deprecated 请使用 {@link #getResourceExtend()}
   */
  @Deprecated
  public ResourceExtend getResourceExtendImpl() {
    return resourceExtend;
  }
}