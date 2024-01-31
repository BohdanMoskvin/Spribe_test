package api.tests.player;

import static api.player.TestGroups.*;
import static api.player.utils.JsonParser.deserialize;
import static api.tests.player.TestDataFactory.generateRandomPassword;

import api.player.createPlayer.CreatePlayerRequest;
import api.player.createPlayer.CreatePlayerRequestDto;
import api.player.createPlayer.CreatePlayerResponseDto;
import api.player.getByIdPlayer.GetByIdResponseDto;
import api.player.playerData.PlayerRoleData;
import io.qameta.allure.Description;
import io.restassured.response.Response;

import java.util.NoSuchElementException;

import lombok.extern.log4j.Log4j;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Log4j
public class CreatePlayerTest extends BaseTest {
    private CreatePlayerRequestDto createPlayerRequestDto;
    private GetByIdResponseDto getSupervisor;

    @Description("SetUpTestData")
    @BeforeMethod
    public void setUpTestData() {
        createPlayerRequestDto = new TestDataFactory().generatePlayerUserRequest();
        getSupervisor = getPlayerWithSpecificRoleById(PlayerRoleData.SUPERVISOR.getRole());
    }

    @Description("Create user by supervisor")
    @Test(groups = {PLAYER_CREATE, POSITIVE, ALL, API})
    public void testCreateUserBySupervisor() {
        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

        CreatePlayerResponseDto playerResponseDto =
                deserialize(response, CreatePlayerResponseDto.class);

        softAssertCreatePlayer(createPlayerRequestDto, playerResponseDto);
    }

    @DataProvider(name = "requestData")
    public Object[][] requestData() {

        CreatePlayerRequestDto data1 = new TestDataFactory().generatePlayerUserRequest();
        data1.setAge(null);

        CreatePlayerRequestDto data2 = new TestDataFactory().generatePlayerUserRequest();
        data2.setGender(null);

        CreatePlayerRequestDto data3 = new TestDataFactory().generatePlayerUserRequest();
        data3.setLogin(null);

        CreatePlayerRequestDto data4 = new TestDataFactory().generatePlayerUserRequest();
        data4.setPassword(null);

        CreatePlayerRequestDto data5 = new TestDataFactory().generatePlayerUserRequest();
        data5.setRole(null);

        CreatePlayerRequestDto data6 = new TestDataFactory().generatePlayerUserRequest();
        data6.setScreenName(null);

        return new Object[][]{{data1}, {data2}, {data3}, {data4}, {data5}, {data6}};
    }

    @Description("Create player with out required field")
    @Test(dataProvider = "requestData", groups = {PLAYER_CREATE, NEGATIVE, ALL, API})
    public void testQueryParamRequiredFields(CreatePlayerRequestDto createPlayerRequestDto) {
        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_BAD_REQUEST);

        Assert.assertNotNull(response.getBody().asString(), "Response body should not be null");
    }

    @DataProvider(name = "invalidAgeParameters")
    private Object[][] invalidAgeParameters() {
        return new Object[][]{{"15"}, {"61"}, {"age"}};
    }

    @Description("Create player with invalid age")
    @Test(dataProvider = "invalidAgeParameters", groups = {PLAYER_CREATE, NEGATIVE, ALL, API})
    public void testCreatePlayerWithInvalidAge(String age) {
        createPlayerRequestDto.setScreenName(age);

        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_BAD_REQUEST);

        Assert.assertNotNull(response.getBody().asString(), "Response body should not be null");
    }

    @Description("Create player by admin role")
    @Test(groups = {PLAYER_CREATE, POSITIVE, ALL, API})
    public void testCreatePlayerByAdminRole() {
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

        CreatePlayerResponseDto playerResponseDto =
                deserialize(response, CreatePlayerResponseDto.class);

        softAssertCreatePlayer(createPlayerRequestDto, playerResponseDto);

    }

    @Description("Create player by user role")
    @Test (groups = { PLAYER_CREATE, NEGATIVE, ALL, API})
    public void testCreatePlayerByUserRole() {
        GetByIdResponseDto getUser;

        try {
            getUser = getPlayerWithSpecificRoleById(PlayerRoleData.USER.getRole());

        } catch (NoSuchElementException e) {
            log.error("Player not found. Creating a new player");
//
            new CreatePlayerRequest(getSupervisor.getLogin())
                    .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

            getUser = getPlayerWithSpecificRoleById(PlayerRoleData.USER.getRole());
        }
        createPlayerRequestDto = new TestDataFactory().generatePlayerUserRequest();
        new CreatePlayerRequest(getUser.getLogin())
                .sendRequest(createPlayerRequestDto, HttpStatus.SC_FORBIDDEN);
    }

    @Description("Unique login filed")
    @Test (groups = { PLAYER_CREATE, NEGATIVE, ALL, API})
    public void testUniqueLoginField() {

        String anotherScreenName = "another" + System.currentTimeMillis();

        new CreatePlayerRequest(getSupervisor.getLogin())
                .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

        createPlayerRequestDto.setScreenName(anotherScreenName);

        new CreatePlayerRequest(getSupervisor.getLogin())
                .sendRequest(createPlayerRequestDto, HttpStatus.SC_BAD_REQUEST);
    }

    @Description("Unique screen name filed")
    @Test (groups = { PLAYER_CREATE, NEGATIVE, ALL, API})
    public void testUniqueScreenNameFieldForUsers() {

        String anotherLogin = "another" + System.currentTimeMillis();

        new CreatePlayerRequest(getSupervisor.getLogin())
                .sendRequest(createPlayerRequestDto, HttpStatus.SC_OK);

        createPlayerRequestDto.setScreenName(anotherLogin);

        new CreatePlayerRequest(getSupervisor.getLogin())
                .sendRequest(createPlayerRequestDto, HttpStatus.SC_BAD_REQUEST);
    }

    @DataProvider(name = "invalidPasswordParameters")
    private Object[][] invalidPasswordParameters() {
        return new Object[][]{
                {generateRandomPassword(6)}, {generateRandomPassword(16)}, {"tttttttt"}, {"1234567"}
        };
    }

    @Description("Create player with invalid password")
    @Test(dataProvider = "invalidPasswordParameters", groups = { PLAYER_CREATE, NEGATIVE, ALL, API})
    public void testCreatePlayerWithInvalidPassword(String password) {
        createPlayerRequestDto.setPassword(password);

        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_BAD_REQUEST);

        Assert.assertNotNull(response.getBody().asString(), "Response body should not be null");
    }

    @Description("Create player with fake gender")
    @Test(groups = { PLAYER_CREATE, NEGATIVE, ALL, API})
    public void testCreatePlayerWithFakeGender() {
        createPlayerRequestDto.setGender("fakeGender");

        Response response =
                new CreatePlayerRequest(getSupervisor.getLogin())
                        .sendRequest(createPlayerRequestDto, HttpStatus.SC_BAD_REQUEST);

        Assert.assertNotNull(response.getBody().asString(), "Response body should not be null");
    }
}
