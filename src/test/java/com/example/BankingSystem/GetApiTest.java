package com.example.BankingSystem;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetApiTest {

    @Test
    public void testGetAllAccounts(){

        Response response =
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost:8082/api/getAllAccounts")
                .when()
                    .get()
                .then()
                    .assertThat()
                .log().all()
                    .statusCode(200)
                    .header("Connection", "keep-alive")
                .extract()
                    .response();

        List<String> accountNumbers = response.then().extract().path("accountNumber");
        Assert.assertFalse(accountNumbers.isEmpty());
        accountNumbers.forEach(accountNumber ->
                Assert.assertTrue(accountNumber != null && !accountNumber.isEmpty())
        );
    }

    @Test
    public void getAccountsByCustomerId(){

//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        Response response =
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost:8082/api/getAccountsByCustomerId/5")
//                    .log().all()
                .when()
                    .get()
                .then()
                    .assertThat()
                    .log().all()
                    .statusCode(200)
                    .header("Connection","keep-alive")
                .extract()
                    .response();

    }
}
