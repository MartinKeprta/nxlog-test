package co.nxlog.qa;

import co.nxlog.qa.dto.TestConfig;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class SecurityTest {
    //Test variables
    static TestConfig t=new TestConfig(System.getProperty("testConfig"));

    @BeforeAll
    public static void prepare() {


        System.out.println("System configuration path is:"+System.getProperty("testConfig"));


        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(t.baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addQueryParam("_format", "json")
                .log(LogDetail.ALL)
                .build();


    }

    @Test
    @Feature("Retrieve single user with valid API token")
    @Description("Retrieve single user with valid API token")
    @DisplayName("Retrieve single user with valid API token")
    @Epic("Security")
    @Severity(SeverityLevel.CRITICAL)
    public void accessWithCorrectApiKey() {
        RestAssured.given()
                .basePath("/users/"+t.userId)
                .with().queryParam("access-token", t.auth)
                .get()
                .then()
                .body("_meta.success.value", is(true))
                .body("_meta.code.value",is(200))
                .body(matchesJsonSchemaInClasspath("UserSchema.json"));
    }

    @Test
    @Feature("Retrieve single user with with invalid header")
    @Description("Retrieve single user with invalid header")
    @DisplayName("Retrieve single user with invalid header")
    @Epic("Security")
    @Severity(SeverityLevel.CRITICAL)
    public void accessWithIncorrectHeader() {
        RestAssured.given()
                .basePath("/users/"+t.userId)
                .with().queryParam("api-key", t.auth)
                .get()
                .then()
                .body("_meta.success.value", is(false))
                .body("_meta.code.value",is(401))
                .body(matchesJsonSchemaInClasspath("ErrorSchema.json"));
    }

    @Test
    @Feature("Retrieve single user with invalid API token")
    @Description("Retrieve single user with invalid API token")
    @DisplayName("Retrieve single user with invalid API token")
    @Epic("Security")
    @Severity(SeverityLevel.CRITICAL)
    public void accessWithInCorrectApiKey() {
        RestAssured.given()
                .basePath("/users/"+t.userId)
                .with().queryParam("access-token", "invalid_api_key")
                .get()
                .then()
                .body("_meta.success.value", is(false))
                .body("_meta.code.value",is(401))
                .body(matchesJsonSchemaInClasspath("ErrorSchema.json"));
    }

    @Test
    @Feature("Retrieve single user with without API key header")
    @Description("Retrieve single user with without API key header")
    @DisplayName("Retrieve single user with without API key header")
    @Epic("Security")
    @Severity(SeverityLevel.CRITICAL)
    public void accessWithoutApiKey() {
        RestAssured.given()
                .basePath("/users/"+t.userId)
                .get()
                .then()
                .body("_meta.success.value", is(false))
                .body("_meta.code.value",is(401))
                .body(matchesJsonSchemaInClasspath("ErrorSchema.json"));
    }


}
