package com.kratos.core.rest.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FailedRequestFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification,
                           FilterableResponseSpecification filterableResponseSpecification,
                           FilterContext filterContext) {
        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
        if (isStatusCodeNotSuccess(response)) {
            log.warn(String.format("Method: %s, URI: %s, Status code: %s",
                    filterableRequestSpecification.getMethod(),
                    filterableRequestSpecification.getURI(),
                    response.getStatusCode()));
        }
        return response;
    }

    private boolean isStatusCodeNotSuccess(Response response) {
        return response.getStatusCode() < HttpStatus.SC_OK || response.getStatusCode() > HttpStatus.SC_MULTIPLE_CHOICES;
    }
}
