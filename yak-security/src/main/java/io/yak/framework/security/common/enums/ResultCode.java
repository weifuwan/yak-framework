package io.yak.framework.security.common.enums;

import io.yak.framework.security.exception.CodeMsg;
public enum ResultCode implements CodeMsg {
  SUCCESS(200, "\u6210\u529f"),
  COMMON_FAIL(999, "\u5931\u8d25"),
  PARAM_NOT_VALID(1001, "\u53c2\u6570\u65e0\u6548"),
  PARAM_IS_BLANK(1002, "\u53c2\u6570\u4e3a\u7a7a"),
  PARAM_ID_IS_BLANK(1003, "\u53c2\u6570id\u4e3a\u7a7a"),
  PARAM_TYPE_ERROR(1004, "\u53c2\u6570\u7c7b\u578b\u9519\u8bef"),
  PARAM_NOT_COMPLETE(1005, "\u53c2\u6570\u7f3a\u5931"),
  PARAM_LENGTH_ERROR(1006, "\u53c2\u6570\u957f\u5ea6\u4e0d\u6b63\u786e"),
  PARAM_ERROR(1007, "\u53c2\u6570\u9519\u8bef"),
  USER_NOT_LOGIN(2001, "\u7528\u6237\u672a\u767b\u5f55"),
  USER_ACCOUNT_EXPIRED(2002, "\u8d26\u53f7\u5df2\u8fc7\u671f"),
  USER_CREDENTIALS_ERROR(2003, "\u5bc6\u7801\u9519\u8bef"),
  USER_CREDENTIALS_EXPIRED(2004, "\u5bc6\u7801\u8fc7\u671f"),
  USER_ACCOUNT_DISABLE(2005, "\u8d26\u53f7\u4e0d\u53ef\u7528"),
  USER_ACCOUNT_LOCKED(2006, "\u8d26\u53f7\u88ab\u9501\u5b9a"),
  USER_ACCOUNT_NOT_EXIST(2007, "\u8d26\u53f7\u4e0d\u5b58\u5728"),
  USER_ACCOUNT_ALREADY_EXIST(2008, "\u8d26\u53f7\u5df2\u5b58\u5728"),
  USER_ACCOUNT_USE_BY_OTHERS(2009, "\u8d26\u53f7\u4e0b\u7ebf"),
  USER_ACCOUNT_INSERT_FAIL(2010, "\u7528\u6237\u6ce8\u518c\u5931\u8d25"),
  USER_PHONE_EXIST(2011, "\u624b\u673a\u53f7\u5df2\u5b58\u5728"),
  USER_EMAIL_FORMAT_ERROR(2012, "\u90ae\u7bb1\u683c\u5f0f\u9519\u8bef"),
  USER_EMAIL_EXIST(2013, "\u90ae\u7bb1\u5df2\u5b58\u5728"),
  USER_PASSWORD_DECRYPT_ERROR(2014, "\u5bc6\u7801\u89e3\u5bc6\u51fa\u9519"),
  USER_PASSWORD_ENCODE_ERROR(2015, "\u5bc6\u7801\u89e3\u5bc6\u51fa\u9519"),
  USER_ID_CANNOT_BE_NULL(2016, "\u7528\u6237id\u4e0d\u53ef\u4e3a\u7a7a"),
  USER_NOT_EXISTS(2017, "\u7528\u6237\u4e0d\u5b58\u5728"),
  USER_ACCOUNT_UPDATE_FAIL(2018, "\u7528\u6237\u66f4\u65b0\u5931\u8d25"),
  USER_PHONE_FORMAT_ERROR(2019, "\u624b\u673a\u53f7\u683c\u5f0f\u9519\u8bef"),
  USER_NAME_FORMAT_ERROR(2020, "\u7528\u6237\u540d\u683c\u5f0f\u9519\u8bef"),
  USER_NAME_EXISTS(2021, "\u7528\u6237\u540d\u5df2\u7ecf\u5b58\u5728"),
  NO_PERMISSION(3001, "\u6ca1\u6709\u6743\u9650"),
  ROLE_BUSINESS_ERROR(4001, "\u5185\u90e8\u9519\u8bef"),
  ROLE_NOT_EXISTS(4002, "\u89d2\u8272\u4e0d\u5b58\u5728"),
  ROLE_USER_AUTHED(4003,
                   "\u6709\u7528\u6237\u5df2\u7ed1\u5b9a\u8be5\u89d2\u8272"),
  ROLE_NAME_ALREADY_EXISTS(4004, "\u89d2\u8272\u540d\u5df2\u5b58\u5728"),
  ROLE_NAME_CANNOT_BE_BLANK(4005, "\u89d2\u8272\u540d\u4e0d\u53ef\u4e3a\u7a7a"),
  ROLE_DEPT_CANNOT_BE_BLANK(4006,
                            "\u89d2\u8272\u63cf\u8ff0\u4e0d\u53ef\u4e3a\u7a7a"),
  ROLE_PERMISSION_CANNOT_BE_NULL(
      4007, "\u89d2\u8272\u6743\u9650\u4e0d\u53ef\u4e3a\u7a7a"),
  ROLE_ASSIGN_FLAG_IS_NULL(
      4008, "\u89d2\u8272\u5206\u914dflag\u4e0d\u53ef\u4e3a\u7a7a"),
  ROLE_ID_CANNOT_BE_NULL(4009, "\u89d2\u8272id\u4e0d\u53ef\u4e3a\u7a7a"),
  PROJECT_NAME_ALREADY_EXISTS(5001, "\u9879\u76ee\u540d\u5df2\u5b58\u5728"),
  PROJECT_NOT_EXISTS(5002, "\u9879\u76ee\u4e0d\u5b58\u5728"),
  PROJECT_UN_RUNNING(5003, "\u9879\u76ee\u672a\u8fd0\u884c"),
  PROJECT_ID_CANNOT_BE_NULL(5004, "\u9879\u76eeid\u4e0d\u53ef\u4e3a\u7a7a"),
  PROJECT_NAME_CANNOT_BE_BLANK(5005,
                               "\u9879\u76ee\u540d\u4e0d\u53ef\u4e3a\u7a7a"),
  PROJECT_DES_CANNOT_BE_BLANK(
      5006, "\u9879\u76ee\u63cf\u8ff0\u4e0d\u53ef\u4e3a\u7a7a"),
  PROJECT_DEPT_CANNOT_BE_NULL(
      5007, "\u9879\u76ee\u4f7f\u7528\u90e8\u95e8\u4e0d\u53ef\u4e3a\u7a7a"),
  PROJECT_CHARGE_USER_CANNOT_BE_NULL(
      5008, "\u9879\u76ee\u8d1f\u8d23\u4eba\u4e0d\u53ef\u4e3a\u7a7a"),
  PROJECT_DEL_RESOURCE_NOT_NULL(
      5009, "\u9879\u76ee\u5b58\u5728\u6240\u5c5e\u8d44\u6e90\uff0c\u4e0d" +
            "\u80fd\u5220\u9664\u8be5\u9879\u76ee"),
  OPLOG_NOT_EXIST(6001, "\u64cd\u4f5c\u65e5\u5fd7\u4e0d\u5b58\u5728"),
  MESSAGE_NOT_EXIST(7001, "\u6d88\u606f\u4e0d\u5b58\u5728"),
  PERMISSION_DATA_ERROR(8001,
                        "\u83b7\u53d6\u6743\u9650\u6570\u636e\u5f02\u5e38"),
  DEPT_DATA_ERROR(9001,
                  "\u83b7\u53d6\u90e8\u95e8\u6570\u636e\u5f02\u5e38\uff0c" +
                  "\u8bf7\u68c0\u67e5\u90e8\u95e8\u8868\u6570\u636e"),
  RESOURCE_ASSIGN_ERROR(
      10001, "\u8d44\u6e90\u6743\u9650\u5206\u914d\u5f02\u5e38\uff0c\u5177" +
             "\u4f53\u8d44\u6e90id\u4e0d\u4e3anull\uff0c\u5219\u8d44\u6e90" +
             "\u7c7b\u522bid\u4e0d\u53ef\u4e3anull"),
  RESOURCE_ASSIGN_ERROR_2(10002,
                          "\u8d44\u6e90\u6743\u9650\u5206\u914d\u5f02\u5e38" +
                          "\uff0c\u8d44\u6e90\u7c7b\u522bid\u4e0d\u4e3anull" +
                          "\uff0c\u5219\u9879\u76eeid\u4e0d\u53ef\u4e3anull"),
  RESOURCE_INVALID_SHOW_LEVEL(
      10003, "\u8bf7\u8f93\u5165\u6709\u6548\u7684\u5c55\u793a\u7ea7\u522b" +
             "\uff081 <= showLevel <= 3\uff09"),
  RESOURCE_SHOW_LEVEL_ERROR(10004, "2\u7ea7\u5c55\u793a\u7ea7\u522b\uff0c" +
                                   "\u9879\u76eeid\u4e0d\u53ef\u4e3a\u7a7a"),
  RESOURCE_SHOW_LEVEL_ERROR_2(
      10005, "3\u7ea7\u5c55\u793a\u7ea7\u522b\uff0c\u9879\u76eeid\u6216\u8d44" +
             "\u6e90\u7c7b\u522bid\u4e0d\u53ef\u4e3anull"),
  RESOURCE_ASSIGN_BATCH_FLAG_CANNOT_BE_NULL(
      10006, "\u8d44\u6e90\u6743\u9650\u6279\u91cf\u5206\u914d\u7684\u6807" +
             "\u8bc6\u4e0d\u53ef\u4e3a\u7a7a"),
  RESOURCE_INVALID_CONTROL_LEVEL(
      10007, "\u8bf7\u8f93\u5165\u6709\u6548\u7684\u8d44\u6e90\u6743\u9650" +
             "\u63a7\u5236\u7ea7\u522b\uff081 <= controlLevel <= 2\uff09"),
  RESOURCE_TYPE_ID_CANNOT_BE_NULL(
      10008, "\u8d44\u6e90\u7c7b\u522bid\u4e0d\u53ef\u4e3a\u7a7a"),
  RESOURCE_ID_CANNOT_BE_NULL(
      10009, "\u5177\u4f53\u8d44\u6e90id\u4e0d\u53ef\u4e3a\u7a7a"),
  RESOURCE_TYPE_NOT_EXISTS(10010, "\u8d44\u6e90\u7c7b\u522b\u4e0d\u5b58\u5728"),
  RESOURCE_DUPLICATION(10004, "\u6570\u636e\u5df2\u5b58\u5728");

  private final Integer code;
  private final String message;

  private ResultCode(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public Integer getCode() {
    return this.code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  public static String getMessageByCode(Integer code) {
    for (ResultCode ele : ResultCode.values()) {
      if (!ele.getCode().equals(code))
        continue;
      return ele.getMessage();
    }
    return null;
  }
}
