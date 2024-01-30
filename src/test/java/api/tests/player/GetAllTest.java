package api.tests.player;

import api.player.createPlayer.CreatePlayerRequest;
import api.player.createPlayer.CreatePlayerRequestDto;
import api.player.getAllPlayer.GetAllPlayerRequest;
import api.player.getAllPlayer.GetAllPlayersResponseDto;
import api.player.getAllPlayer.Player;
import api.player.getByIdPlayer.GetByIdResponseDto;
import api.player.playerData.PlayerRoleData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static api.player.utils.JsonParser.deserialize;

@Log4j
public class GetAllTest extends BaseTest {
    @Description("get all player")
    @Test
    public void testGetAllPlayers() {
        Response response = new GetAllPlayerRequest().sendEmptyRequest(HttpStatus.SC_OK);
        GetAllPlayersResponseDto responseDto = deserialize(response, GetAllPlayersResponseDto.class);
        List<Player> players = responseDto.getPlayers();

        Assert.assertFalse(players.isEmpty(), "List of players should not be empty");
        Assert.assertTrue(areAllPlayersFieldsNonNull(players), "All players should have non-null fields");
    }

    @Step("Are All Players Fields Non Null")
    private boolean areAllPlayersFieldsNonNull(List<Player> players) {
        log.info("=============================Are All Players Fields Non Null============================");
        return players.stream()
                .allMatch(player -> {
                    boolean result = player.getId() != null &&
                            player.getScreenName() != null &&
                            player.getGender() != null &&
                            player.getAge() != null &&
                            player.getRole() != null;

                    if (!result) {
                        System.out.println("Mismatch found for player with ID: " + player.getId());
                        System.out.println("ID: " + player.getId());
                        System.out.println("ScreenName: " + player.getScreenName());
                        System.out.println("Gender: " + player.getGender());
                        System.out.println("Age: " + player.getAge());
                        System.out.println("Role: " + player.getRole());
                    }

                    return result;
                });
    }

    @Description("Number of players no more than 10")
    @Test
    public void testGetMoreTenPlayers() {

        int numberOfPlayersToCreate = 15;

        GetByIdResponseDto getSupervisor = getPlayerWithSpecificRoleById(PlayerRoleData.SUPERVISOR.getRole());

        for (int i = 0; i < numberOfPlayersToCreate; i++) {
            CreatePlayerRequestDto createPlayerRequestDto = new TestDataFactory().generatePlayerUserRequest();
            Response response = new CreatePlayerRequest(getSupervisor.getLogin())
                    .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);


        }
        Response response = new GetAllPlayerRequest().sendEmptyRequest(HttpStatus.SC_OK);
        GetAllPlayersResponseDto responseDto = deserialize(response, GetAllPlayersResponseDto.class);
        List<Player> players = responseDto.getPlayers();

        Assert.assertTrue(players.size() > 10, "Number of players no more than 10");

    }
}


