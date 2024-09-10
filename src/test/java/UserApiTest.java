import constants.Endpoints;
import constants.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserApiTest {

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
    private User newUser() {
        return new User().builder()
                .id(2)
                .username("newtestuser" + String.valueOf(new Random().nextInt(5000)))
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("test@test.ru")
                .password("password")
                .phone("999999999")
                .userStatus(0)
                .build();
    }

    private void createUser(User user) {
        given()
                .body(user)
                .when().post()
                .then().assertThat().statusCode(200);
    }

    private User getUser(String username) {
        return given().pathParams("username", username)
                .when().get(Endpoints.GET_PUT_DELETE_USER)
                .then().assertThat()
                .statusCode(200).extract().body().as(User.class);
    }

    @Test
    void createUserTest() {
        User user = sampleUser();
        createUser(user);
        User apiUser = getUser(user.getUsername());
        Assert.assertEquals(user, apiUser);
    }

    @Test
    void getUserTest() {
        User user = sampleUser();
        createUser(user);
        User apiUser = getUser(user.getUsername());
        Assert.assertEquals(user, apiUser);
    }

    @Test
    void deleteUserTest() {
        User user = sampleUser();
        createUser(user);
        given().pathParam("username", user.getUsername())
                .when().delete(Endpoints.GET_PUT_DELETE_USER)
                .then().assertThat().statusCode(200);
        given().pathParam("username", user.getUsername())
                .when().get(Endpoints.GET_PUT_DELETE_USER)
                .then().assertThat().statusCode(404);
    }

    @Test
    void updateUserTest() {
        User user = sampleUser();
        createUser(user);
        User newUser = newUser();
        given().pathParam("username", user.getUsername())
                .body(newUser)
                .when().put(Endpoints.GET_PUT_DELETE_USER)
                .then().assertThat().statusCode(200);
        User checkUser =
        given().pathParam("username", newUser.getUsername())
                .when().get(Endpoints.GET_PUT_DELETE_USER)
                .then().assertThat().statusCode(200).extract().body().as(User.class);
        Assert.assertEquals(newUser, checkUser);
    }
}
