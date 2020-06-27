package com.github.egorklimov.springkata.api;

import com.github.egorklimov.springkata.api.dto.BranchDTO;
import com.github.egorklimov.springkata.api.model.BranchWithDistanceResponse;
import com.github.egorklimov.springkata.integration.jdbc.JdbcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

  private final JdbcService postgres;
  private static final int EARTH_RADIUS = 6371 * 1000;

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

  public Optional<BranchWithDistanceResponse> loadNearest(Double lon, Double lat) {
    List<BranchDTO> allBranches = postgres.queryFor(
            "SELECT * FROM BRANCHES;",
            rs -> {
              List<BranchDTO> branchDTOList = new ArrayList<>();
              while (rs.next()) {
                branchDTOList.add(
                        new BranchDTO(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getDouble("lon"),
                                rs.getDouble("lat"),
                                rs.getString("address")
                        )
                );
              }
              return branchDTOList;
            }
    );
    return allBranches.stream()
            .map(b -> new BranchWithDistanceResponse(
                    b.getId(),
                    b.getTitle(),
                    b.getLon(),
                    b.getLat(),
                    b.getAddress(),
                    Math.round(
                            2 * EARTH_RADIUS * Math.asin(
                                    Math.sqrt(
                                            Math.pow(
                                                    Math.sin((Math.toRadians(b.getLat()) - Math.toRadians(lat)) / 2),
                                                    2
                                            )
                                            +
                                            Math.cos(Math.toRadians(b.getLat())) * Math.cos(Math.toRadians(lat))
                                            *
                                            Math.pow(
                                                    Math.sin((Math.toRadians(b.getLon()) - Math.toRadians(lon)) / 2),
                                                    2
                                            )
                                    )
                            )
                    )
            ))
            //.peek(d -> System.out.println(String.format("\n\n%s\n\n", d.toString())))
            .min(Comparator.comparingLong(BranchWithDistanceResponse::getDistance));
  }
}
