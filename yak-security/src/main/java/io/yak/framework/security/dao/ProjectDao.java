package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.dto.project.ProjectBriefQueryDTO;
import io.yak.framework.security.common.dto.project.ProjectQueryDTO;
import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.entity.project.ProjectBrief;
import java.util.List;

public interface ProjectDao {
  public Project selectByProjectId(Integer var1);

  public void insert(Project var1);

  public IPage<Project> selectPageByDeptIdListAndProjectIdList(
      ProjectQueryDTO var1, List<Integer> var2, List<Integer> var3);

  public void deleteByProjectId(Integer var1);

  public int selectCountByProjectNameAndNotProjectId(String var1, Integer var2);

  public IPage<ProjectBrief> selectBriefPage(ProjectBriefQueryDTO var1);

  public List<ProjectBrief> selectAllBriefList();

  public void update(Project var1);

  public List<Project> selectProjectBriefByProjectIds(List<Integer> var1);
}
