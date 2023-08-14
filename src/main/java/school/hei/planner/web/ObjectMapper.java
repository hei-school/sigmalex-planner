package school.hei.planner.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapper {
  private final com.fasterxml.jackson.databind.ObjectMapper om;

  public ObjectMapper() {
    om = new com.fasterxml.jackson.databind.ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);
    om.registerModule(new JavaTimeModule());
    om.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }

  public String writeValueAsString(Object o) {
    try {
      return om.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T readValue(String s, Class<T> clazz) {
    try {
      return om.readValue(s, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
