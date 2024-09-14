import constants.Endpoints;
import constants.LoggerMessages;
import constants.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);

   @BeforeClass
    public void setUp() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(Endpoints.BASE_URI_USER)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        RestAssured.filters(new ResponseLoggingFilter());
    }

    private User sampleUser() {
        return new User().builder()
                .id(2)
                .username("testuser" + String.valueOf(new Random().nextInt(5000)))
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("test@test.ru")
                .password("password")
                .phone("999999999")
                .userStatus(0)
                .build();
    }
    private User sampleUser(User user) {
       user.setUsername("newtestuser" + String.valueOf(new Random().nextInt(5000)));
        return user;
    }

    private int createUser(User user) {
       return given()
                .body(user)
                .post()
                .getStatusCode();
    }

    private int getUserStatusCode(String username) {
        return given().pathParams("username", username)
                .get(Endpoints.GET_PUT_DELETE_USER)
                .getStatusCode();
    }

    private User getUserBody(String username) {
       return given().pathParam("username", username)
               .get(Endpoints.GET_PUT_DELETE_USER)
               .getBody().as(User.class);
    }

    @Test
    void createUserTest() {
        User user = sampleUser();
        LOGGER.info(LoggerMessages.SEND_POST_REQUEST);
        int statusCode = createUser(user);
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(statusCode, 200, LoggerMessages.CHECK_CODE_TO_BE_200 + statusCode);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        User apiUser = getUserBody(user.getUsername());
        Assert.assertEquals(user, apiUser, "Assertion failed: user should be: " + user + "but was: " + apiUser);
    }

    @Test
    void getUserTest() {
        User user = sampleUser();
        LOGGER.info(LoggerMessages.SEND_POST_REQUEST);
        createUser(user);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        int statusCode = getUserStatusCode(user.getUsername());
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(statusCode, 200, LoggerMessages.STATUS_CODE_200_FAILED + statusCode);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        User apiUser = getUserBody(user.getUsername());
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(user, apiUser, "Assertion failed: user should be: " + user + "but was: " + apiUser);
    }

    @Test
    void deleteUserTest() {
        User user = sampleUser();
        LOGGER.info(LoggerMessages.SEND_POST_REQUEST);
        createUser(user);
        int statusCode =
        given().pathParam("username", user.getUsername())
                .delete(Endpoints.GET_PUT_DELETE_USER)
                .getStatusCode();
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(statusCode, 200, LoggerMessages.STATUS_CODE_200_FAILED + statusCode);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        int statusCode2 =
        given().pathParam("username", user.getUsername())
                .get(Endpoints.GET_PUT_DELETE_USER)
                .getStatusCode();
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_404);
        Assert.assertEquals(statusCode2, 404, LoggerMessages.STATUS_CODE_404_FAILED + statusCode2);
    }

    @Test
    void updateUserTest() {
        User user = sampleUser();
        LOGGER.info(LoggerMessages.SEND_POST_REQUEST);
        createUser(user);
        LOGGER.info(LoggerMessages.UPDATE_USER);
        User newUser = sampleUser(user);
        LOGGER.info(LoggerMessages.SEND_PUT_REQUEST);
        int statusCode =
        given().pathParam("username", user.getUsername())
                .body(newUser)
                .put(Endpoints.GET_PUT_DELETE_USER)
                .getStatusCode();
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(statusCode, 200, LoggerMessages.STATUS_CODE_200_FAILED + statusCode);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        User checkUser =
        given().pathParam("username", newUser.getUsername())
                .get(Endpoints.GET_PUT_DELETE_USER)
                .getBody().as(User.class);
        LOGGER.info(LoggerMessages.CHECK_UPDATE);
        Assert.assertEquals(newUser, checkUser, "Assertion failed: new user should be: " + user + "but was: " + checkUser);
    }
}
