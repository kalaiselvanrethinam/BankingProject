package com.example.BankingSystem;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class PutApiTest {

    @Test
    public void testDeposit(){
        Response response = RestAssured
                .given()
                    .baseUri("http://localhost:8080/api/deposit/1/1234567890")
                    .contentType(ContentType.JSON)
                .param("amount", 1000)
                .when()
                    .put()
                .then()
                    .assertThat()
                    .log().all()
                    .statusCode(200)
                    .header("Content-Type","application/json")
                .extract()
                    .response();


        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asPrettyString());
    }

    @Test
    public void testWithdraw(){

        Response response =
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost:8080/api/withdraw/1/1234567890")
                    .body("""
                            {
                                "amount": 1000
                            }
                            """)
                .when()
                    .put()
                .then()
                    .assertThat()
                    .statusCode(200)
                    .header("Content-Type","application/json")
                .extract()
                    .response();

        System.out.println("Status Code " + response.getStatusCode());
        System.out.println("Response " + response.getBody().asPrettyString());

    }
}
