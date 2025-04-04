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
                    .baseUri("http://localhost:8080/api/deleteAccount/5/123411166")
                    .contentType(ContentType.JSON)
                .when()
                    .delete()
                .then()
                    .assertThat()
                .log().all()
                    .statusCode(200)
//                    .header("Content-type", "application/json")
                .extract()
                    .response();

        System.out.println("Status Code :" + response.getStatusCode());
        System.out.println("Response :" + response.getBody().asString());

    }
}



