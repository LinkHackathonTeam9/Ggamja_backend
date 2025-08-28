package com.ggamja.domain.member.service;

import com.ggamja.domain.attendance.entity.Attendance;
import com.ggamja.domain.attendance.repository.AttendanceRepository;
import com.ggamja.domain.level.entity.Level;
import com.ggamja.domain.member.dto.response.*;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.member.dto.request.PostMemberLoginRequest;
import com.ggamja.domain.member.dto.request.PostMemberRegisterRequest;
import com.ggamja.domain.member.dto.request.PutMyInfoRequest;

import com.ggamja.domain.level.repository.LevelRepository;
import com.ggamja.domain.member.repository.MemberRepository;
import com.ggamja.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final LevelRepository levelRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;

    public PostMemberRegisterResponse register(PostMemberRegisterRequest request) {
        if (!request.password().equals(request.passwordConfirm())) {
            throw new CustomException(PASSWORD_MISMATCH);
        }

        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new CustomException(DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Level defaultLevel = levelRepository.findById(1L)
                .orElseThrow(() -> new CustomException(LEVEL_NOT_FOUND));

        Member member = Member.create(
                request.nickname(),
                request.email(),
                encodedPassword,
                defaultLevel
        );

        Member saved = memberRepository.save(member);

        return new PostMemberRegisterResponse(saved.getId(), saved.getNickname());
    }


    @Transactional
    public PostMemberLoginResponse login(PostMemberLoginRequest request, HttpServletRequest httpRequest) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }

        boolean bonusGiven = false;
        LocalDate today = LocalDate.now();

        // 오늘 이미 로그인했다면 포인트 X
        if (member.getLastLogin() != null &&
                member.getLastLogin().toLocalDate().isEqual(today)) {
            saveAuthenticationToSession(member, httpRequest);
            Level next = findNextLevel(member.getLevel());
            return PostMemberLoginResponse.of(member, false, false, next);
        }

        // 이번 주 월요일 날짜
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);

        // 이번 주차가 아니면 출석 카운트 리셋
        if (member.getWeekStartDate() == null ||
                !member.getWeekStartDate().isEqual(thisWeekMonday)) {
            member.resetWeeklyAttendance(thisWeekMonday);
        } else { // 이번 주 출석이면 출석일수 ++
            member.increaseAttendance();
        }

        // 기본 출석 포인트 +1
        member.addPoints(1);

        // Attendance 테이블에 오늘 기록 추가
        if (!attendanceRepository.existsByMemberAndDate(member, today)) {
            Attendance attendance = Attendance.builder()
                    .member(member)
                    .date(today)
                    .build();
            attendanceRepository.save(attendance);
        }

        // 일요일 & 이번 주 7일 출석 완료 → 보너스 지급
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY &&
                member.getWeeklyAttendanceCount() == 7) {
            member.addPoints(7);
            bonusGiven = true;
        }

        // 마지막 로그인 시간 갱신
        member.updateLastLogin(LocalDateTime.now());

        // 레벨 갱신
        boolean levelChanged = updateMemberLevel(member);

        // 로그인 성공 시 세션에 Authentication 저장
        saveAuthenticationToSession(member, httpRequest);

        Level next = findNextLevel(member.getLevel());

        return PostMemberLoginResponse.of(member, bonusGiven, levelChanged, next);
    }

    public PutMyInfoResponse updateMyInfo(Member member, PutMyInfoRequest request,  HttpServletRequest httpRequest) {
        // 닉네임 변경
        if (request.nickname() != null && !request.nickname().isBlank()) {
            member.updateNickname(request.nickname());
        }

        // 비밀번호 변경
        if (request.password() != null && !request.password().isBlank()) {
            if (!request.password().equals(request.passwordConfirm())) {
                throw new CustomException(PASSWORD_MISMATCH);
            }
            member.updatePassword(passwordEncoder.encode(request.password()));
        }

        memberRepository.save(member);

        saveAuthenticationToSession(member, httpRequest);

        return PutMyInfoResponse.of(member);
    }

    @Transactional(readOnly = true)
    public GetMyInfoResponse getMyInfo(Member member) {
        updateMemberLevel(member);

        Level next = findNextLevel(member.getLevel());
        return GetMyInfoResponse.of(member, next);
    }

    @Transactional(readOnly = true)
    public GetHomeResponse getHome(Member member) { // 홈화면 조회
        updateMemberLevel(member);

        Level next = findNextLevel(member.getLevel());
        return GetHomeResponse.of(member, next);
    }

    public void deleteMyInfo(Member member) {
        memberRepository.deleteById(member.getId());
    }

    private void saveAuthenticationToSession(Member member, HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member,  // principal
                null,    // credentials (null 처리)
                List.of() // 권한이 있다면 member.getAuthorities() 같은 걸 넣으면 됨
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
    }

    private Level findNextLevel(Level currentLevel) {
        return levelRepository.findFirstByLevelGreaterThanOrderByLevelAsc(currentLevel.getLevel())
                .orElse(null);
    }

    private boolean updateMemberLevel(Member member) {
        Level currentLevel = member.getLevel();

        Level newLevel = levelRepository
                .findTopByStartPointLessThanEqualOrderByStartPointDesc(member.getPoints())
                .orElseThrow(() -> new CustomException(LEVEL_NOT_FOUND));

        if (!newLevel.equals(currentLevel)) {
            member.updateLevel(newLevel);
            return true; // 레벨 갱신
        }
        return false; // 레벨 그대로
    }
}

