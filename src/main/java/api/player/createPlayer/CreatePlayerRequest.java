package api.player.createPlayer;

import api.player.AbstractRequest;
import api.player.RequestDto;
import api.player.endpoints.PlayerEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j;

@Log4j
public class CreatePlayerRequest extends AbstractRequest {
  private final String editor;

  public CreatePlayerRequest(String editor) {
    super(PlayerEndpoints.CREATE.getValue());
    this.editor = editor;
  }

  @Step("Create player")
  @Override
  public Response sendRequest(RequestDto requestDto, int expectedStatusCode) {
    log.info("============================create new player==============================");
    CreatePlayerRequestDto createPlayerRequestDto = (CreatePlayerRequestDto) requestDto;
    return given()
        .pathParam("editor", editor)
        .queryParam("age", createPlayerRequestDto.getAge())
        .queryParam("gender", createPlayerRequestDto.getGender())
        .queryParam("login", createPlayerRequestDto.getLogin())
        .queryParam("password", createPlayerRequestDto.getPassword())
        .queryParam("role", createPlayerRequestDto.getRole())
        .queryParam("screenName", createPlayerRequestDto.getScreenName())
        .when()
        .log()
        .all()
        .get()
        .then()
        .statusCode(expectedStatusCode)
        .extract()
        .response();
  }
}
