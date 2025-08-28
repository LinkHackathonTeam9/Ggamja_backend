package com.ggamja.domain.studylog.dto;

import com.ggamja.domain.studylog.entity.StudyLog;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record GetStudyLogListResponse(
        @Schema(description = "학습 이력 목록")
        List<StudyLogDto> studyLogs,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
    public static GetStudyLogListResponse of(List<StudyLogDto> studyLogs, boolean hasNext) {
        return new GetStudyLogListResponse(studyLogs, hasNext);
    }

    public record StudyLogDto(
            @Schema(description = "학습 로그 ID", example = "1")
            Long logId,

            @Schema(description = "카테고리", example = "CAPITAL")
            String category,

            @Schema(description = "카드 제목", example = "서울")
            String title,

            @Schema(description = "난이도", example = "1")
            int difficulty,

            @Schema(description = "학습한 날짜", example = "2025-08-28T10:12:30")
            LocalDateTime date
    ) {
        public static StudyLogDto from(StudyLog studyLog) {
            return new StudyLogDto(
                    studyLog.getId(),
                    studyLog.getCard().getCategory().name(),
                    studyLog.getCard().getTitle(),
                    studyLog.getCard().getDifficulty(),
                    studyLog.getDate()
            );
        }
    }
}