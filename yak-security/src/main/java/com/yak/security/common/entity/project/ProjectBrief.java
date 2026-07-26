/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.common.entity.project;

public class ProjectBrief {
    private Integer id;
    private String projectName;
    private String projectCode;

    public Integer getId() {
        return this.id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectBrief)) {
            return false;
        }
        ProjectBrief other = (ProjectBrief)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$projectName = this.getProjectName();
        String other$projectName = other.getProjectName();
        if (this$projectName == null ? other$projectName != null : !this$projectName.equals(other$projectName)) {
            return false;
        }
        String this$projectCode = this.getProjectCode();
        String other$projectCode = other.getProjectCode();
        return !(this$projectCode == null ? other$projectCode != null : !this$projectCode.equals(other$projectCode));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ProjectBrief;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $projectName = this.getProjectName();
        result = result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
        String $projectCode = this.getProjectCode();
        result = result * 59 + ($projectCode == null ? 43 : $projectCode.hashCode());
        return result;
    }

    public String toString() {
        return "ProjectBrief(id=" + this.getId() + ", projectName=" + this.getProjectName() + ", projectCode=" + this.getProjectCode() + ")";
    }
}

