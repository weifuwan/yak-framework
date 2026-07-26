package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.dto.project.ProjectBriefQueryDTO;
import io.yak.framework.security.common.dto.project.ProjectQueryDTO;
import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.entity.project.ProjectBrief;
import java.util.List;

public interface ProjectDao {
  public Project selectByProjectId(Long projectId);

  public void insert(Project var1);

  public IPage<Project> selectPageByDeptIdListAndProjectIdList(
      ProjectQueryDTO var1, List<Long> var2, List<Long> var3);

  public void deleteByProjectId(Long projectId);

  public int selectCountByProjectNameAndNotProjectId(String projectName, Long projectId);

  public IPage<ProjectBrief> selectBriefPage(ProjectBriefQueryDTO var1);

  public List<ProjectBrief> selectAllBriefList();

  public void update(Project var1);

  public List<Project> selectProjectBriefByProjectIds(List<Long> var1);
}
