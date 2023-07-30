import client.Client;
import courier.ApiCourier;
import courier.CourierCustomData;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
public class DeleteCourierTest {
    static String id;
    ApiCourier apiCourier = new ApiCourier();

    @Before
    public void setUp() {
        RestAssured.baseURI = Client.BASE_URI;
    }
    @Test
    public void deleteCourierTest() {
        ValidatableResponse response = apiCourier.courierReg(CourierCustomData.getCourierNew());
        response.assertThat().body("ok", is(true)).and().statusCode(SC_CREATED);

        ValidatableResponse responseLogin = apiCourier.courierLogin(CourierCustomData.getCourierNew());
        id = responseLogin.extract().path("id").toString();
        ValidatableResponse deleteCourier = apiCourier.courierDelete(id);
        deleteCourier.statusCode(SC_OK).and().assertThat().body("ok", is(true));
    }
    @After
    public void tearDown() {
        apiCourier.courierDelete(id);
    }
}
