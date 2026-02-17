package local.lyngberg.microservice.docker.sharedkernel.domain.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class GuardsTest {
   // simple record and enum to exercise the overloads
  record DummyRec(String id) {}
  enum DummyEnum { A, B }

  // ---------- requireRecordNonNull (Record) ----------

  @Test
  void requireRecordNonNull_record_returnsSameInstance_whenNotNull() {
    var rec = new DummyRec("42");
    var out = Guards.requireRecordNonNull(rec, "rec.required", "field", "dummyRec");
    assertSame(rec, out);
  }

  @Test
  void requireRecordNonNull_record_throwsDomainException_whenNull() {
    var ex = assertThrows(DomainException.class,
        () -> Guards.requireRecordNonNull((DummyRec) null, "rec.required", "field", "dummyRec"));
    assertEquals(ErrorCode.VALIDATION, ex.code());
    assertEquals("rec.required", ex.messageKey());
    assertEquals("dummyRec", ex.args().get("field"));
  }

  // ---------- requireNonNull (generic) ----------

  @Test
  void requireNonNull_generic_returnsSameInstance_whenNotNull() {
    var s = "ok";
    var out = Guards.requireNonNull(s, "val.required", "field", "name");
    assertSame(s, out);
  }

  @Test
  void requireNonNull_generic_throwsDomainException_whenNull() {
    var ex = assertThrows(DomainException.class,
        () -> Guards.requireNonNull(null, "val.required", "field", "name"));
    assertEquals(ErrorCode.VALIDATION, ex.code());
    assertEquals("val.required", ex.messageKey());
    assertEquals("name", ex.args().get("field"));
  }

  // ---------- requireRecordNonNull (Enum overload) ----------

  @Test
  void requireRecordNonNull_enum_returnsSameInstance_whenNotNull() {
    var out = Guards.requireRecordNonNull(DummyEnum.A, "enum.required", "field", "dummyEnum");
    assertEquals(DummyEnum.A, out);
  }

  @Test
  void requireRecordNonNull_enum_throwsDomainException_whenNull() {
    var ex = assertThrows(DomainException.class,
        () -> Guards.requireRecordNonNull((DummyEnum) null, "enum.required", "field", "dummyEnum"));
    assertEquals(ErrorCode.VALIDATION, ex.code());
    assertEquals("enum.required", ex.messageKey());
    assertEquals("dummyEnum", ex.args().get("field"));
  }
}
