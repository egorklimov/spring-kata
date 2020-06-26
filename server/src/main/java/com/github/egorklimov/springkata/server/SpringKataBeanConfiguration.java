package com.github.egorklimov.springkata.server;

import com.github.egorklimov.springkata.integration.jdbc.JdbcSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
@ServletComponentScan
@ComponentScan(basePackages = {"com.github.egorklimov.springkata"})
public class SpringKataBeanConfiguration {

  private final DataSource dataSource;

  @Autowired
  public SpringKataBeanConfiguration(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Bean
  public JdbcSource postgres() {
    return new JdbcSource(dataSource);
  }
}
