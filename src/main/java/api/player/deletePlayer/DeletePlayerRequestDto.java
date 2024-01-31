package api.player.deletePlayer;

import api.player.RequestDto;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class DeletePlayerRequestDto implements RequestDto {
    private Integer playerId;
}
