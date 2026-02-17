package local.lyngberg.microservice.docker.interfaceadapters.web;

import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ConstraintKeyRegistry {
    private final Map<String,String> map = new java.util.HashMap<>();
    public ConstraintKeyRegistry() {
        // fyld fra properties/yaml i praksis
        map.put("uk_person_email", "person.email.exists");
        map.put("uk_student_person", "student.person.unique");
        map.put("uq_email", "person.email.exists");
    }
    public String findKey(String constraintName) {
        if (constraintName == null) return null;
        return map.get(constraintName.toLowerCase(Locale.ROOT));
    }
}
