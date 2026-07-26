package io.yak.framework.security.common.dto.resource;

import lombok.Data;

import java.util.List;
/**
 * 资源批量分配数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class BatchAssignDTO {
  /** 用户标识列表。 */
  private List<Long> userIdList;
  /** 项目标识。 */
  private Long projectId;
  /** 资源类型标识。 */
  private Long resourceTypeId;
  /** 资源标识列表。 */
  private List<Long> idList;
  /** 管控级别。 */
  private Integer controlLevel;
  /** 分配标记。 */
  private Boolean assignFlag;

  public List<Long> getUserIdList() { return this.userIdList; }

  public Long getProjectId() { return this.projectId; }

  public Long getResourceTypeId() { return this.resourceTypeId; }

  public List<Long> getIdList() { return this.idList; }

  public Integer getControlLevel() { return this.controlLevel; }

  public Boolean getAssignFlag() { return this.assignFlag; }

  public void setUserIdList(List<Long> userIdList) {
    this.userIdList = userIdList;
  }

  public void setProjectId(Long projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Long resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setIdList(List<Long> idList) { this.idList = idList; }

  public void setControlLevel(Integer controlLevel) {
    this.controlLevel = controlLevel;
  }

  public void setAssignFlag(Boolean assignFlag) {
    this.assignFlag = assignFlag;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BatchAssignDTO)) {
      return false;
    }
    BatchAssignDTO other = (BatchAssignDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$projectId = this.getProjectId();
    Long other$projectId = other.getProjectId();
    if (this$projectId == null
            ? other$projectId != null
            : !((Object)this$projectId).equals(other$projectId)) {
      return false;
    }
    Long this$resourceTypeId = this.getResourceTypeId();
    Long other$resourceTypeId = other.getResourceTypeId();
    if (this$resourceTypeId == null
            ? other$resourceTypeId != null
            : !((Object)this$resourceTypeId).equals(other$resourceTypeId)) {
      return false;
    }
    Integer this$controlLevel = this.getControlLevel();
    Integer other$controlLevel = other.getControlLevel();
    if (this$controlLevel == null
            ? other$controlLevel != null
            : !((Object)this$controlLevel).equals(other$controlLevel)) {
      return false;
    }
    Boolean this$assignFlag = this.getAssignFlag();
    Boolean other$assignFlag = other.getAssignFlag();
    if (this$assignFlag == null
            ? other$assignFlag != null
            : !((Object)this$assignFlag).equals(other$assignFlag)) {
      return false;
    }
    List<Long> this$userIdList = this.getUserIdList();
    List<Long> other$userIdList = other.getUserIdList();
    if (this$userIdList == null
            ? other$userIdList != null
            : !((Object)this$userIdList).equals(other$userIdList)) {
      return false;
    }
    List<Long> this$idList = this.getIdList();
    List<Long> other$idList = other.getIdList();
    return !(this$idList == null ? other$idList != null
                                 : !((Object)this$idList).equals(other$idList));
  }

  protected boolean canEqual(Object other) {
    return other instanceof BatchAssignDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    Long $resourceTypeId = this.getResourceTypeId();
    result =
        result * 59 +
        ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
    Integer $controlLevel = this.getControlLevel();
    result = result * 59 +
             ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
    Boolean $assignFlag = this.getAssignFlag();
    result = result * 59 +
             ($assignFlag == null ? 43 : ((Object)$assignFlag).hashCode());
    List<Long> $userIdList = this.getUserIdList();
    result = result * 59 +
             ($userIdList == null ? 43 : ((Object)$userIdList).hashCode());
    List<Long> $idList = this.getIdList();
    result =
        result * 59 + ($idList == null ? 43 : ((Object)$idList).hashCode());
    return result;
  }

  public String toString() {
    return "BatchAssignDTO(userIdList=" + this.getUserIdList() +
        ", projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", idList=" + this.getIdList() +
        ", controlLevel=" + this.getControlLevel() +
        ", assignFlag=" + this.getAssignFlag() + ")";
  }
}
