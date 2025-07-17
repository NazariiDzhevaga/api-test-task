package factories;

import constants.BaseConstants;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.Config;

public class SpecificationApiFactory {
    public static RequestSpecification getApiRequestSpecification() {
        return RestAssured.given()
                .log().all()
                .baseUri(Config.get(BaseConstants.BASE_URI));
    }
}
