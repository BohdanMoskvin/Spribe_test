package api.tests.player;

import api.player.createPlayer.CreatePlayerRequest;
import api.player.createPlayer.CreatePlayerRequestDto;
import api.player.createPlayer.CreatePlayerResponseDto;
import api.player.getByIdPlayer.GetByIdResponseDto;
import api.player.playerData.PlayerRoleData;
import api.player.updatePlayer.UpdatePlayerRequest;
import api.player.updatePlayer.UpdatePlayerRequestDto;
import api.player.updatePlayer.UpdatePlayerResponseDto;
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
public class UpdatePlayerTest extends BaseTest {
    private UpdatePlayerRequestDto updatePlayerRequestDto;
    private CreatePlayerRequestDto createPlayerRequestDto;
    private GetByIdResponseDto getSupervisor;

    @BeforeMethod
    public void setUpMethod() {
        updatePlayerRequestDto = UpdatePlayerRequestDto.builder()
                .age(19)
                .gender("female")
                .screenName("userAnothe1123r")
                .role("admin")
                .build();

        createPlayerRequestDto = new TestDataFactory().generatePlayerUserRequest();
        getSupervisor = getPlayerWithSpecificRoleById(PlayerRoleData.SUPERVISOR.getRole());
    }


    @Description("Update user to admin  by supervisor")
    @Test (groups = { PLAYER_UPDATE, POSITIVE, ALL, API})
    public void testUpdateUserToAdmin() {

        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

        CreatePlayerResponseDto playerResponseDto = deserialize(response, CreatePlayerResponseDto.class);

        Response response1 = new UpdatePlayerRequest(getSupervisor.getLogin(), playerResponseDto.getId())
                .sendRequest(updatePlayerRequestDto, HttpStatus.SC_OK);

        UpdatePlayerResponseDto updatePlayerResponseDto = deserialize(response1, UpdatePlayerResponseDto.class);

        softAssertUpdatePlayer(updatePlayerRequestDto, playerResponseDto, updatePlayerResponseDto);

    }

    @Description("Update admin to user by supervisor")
    @Test(groups = { PLAYER_UPDATE, POSITIVE, ALL, API})
    public void testUpdateAdminToUser() {
        createPlayerRequestDto.setRole(PlayerRoleData.ADMIN.getRole());

        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

        CreatePlayerResponseDto playerResponseDto = deserialize(response, CreatePlayerResponseDto.class);

        updatePlayerRequestDto.setRole("user");

        Response response1 = new UpdatePlayerRequest(getSupervisor.getLogin(), playerResponseDto.getId())
                .sendRequest(updatePlayerRequestDto, HttpStatus.SC_OK);

        UpdatePlayerResponseDto updatePlayerResponseDto = deserialize(response1, UpdatePlayerResponseDto.class);

        softAssertUpdatePlayer(updatePlayerRequestDto, playerResponseDto, updatePlayerResponseDto);
    }

    @Description("Update user by admin")
    @Test (groups = { PLAYER_UPDATE, NEGATIVE, ALL, API})
    public void testUpdateUserByAdmin() {
        GetByIdResponseDto getAdmin;

        try {
            getAdmin = getPlayerWithSpecificRoleById(PlayerRoleData.ADMIN.getRole());

        } catch (NoSuchElementException e) {
            log.error("Player not found. Creating a new player");
            CreatePlayerRequestDto createAdminRequestDto =
                    new TestDataFactory().generatePlayerAdminRequest();

            new CreatePlayerRequest(getSupervisor.getLogin())
                    .sendRequest(createAdminRequestDto, HttpStatus.SC_OK);

            getAdmin = getPlayerWithSpecificRoleById(PlayerRoleData.ADMIN.getRole());
        }

        Response response =
                new CreatePlayerRequest(getAdmin.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

        CreatePlayerResponseDto playerResponseDto = deserialize(response, CreatePlayerResponseDto.class);

        new UpdatePlayerRequest(getAdmin.getLogin(), playerResponseDto.getId())
                .sendRequest(updatePlayerRequestDto, HttpStatus.SC_BAD_REQUEST);

    }

}
