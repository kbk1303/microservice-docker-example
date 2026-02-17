package local.lyngberg.microservice.docker.sharedkernel.domain.common;

import java.util.Map;

public class ValidationException extends DomainException {
    public ValidationException(String messageKey, Map<String,Object> args) {
        super(ErrorCode.VALIDATION, messageKey, args);
    }
}
