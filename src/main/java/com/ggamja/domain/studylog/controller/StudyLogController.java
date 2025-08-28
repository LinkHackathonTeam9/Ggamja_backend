package com.ggamja.domain.studylog.controller;

import com.ggamja.domain.studylog.dto.response.GetStudyLogDetailResponse;
import com.ggamja.domain.studylog.dto.response.StudyLogDto;
import com.ggamja.domain.studylog.service.StudyLogService;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studylogs")
@Tag(name = "StudyLog", description = "StudyLogController - 학습 기록 관련 api")
public class StudyLogController {

    private final StudyLogService studyLogService;

    @Operation(
            summary = "학습 이력 목록 조회",
            description = "사용자가 조회한 학습 이력을 무한스크롤 형식으로 조회합니다."
    )
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED})
    @GetMapping
    public ResponseEntity<BaseResponse<Page<StudyLogDto>>> getStudyLogs(
            @AuthenticationPrincipal Member member,
            @ParameterObject
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(BaseResponse.ok(studyLogService.getStudyLogs(member, pageable)));
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