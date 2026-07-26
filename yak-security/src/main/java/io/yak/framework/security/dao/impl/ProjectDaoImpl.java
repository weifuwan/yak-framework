package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.project.ProjectBriefQueryDTO;
import io.yak.framework.security.common.dto.project.ProjectQueryDTO;
import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.entity.project.ProjectBrief;
import io.yak.framework.security.common.po.ProjectPO;
import io.yak.framework.security.dao.ProjectDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.ProjectMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class ProjectDaoImpl
    extends BaseDaoImpl<ProjectPO> implements ProjectDao {
  @Autowired private ProjectMapper projectMapper;

  @Override
  public Project selectByProjectId(Integer projectId) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "id", (Object)projectId);
    return CopyBeanUtil.copy(
        this.projectMapper.selectOne((Wrapper)queryWrapper), Project.class);
  }

  @Override
  public void insert(Project project) {
    ProjectPO projectPO = CopyBeanUtil.copy(project, ProjectPO.class);
    projectPO.setAppName(this.yakSecurityProperties.getApplicationName());
    this.projectMapper.insert(projectPO);
    project.setId(projectPO.getId());
  }

  @Override
  public void deleteByProjectId(Integer projectId) {
    this.projectMapper.deleteById(projectId);
  }

  @Override
  public void update(Project project) {
    this.projectMapper.updateById(CopyBeanUtil.copy(project, ProjectPO.class));
  }

  @Override
  public int selectCountByProjectNameAndNotProjectId(String projectName,
                                                     Integer projectId) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    ((QueryWrapper)queryWrapper.eq((Object) "project_name",
                                   (Object)projectName))
        .ne(projectId != null, (Object) "id", (Object)projectId);
    return this.projectMapper.selectCount((Wrapper)queryWrapper);
  }

  private QueryWrapper<ProjectPO> wrapBriefQuery() {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"id", "project_code", "project_name"});
    return queryWrapper;
  }

  @Override
  public IPage<ProjectBrief> selectBriefPage(ProjectBriefQueryDTO queryDTO) {
    Page page = new Page((long)queryDTO.getPage(), (long)queryDTO.getSize());
    QueryWrapper<ProjectPO> projectWrapper = this.wrapBriefQuery();
    String projectName = queryDTO.getProjectName();
    if (!StringUtils.isEmpty((Object)projectName)) {
      projectWrapper.like((Object) "project_name", (Object)projectName);
    }
    this.projectMapper.selectPage((IPage)page, (Wrapper)projectWrapper);
    return CopyBeanUtil.copyPage(page, ProjectBrief.class);
  }

  @Override
  public List<ProjectBrief> selectAllBriefList() {
    return CopyBeanUtil.copyList(
        this.projectMapper.selectList((Wrapper)this.wrapBriefQuery()),
        ProjectBrief.class);
  }

  @Override
  public IPage<Project>
  selectPageByDeptIdListAndProjectIdList(ProjectQueryDTO queryDTO,
                                         List<Integer> deptIdList,
                                         List<Integer> projectIdList) {
    Page page = new Page((long)queryDTO.getPage(), (long)queryDTO.getSize());
    QueryWrapper projectWrapper = this.getQueryWrapperWithAppName();
    String projectCode = queryDTO.getProjectCode();
    String projectName = queryDTO.getProjectName();
    ((QueryWrapper)((QueryWrapper)((QueryWrapper)((QueryWrapper)projectWrapper
                                                      .eq(queryDTO.getRunning() !=
                                                              null,
                                                          (Object) "running",
                                                          (Object)queryDTO
                                                              .getRunning()))
                                       .eq(!StringUtils.isEmpty(
                                               (Object)projectCode),
                                           (Object) "project_code",
                                           (Object)projectCode))
                        .like(!StringUtils.isEmpty((Object)projectName),
                              (Object) "project_name", (Object)projectName))
         .in(deptIdList != null && !deptIdList.isEmpty(), (Object) "dept_id",
             deptIdList))
        .in(projectIdList != null && !projectIdList.isEmpty(), (Object) "id",
            projectIdList);
    return CopyBeanUtil.copyPage(
        this.projectMapper.selectPage((IPage)page, (Wrapper)projectWrapper),
        Project.class);
  }

  @Override
  public List<Project>
  selectProjectBriefByProjectIds(List<Integer> projectIds) {
    if (CollectionUtils.isEmpty(projectIds)) {
      return Collections.emptyList();
    }
    QueryWrapper projectWrapper = this.getQueryWrapperWithAppName();
    projectWrapper.select(new String[] {"id", "project_name", "project_code"});
    projectWrapper.in((Object) "id", projectIds);
    return CopyBeanUtil.copyList(
        this.projectMapper.selectList((Wrapper)projectWrapper), Project.class);
  }
}
