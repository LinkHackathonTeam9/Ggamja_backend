package com.ggamja.domain.attendance.service;

import com.ggamja.domain.attendance.dto.response.GetWeeklyAttendanceResponse;
import com.ggamja.domain.attendance.entity.Attendance;
import com.ggamja.domain.attendance.repository.AttendanceRepository;
import com.ggamja.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public GetWeeklyAttendanceResponse getWeeklyAttendance(Member member) {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        // 이번 주 출석 기록
        List<Attendance> attendances = attendanceRepository
                .findByMemberAndDateBetween(member, weekStart, weekEnd);

        Set<LocalDate> attendanceDates = attendances.stream()
                .map(Attendance::getDate)
                .collect(Collectors.toSet());

        // 월요일 → 일요일 고정
        List<GetWeeklyAttendanceResponse.DayAttendanceDto> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            boolean attended = attendanceDates.contains(date);
            days.add(new GetWeeklyAttendanceResponse.DayAttendanceDto(attended));
        }

        // Member 필드 기반으로 주간 출석 완료 여부 확인
        boolean allCompleted = member.getWeeklyAttendanceCount() == 7;

        return new GetWeeklyAttendanceResponse(weekStart, days, allCompleted);
    }
}