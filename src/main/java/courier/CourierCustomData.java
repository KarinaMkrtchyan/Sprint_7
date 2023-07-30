package courier;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierCustomData {
    private static final Courier courierNew = new Courier(RandomStringUtils.randomAlphabetic(3), "12345", "karina");
    private static final Courier courierWithoutLogin = new Courier("", RandomStringUtils.randomAlphabetic(3), RandomStringUtils.randomAlphabetic(3));

    public static Courier getCourierNew() {
        return courierNew;
    }

    public static Courier getCourierWithoutLogin() {
        return courierWithoutLogin;
    }
}
