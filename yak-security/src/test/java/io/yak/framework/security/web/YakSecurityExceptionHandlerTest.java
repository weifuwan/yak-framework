package io.yak.framework.security.web;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.controller.v1.LoginController;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class YakSecurityExceptionHandlerTest {

  private final YakSecurityExceptionHandler handler =
          new YakSecurityExceptionHandler();

  @Test
  void mapsBusinessCodesToStandardHttpStatuses() {
    assertStatus(ResultCode.PARAM_ERROR, 400);
    assertStatus(ResultCode.USER_NOT_LOGIN, 401);
    assertStatus(ResultCode.NO_PERMISSION, 403);
    assertStatus(ResultCode.PROJECT_NOT_EXISTS, 404);
    assertStatus(ResultCode.ROLE_NAME_ALREADY_EXISTS, 409);
    assertStatus(ResultCode.PERMISSION_DATA_ERROR, 500);
  }

  @Test
  void controllerLetsBusinessExceptionReachGlobalHandler() throws Exception {
    LoginService loginService = mock(LoginService.class);
    when(loginService.verifyLogin(any(), any(), any()))
            .thenThrow(new YakSecurityException(
                    ResultCode.USER_CREDENTIALS_ERROR));
    MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new LoginController(loginService))
            .setControllerAdvice(handler)
            .build();

    mockMvc.perform(post("/yak-security/api/v1/account/login")
                    .contentType("application/json")
                    .content("{\"userName\":\"yak\",\"pw\":\"wrong\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(2003));
  }

  @Test
  void invalidLoginBodyReturnsBadRequestWithoutCallingService()
          throws Exception {
    LoginService loginService = mock(LoginService.class);
    MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new LoginController(loginService))
            .setControllerAdvice(handler)
            .build();

    mockMvc.perform(post("/yak-security/api/v1/account/login")
                    .contentType("application/json")
                    .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(1001));
  }

  private void assertStatus(ResultCode code, int expectedStatus) {
    ResponseEntity<Result<Void>> response =
            handler.handleYakSecurityException(
                    new YakSecurityException(code));
    assertEquals(expectedStatus, response.getStatusCode().value());
    assertEquals(code.getCode(), response.getBody().getCode());
  }
}
