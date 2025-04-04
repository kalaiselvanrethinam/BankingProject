package com.example.BankingSystem;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class PostApiTest {

    @Test
    public void testCreateAccount() throws JSONException {
        RestAssured.baseURI = "http://localhost:8080/api/createAccount";
        String requestBody = """
                {
                    "accountNumber": "3214567888",
                    "accountType": "Savings",
                    "balance": 15000.0,
                    "status": "Active",
                    "customerId": 1
                }
                """;
        Response response =
        RestAssured
                .given()
                    .contentType(ContentType.JSON).body(requestBody).log().body()
                .when()
                    .post()
                .then()
                    .assertThat().log().all().statusCode(201)
                    .body("accountNumber", equalTo("3214567888"))
                    .body("accountType", equalTo("Savings"))
                    .body("balance", equalTo(15000.0F))
                    .body("status", equalTo("Active"))
                    .body("customerId", equalTo(1))
                .extract().response();

        System.out.println("Response" + response.getBody().asString());

    }
}
