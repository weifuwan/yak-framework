/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.service;

import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.Result;
import com.didiglobal.logi.security.common.dto.project.ProjectBriefQueryDTO;
import com.didiglobal.logi.security.common.dto.project.ProjectQueryDTO;
import com.didiglobal.logi.security.common.dto.project.ProjectSaveDTO;
import com.didiglobal.logi.security.common.vo.project.ProjectBriefVO;
import com.didiglobal.logi.security.common.vo.project.ProjectBriefVOWithUser;
import com.didiglobal.logi.security.common.vo.project.ProjectDeleteCheckVO;
import com.didiglobal.logi.security.common.vo.project.ProjectVO;
import com.didiglobal.logi.security.common.vo.user.UserBriefVO;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import java.util.List;

public interface ProjectService {
    public ProjectVO createProject(ProjectSaveDTO var1, String var2) throws LogiSecurityException;

    public ProjectVO getProjectDetailByProjectId(Integer var1) throws LogiSecurityException;

    public ProjectBriefVO getProjectBriefByProjectId(Integer var1);

    public PagingData<ProjectVO> getProjectPage(ProjectQueryDTO var1);

    public PagingData<ProjectVO> getProjectPage(ProjectQueryDTO var1, List<Integer> var2);

    public void deleteProjectByProjectId(Integer var1, String var2);

    public void updateProject(ProjectSaveDTO var1, String var2) throws LogiSecurityException;

    public void changeProjectStatus(Integer var1, String var2);

    public void addProjectUser(Integer var1, Integer var2, String var3);

    public void delProjectUser(Integer var1, Integer var2, String var3);

    public void addProjectOwner(Integer var1, Integer var2, String var3);

    public void delProjectOwner(Integer var1, Integer var2, String var3);

    public List<ProjectBriefVO> getProjectBriefList();

    public ProjectDeleteCheckVO checkBeforeDelete(Integer var1);

    public PagingData<ProjectBriefVO> getProjectBriefPage(ProjectBriefQueryDTO var1);

    public boolean checkProjectExist(Integer var1);

    public Result<List<UserBriefVO>> unassignedByProjectId(Integer var1) throws LogiSecurityException;

    public Result<List<ProjectBriefVO>> getProjectBriefByUserId(Integer var1);

    public List<ProjectBriefVOWithUser> listProjectBriefVOWithUserByProjectIds(List<Integer> var1);
}

