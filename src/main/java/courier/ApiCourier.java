package courier;
import client.Client;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
public class ApiCourier extends Client {
    private static final String API_CREATE_COURIER = "/api/v1/courier";
    private static final String API_LOGIN = "/api/v1/courier/login";
    private static final String API_DELETE = "/api/v1/courier/";

    public ValidatableResponse courierReg(Courier courier) {
        return given()
                .spec(Client.requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(API_CREATE_COURIER)
                .then();
    }

    public ValidatableResponse courierLogin(Courier courier) {
        return given()
                .spec(Client.requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(API_LOGIN)
                .then();
    }

    public ValidatableResponse courierDelete(String id) {
        return given()
                .spec(Client.requestSpecification())
                .delete(API_DELETE + id)
                .then();
    }

    public void courierDeleteWithoutPassword(Courier courier) {
        if (courier.getId() != null) {
            Integer id = given()
                    .spec(Client.requestSpecification())
                    .body(courier)
                    .when()
                    .post("/api/v1/courier/login")
                    .then().log().all().extract().body().<Integer>path("id");
            if (id != null) {
                given()
                        .spec(Client.requestSpecification())
                        .when()
                        .delete("/api/v1/courier/" + id)
                        .then().statusCode(200).log().all();
            }
        }
    }

    public ValidatableResponse checkCourierDeletedWithoutPassword(Integer id) {
        return given()
                .spec(Client.requestSpecification())
                .when()
                .get("/api/v1/courier/" + id + "/ordersCount")
                .then()
                .statusCode(404);
    }
}
