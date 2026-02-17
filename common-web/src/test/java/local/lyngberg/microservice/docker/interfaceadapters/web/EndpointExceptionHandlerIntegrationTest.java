package local.lyngberg.microservice.docker.interfaceadapters.web;

/* ------------------start ChatGPT suggestion ------------------------ */

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
class EndpointExceptionHandlerIntegrationTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;
  @Test
  void domainConflict409() throws Exception {
    mvc.perform(get("/__test/domain-conflict"))
    .andExpect(status().isConflict())
    // Use value(...) overload to avoid Hamcrest generics warnings
    .andExpect(jsonPath("$.type").value("about:blank#conflict"))
    .andExpect(jsonPath("$.detail").value("Email is already in use."))
    .andExpect(jsonPath("$.key").value("person.email.exists"))
    .andExpect(jsonPath("$.args.field").value("email"));
  }
  
  @Test
        void bodyValidation400() throws Exception {
        var badBody = Map.of("name", ""); // @NotBlank fails

        var mvcResult =
        mvc.perform(post("/__test/body")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(om.writeValueAsString(badBody))))
                .andExpect(status().isBadRequest())
                // Brug value(...) overloads (ingen Hamcrest)
                .andExpect(jsonPath("$.type").value("about:blank#validation"))
                .andExpect(jsonPath("$.detail").value("One or more fields are invalid"))
                // sørg for at feltet findes
                .andExpect(jsonPath("$.errors.name").exists())
                .andReturn();

        // Tjek "ikke tom" uden Hamcrest:
        var json = mvcResult.getResponse().getContentAsString();
        // enkel, uden ekstra libs:
        var node = om.readTree(json);
        var nameErr = node.at("/errors/name").asText();  // "" hvis tom/ikke-streng
        org.assertj.core.api.Assertions.assertThat(nameErr).isNotBlank();
        }

  @Test
  void malformedJson400() throws Exception {
    // invalid JSON -> HttpMessageNotReadableException
    mvc.perform(post("/__test/body")
        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
        .content("{ invalid json"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.type").value("about:blank#malformed_json"))//, is("about:blank#malformed_json")))
      .andExpect(jsonPath("$.detail").value("Malformed JSON request body"));//, is("Malformed JSON request body"))); // your handler returns a key; adapt if needed
  }

  @Test
  void missingParam400() throws Exception {
    var res = mvc.perform(get("/__test/needs-q"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.type").value("about:blank#missing_parameter"))//, is("about:blank#missing_parameter")))
      .andExpect(jsonPath("$.type").value("about:blank#missing_parameter"))
      .andReturn();

      var detail = om.readTree(res.getResponse().getContentAsString()).path("detail").asText();
      org.assertj.core.api.Assertions.assertThat(detail).contains("Missing parameter: q");
  }


  @Test
  void typeMitchMatch400() throws Exception {
        var res = mvc.perform(get("/__test/type?id=abc"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.type").value("about:blank#type_mismatch"))//, is("about:blank#type_mismatch")))
          .andExpect(jsonPath("$.args.param").value("id"))
          .andReturn();

        var detail = om.readTree(res.getResponse().getContentAsString()).path("detail").asText();
       org.assertj.core.api.Assertions.assertThat(detail).contains("expected Long");
  }

  @Test
  void paramConstraint400() throws Exception {
    mvc.perform(get("/__test/age?age=10"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.type").value("about:blank#validation"))//, is("about:blank#validation")))
      .andExpect(jsonPath("$.detail").value("Parameter validation failed"));//, is("Parameter validation failed")));
  }

  @Test
  void dataIntegrity_unique409_postgres() throws Exception {
    mvc.perform(get("/__test/db/unique"))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.type").value("about:blank#unique_violation"))//, is("about:blank#unique_violation")))
      .andExpect(jsonPath("$.detail").value("Unique constraint violated"));//, is("Unique constraint violated")));
  }

  @Test
  void dataIntegrity_unique409_sqlserver() throws Exception {
    mvc.perform(get("/__test/db/sqlserver-unique"))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.type").value("about:blank#unique_violation"))//, is("about:blank#unique_violation")))
      .andExpect(jsonPath("$.detail").value("Unique constraint violated"));//, is("Unique constraint violated")));
  }

  @Test
  void dataIntegrity_conflict409_fallback() throws Exception {
    mvc.perform(get("/__test/db/conflict"))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.type").value("about:blank#conflict"))//, is("about:blank#conflict")))
      .andExpect(jsonPath("$.detail").value("Conflict"));//), is("Conflict")));
  }

  @Test
  void generic500() throws Exception {
    mvc.perform(get("/__test/boom"))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.type").value("about:blank#internal_error"))//, is("about:blank#internal_error")))
      .andExpect(jsonPath("$.detail").value("Internal server error"));//, is("Internal server error")));
  }
}
