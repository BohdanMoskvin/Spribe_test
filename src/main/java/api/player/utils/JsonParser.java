package api.player.utils;

import io.restassured.response.Response;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class JsonParser {
  public static <T> T deserialize(Response response, Class<T> type) {
    log.info("======================deserialize====================" + type.getName());
    return response.as(type);
  }

  public static <T> List<T> deserializeList(Response response, String path, Class<T> tClass) {
    log.info("========================deserialize list of===================" + tClass.getName());
    return response.jsonPath().getList(path, tClass);
  }
}
