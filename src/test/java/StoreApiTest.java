import constants.Endpoints;
import constants.Order;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;


public class StoreApiTest {

    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(Endpoints.BASE_URI_STORE)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        RestAssured.filters(new ResponseLoggingFilter());
    }

    private Order sampleOrder() {
        return new Order().builder()
                .id(new Random().nextInt(100))
                .petId(new Random().nextInt(10000))
                .quantity(new Random().nextInt(5))
                .shipDate("2012-04-23T18:25:43.511+0000")
                .status("available")
                .complete(false)
                .build();
    }

    private void createOrder(Order order) {
        given()
                .body(order)
                .when().post(Endpoints.POST_ORDER)
                .then().assertThat().statusCode(200);
    }

    private Order getOrder(int id) {
        return given().pathParams("id", id)
                .when().get(Endpoints.GET_ORDER)
                .then().assertThat()
                .statusCode(200).extract().body().as(Order.class);
    }

    @Test
    void createOrderTest() {
        Order order = sampleOrder();
        createOrder(order);
        Order apiOrder = getOrder(order.getId());
        Assert.assertEquals(order, apiOrder);
    }

    @Test
    void getOrderTest() {
        Order order = sampleOrder();
        createOrder(order);
        Order apiOrder = getOrder(order.getId());
        Assert.assertEquals(order, apiOrder);
    }

    @Test
    void deleteOrderTest() {
        Order order = sampleOrder();
        createOrder(order);
        given().pathParam("id", order.getId())
                .when().delete(Endpoints.DELETE_ORDER)
                .then().assertThat()
                .statusCode(200);
        given().pathParam("id", order.getId())
                .when().get(Endpoints.GET_ORDER)
                .then().assertThat()
                .statusCode(404);
    }

    @Test
    void getInventoryTest() {
        Map result = given()
                .when().get(Endpoints.GET_INVENTORY)
                .then().assertThat()
                .statusCode(200).extract().body().as(Map.class);
        Assert.assertTrue(result.containsKey("available"));
    }
}
