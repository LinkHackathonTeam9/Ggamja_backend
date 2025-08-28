package com.ggamja.domain.studylog.controller;

import com.ggamja.domain.studylog.dto.GetStudyLogDetailResponse;
import com.ggamja.domain.studylog.dto.GetStudyLogListResponse;
import com.ggamja.domain.studylog.service.StudyLogService;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studylogs")
public class StudyLogController {

    private final StudyLogService studyLogService;

    @Operation(summary = "학습한 기록 목록 조회", description = "로그인된 사용자의 모든 학습 이력을 최신순으로 반환합니다.")
    @DocumentedApiErrors({ AUTH_UNAUTHENTICATED })
    @GetMapping
    public ResponseEntity<BaseResponse<GetStudyLogListResponse>> getStudyLogs(
            @AuthenticationPrincipal Member member
    ) {
        GetStudyLogListResponse response = studyLogService.getStudyLogs(member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }

    @Operation(summary = "학습한 기록 상세 조회", description = "특정 학습 이력의 상세 정보를 반환합니다.")
    @DocumentedApiErrors({ AUTH_UNAUTHENTICATED, STUDY_LOG_NOT_FOUND })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<GetStudyLogDetailResponse>> getStudyLogDetail(
            @Parameter(description = "학습 이력 ID", example = "10")
            @PathVariable Long id,
            @AuthenticationPrincipal Member member
    ) {
        GetStudyLogDetailResponse response = studyLogService.getStudyLogDetail(id, member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }

}