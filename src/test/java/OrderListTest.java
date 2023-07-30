import client.Client;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import order.ApiOrder;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;
public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Client.BASE_URI;
    }

    @Test
    public void getOrderListTest() {
        ValidatableResponse response = given()
                .spec(Client.requestSpecification())
                .when()
                .get(ApiOrder.API_ORDERS)
                .then()
                .statusCode(SC_OK).assertThat().body("orders", notNullValue());
    }
}
