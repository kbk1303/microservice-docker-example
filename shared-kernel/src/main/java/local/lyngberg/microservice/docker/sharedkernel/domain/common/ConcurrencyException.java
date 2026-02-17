package local.lyngberg.microservice.docker.sharedkernel.domain.common;

import java.util.Map;

public class ConcurrencyException extends DomainException {
    public ConcurrencyException(String messageKey, Map<String,Object> args) {
        super(ErrorCode.CONCURRENCY, messageKey, args);
    }
}
