package local.lyngberg.microservice.docker.sharedkernel.domain.common.valueobjects.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import local.lyngberg.microservice.docker.sharedkernel.domain.common.ValidationException;

public class UUIDUtilTest {

    private ReloadableResourceBundleMessageSource ms;
    private final Locale DA = Locale.forLanguageTag("da");

    @BeforeEach
    public void setup() {
        var r = new ReloadableResourceBundleMessageSource();
        r.setBasenames("classpath:i18n/shared-messages", "classpath:i18n/web-messages");
        r.setDefaultEncoding("UTF-8");
        r.setFallbackToSystemLocale(false);
        r.setUseCodeAsDefaultMessage(false);
        r.setCacheMillis(-1);
        ms = r;

    }

    //CTOR is private
    @Test
    public void privateConstructor_shouldInstantiate() throws Exception {
        var constructor = UUIDUtil.class.getDeclaredConstructors()[0];
        assertTrue(constructor.getModifiers() == java.lang.reflect.Modifier.PRIVATE);
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
    public void happyPath_newId_shouldCreateWithoutErrors() {
        var expected = UUID.randomUUID();
        try(MockedStatic<UUID> mocked = org.mockito.Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(expected);
            UUID actual = UUIDUtil.newUuid();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void happypath_parseRequired_validUUID_shouldReturnUUID() {
        UUID expected = UUID.randomUUID();
        try(MockedStatic<UUID> mocked = org.mockito.Mockito.mockStatic(UUID.class)) {
            mocked.when(() -> UUID.fromString(anyString())).thenReturn(expected);
            UUID actual = UUIDUtil.parseRequired(expected.toString(), "TestField");
            assertEquals(expected, actual);
            mocked.verify(() -> UUID.fromString(expected.toString()), org.mockito.Mockito.times(1));
            mocked.verifyNoMoreInteractions();
            mocked.verify(() -> UUID.randomUUID(), org.mockito.Mockito.never());

        }
    }

    @Test
    public void unhappyPath_parseRequired_invalidUUID_shouldThrowIllegalArgumentException() {
        String invalidUUID = "invalid-uuid";
        try(MockedStatic<UUID> mocked = org.mockito.Mockito.mockStatic(UUID.class)) {
            mocked.when(() -> UUID.fromString(invalidUUID))
            .thenAnswer(inv -> {
                var arg = inv.getArgument(0);
                throw new IllegalArgumentException("Invalid UUID string: " + arg);
            });
            var ex = assertThrows(
                ValidationException.class,
                () -> {
                    UUIDUtil.parseRequired(invalidUUID, "TestField");
                }
            );
            assertEquals("UUID nøgle er ugyldig", ms.getMessage(ex.messageKey(), null, DA));
            assertEquals("TestField", ex.args().get("field"));
            assertEquals(invalidUUID, ex.args().get("value"));  
            mocked.verify(() -> UUID.fromString(invalidUUID), org.mockito.Mockito.times(1));
            mocked.verifyNoMoreInteractions();
            mocked.verify(() -> UUID.randomUUID(), org.mockito.Mockito.never());
        }
    }

    @Test
    public void unhappyPath_parseRequired_nullUUID_shouldThrowValidationException() {
        var ex = assertThrows(ValidationException.class, () -> {UUIDUtil.parseRequired(null, "TestField");});
        assertEquals("UUID nøgle er krævet", ms.getMessage(ex.messageKey(), null, DA));
        assertEquals("TestField", ex.args().get("field"));

    }

    @Test
    public void unhappyPath_require_not_blank_shouldThrowValidationException() {
        var ex = assertThrows(ValidationException.class, () -> {UUIDUtil.parseRequired("   ", "TestField");});
        assertEquals("UUID nøgle er krævet", ms.getMessage(ex.messageKey(), null, DA));
        assertEquals("TestField", ex.args().get("field"));
    }

    @Test
    public void happyPath_require_valid_UUID_shouldReturnUUID() {
        UUID expected = UUID.randomUUID();
        UUID actual = UUIDUtil.require(expected, "TestField");
        assertEquals(expected, actual);
    }

    @Test
    public void unhappypath_require_null_UUID_shouldThrowValidationException() {
        var ex = assertThrows(ValidationException.class, () -> {UUIDUtil.require(null, "TestField");});
        assertEquals("UUID nøgle er krævet", ms.getMessage(ex.messageKey(), null, DA));
        assertEquals("TestField", ex.args().get("field"));
    }

    @Test
    public void happyPath_newUuid_shouldReturnRandomUUID() {
        UUID expected = UUID.randomUUID();
        try(MockedStatic<UUID> mocked = org.mockito.Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(expected);
            UUID actual = UUIDUtil.newUuid();
            assertEquals(expected, actual);
            mocked.verify(UUID::randomUUID, org.mockito.Mockito.times(1));
            mocked.verifyNoMoreInteractions();  
        }
    }
}
