package local.lyngberg.microservice.docker.interfaceadapters.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import local.lyngberg.microservice.docker.sharedkernel.domain.common.ConflictException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/__test")
@Validated
class DummyController {

  // ---- DomainException (mapped by your advice) ----
  @GetMapping("/domain-conflict")
  public void domainConflict() {
    throw new ConflictException("person.email.exists", Map.of("field","email"));
  }

  // ---- @RequestBody bean validation ----
  public record CreateReq(@NotBlank String name) { }

  @PostMapping(path="/body", consumes = "application/json")
  public String create(@Valid @RequestBody CreateReq req) {
    return "ok";
  }

  // ---- Missing parameter ----
  @GetMapping("/needs-q")
  public String needsQ(@RequestParam("q") String q) {
    return q;
  }

  // ---- Type mismatch (?id=abc) ----
  @GetMapping("/type")
  public String type(@RequestParam("id") Long id) {
    return String.valueOf(id);
  }

  // ---- Parameter validation (@Min) -> ConstraintViolationException ----
  @GetMapping("/age")
  public String age(@RequestParam("age") @Min(18) int age) {
    return String.valueOf(age);
  }

  // ---- DB unique (Postgres/H2 23505) ----
  @GetMapping("/db/unique")
  public void dbUnique() {
    throw new DataIntegrityViolationException("dup", new SQLException("duplicate", "23505", 0));
  }

  // ---- DB unique (SQL Server: SQLState 23000 + vendor 2627) ----
  @GetMapping("/db/sqlserver-unique")
  public void dbSqlServerUnique() {
    throw new DataIntegrityViolationException("dup", new SQLException("violation", "23000", 2627));
  }

  // ---- DB generic conflict fallback ----
  @GetMapping("/db/conflict")
  public void dbConflict() {
    throw new DataIntegrityViolationException("other", new SQLException("other", "99999", 0));
  }

  // ---- Generic error -> fallback 500 ----
  @GetMapping("/boom")
  public void boom() {
    throw new IllegalStateException("boom");
  }
}


