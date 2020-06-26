package com.github.egorklimov.springkata.server;

import com.github.egorklimov.springkata.integration.ExceptionSafeFunction;
import com.github.egorklimov.springkata.integration.jdbc.JdbcEntityIdentifier;
import com.github.egorklimov.springkata.integration.jdbc.JdbcResponse;
import com.github.egorklimov.springkata.integration.jdbc.JdbcSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ResultSet;

@SpringBootTest
@ContextConfiguration(initializers = {SpringKataApplicationTests.Initializer.class})
class SpringKataApplicationTests {

	private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>()
					.withDatabaseName("test")
					.withUsername("user")
					.withPassword("pass");

	private final JdbcSource postgres;

	@Autowired
	SpringKataApplicationTests(final JdbcSource postgres) {
		this.postgres = postgres;
	}

	@Test
	void checkQueryToDb() {
		IntDatabaseRecord record = postgres.get(
						IntDatabaseRecord.jdbcSelector(),
						IntDatabaseRecord.jdbcMapper
		);
		Assertions.assertEquals(1, record.getValue());
	}

	@Data
	@RequiredArgsConstructor
	private static class IntDatabaseRecord {
		private final Integer value;

		public static JdbcEntityIdentifier jdbcSelector() {
			return new JdbcEntityIdentifier("SELECT 1 AS VALUE;");
		}

		public static ExceptionSafeFunction<JdbcResponse, IntDatabaseRecord> jdbcMapper = response -> {
			final ResultSet rs = response.getResultSet();
			while (rs.next()) {
				return new IntDatabaseRecord(rs.getInt("VALUE"));
			}
			throw new RuntimeException();
		};
	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			postgreSQLContainer.start();
			TestPropertyValues.of(
							"spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
							"spring.datasource.username=" + postgreSQLContainer.getUsername(),
							"spring.datasource.password=" + postgreSQLContainer.getPassword()
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}
}
