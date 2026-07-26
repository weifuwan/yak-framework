package io.yak.framework.security.common;

import io.yak.framework.security.common.BaseResult;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.exception.YakSecurityException;
public class PagingResult<T> extends BaseResult {
  private PagingData<T> data;

  private PagingResult(Integer code) { this.code = code; }

  private PagingResult(Integer code, String msg) {
    this.code = code;
    this.message = msg;
  }

  public static <T> PagingResult<T> success(PagingData<T> data) {
    PagingResult<T> ret = new PagingResult<T>(ResultCode.SUCCESS.getCode());
    ret.setMessage(ResultCode.SUCCESS.getMessage());
    ret.setData(data);
    return ret;
  }

  public static <T> PagingResult<T> success() {
    return new PagingResult<T>(ResultCode.SUCCESS.getCode(),
                               ResultCode.SUCCESS.getMessage());
  }

  public static <T> PagingResult<T> fail(ResultCode resultCode) {
    PagingResult<T> ret = new PagingResult<T>(resultCode.getCode());
    ret.setMessage(resultCode.getMessage());
    return ret;
  }

  public static <T> PagingResult<T> fail(Integer code, String msg) {
    PagingResult<T> ret = new PagingResult<T>(code);
    ret.setMessage(msg);
    return ret;
  }

  public static <T> PagingResult<T> fail(String msg) {
    PagingResult<T> ret = new PagingResult<T>(ResultCode.COMMON_FAIL.getCode());
    ret.setMessage(msg);
    return ret;
  }

  public static <T> PagingResult<T> fail(YakSecurityException e) {
    String[] s = e.getMessage().split("-", 2);
    return PagingResult.fail(Integer.parseInt(s[0]), s[1]);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PagingResult)) {
      return false;
    }
    PagingResult other = (PagingResult)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    PagingData<T> this$data = this.getData();
    PagingData<T> other$data = other.getData();
    return !(this$data == null ? other$data != null
                               : !((Object)this$data).equals(other$data));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof PagingResult;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    PagingData<T> $data = this.getData();
    result = result * 59 + ($data == null ? 43 : ((Object)$data).hashCode());
    return result;
  }

  public PagingData<T> getData() { return this.data; }

  public void setData(PagingData<T> data) { this.data = data; }

  @Override
  public String toString() {
    return "PagingResult(data=" + this.getData() + ")";
  }
}
