package com.github.egorklimov.springkata.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egorklimov.springkata.api.dto.BranchDTO;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BranchController {

  private final BranchService branchService;

  @Autowired
  public BranchController(final BranchService branchService) {
    this.branchService = branchService;
  }

  @GetMapping("branches/{id}")
  public ResponseEntity<?> find(@PathVariable("id") Long id) {
    Optional<BranchDTO> founded = branchService.loadById(id);
    if (founded.isPresent()) {
      return ResponseEntity.ok(founded.get());
    }
    return new ResponseEntity<>(new BranchNotFoundResponse().toString(), HttpStatus.NOT_FOUND);
  }

  @Data
  private static class BranchNotFoundResponse {
    private String status;

    private BranchNotFoundResponse() {
      status = "branch not found";
    }

    @Override
    @SneakyThrows
    public String toString() {
      return new ObjectMapper().writeValueAsString(this);
    }
  }
}
