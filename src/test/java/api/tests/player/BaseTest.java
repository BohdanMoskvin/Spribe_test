package api.tests.player;

import api.player.createPlayer.CreatePlayerRequestDto;
import api.player.createPlayer.CreatePlayerResponseDto;
import api.player.getAllPlayer.GetAllPlayerRequest;
import api.player.getAllPlayer.Player;
import api.player.getByIdPlayer.GetByIdPlayerRequest;
import api.player.getByIdPlayer.GetByIdRequestDto;
import api.player.getByIdPlayer.GetByIdResponseDto;
import api.player.updatePlayer.UpdatePlayerRequestDto;
import api.player.updatePlayer.UpdatePlayerResponseDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpStatus;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static api.player.utils.JsonParser.deserialize;
import static api.player.utils.JsonParser.deserializeList;

@Log4j
public class BaseTest {

    @Step("Get player with specific role")
    protected GetByIdResponseDto getPlayerWithSpecificRoleById(String role) {
        log.info(
                "============================get player with specific role================================");

        Response playersResponse = new GetAllPlayerRequest().sendEmptyRequest(HttpStatus.SC_OK);

        List<Player> players = deserializeList(playersResponse, "players", Player.class);
        Player player =
                findPlayerWithRole(
                        players, p -> p.getScreenName().toLowerCase().contains(role.toLowerCase()));

        GetByIdRequestDto requestDto = GetByIdRequestDto.builder().playerId(player.getId()).build();

        Response playerResponse = new GetByIdPlayerRequest().sendRequest(requestDto, HttpStatus.SC_OK);

        return deserialize(playerResponse, GetByIdResponseDto.class);
    }

    @Step("Find player with role from list players")
    protected Player findPlayerWithRole(List<Player> players, Predicate<Player> predicate) {
        log.info(
                "============================find player with role from list players=======================");

        return players.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Player not found"));
    }

    @Step("Assert create player")
    protected static void softAssertCreatePlayer(CreatePlayerRequestDto expected, CreatePlayerResponseDto actual) {
        log.info(
                "=================================Assert create player==========================================");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(actual.getId(), "Id should not be null");
        softAssert.assertEquals(actual.getScreenName(), expected.getScreenName(), "ScreenName is incorrect");
        softAssert.assertEquals(actual.getGender(), expected.getGender(), "Gender is incorrect");
        softAssert.assertEquals(actual.getAge(), expected.getAge(), "Age is incorrect");
        softAssert.assertEquals(actual.getRole(), expected.getRole(), "Role is incorrect");

        softAssert.assertAll();
    }

    @Step("Assert get by id player")
    protected static void softAssertGetById(CreatePlayerRequestDto expected, GetByIdResponseDto actual) {
        log.info(
                "=================================Assert create player==========================================");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(actual.getId(), "Id should not be null");
        softAssert.assertEquals(actual.getScreenName(), expected.getScreenName(), "ScreenName is incorrect");
        softAssert.assertEquals(actual.getGender(), expected.getGender(), "Gender is incorrect");
        softAssert.assertEquals(actual.getAge(), expected.getAge(), "Age is incorrect");
        softAssert.assertEquals(actual.getRole(), expected.getRole(), "Role is incorrect");

        softAssert.assertAll();
    }

    @Step("Assert update player")
    protected static void softAssertUpdatePlayer(UpdatePlayerRequestDto expected, CreatePlayerResponseDto expectedCreate, UpdatePlayerResponseDto actual) {
        log.info(
                "=================================Assert update player==========================================");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actual.getId(), expectedCreate.getId(), "Id is incorrect");
        softAssert.assertEquals(actual.getScreenName(), expected.getScreenName(), "ScreenName is incorrect");
        softAssert.assertEquals(actual.getGender(), expected.getGender(), "Gender is incorrect");
        softAssert.assertEquals(actual.getAge(), expected.getAge(), "Age is incorrect");
        softAssert.assertEquals(actual.getRole(), expected.getRole(), "Role is incorrect");

        softAssert.assertAll();
    }
}
