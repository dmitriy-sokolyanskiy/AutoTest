package org.example.lab2;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetTest {
    private static final String baseUrl = "https://petstore.swagger.io/v2";

    private static final String PET = "/pet",
            PET_ID = PET + "/{petId}";

    private String name;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyPostAction(ITestContext context) {
        name = Faker.instance().dog().name();
        Map<String, ?> body = Map.of(
                "category", Map.of( "name", Faker.instance().dog().breed()),
                "name", name,
                "photoUrls", List.of(Faker.instance().internet().image()),
                "tags", List.of(Map.of("name", "Lucky")),
                "status", "available"
        );
        Response response = given().body(body).post(PET);
        response.then().statusCode(HttpStatus.SC_OK);
        System.out.println("Response : " + response.jsonPath().get().toString());
        context.setAttribute("petId", response.jsonPath().get("id").toString());
        System.out.println("petId : " + context.getAttribute("petId"));
    }

    @Test(dependsOnMethods = "verifyPostAction")
    public void verifyCreatedPet (ITestContext context) {
        given().pathParam("petId", context.getAttribute("petId")).
                get(PET_ID).then().statusCode(HttpStatus.SC_OK).and().body("name", equalTo(name));
    }

    @Test(dependsOnMethods = "verifyPostAction")
    public void verifyUpdateAction (ITestContext context) {
        name = Faker.instance().dog().name();
        Map<String, ?> body = Map.of(
                "id", context.getAttribute("petId"),
                "category", Map.of( "name", Faker.instance().dog().breed()),
                "name", name,
                "photoUrls", List.of(Faker.instance().internet().image()),
                "tags", List.of(Map.of("name", "Boomer")),
                "status", "pending"
        );
        Response response = given().body(body).put(PET);
        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyUpdateAction")
    public void verifyUpdatedPet (ITestContext context) {
        given().pathParam("petId", context.getAttribute("petId")).
                get(PET_ID).then().statusCode(HttpStatus.SC_OK).and().body("name", equalTo(name));
    }
}
