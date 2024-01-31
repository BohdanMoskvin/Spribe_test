package api.player.deletePlayer;

import api.player.AbstractRequest;
import api.player.RequestDto;
import api.player.endpoints.PlayerEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j;

@Log4j
public class DeletePlayerRequest extends AbstractRequest {

    private final String editor;

    public DeletePlayerRequest(String editor) {
        super(PlayerEndpoints.DELETE.getEndpoint());
        this.editor = editor;
    }

    @Step("Delete player request")
    @Override
    public Response sendRequest(RequestDto requestDto, int expectedStatusCode) {
        log.info("=======================Delete player request=========================");
        return given()
                .pathParam("editor", editor)
                .body(requestDto)
                .when()
                .log().all()
                .delete()
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }
}
