package com.github.egorklimov.springkata.server;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan
@ComponentScan(basePackages = {"com.github.egorklimov.springkata"})
public class SpringKataBeanConfiguration {

//  @Autowired
//  DataSource dataSource;
//
//  public JdbcSource postgres() {
//    return new JdbcSource(dataSource);
//  }
}
