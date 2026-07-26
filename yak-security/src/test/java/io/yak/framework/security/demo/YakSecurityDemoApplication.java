package io.yak.framework.security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Yak Security 本地验证启动入口。
 *
 * <p>该类位于测试源码目录，仅用于开发阶段启动和接口联调，
 * 不会被打包到正式发布的 starter 中。</p>
 */
@SpringBootApplication
public class YakSecurityDemoApplication {

  /**
   * 启动 Yak Security 测试应用。
   *
   * @param args 启动参数
   */
  public static void main(String[] args) {
    SpringApplication.run(
            YakSecurityDemoApplication.class,
            args);
  }
}
