package io.yak.framework.security.common.vo.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前登录用户身份。
 *
 * @author weifuwan
 */
@Data
public class CurrentUserVO {

    private Long id;

    private String userName;

    private String realName;

    private Long deptId;

    private String phone;

    private String email;

    private List<String> roleList =
            new ArrayList<>();

    /**
     * 当前用户拥有的权限编码。
     */
    private List<String> permissionCodes =
            new ArrayList<>();
}