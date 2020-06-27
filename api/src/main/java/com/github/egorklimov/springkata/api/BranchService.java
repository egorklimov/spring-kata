package com.github.egorklimov.springkata.api;

import com.github.egorklimov.springkata.api.dto.BranchDTO;
import com.github.egorklimov.springkata.integration.jdbc.JdbcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BranchService {

  private final JdbcService postgres;

  @Autowired
  public BranchService(final JdbcService postgres) {
    this.postgres = postgres;
  }

  public Optional<BranchDTO> loadById(Long id) {
    return postgres.queryFor(
            String.format(
                    "SELECT * FROM BRANCHES WHERE id = %s", id
            ),
            rs -> {
              if (rs.next()) {
                return Optional.of(
                        new BranchDTO(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getDouble("lon"),
                                rs.getDouble("lat"),
                                rs.getString("address")
                        )
                );
              }
              return Optional.empty();
            }
    );
  }
}
