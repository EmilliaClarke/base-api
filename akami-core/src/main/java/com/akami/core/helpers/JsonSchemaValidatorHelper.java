package com.akami.core.helpers;

import com.akami.core.exception.ExceptionError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class JsonSchemaValidatorHelper {
    JsonSchemaFactory jsonSchemaFactory;

    public Set<ValidationMessage> validateJsonScheme(final String jsonSchema, final String jsonResponse) {
        jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema schema = jsonSchemaFactory.getSchema(jsonSchema);
        try {
            return schema.validate(new ObjectMapper().readTree(jsonResponse));
        } catch (JsonProcessingException e) {
            throw new ExceptionError("Unable to parse json from response");
        }
    }
}
