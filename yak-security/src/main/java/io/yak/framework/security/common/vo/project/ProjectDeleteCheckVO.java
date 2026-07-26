package io.yak.framework.security.common.vo.project;

import lombok.Data;

import java.util.List;
/**
 * 项目删除检查结果视图对象。
 *
 * @author weifuwan
 */
@Data
public class ProjectDeleteCheckVO {
  /** 项目标识。 */
  private Long projectId;
  /** 关联的资源名称列表。 */
  private List<String> resourceNameList;

  public ProjectDeleteCheckVO(Long projectId,
                              List<String> resourceNameList) {
    this.projectId = projectId;
    this.resourceNameList = resourceNameList;
  }

}
