package api.player.getAllPlayer;

import java.util.ArrayList;
import lombok.Data;

@Data
public class GetAllPlayersResponseDto {
  private ArrayList<Player> players;
}
