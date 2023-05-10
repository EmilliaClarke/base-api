package com.kratos.core.rest.request;


import com.kratos.core.configuration.ConfigurationHelper;
import com.kratos.core.helpers.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kratos.core.rest.filter.FailedRequestFilter;
import com.kratos.core.constans.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;


@Slf4j
@PropertySource(value = {"classpath:en-us.properties"})
public abstract class BaseAPIRequest {

    @Autowired
    private FailedRequestFilter requestFilter;
    @Autowired
    private Context context;

    private RequestSpecification requestSpecification;

    protected Response sendGetRequest(final String uri, Map<String, String> queryParameterMap,
                                      Map<String, String> pathParameterMap, Headers headers, String token) {
        requestSpecification = given().redirects().follow(false);
        MapUtils.emptyIfNull(queryParameterMap).forEach(requestSpecification::queryParam);
        MapUtils.emptyIfNull(pathParameterMap).forEach(requestSpecification::pathParam);
        return requestSpecification.relaxedHTTPSValidation()
                .filter(requestFilter)
                .headers(headers)
                .auth()
                .oauth2(token)
                .when()
                .log()
                .method()
                .log()
                .uri()
                .get(uri);
    }

    protected Response sendPostRequest(final String uri, final Object rawBody,
                                       Map<String, Object> pathParameterMap, Map<String, Object> headers) {
        requestSpecification = given().redirects().follow(false);
        MapUtils.emptyIfNull(pathParameterMap).forEach(requestSpecification::pathParam);
        MapUtils.emptyIfNull(headers).forEach(requestSpecification::header);
        return requestSpecification.relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .body(from(rawBody))
                .log().method()
                .log().body(true)
                .log().uri()
                .when()
                .post(uri);
    }

	protected Response sendPatchRequest(final String uri, final Object rawBody,
			Map<String, Object> pathParameterMap, Map<String, Object> headers) {
		requestSpecification = given().redirects().follow(false);
		MapUtils.emptyIfNull(pathParameterMap).forEach(requestSpecification::pathParam);
		MapUtils.emptyIfNull(headers).forEach(requestSpecification::header);
		return requestSpecification.relaxedHTTPSValidation()
				.contentType(ContentType.JSON)
				.body(from(rawBody))
				.log().method()
				.log().body(true)
				.log().uri()
				.when()
				.patch(uri);
	}

	protected Response sendPutRequest(final String uri, final Object payload,
                                      Map<String, Object> pathParameterMap, Map<String, Object> headers) {
        requestSpecification = given().redirects().follow(false);
        MapUtils.emptyIfNull(pathParameterMap).forEach(requestSpecification::pathParam);
        MapUtils.emptyIfNull(headers).forEach(requestSpecification::header);
        return requestSpecification.relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .body(from(payload))
                .log().method()
                .log().body(true)
                .log().uri()
                .when()
                .put(uri);
    }

    protected Response getRequest(final String uri, final int statusCode, String token) {
        return given()
                .when()
                .header(Constants.X_AUTH, token)
                .get(uri)
                .then().statusCode(statusCode)
                .extract().response();
    }

    protected Response getRequest(final String uri, final int statusCode) {
        return given()
                .when()
                .get(uri)
                .then().statusCode(statusCode)
                .extract().response();
    }

    protected Response postRequest(final String uri, final int statusCode, final Object rawBody, String token) {
        return given()
                .contentType(ContentType.JSON)
                .body(from(rawBody))
                .when()
                .header(Constants.X_AUTH, token)
                .post(uri)
                .then().statusCode(statusCode)
                .extract().response();
    }

    protected Response putRequest(final String uri, final int statusCode, final Object rawBody, String token) {
        return given()
                .contentType(ContentType.JSON)
                .body(from(rawBody))
                .when()
                .header(Constants.X_AUTH, token)
                .put(uri)
                .then().statusCode(statusCode)
                .extract().response();
    }

    protected Response getToken(final int statusCode, final Object rawBody) {
        return RestAssured.given()
                .baseUri(ConfigurationHelper.getTokenURI())
                .contentType(ContentType.JSON)
                .body(from(rawBody))
                .when()
                .log()
                .uri()
                .post(ConfigurationHelper.getTokenURI())
                .then().statusCode(statusCode)
                .extract().response();
    }

    private String from(Object payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        String body = "";
        try {
            body = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return body;
    }

    protected RequestSpecification given() {
        requestSpecification = RestAssured.given().log().all(true);
        return requestSpecification.baseUri(ConfigurationHelper.getURI());
    }

    protected void setAuthHeader(String headerValue) {
        Header header = new Header(Constants.X_AUTH, headerValue);
        given().header(header);
    }
}

