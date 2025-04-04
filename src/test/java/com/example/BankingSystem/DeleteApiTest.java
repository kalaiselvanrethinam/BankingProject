package com.example.BankingSystem;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class DeleteApiTest {

    @Test
    public void testDeleteAccount(){


        Response response =
        RestAssured
                .given()
                .baseUri("http://localhost:8082")
                .basePath("/api/deleteAccount")
                .contentType(ContentType.JSON)
                .queryParam("customerId", 1)
                .queryParam("accountNumber", 5250879047L)
                .when()
                .delete()
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.getBody().asString());

    }
}



