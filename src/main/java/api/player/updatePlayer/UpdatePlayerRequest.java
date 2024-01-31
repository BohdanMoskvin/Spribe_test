package api.player.updatePlayer;

import api.player.AbstractRequest;
import api.player.RequestDto;
import api.player.endpoints.PlayerEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j;

@Log4j
public class UpdatePlayerRequest extends AbstractRequest {
    private final int id;
    private final String editor;

    public UpdatePlayerRequest(String editor, int playerId) {
        super(PlayerEndpoints.UPDATE.getEndpoint());
        this.id = playerId;
        this.editor = editor;
    }

    @Step("Update player request")
    @Override
    public Response sendRequest(RequestDto requestDto, int expectedStatusCode)  {
        log.info("===========================Update player request===========================");
        return given()
                .pathParam("editor", editor)
                .pathParam("id", id)
                .body(requestDto)
                .when()
                .log().all()
                .patch()
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }
}
