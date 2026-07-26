/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.vo.project;

import com.yak.security.common.vo.user.UserBasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u9879\u76ee\u7b80\u8981\u4fe1\u606f\u548c\u7528\u6237\u7684\u5173\u7cfb")
public class ProjectBriefVOWithUser {
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u9879\u76ee\u7f16\u53f7", dataType="String", required=false)
    private String projectCode;
    @ApiModelProperty(value="\u9879\u76ee\u540d", dataType="String", required=false)
    private String projectName;
    @ApiModelProperty(value="\u9879\u76ee\u8d1f\u8d23\u4eba", dataType="List<UserBriefVO>", required=false)
    private List<UserBasicVO> ownerList;
    @ApiModelProperty(value="\u9879\u76ee\u6210\u5458", dataType="List<UserBriefVO>", required=false)
    private List<UserBasicVO> userList;

    public Integer getId() {
        return this.id;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public List<UserBasicVO> getOwnerList() {
        return this.ownerList;
    }

    public List<UserBasicVO> getUserList() {
        return this.userList;
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

    public void setOwnerList(List<UserBasicVO> ownerList) {
        this.ownerList = ownerList;
    }

    public void setUserList(List<UserBasicVO> userList) {
        this.userList = userList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectBriefVOWithUser)) {
            return false;
        }
        ProjectBriefVOWithUser other = (ProjectBriefVOWithUser)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        List<UserBasicVO> this$ownerList = this.getOwnerList();
        List<UserBasicVO> other$ownerList = other.getOwnerList();
        if (this$ownerList == null ? other$ownerList != null : !((Object)this$ownerList).equals(other$ownerList)) {
            return false;
        }
        List<UserBasicVO> this$userList = this.getUserList();
        List<UserBasicVO> other$userList = other.getUserList();
        return !(this$userList == null ? other$userList != null : !((Object)this$userList).equals(other$userList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ProjectBriefVOWithUser;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $projectCode = this.getProjectCode();
        result = result * 59 + ($projectCode == null ? 43 : $projectCode.hashCode());
        String $projectName = this.getProjectName();
        result = result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
        List<UserBasicVO> $ownerList = this.getOwnerList();
        result = result * 59 + ($ownerList == null ? 43 : ((Object)$ownerList).hashCode());
        List<UserBasicVO> $userList = this.getUserList();
        result = result * 59 + ($userList == null ? 43 : ((Object)$userList).hashCode());
        return result;
    }

    public String toString() {
        return "ProjectBriefVOWithUser(id=" + this.getId() + ", projectCode=" + this.getProjectCode() + ", projectName=" + this.getProjectName() + ", ownerList=" + this.getOwnerList() + ", userList=" + this.getUserList() + ")";
    }
}

