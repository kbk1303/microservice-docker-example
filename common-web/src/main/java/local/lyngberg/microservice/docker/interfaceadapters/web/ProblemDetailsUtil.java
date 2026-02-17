// Comments in English
package local.lyngberg.microservice.docker.interfaceadapters.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

import local.lyngberg.microservice.docker.sharedkernel.domain.common.ConflictException;

public final class ProblemDetailsUtil {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private ProblemDetailsUtil() {}

  public record ParsedProblem(
      Integer status,
      String type,
      String detail,
      String key,
      Map<String, Object> args
  ) {}

  public static ParsedProblem parse(String body) {
    if (body == null || body.isBlank()) {
      return new ParsedProblem(null, null, null, null, Map.of());
    }
    try {
      JsonNode root = MAPPER.readTree(body);
      String key    = text(root, "key");
      String type   = text(root, "type");
      String detail = text(root, "detail");
      Integer status = (root.has("status") && root.get("status").canConvertToInt())
          ? root.get("status").asInt() : null;

      Map<String, Object> args = toMap(root.get("args"));
      return new ParsedProblem(status, type, detail, key, args);
    } catch (Exception ignore) {
      return new ParsedProblem(null, null, null, null, Map.of());
    }
  }

  public static ConflictException toConflictException(
      String body,
      String defaultKey,
      Map<String, Object> defaultArgs
  ) {
    ParsedProblem p = parse(body);
    String key = (p.key() != null && !p.key().isBlank()) ? p.key() : defaultKey;
    Map<String, Object> args = (p.args() != null && !p.args().isEmpty())
        ? p.args()
        : (defaultArgs != null ? defaultArgs : Map.of());
    return new ConflictException(key, args);
  }

  // --- helpers ---

  private static String text(JsonNode node, String field) {
    if (node == null || !node.hasNonNull(field)) return null;
    JsonNode v = node.get(field);
    return v.isTextual() ? v.asText() : v.toString();
  }

  private static Map<String, Object> toMap(JsonNode node) {
    if (node == null || !node.isObject()) return Collections.emptyMap();
    Map<String, Object> m = new LinkedHashMap<>();
    // Jackson 2.19+: iterate properties() set directly
    for (Map.Entry<String, JsonNode> e : node.properties()) {
      String k = e.getKey();
      JsonNode v = e.getValue();
      Object val = v.isTextual() ? v.asText()
                 : v.isNumber()  ? v.numberValue()
                 : v.isBoolean() ? v.asBoolean()
                 : v.isNull()    ? null
                 : v.toString();
      m.put(k, val);
    }
    return m;
  }
}
