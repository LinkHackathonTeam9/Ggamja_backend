package com.ggamja.domain.quizlog.controller;

import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.quizlog.dto.response.GetQuizLogDetailResponse;
import com.ggamja.domain.quizlog.dto.response.GetQuizLogListResponse;
import com.ggamja.domain.quizlog.service.QuizLogService;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@RestController
@RequestMapping("/api/quizlogs")
@RequiredArgsConstructor
@Tag(name = "QuizLog", description = "QuizLogController - 퀴즈 로그 관련 api")
public class QuizLogController {
    private final QuizLogService quizLogService;

    @Operation(
            summary = "퀴즈 로그 목록 조회",
            description = "사용자가 푼 퀴즈 로그를 무한스크롤 형식으로 조회합니다."
    )
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED})
    @GetMapping
    public ResponseEntity<BaseResponse<GetQuizLogListResponse>> getQuizLogsList(
            @AuthenticationPrincipal Member member,
            @ParameterObject
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(BaseResponse.ok(quizLogService.getQuizLogsList(member, pageable)));
    }

    @Operation(
            summary = "퀴즈 로그 상세 조회",
            description = "특정 퀴즈 로그의 상세 정보를 조회합니다."
    )
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED})
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<GetQuizLogDetailResponse>> getQuizLogDetail(
            @AuthenticationPrincipal Member member,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(BaseResponse.ok(quizLogService.getQuizLogDetail(member, id)));
    }

}
