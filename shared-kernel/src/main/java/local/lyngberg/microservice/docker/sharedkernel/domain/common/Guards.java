package local.lyngberg.microservice.docker.sharedkernel.domain.common;

import java.util.Map;

public final class Guards {

    private Guards() {}


     /**
     * Guard that throws a domain-level DomainException using an i18n key.
     * Extra args can be passed (e.g., field names) and will be surfaced in ProblemDetails.
     * Protects record objects
     */
    public static <T extends Record> T requireRecordNonNull(
            T value,
            String messageKey,
            String argKey,
            String argVal) {
        // Guard: if the record reference is null, throw a domain-level validation error
        if (value == null) {
            // Adapt to your DomainException signature:
            // e.g. new DomainException(ErrorCode.VALIDATION, messageKey, Map.of(argKey, argVal))
            throw new DomainException(ErrorCode.VALIDATION, messageKey, Map.of(argKey, argVal));
        }
        return value;
    }

    public static <C> C  requireNonNull(
            C value,
            String messageKey,
            String argKey,
            String argVal) {
        // Guard: if the record reference is null, throw a domain-level validation error
        if (value == null) {
            // Adapt to your DomainException signature:
            // e.g. new DomainException(ErrorCode.VALIDATION, messageKey, Map.of(argKey, argVal))
            throw new DomainException(ErrorCode.VALIDATION, messageKey, Map.of(argKey, argVal));
        }
        return value;
    }

    /**
     * Guard that throws a domain-level DomainException using an i18n key.
     * Extra args can be passed (e.g., field names) and will be surfaced in ProblemDetails.
     * Protects Enum
     */
    public static <E extends Enum<E>> E requireRecordNonNull(
            E value,
            String messageKey,
            String argKey,
            String argVal) {
        // Guard: if the record reference is null, throw a domain-level validation error
        if (value == null) {
            // Adapt to your DomainException signature:
            // e.g. new DomainException(ErrorCode.VALIDATION, messageKey, Map.of(argKey, argVal))
            throw new DomainException(ErrorCode.VALIDATION, messageKey, Map.of(argKey, argVal));
        }
        return value;
    }

}
