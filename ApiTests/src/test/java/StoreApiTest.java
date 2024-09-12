import constants.Endpoints;
import constants.LoggerMessages;
import constants.Order;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;


public class StoreApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);

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

    private int createOrder(Order order) {
        return given()
                .body(order)
                .post(Endpoints.POST_ORDER)
                .getStatusCode();
    }

    private Order getOrderBody(int id) {
        return given().pathParams("id", id)
                .get(Endpoints.GET_ORDER)
                .getBody().as(Order.class);
    }

    private int getOrderStatusCode(int id) {
        return given().pathParams("id", id)
                .get(Endpoints.GET_ORDER)
                .getStatusCode();
    }

    @Test
    public void createOrderTest() {
        Order order = sampleOrder();
        LOGGER.info(LoggerMessages.SEND_POST_REQUEST);
        int statusCode = createOrder(order);
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(statusCode, 200, LoggerMessages.STATUS_CODE_200_FAILED + statusCode);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        Order apiOrder = getOrderBody(order.getId());
        LOGGER.info(LoggerMessages.CHECK_OBJECT);
        Assert.assertEquals(order, apiOrder, "Assertion failed: order should be: " + order + "but was: " + apiOrder);
    }

    @Test
    public void getOrderTest() {
        Order order = sampleOrder();
        LOGGER.info(LoggerMessages.SEND_POST_REQUEST);
        createOrder(order);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        int statusCode = getOrderStatusCode(order.getId());
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(statusCode, 200, LoggerMessages.STATUS_CODE_200_FAILED + statusCode);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        Order apiOrder = getOrderBody(order.getId());
        LOGGER.info(LoggerMessages.CHECK_OBJECT);
        Assert.assertEquals(order, apiOrder);
    }

    @Test
    public void deleteOrderTest() {
        Order order = sampleOrder();
        LOGGER.info(LoggerMessages.SEND_POST_REQUEST);
        createOrder(order);
        LOGGER.info(LoggerMessages.SEND_DELETE_REQUEST);
        int statusCode =
        given().pathParam("id", order.getId())
                .delete(Endpoints.DELETE_ORDER)
                .getStatusCode();
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(statusCode, 200, LoggerMessages.STATUS_CODE_200_FAILED + statusCode);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        int statusCode2 =
        given().pathParam("id", order.getId())
                .get(Endpoints.GET_ORDER)
                .getStatusCode();
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_404);
        Assert.assertEquals(statusCode2, 404, LoggerMessages.STATUS_CODE_404_FAILED + statusCode2);
    }

    @Test
    void getInventoryTest() {
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        int statusCode = given()
                .get(Endpoints.GET_INVENTORY)
                .getStatusCode();
        LOGGER.info(LoggerMessages.CHECK_CODE_TO_BE_200);
        Assert.assertEquals(statusCode, 200, LoggerMessages.STATUS_CODE_200_FAILED + statusCode);
        LOGGER.info(LoggerMessages.SEND_GET_REQUEST);
        Map result = given()
                .get(Endpoints.GET_INVENTORY)
                .getBody().as(Map.class);
        LOGGER.info(LoggerMessages.CHECK_BODY_TO_CONTAIN_KEY);
        Assert.assertTrue(result.containsKey("available"), LoggerMessages.BODY_CONTAINS_KEY_FAILED + "available");
    }
}
