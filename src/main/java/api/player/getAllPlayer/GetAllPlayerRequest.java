package api.player.getAllPlayer;

import api.player.AbstractRequest;
import api.player.RequestDto;
import api.player.endpoints.PlayerEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j;

@Log4j
public class GetAllPlayerRequest extends AbstractRequest {
  public GetAllPlayerRequest() {
    super(PlayerEndpoints.GET_ALL.getEndpoint());
  }

  @Step("Get all players request")
  @Override
  public Response sendRequest(RequestDto requestDto, int expectedStatusCode) {
    log.info("=====================Get all players request===============================");
    return given()
        .when()
        .log()
        .all()
        .get()
        .then()
        .statusCode(expectedStatusCode)
        .extract()
        .response();
  }

  @Step("Get all players request")
  public Response sendEmptyRequest(int expectedStatusCode) {
    log.info("=====================Get all players request===============================");
    return sendRequest(null, expectedStatusCode);
  }
}
