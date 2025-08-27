package com.ggamja.domain.member.service;

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
            // 세션 저장 (한 번 더 로그인할 때도 세션 유지되도록)
            saveAuthenticationToSession(member, httpRequest);
            return PostMemberLoginResponse.of(member, false, false);
        }

        // 이번 주 월요일 날짜
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);

        // 이번 주차가 아니면 출석 카운트 리셋
        if (member.getWeekStartDate() == null ||
                !member.getWeekStartDate().isEqual(thisWeekMonday)) {
            member.setWeekStartDate(thisWeekMonday);
            member.setWeeklyAttendanceCount(1);
        } else { // 이번주 출석이면 출석일수 ++
            member.setWeeklyAttendanceCount(member.getWeeklyAttendanceCount() + 1);
        }

        // 기본 출석 포인트 +1
        member.setPoints(member.getPoints() + 1);

        // 일요일 & 이번 주 7일 출석 완료 → 보너스 지급
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY &&
                member.getWeeklyAttendanceCount() == 7) {
            member.setPoints(member.getPoints() + 7);
            bonusGiven = true;
        }

        // 마지막 로그인 시간 갱신
        member.setLastLogin(LocalDateTime.now());

        // 레벨 계산
        Level currentLevel = member.getLevel();
        Level newLevel = levelRepository.findTopByStartPointLessThanEqualOrderByStartPointDesc(member.getPoints())
                .orElseThrow(() -> new CustomException(LEVEL_NOT_FOUND));

        boolean levelChanged = false;
        if (!newLevel.equals(currentLevel)) {
            member.setLevel(newLevel);
            levelChanged = true;
        }

        // 로그인 성공 시 세션에 Authentication 저장
        saveAuthenticationToSession(member, httpRequest);

        return PostMemberLoginResponse.of(member, bonusGiven, levelChanged);
    }


    public PutMyInfoResponse updateMyInfo(Member member, PutMyInfoRequest request,  HttpServletRequest httpRequest) {
        // 닉네임 변경
        if (request.nickname() != null && !request.nickname().isBlank()) {
            member.setNickname(request.nickname());
        }

        // 비밀번호 변경
        if (request.password() != null && !request.password().isBlank()) {
            if (!request.password().equals(request.passwordConfirm())) {
                throw new CustomException(PASSWORD_MISMATCH);
            }
            member.setPassword(passwordEncoder.encode(request.password()));
        }

        memberRepository.save(member);

        saveAuthenticationToSession(member, httpRequest);

        return PutMyInfoResponse.of(member);
    }

    @Transactional(readOnly = true)
    public GetMyInfoResponse getMyInfo(Member member) {
        return GetMyInfoResponse.of(member);
    }

    public void deleteMyInfo(Member member) {
        memberRepository.deleteById(member.getId());
    }

    @Transactional(readOnly = true)
    public GetHomeResponse getHome(Member member) { // 홈화면 조회
        return GetHomeResponse.of(member);
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

}

