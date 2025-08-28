package com.ggamja.domain.attendance.controller;

import com.ggamja.domain.attendance.dto.response.GetWeeklyAttendanceResponse;
import com.ggamja.domain.attendance.service.AttendanceService;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "주간 출석 내역 조회", description = "이번 주 월요일-일요일 출석 여부를 반환합니다.")
    @GetMapping("/weekly")
    public ResponseEntity<BaseResponse<GetWeeklyAttendanceResponse>> getWeeklyAttendance(
            @AuthenticationPrincipal Member member
    ) {
        GetWeeklyAttendanceResponse response = attendanceService.getWeeklyAttendance(member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }
}