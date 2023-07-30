import client.Client;
import courier.Courier;
import courier.ApiCourier;
import courier.CourierCustomData;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;
public class CreateNewCourierTest {
    static String id;
    ApiCourier apiCourier = new ApiCourier();

    @Before
    public void setUp() {
        RestAssured.baseURI = Client.BASE_URI;
    }
    @Test
    public void regNewCourierTest() {
        ValidatableResponse response = apiCourier.courierReg(CourierCustomData.getCourierNew());
        response.assertThat().statusCode(SC_CREATED).body("ok", is(true)).log().all();
        ValidatableResponse loginResponse = apiCourier.courierLogin(CourierCustomData.getCourierNew());
        id = loginResponse.extract().path("id").toString();
    }

    @Test
    public void regDuplicateCourierTest() {
        Courier courier = new Courier("angel57", "1234", "Anna");
        ValidatableResponse response = apiCourier.courierReg(courier);
        response.statusCode(SC_CREATED);
        ValidatableResponse loginResponse = apiCourier.courierLogin(courier);
        id = loginResponse.extract().path("id").toString();
        ValidatableResponse response2 = apiCourier.courierReg(courier);
        response2.statusCode(SC_CONFLICT)
                .and().assertThat().body("message", is("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    public void regCourierWithoutLoginTest() {
        ValidatableResponse response = apiCourier.courierReg(CourierCustomData.getCourierWithoutLogin());
        response.statusCode(SC_BAD_REQUEST)
                .and().assertThat().body("message", is("Недостаточно данных для создания учетной записи"));
    }
    @After
    public void tearDown() {
        apiCourier.courierDelete(id);
    }
}
