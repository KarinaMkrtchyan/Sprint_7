import client.Client;
import courier.Courier;
import courier.ApiCourier;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    static String id;
    Courier courier = new Courier("angel57", "1234", "Anna");
    ApiCourier apiCourier = new ApiCourier();

    @Before
    public void setUp() {
        RestAssured.baseURI = Client.BASE_URI;
    }

    @Test
    public void successLoginCourierTest() {
        ValidatableResponse response = apiCourier.courierReg(courier);
        ValidatableResponse loginResponse = apiCourier.courierLogin(courier);
        id = loginResponse.extract().path("id").toString();
        loginResponse.assertThat().statusCode(SC_OK).body("id", notNullValue());
    }

    @Test
    public void noLoginCourierWithoutLoginTest() {
        Courier courier = new Courier("", "1234", "Anna");
        ValidatableResponse response = apiCourier.courierLogin(courier);
        response.assertThat().statusCode(SC_BAD_REQUEST).and().body("message", is("Недостаточно данных для входа"));
    }

    @Test
    public void noLoginCourierWithoutPasswordTest() {
        Courier courier = new Courier("angel57", "", "Anna");
        ValidatableResponse response = apiCourier.courierLogin(courier);
        response.assertThat().statusCode(SC_BAD_REQUEST).and().body("message", is("Недостаточно данных для входа"));
    }

    @Test
    public void noLoginCourierWithWrongPasswordTest() {
        Courier courier = new Courier("angel57", "123456","Anna");
        ValidatableResponse response = apiCourier.courierLogin(courier);
        response.assertThat().statusCode(SC_NOT_FOUND).and().body("message", is("Учетная запись не найдена"));
    }

    @Test
    public void noLoginCourierWithWrongLoginTest() {
        Courier courier = new Courier("angel57", "1234");
        ValidatableResponse response = apiCourier.courierLogin(courier);
        response.assertThat().statusCode(SC_NOT_FOUND).and().body("message", is("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        apiCourier.courierDelete(id);
    }
}
