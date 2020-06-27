package com.github.egorklimov.springkata.server;

import com.github.egorklimov.springkata.integration.ThrowingFunction;
import com.github.egorklimov.springkata.integration.jdbc.JdbcService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ResultSet;

@ContextConfiguration(initializers = {SpringKataApplicationTests.Initializer.class})
class SpringKataApplicationTests {

	private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>()
					.withDatabaseName("test")
					.withUsername("user")
					.withPassword("pass");

	private final JdbcService postgres;

	@Autowired
	SpringKataApplicationTests(final JdbcService postgres) {
		this.postgres = postgres;
	}

	@Disabled
	@Test
	void checkQueryToDb() {
		IntDatabaseRecord record = postgres.queryFor(
						IntDatabaseRecord.jdbcSelector(),
						IntDatabaseRecord.jdbcMapper
		);
		Assertions.assertEquals(1, record.getValue());
	}

	@Data
	@RequiredArgsConstructor
	private static class IntDatabaseRecord {

		private final Integer value;

		public static String jdbcSelector() {
			return "SELECT 1 AS VALUE;";
		}

		public static ThrowingFunction<ResultSet, IntDatabaseRecord> jdbcMapper = rs -> {
			if (rs.next()) {
				return new IntDatabaseRecord(rs.getInt("VALUE"));
			}
			throw new RuntimeException("Результат запроса пуст или содержит больше одной записи");
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
