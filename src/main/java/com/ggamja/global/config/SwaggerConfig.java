package com.ggamja.global.config;

import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseErrorResponse;
import com.ggamja.global.response.status.BaseExceptionResponseStatus;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@OpenAPIDefinition(
        info = @Info(
                title = "Ggamja API",
                version = "v1",
                description = "깜자 API 명세서"
        )
)
@Configuration
public class SwaggerConfig {
        private void addExamples(ApiResponses responses, List<BaseExceptionResponseStatus> errors) {
                for (BaseExceptionResponseStatus error : errors) {
                        String statusCode = String.valueOf(error.getHttpStatus().value());

                        ApiResponse apiResponse = responses.containsKey(statusCode)
                                ? responses.get(statusCode)
                                : new ApiResponse().description(error.getHttpStatus().getReasonPhrase());

                        MediaType mediaType = apiResponse.getContent() != null &&
                                apiResponse.getContent().containsKey("application/json")
                                ? apiResponse.getContent().get("application/json")
                                : new MediaType();

                        Example example = new Example()
                                .summary(error.name())
                                .value(new BaseErrorResponse(error));

                        mediaType.addExamples(error.name(), example);

                        Content content = new Content().addMediaType("application/json", mediaType);
                        apiResponse.setContent(content);

                        responses.addApiResponse(statusCode, apiResponse);
                }
        }

        @Bean
        public OpenApiCustomizer addGlobalErrors() {
                return openApi -> {
                        List<BaseExceptionResponseStatus> globals = Arrays.asList(
                                BAD_REQUEST,
                                NOT_FOUND,
                                INTERNAL_SERVER_ERROR,
                                AUTH_UNAUTHENTICATED
                        );

                        if (openApi.getPaths() == null) return;
                        openApi.getPaths().values().forEach(pathItem ->
                                pathItem.readOperations().forEach(op -> {
                                        ApiResponses responses = op.getResponses();
                                        addExamples(responses, globals);
                                })
                        );
                };
        }

        @Bean
        public OperationCustomizer customizeErrorExamples() {
                return (Operation operation, HandlerMethod handlerMethod) -> {
                        DocumentedApiErrors annotation = handlerMethod.getMethodAnnotation(DocumentedApiErrors.class);

                        if (annotation != null) {
                                ApiResponses responses = operation.getResponses();
                                addExamples(responses, Arrays.asList(annotation.value()));
                        }

                        return operation;
                };
        }

}
