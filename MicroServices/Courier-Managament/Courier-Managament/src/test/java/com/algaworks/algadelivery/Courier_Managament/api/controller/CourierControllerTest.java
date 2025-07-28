package com.algaworks.algadelivery.Courier_Managament.api.controller;

import com.algaworks.algadelivery.Courier_Managament.domain.models.Courier;
import com.algaworks.algadelivery.Courier_Managament.domain.repository.CourierRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.protocol.HTTP;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class CourierControllerTest {

    @Autowired
    private CourierRepository courierRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp()
    {
        RestAssured.port = port;
        RestAssured.basePath = "/couriers";
    }

    @Test
    public void shouldReturn201()
    {
        String requestBody = """
                {
                "name": "John Doe",
                "phone": "1234567890",
                }
                """;

        RestAssured.given()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("John Doe"));

    }

    @Test
    public void shouldReturn200()
    {

       UUID courierId = courierRepository.saveAndFlush(Courier.brandNew(
                "Jane Doe",
                "0987654321"
        )).getId();
        RestAssured.given()
                .pathParam("courierId", courierId)
                .accept(ContentType.JSON)
                .when()
                .get("/{courierId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("Jane Doe"))
                .body("phone", Matchers.equalTo("0987654321"));
    }

}