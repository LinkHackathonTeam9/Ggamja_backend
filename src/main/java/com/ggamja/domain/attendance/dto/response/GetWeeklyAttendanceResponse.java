package com.ggamja.domain.attendance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record GetWeeklyAttendanceResponse(
        @Schema(description = "이번 주 시작일(월요일)", example = "2025-08-25")
        LocalDate weekStartDate,

        @Schema(description = "요일별 출석 내역")
        List<DayAttendanceDto> days,

        @Schema(description = "이번 주 출석 7일 완료 여부", example = "false")
        boolean allCompleted
) {
    public record DayAttendanceDto(
            @Schema(description = "출석 여부", example = "true")
            boolean attended
    ) {}
}