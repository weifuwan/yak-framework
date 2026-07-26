package io.yak.framework.security.controller.v1;

import com.google.common.collect.Lists;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.PagingResult;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.user.UserDTO;
import io.yak.framework.security.common.dto.user.UserQueryDTO;
import io.yak.framework.security.common.vo.role.AssignInfoVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.common.vo.user.UserVO;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.util.HttpRequestUtil;
import io.yak.framework.security.util.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/user"})
public class UserController {
  @Autowired private UserService userService;

  @GetMapping(value = {"/{type}/{value}/check"})
  public Result<Void> check(@PathVariable Integer type,
                            @PathVariable String value) {
    return this.userService.check(type, value);
  }

  @GetMapping
  public Result<List<UserVO>>
  detailList(@RequestParam(value = "ids") String ids) {
    List idList = Lists.newArrayList();
    try {
      idList = JsonUtils.toList(ids, Integer.class);
    } catch (Exception e) {
      return Result.buildParamIllegal(
          "\u4f20\u5165\u7684\u53c2\u6570\u4e0d\u5c5e\u4e8ejson\u6570\u7ec4");
    }
    return this.userService.getUserDetailByUserIds(idList);
  }

  @GetMapping(value = {"/{id}"})
  public Result<UserVO> detail(@PathVariable Integer id) {
    try {
      UserVO userVo = this.userService.getUserDetailByUserId(id);
      return Result.success(userVo);
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
  }

  @PostMapping(value = {"/page"})
  public PagingResult<UserVO> page(@RequestBody UserQueryDTO queryDTO) {
    PagingData<UserVO> pageUser = this.userService.getUserPage(queryDTO);
    return PagingResult.success(pageUser);
  }

  @GetMapping(value = {"/list/dept/{deptId}"})
  public Result<List<UserBriefVO>> listByDeptId(@PathVariable Integer deptId) {
    List<UserBriefVO> userBriefVOList =
        this.userService.getUserBriefListByDeptId(deptId);
    return Result.success(userBriefVOList);
  }

  @GetMapping(value = {"/list/role/{roleId}"})
  public Result<List<UserBriefVO>> listByRoleId(@PathVariable Integer roleId) {
    List<UserBriefVO> userBriefVOList =
        this.userService.getUserBriefListByRoleId(roleId);
    return Result.success(userBriefVOList);
  }

  @GetMapping(value = {"/assign/list/{userId}"})
  public Result<List<AssignInfoVO>> assignList(@PathVariable Integer userId) {
    try {
      List<AssignInfoVO> assignInfoVOList =
          this.userService.getAssignDataByUserId(userId);
      return Result.success(assignInfoVOList);
    } catch (YakSecurityException e) {
      e.printStackTrace();
      return Result.fail(e);
    }
  }

  @GetMapping(value = {"/list/{name}"})
  public Result<List<UserBriefVO>>
  listByName(@PathVariable(required = false) String name) {
    List<UserBriefVO> userBriefVOList =
        this.userService.getUserBriefListByUsernameOrRealName(name);
    return Result.success(userBriefVOList);
  }

  @PutMapping(value = {"/add"})
  @ResponseBody
  public Result<Void> add(HttpServletRequest request,
                          @RequestBody UserDTO param) {
    return this.userService.addUser(param,
                                    HttpRequestUtil.getOperator(request));
  }

  @PostMapping(value = {"/edit"})
  @ResponseBody
  public Result<Void> edit(HttpServletRequest request,
                           @RequestBody UserDTO param) {
    return this.userService.editUser(param,
                                     HttpRequestUtil.getOperator(request));
  }

  @DeleteMapping(value = {"/{id}"})
  public Result<Void> del(@PathVariable Integer id) {
    return this.userService.deleteByUserId(id);
  }
}
