package co.nxlog.qa;

import co.nxlog.qa.dto.TestConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.mapper.factory.GsonObjectMapperFactory;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class UserTest {
    //Test variables
    static TestConfig t=new TestConfig(System.getProperty("testConfig"));
    @BeforeAll
    public static void prepare() {

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(t.baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addQueryParam("access-token", t.auth)
                .addQueryParam("_format", "json")
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    @Feature("As user I want to retrieve list of users")
    @Description("Retrieve list of user and validate them")
    @DisplayName("Retrieve list of users")
    @Epic("User management")
    @Severity(SeverityLevel.CRITICAL)
    public void getUserList() {
        RestAssured.given()
                .basePath("/users")
                    .get()
                        .then()
                            .body("_meta.success.value", is(true))
                            .body("_meta.code.value",is(200))
                            .body("_meta.totalCount.value",greaterThan(100))
                            .body("result",hasItem(allOf(hasKey("id"))))
                            .body(matchesJsonSchemaInClasspath("UserListSchema.json"));
    }

    @Test
    @Feature("As user I want to retrieve list of users with pagination")
    @Description("Retrieve list of user and validate them with pagination")
    @DisplayName("Retrieve list of users with pagination")
    @Epic("User management")
    @Severity(SeverityLevel.CRITICAL)
    public void getUserListForPage() {
        RestAssured.given()
                .basePath("/users")
                    .with().queryParam("page", 35)
                    .get()
                        .then()
                        .body("_meta.success.value", is(true))
                        .body("_meta.code.value",is(200))
                        .body("_meta.totalCount.value",greaterThan(100))
                        .body("result",hasItem(allOf(hasKey("id"))))
                        .body(matchesJsonSchemaInClasspath("UserListSchema.json"));
            }

    @Test
    @Feature("As user I want to retrieve existing user by ID")
    @Description("Retrieve user and validate response")
    @DisplayName("Retrieve single user")
    @Epic("User management")
    @Severity(SeverityLevel.CRITICAL)
    public void getSpecificUser() {
        RestAssured.given()
                .basePath("/users/"+t.userId)
                    .get()
                        .then()
                            .statusCode(200)
                            .body("_meta.success.value", is(true))
                            .body("_meta.code.value",is(200))
                            .body(matchesJsonSchemaInClasspath("UserSchema.json"));
    }

    @Test
    @Feature("As user I want to retrieve non existing user by ID")
    @Description("Retrieve user and validate response")
    @DisplayName("Retrieve non existing single user")
    @Epic("User management")
    @Severity(SeverityLevel.CRITICAL)
    public void getNonExistingUser() {
        RestAssured.given()
                .basePath("/users/9999999")
                    .get()
                        .then()
                            .body("_meta.success.value", is(false))
                            .body("_meta.code.value",is(404))
                            .body(matchesJsonSchemaInClasspath("ErrorSchema.json"));
    }
}
