package local.lyngberg.microservice.docker.sharedkernel.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/** SagaOutcome enum under test:
 *  public enum SagaOutcome { SUCCEEDED, IDEMPOTENT, COMPENSATED, PARTIAL_FAILURE }
 */
public class SagaOutcomeTest {

    @Test
    @DisplayName("values() should contain exactly the expected constants in a stable order")
    void values_shouldContainExactly() {
        List<SagaOutcome> expected = List.of(
            SagaOutcome.SUCCEEDED,
            SagaOutcome.IDEMPOTENT,
            SagaOutcome.COMPENSATED,
            SagaOutcome.PARTIAL_FAILURE
        );
        assertEquals(expected, List.of(SagaOutcome.values()),
            "Enum constants changed (added/removed/reordered). Review callers and update test if intentional.");
    }
}
