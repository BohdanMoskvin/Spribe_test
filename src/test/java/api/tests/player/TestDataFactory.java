package api.tests.player;

import api.player.createPlayer.CreatePlayerRequestDto;
import api.player.playerData.PlayerRoleData;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.ThreadLocalRandom;

@Log4j
public class TestDataFactory {

    @Step("generate player user request")
    public CreatePlayerRequestDto generatePlayerUserRequest() {
        log.info("=========================generate player user request=======================");
        String gender = ThreadLocalRandom.current().nextBoolean() ? "female" : "male";
        String password = generateRandomPassword();

        return CreatePlayerRequestDto.builder()
                .age(ThreadLocalRandom.current().nextInt(16, 61))
                .gender(gender)
                .login("Login" + System.currentTimeMillis())
                .password(password)
                .role(PlayerRoleData.USER.getRole())
                .screenName("user" + System.currentTimeMillis())
                .build();
    }

    @Step("generate player user request")
    public CreatePlayerRequestDto generatePlayerAdminRequest() {
        log.info("=========================generate player admin request=======================");
        String gender = ThreadLocalRandom.current().nextBoolean() ? "female" : "male";
        String password = generateRandomPassword();

        return CreatePlayerRequestDto.builder()
                .age(ThreadLocalRandom.current().nextInt(16, 61))
                .gender(gender)
                .login("Login" + System.currentTimeMillis())
                .password(password)
                .role(PlayerRoleData.ADMIN.getRole())
                .screenName("admin" + System.currentTimeMillis())
                .build();
    }

    public static String generateRandomPassword() {
        log.info("=========================generate random password=======================");
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int passwordLength = ThreadLocalRandom.current().nextInt(7, 16 + 1);

        StringBuilder passwordBuilder = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            passwordBuilder.append(randomChar);
        }

        return passwordBuilder.toString();
    }

    public static String generateRandomPassword(int length) {
        log.info("=========================generate random password=======================");
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder passwordBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            passwordBuilder.append(randomChar);
        }

        return passwordBuilder.toString();
    }
}
