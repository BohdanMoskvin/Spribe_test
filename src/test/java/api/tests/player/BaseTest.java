package api.tests.player;

import static api.player.utils.JsonParser.deserialize;
import static api.player.utils.JsonParser.deserializeList;

import api.player.getAllPlayer.GetAllPlayerRequest;
import api.player.getAllPlayer.Player;
import api.player.getByIdPlayer.GetByIdPlayerRequest;
import api.player.getByIdPlayer.GetByIdRequestDto;
import api.player.getByIdPlayer.GetByIdResponseDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpStatus;

@Log4j
public class BaseTest {

  @Step("get player with specific role")
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

  @Step("find player with role from list players")
  protected Player findPlayerWithRole(List<Player> players, Predicate<Player> predicate) {
    log.info(
        "============================find player with role from list players=======================");

    return players.stream()
        .filter(predicate)
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("Player not found"));
  }
}
