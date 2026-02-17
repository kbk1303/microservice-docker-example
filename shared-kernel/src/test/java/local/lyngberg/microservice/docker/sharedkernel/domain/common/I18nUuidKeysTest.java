package local.lyngberg.microservice.docker.sharedkernel.domain.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class I18nUuidKeysTest {
    private ReloadableResourceBundleMessageSource ms;
    private final Locale DA = Locale.forLanguageTag("da");

    @BeforeEach
    void setUp() {
        var r = new ReloadableResourceBundleMessageSource();
        r.setBasenames("classpath:i18n/shared-messages", "classpath:i18n/web-messages");
        r.setDefaultEncoding("UTF-8");
        r.setFallbackToSystemLocale(false);
        r.setUseCodeAsDefaultMessage(false);
        r.setCacheMillis(-1);
        ms = r;
    }

    @Test
    void uuid_required_shouldResolveDanish() {
        // When
        String actual = ms.getMessage("uuid.required", null, DA);

        // Then
        assertEquals("UUID nøgle er krævet", actual);
    }

    @Test
    void uuid_invalid_shouldResolveDanish() {
        // When
        String actual = ms.getMessage("uuid.invalid", null, DA);

        // Then
        assertEquals("UUID nøgle er ugyldig", actual);
    }

    @Test
    void uuid_required_shouldResolveEnglish() {
        // When
        String actual = ms.getMessage("uuid.required", null, Locale.ENGLISH);

        // Then
        assertEquals("UUID key required", actual);
    }

    @Test
    void uuid_invalid_shouldResolveEnglish() {
        // When
        String actual = ms.getMessage("uuid.invalid", null, Locale.ENGLISH);

        // Then
        assertEquals("UUID key is invalid", actual);
    }

    @Test
    void domainException_messageKey_shouldResolveToDanish() {
        // Given (simulate what your ValidationException would do)
        DomainException ex = new DomainException(ErrorCode.VALIDATION, "uuid.required");

        // When
        String msgDa = ms.getMessage(ex.messageKey(), null, DA);

        // Then
        assertEquals("UUID nøgle er krævet", msgDa);
    }

    @Test
    void validationException_messageKey_shouldResolveToDanish() {
        // Given (simulate what your ValidationException would do)
        ValidationException ex = new ValidationException("uuid.required", Map.of("field", "field"));

        // When
        String msgDa = ms.getMessage(ex.messageKey(), null, DA);

        // Then
        assertEquals("UUID nøgle er krævet", msgDa);
    }
}
