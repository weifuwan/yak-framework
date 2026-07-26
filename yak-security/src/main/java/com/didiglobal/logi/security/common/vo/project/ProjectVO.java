/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.vo.project;

import com.didiglobal.logi.security.common.vo.dept.DeptBriefVO;
import com.didiglobal.logi.security.common.vo.user.UserBriefVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

@ApiModel(description="\u9879\u76ee\u4fe1\u606f")
public class ProjectVO {
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u9879\u76eecode\uff08\u9875\u9762\u5c55\u793a\u53eb\u9879\u76eeid\uff09", dataType="String", required=false)
    private String projectCode;
    @ApiModelProperty(value="\u9879\u76ee\u540d", dataType="String", required=false)
    private String projectName;
    @ApiModelProperty(value="\u9879\u76ee\u6210\u5458", dataType="List<UserBriefVO>", required=false)
    private List<UserBriefVO> userList;
    @ApiModelProperty(value="\u9879\u76ee\u8d1f\u8d23\u4eba", dataType="List<UserBriefVO>", required=false)
    private List<UserBriefVO> ownerList;
    @ApiModelProperty(value="\u63cf\u8ff0", dataType="String", required=false)
    private String description;
    @ApiModelProperty(value="\u8fd0\u884c\u72b6\u6001", dataType="Boolean", required=false)
    private Boolean running;
    @ApiModelProperty(value="\u90e8\u95e8\u4fe1\u606f\uff08\u6570\u7ec4\uff0c\u7236->\u5b50\uff08\u4e0b\u68070~len\uff09\uff09", dataType="List<DeptBriefVO>", required=false)
    private List<DeptBriefVO> deptList;
    @ApiModelProperty(value="\u4f7f\u7528\u90e8\u95e8id\uff08\u5b50\uff09", dataType="Integer", required=false)
    private Integer deptId;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4", dataType="Long", required=false)
    private Date createTime;

    public Integer getId() {
        return this.id;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public List<UserBriefVO> getUserList() {
        return this.userList;
    }

    public List<UserBriefVO> getOwnerList() {
        return this.ownerList;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getRunning() {
        return this.running;
    }

    public List<DeptBriefVO> getDeptList() {
        return this.deptList;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setUserList(List<UserBriefVO> userList) {
        this.userList = userList;
    }

    public void setOwnerList(List<UserBriefVO> ownerList) {
        this.ownerList = ownerList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public void setDeptList(List<DeptBriefVO> deptList) {
        this.deptList = deptList;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectVO)) {
            return false;
        }
        ProjectVO other = (ProjectVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Boolean this$running = this.getRunning();
        Boolean other$running = other.getRunning();
        if (this$running == null ? other$running != null : !((Object)this$running).equals(other$running)) {
            return false;
        }
        Integer this$deptId = this.getDeptId();
        Integer other$deptId = other.getDeptId();
        if (this$deptId == null ? other$deptId != null : !((Object)this$deptId).equals(other$deptId)) {
            return false;
        }
        String this$projectCode = this.getProjectCode();
        String other$projectCode = other.getProjectCode();
        if (this$projectCode == null ? other$projectCode != null : !this$projectCode.equals(other$projectCode)) {
            return false;
        }
        String this$projectName = this.getProjectName();
        String other$projectName = other.getProjectName();
        if (this$projectName == null ? other$projectName != null : !this$projectName.equals(other$projectName)) {
            return false;
        }
        List<UserBriefVO> this$userList = this.getUserList();
        List<UserBriefVO> other$userList = other.getUserList();
        if (this$userList == null ? other$userList != null : !((Object)this$userList).equals(other$userList)) {
            return false;
        }
        List<UserBriefVO> this$ownerList = this.getOwnerList();
        List<UserBriefVO> other$ownerList = other.getOwnerList();
        if (this$ownerList == null ? other$ownerList != null : !((Object)this$ownerList).equals(other$ownerList)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        List<DeptBriefVO> this$deptList = this.getDeptList();
        List<DeptBriefVO> other$deptList = other.getDeptList();
        if (this$deptList == null ? other$deptList != null : !((Object)this$deptList).equals(other$deptList)) {
            return false;
        }
        Date this$createTime = this.getCreateTime();
        Date other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ProjectVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Boolean $running = this.getRunning();
        result = result * 59 + ($running == null ? 43 : ((Object)$running).hashCode());
        Integer $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        String $projectCode = this.getProjectCode();
        result = result * 59 + ($projectCode == null ? 43 : $projectCode.hashCode());
        String $projectName = this.getProjectName();
        result = result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
        List<UserBriefVO> $userList = this.getUserList();
        result = result * 59 + ($userList == null ? 43 : ((Object)$userList).hashCode());
        List<UserBriefVO> $ownerList = this.getOwnerList();
        result = result * 59 + ($ownerList == null ? 43 : ((Object)$ownerList).hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        List<DeptBriefVO> $deptList = this.getDeptList();
        result = result * 59 + ($deptList == null ? 43 : ((Object)$deptList).hashCode());
        Date $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        return result;
    }

    public String toString() {
        return "ProjectVO(id=" + this.getId() + ", projectCode=" + this.getProjectCode() + ", projectName=" + this.getProjectName() + ", userList=" + this.getUserList() + ", ownerList=" + this.getOwnerList() + ", description=" + this.getDescription() + ", running=" + this.getRunning() + ", deptList=" + this.getDeptList() + ", deptId=" + this.getDeptId() + ", createTime=" + this.getCreateTime() + ")";
    }
}

