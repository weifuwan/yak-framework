package com.yak.security.audit;
import com.yak.security.annotation.*;
import com.yak.security.domain.OperationLog;
import com.yak.security.mapper.OperationLogMapper;
import com.yak.security.service.*;
import java.time.LocalDateTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
@Aspect
public class SecurityAspects {
  private final PermissionChecker checker;
  private final OperationLogMapper logs;
  public SecurityAspects(PermissionChecker c, OperationLogMapper l) {
    checker = c;
    logs = l;
  }
  @Around("@annotation(required)")
  public Object authorize(ProceedingJoinPoint p, RequiresPermission required)
      throws Throwable {
    checker.require(required.value());
    return p.proceed();
  }
  @Around("@annotation(audited)")
  public Object audit(ProceedingJoinPoint p, AuditedOperation audited)
      throws Throwable {
    boolean ok = false;
    String detail = null;
    try {
      Object result = p.proceed();
      ok = true;
      return result;
    } catch (Throwable e) {
      detail = e.getClass().getSimpleName() + ": " + e.getMessage();
      throw e;
    } finally {
      OperationLog log = new OperationLog();
      AuthenticatedUser u = SecurityContext.get();
      if (u != null) {
        log.setUserId(u.getId());
        log.setUsername(u.getUsername());
      }
      log.setOperation(audited.value());
      log.setMethod(p.getSignature().toShortString());
      log.setSuccess(ok);
      log.setDetail(detail);
      log.setCreatedAt(LocalDateTime.now());
      logs.insert(log);
    }
  }
}
