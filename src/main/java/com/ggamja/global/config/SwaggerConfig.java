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
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@OpenAPIDefinition(
        info = @Info(
                title = "Ggamja API",
                version = "v1",
                description = "깜자 API 명세서"
        )
)
@Configuration
public class SwaggerConfig {
        @Bean
        public OperationCustomizer customizeErrorExamples() {
                return (Operation operation, HandlerMethod handlerMethod) -> {
                        DocumentedApiErrors annotation = handlerMethod.getMethodAnnotation(DocumentedApiErrors.class);

                        if (annotation != null) {
                                ApiResponses responses = operation.getResponses();

                                for (BaseExceptionResponseStatus error : annotation.value()) {
                                        Example example = new Example();
                                        example.setSummary(error.name());
                                        example.setValue(new BaseErrorResponse(error));

                                        MediaType mediaType = new MediaType();
                                        mediaType.addExamples(error.name(), example);

                                        Content content = new Content().addMediaType("application/json", mediaType);

                                        ApiResponse apiResponse = new ApiResponse()
                                                .description("에러 응답 예시")
                                                .content(content);


                                        responses.addApiResponse(String.valueOf(error.getHttpStatus().value()), apiResponse);
                                }
                        }

                        return operation;
                };
        }

}
