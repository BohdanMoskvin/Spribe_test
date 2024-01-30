package api.tests.player;

import api.player.createPlayer.CreatePlayerRequest;
import api.player.createPlayer.CreatePlayerRequestDto;
import api.player.getAllPlayer.GetAllPlayerRequest;
import api.player.getAllPlayer.GetAllPlayersResponseDto;
import api.player.getAllPlayer.Player;
import api.player.getByIdPlayer.GetByIdPlayerRequest;
import api.player.getByIdPlayer.GetByIdRequestDto;
import api.player.getByIdPlayer.GetByIdResponseDto;
import api.player.playerData.PlayerRoleData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static api.player.utils.JsonParser.deserialize;

public class GetPlayerById extends BaseTest {
    @Test
    public void testGetById() {
        GetByIdResponseDto getSupervisor = getPlayerWithSpecificRoleById(PlayerRoleData.SUPERVISOR.getRole());
        CreatePlayerRequestDto createPlayerRequestDto = new TestDataFactory().generatePlayerUserRequest();

        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);
        GetByIdResponseDto playerResponseDto = deserialize(response, GetByIdResponseDto.class);

        GetByIdRequestDto getByIdRequestDto = GetByIdRequestDto.builder().playerId(playerResponseDto.getId()).build();

        Response responseById = new GetByIdPlayerRequest().sendRequest(getByIdRequestDto, HttpStatus.SC_OK);

        GetByIdResponseDto getByIdResponseDto = deserialize(responseById, GetByIdResponseDto.class);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getByIdResponseDto.getId(), "Id should not be null");
        softAssert.assertEquals(
                getByIdResponseDto.getScreenName(), createPlayerRequestDto.getScreenName(), "ScreenName is incorrect");
        softAssert.assertEquals(
                getByIdResponseDto.getGender(), createPlayerRequestDto.getGender(), "Gender is incorrect");
        softAssert.assertEquals(
                getByIdResponseDto.getAge(), createPlayerRequestDto.getAge(), "ScreenName is incorrect");
        softAssert.assertEquals(
                getByIdResponseDto.getRole(), createPlayerRequestDto.getRole(), "Role is incorrect");
        softAssert.assertAll();
    }

    @Step("Get player with invalid id")
    @Test()
    public void testFindNonExistentUser() {
        Integer invalidId = 123123123;

        Response response = new GetAllPlayerRequest().sendEmptyRequest(HttpStatus.SC_OK);
        GetAllPlayersResponseDto responseDto = deserialize(response, GetAllPlayersResponseDto.class);
        List<Player> players = responseDto.getPlayers();

        boolean nonExistentUserFound = players.stream()
                .anyMatch(player -> player.getId().equals(invalidId));


        if (!nonExistentUserFound) {

            GetByIdRequestDto getByIdRequestDto = GetByIdRequestDto.builder().playerId(invalidId).build();
            new GetByIdPlayerRequest().sendRequest(getByIdRequestDto, HttpStatus.SC_NOT_FOUND);
        }

    }
}
