/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.vo.resource;

import com.yak.security.common.vo.dept.DeptBriefVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u8d44\u6e90\u6743\u9650\u7ba1\u7406\uff08\u6309\u7528\u6237\u7ba1\u7406\u7684\u5217\u8868\u4fe1\u606f\uff09")
public class MByUVO {
    @ApiModelProperty(value="\u7528\u6237id", dataType="Integer", required=false)
    private Integer userId;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7", dataType="String", required=false)
    private String userName;
    @ApiModelProperty(value="\u771f\u5b9e\u59d3\u540d", dataType="String", required=false)
    private String realName;
    @ApiModelProperty(value="\u90e8\u95e8\u4fe1\u606f", dataType="List<DeptBriefVO>", required=false)
    private List<DeptBriefVO> deptList;
    @ApiModelProperty(value="\u7ba1\u7406\u6743\u9650\u8d44\u6e90\u6570", dataType="Integer", required=false)
    private Integer adminResourceCnt;
    @ApiModelProperty(value="\u67e5\u770b\u6743\u9650\u8d44\u6e90\u6570", dataType="Integer", required=false)
    private Integer viewResourceCnt;

    public Integer getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getRealName() {
        return this.realName;
    }

    public List<DeptBriefVO> getDeptList() {
        return this.deptList;
    }

    public Integer getAdminResourceCnt() {
        return this.adminResourceCnt;
    }

    public Integer getViewResourceCnt() {
        return this.viewResourceCnt;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setDeptList(List<DeptBriefVO> deptList) {
        this.deptList = deptList;
    }

    public void setAdminResourceCnt(Integer adminResourceCnt) {
        this.adminResourceCnt = adminResourceCnt;
    }

    public void setViewResourceCnt(Integer viewResourceCnt) {
        this.viewResourceCnt = viewResourceCnt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MByUVO)) {
            return false;
        }
        MByUVO other = (MByUVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$adminResourceCnt = this.getAdminResourceCnt();
        Integer other$adminResourceCnt = other.getAdminResourceCnt();
        if (this$adminResourceCnt == null ? other$adminResourceCnt != null : !((Object)this$adminResourceCnt).equals(other$adminResourceCnt)) {
            return false;
        }
        Integer this$viewResourceCnt = this.getViewResourceCnt();
        Integer other$viewResourceCnt = other.getViewResourceCnt();
        if (this$viewResourceCnt == null ? other$viewResourceCnt != null : !((Object)this$viewResourceCnt).equals(other$viewResourceCnt)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$realName = this.getRealName();
        String other$realName = other.getRealName();
        if (this$realName == null ? other$realName != null : !this$realName.equals(other$realName)) {
            return false;
        }
        List<DeptBriefVO> this$deptList = this.getDeptList();
        List<DeptBriefVO> other$deptList = other.getDeptList();
        return !(this$deptList == null ? other$deptList != null : !((Object)this$deptList).equals(other$deptList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MByUVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $adminResourceCnt = this.getAdminResourceCnt();
        result = result * 59 + ($adminResourceCnt == null ? 43 : ((Object)$adminResourceCnt).hashCode());
        Integer $viewResourceCnt = this.getViewResourceCnt();
        result = result * 59 + ($viewResourceCnt == null ? 43 : ((Object)$viewResourceCnt).hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        List<DeptBriefVO> $deptList = this.getDeptList();
        result = result * 59 + ($deptList == null ? 43 : ((Object)$deptList).hashCode());
        return result;
    }

    public String toString() {
        return "MByUVO(userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", realName=" + this.getRealName() + ", deptList=" + this.getDeptList() + ", adminResourceCnt=" + this.getAdminResourceCnt() + ", viewResourceCnt=" + this.getViewResourceCnt() + ")";
    }
}

