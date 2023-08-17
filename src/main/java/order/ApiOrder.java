package order;
import client.Client;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
public class ApiOrder {
    public static final String API_ORDERS = "/api/v1/orders";

    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(Client.requestSpecification())
                .and()
                .body(order)
                .when()
                .post(API_ORDERS)
                .then();
    }
}
