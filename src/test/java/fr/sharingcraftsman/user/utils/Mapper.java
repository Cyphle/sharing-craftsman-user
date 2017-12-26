package fr.sharingcraftsman.user.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Mapper {
  private static ObjectMapper mapper = new ObjectMapper();

  public static <T> String fromObjectToJsonString(T object) throws JsonProcessingException {
    return mapper.writeValueAsString(object);
  }

  public static <T> T fromJsonStringToObject(String json, Class<T> type) throws IOException {
    return mapper.readValue(json, type);
  }
}
