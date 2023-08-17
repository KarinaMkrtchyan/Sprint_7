import client.Client;
import courier.Courier;
import courier.ApiCourier;
import courier.CourierCustomData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
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
    @DisplayName("Регистрации нового курьера")
    @Description("Проверка, что можно создать нового курьера с валидными значениями")
    public void regNewCourierTest() {
        ValidatableResponse response = apiCourier.courierReg(CourierCustomData.getCourierNew());
        response.assertThat().statusCode(SC_CREATED).body("ok", is(true)).log().all();
        ValidatableResponse loginResponse = apiCourier.courierLogin(CourierCustomData.getCourierNew());
        id = loginResponse.extract().path("id").toString();
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться двух одинаковых курьеров")
    @Description("Проверка, что нельзя создать нового курьера, если вводимый логин уже есть в системе")
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
    @DisplayName("Нельзя зарегистрировать курьера без логина")
    @Description("Проверка, что появится ошибка при попытке создания курьера без заполнения логина")
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
