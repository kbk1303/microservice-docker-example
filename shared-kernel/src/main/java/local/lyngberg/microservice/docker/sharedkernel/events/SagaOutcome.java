package local.lyngberg.microservice.docker.sharedkernel.events;

public enum SagaOutcome {
    SUCCEEDED, IDEMPOTENT, COMPENSATED, PARTIAL_FAILURE 
}
