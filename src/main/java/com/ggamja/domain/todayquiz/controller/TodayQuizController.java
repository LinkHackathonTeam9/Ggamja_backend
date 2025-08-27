package com.ggamja.domain.todayquiz.controller;

import com.ggamja.domain.member.dto.response.PostMemberRegisterResponse;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.todayquiz.dto.response.GetTodayQuizResponse;
import com.ggamja.domain.todayquiz.service.TodayQuizService;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.AUTH_UNAUTHENTICATED;
import static com.ggamja.global.response.status.BaseExceptionResponseStatus.TODAYQUIZ_NOT_FOUND;

@RestController
@RequestMapping("/api/todayquizzes")
@RequiredArgsConstructor
public class TodayQuizController {

    private final TodayQuizService todayQuizService;

    @Operation(summary = "오늘의 퀴즈 조회", description = "오늘의 퀴즈를 조회합니다.")
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED, TODAYQUIZ_NOT_FOUND})
    @GetMapping()
    public ResponseEntity<BaseResponse<GetTodayQuizResponse>> getTodayQuiz(
            @AuthenticationPrincipal Member member) {
        GetTodayQuizResponse response = todayQuizService.getTodayQuiz();
        return ResponseEntity.ok(BaseResponse.ok(response));
    }
}
