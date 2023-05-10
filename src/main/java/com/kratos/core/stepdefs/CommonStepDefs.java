package com.kratos.core.stepdefs;

import com.kratos.core.helpers.APIHelper;
import com.kratos.core.helpers.Context;
import com.kratos.core.helpers.JsonSchemaValidatorHelper;
import com.kratos.core.constans.Constants;
import com.networknt.schema.ValidationMessage;
import io.cucumber.java.en.Given;
import io.cucumber.messages.internal.com.google.common.io.ByteStreams;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class CommonStepDefs {

    @Autowired
    APIHelper apiHelper;
    @Autowired
    JsonSchemaValidatorHelper jsonSchemaValidatorHelper;
    @Given("validate response via (.*) jsonSchema$")
    public void validate_response_via_path_json_schema(String pathToSchema) throws IOException {
        Response response = (Response) Context.getSharedParameter(Constants.REST_RESPONSE);
        final String responseJsonString = new String(ByteStreams.toByteArray(response.body().asInputStream()));
        Set<ValidationMessage> validationErrors =
                jsonSchemaValidatorHelper.validateJsonScheme(apiHelper.getFileContent(pathToSchema), responseJsonString);
        assertTrue("Errors: " + validationErrors.stream().map(ValidationMessage::toString)
                .collect(Collectors.joining("\n")), validationErrors.size() < 1);
    }
}
