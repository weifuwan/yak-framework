package io.yak.framework.security.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class NetworkUtil {
  private static final String UNKNOWN = "unknown";

  public static String getRealIpAddress() {
    RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
    ServletRequestAttributes sra = (ServletRequestAttributes)ra;
    HttpServletRequest request = sra.getRequest();
    return NetworkUtil.getRealIpAddress(request);
  }

  private static boolean isNotOk(String ipAddress) {
    return ipAddress == null || ipAddress.length() == 0 ||
        UNKNOWN.equalsIgnoreCase(ipAddress);
  }

  public static String getRealIpAddress(HttpServletRequest request) {
    String ipAddress = request.getHeader("x-forwarded-for");
    if (NetworkUtil.isNotOk(ipAddress)) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }
    if (NetworkUtil.isNotOk(ipAddress)) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }
    if (NetworkUtil.isNotOk(ipAddress)) {
      ipAddress = request.getRemoteAddr();
      String localIp = "127.0.0.1";
      String localIpv6 = "0:0:0:0:0:0:0:1";
      if (ipAddress.equals(localIp) || ipAddress.equals(localIpv6)) {
        InetAddress inet = null;
        try {
          inet = InetAddress.getLocalHost();
          ipAddress = inet.getHostAddress();
        } catch (UnknownHostException e) {
          e.printStackTrace();
        }
      }
    }
    String ipSeparate = ",";
    int ipLength = 15;
    if (ipAddress != null && ipAddress.length() > ipLength &&
        ipAddress.contains(ipSeparate)) {
      ipAddress = ipAddress.substring(0, ipAddress.indexOf(ipSeparate));
    }
    return ipAddress;
  }

  private NetworkUtil() {}
}
