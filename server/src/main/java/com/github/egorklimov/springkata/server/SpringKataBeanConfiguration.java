package com.github.egorklimov.springkata.server;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ServletComponentScan
@ComponentScan(basePackages = {"com.github.egorklimov.springkata"})
public class SpringKataBeanConfiguration {

//  private final DataSource dataSource;
//
//  @Autowired
//  public SpringKataBeanConfiguration(final DataSource dataSource) {
//    this.dataSource = dataSource;
//  }
//
//  @Bean
//  public JdbcService postgres() {
//    return new JdbcService(dataSource);
//  }
}
