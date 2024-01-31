package api.tests.player;

import api.player.createPlayer.CreatePlayerRequest;
import api.player.createPlayer.CreatePlayerRequestDto;
import api.player.createPlayer.CreatePlayerResponseDto;
import api.player.deletePlayer.DeletePlayerRequest;
import api.player.deletePlayer.DeletePlayerRequestDto;
import api.player.getByIdPlayer.GetByIdPlayerRequest;
import api.player.getByIdPlayer.GetByIdRequestDto;
import api.player.getByIdPlayer.GetByIdResponseDto;
import api.player.playerData.PlayerRoleData;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static api.player.TestGroups.*;
import static api.player.TestGroups.API;
import static api.player.utils.JsonParser.deserialize;

@Log4j
public class DeletePlayerTest extends BaseTest {

    private CreatePlayerRequestDto createPlayerRequestDto;
    private GetByIdResponseDto getSupervisor;

    @BeforeMethod
    public void setUpMethod() {
        createPlayerRequestDto = new TestDataFactory().generatePlayerUserRequest();
        getSupervisor = getPlayerWithSpecificRoleById(PlayerRoleData.SUPERVISOR.getRole());
    }

    @Description("Delete user by supervisor")
    @Test (groups = { PLAYER_DELETE, POSITIVE, ALL, API})
    public void testDeleteUserBySupervisor() {

        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

        CreatePlayerResponseDto playerResponseDto = deserialize(response, CreatePlayerResponseDto.class);
        DeletePlayerRequestDto deletePlayerRequestDto = DeletePlayerRequestDto.builder().playerId(playerResponseDto.getId()).build();

        new DeletePlayerRequest(getSupervisor.getLogin())
                .sendRequest(deletePlayerRequestDto, HttpStatus.SC_NO_CONTENT);

        GetByIdRequestDto getByIdRequestDto = GetByIdRequestDto.builder().playerId(playerResponseDto.getId()).build();

        new GetByIdPlayerRequest().sendRequest(getByIdRequestDto, HttpStatus.SC_NOT_FOUND);

    }


    @Description("Delete user by admin")
    @Test(groups = { PLAYER_DELETE, NEGATIVE, ALL, API})
    public void testDeleteUserByAdmin() {

        GetByIdResponseDto getAdmin;

        try {
            getAdmin = getPlayerWithSpecificRoleById(PlayerRoleData.ADMIN.getRole());

        } catch (NoSuchElementException e) {
            log.error("Player not found. Creating a new player");
            CreatePlayerRequestDto createPlayerRequestDto =
                    new TestDataFactory().generatePlayerAdminRequest();

            new CreatePlayerRequest(getSupervisor.getLogin())
                    .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

            getAdmin = getPlayerWithSpecificRoleById(PlayerRoleData.ADMIN.getRole());
        }

        Response response =
                new CreatePlayerRequest(getAdmin.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);


        CreatePlayerResponseDto playerResponseDto = deserialize(response, CreatePlayerResponseDto.class);
        DeletePlayerRequestDto deletePlayerRequestDto = DeletePlayerRequestDto.builder().playerId(playerResponseDto.getId()).build();

        new DeletePlayerRequest(getAdmin.getLogin())
                .sendRequest(deletePlayerRequestDto, HttpStatus.SC_BAD_REQUEST);

    }

}
