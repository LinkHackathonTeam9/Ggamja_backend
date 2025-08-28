package com.ggamja.domain.studylog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GetStudyLogListResponse(
        List<GetStudyLogResponse> logs,
        boolean hasNext
) {
    public static GetStudyLogListResponse of(List<GetStudyLogResponse> logs) {
        // Todo 페이징 처리 해야 해서 이 부분 수정
        return GetStudyLogListResponse.builder()
                .logs(logs)
                .hasNext(false)
                .build();
    }
}