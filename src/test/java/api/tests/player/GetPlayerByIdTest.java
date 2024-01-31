package api.tests.player;

import api.player.createPlayer.CreatePlayerRequest;
import api.player.createPlayer.CreatePlayerRequestDto;
import api.player.createPlayer.CreatePlayerResponseDto;
import api.player.getAllPlayer.GetAllPlayerRequest;
import api.player.getAllPlayer.GetAllPlayersResponseDto;
import api.player.getAllPlayer.Player;
import api.player.getByIdPlayer.GetByIdPlayerRequest;
import api.player.getByIdPlayer.GetByIdRequestDto;
import api.player.getByIdPlayer.GetByIdResponseDto;
import api.player.playerData.PlayerRoleData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.List;

import static api.player.TestGroups.*;
import static api.player.TestGroups.API;
import static api.player.utils.JsonParser.deserialize;

public class GetPlayerByIdTest extends BaseTest {
    @Description("Get player by id")
    @Test (groups = { PLAYER_GET_BY_ID, POSITIVE, ALL, API})
    public void testGetById() {
        GetByIdResponseDto getSupervisor = getPlayerWithSpecificRoleById(PlayerRoleData.SUPERVISOR.getRole());
        CreatePlayerRequestDto createPlayerRequestDto = new TestDataFactory().generatePlayerUserRequest();

        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

        CreatePlayerResponseDto playerResponseDto = deserialize(response, CreatePlayerResponseDto.class);

        GetByIdRequestDto getByIdRequestDto = GetByIdRequestDto.builder().playerId(playerResponseDto.getId()).build();

        Response responseById = new GetByIdPlayerRequest().sendRequest(getByIdRequestDto, HttpStatus.SC_OK);

        GetByIdResponseDto getByIdResponseDto = deserialize(responseById, GetByIdResponseDto.class);

        softAssertGetById(createPlayerRequestDto, getByIdResponseDto);
    }

    @Step("Get player with invalid id")
    @Test(groups = { PLAYER_GET_BY_ID, NEGATIVE, ALL, API})
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
