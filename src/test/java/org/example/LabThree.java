package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LabThree {
    private static final String baseUrl = "https://59d51e67-7fad-4146-b831-9e6e6b1e2fa5.mock.pstmn.io";
    private static final String OWNER = "/ownerName",
            OWNER_UNSUCCESS = OWNER + "/unsuccess",
            CREATE = "/createSomething",
            UPDATE = "/updateMe",
            DELETE = "/deleteWorld";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test()
    public void verifyGetSuccess() {
        String name = "Dmytro Sokolianskyi";
        given().get(OWNER).then().statusCode(HttpStatus.SC_OK).and().body("name", equalTo(name));
    }

    @Test()
    public void verifyGetUnsuccess() {
        String exception = "I won't say my name!";
        given().get(OWNER_UNSUCCESS).then().statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body("exception", equalTo(exception));
    }

    @Test()
    public void verifyPost200() {
        String result = "'Nothing' was created";
        given().queryParam("permission", "yes").post(CREATE).then().statusCode(HttpStatus.SC_OK)
                .and().body("result", equalTo(result));
    }

    @Test()
    public void verifyPost400() {
        String result = "You don't have permission to create Something";
        given().post(CREATE).then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and().body("result", equalTo(result));
    }

    @Test()
    public void verifyPut500() {
        given().put(UPDATE).then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test()
    public void verifyDeleteAction() {
        String sessionID = "123456789", world = "0";
        given().header("SessionID", sessionID).when().delete(DELETE)
                .then().statusCode(HttpStatus.SC_GONE).and().body("world", equalTo(world));
    }
}
