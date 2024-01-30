package api.player.getByIdPlayer;

import api.player.AbstractRequest;
import api.player.RequestDto;
import api.player.endpoints.PlayerEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j;

@Log4j
public class GetByIdPlayerRequest extends AbstractRequest {
  public GetByIdPlayerRequest() {
    super(PlayerEndpoints.GET_BY_ID.getValue());
  }

  @Step("Get player by id")
  @Override
  public Response sendRequest(RequestDto requestDto, int expectedStatusCode) {
    log.info("=========================Get player by id=======================");
    return given()
        .body(requestDto)
        .when()
        .log()
        .all()
        .post()
        .then()
        .statusCode(expectedStatusCode)
        .extract()
        .response();
  }
}
