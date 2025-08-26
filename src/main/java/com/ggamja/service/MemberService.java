package com.ggamja.service;

import com.ggamja.domain.Level;
import com.ggamja.domain.Member;
import com.ggamja.dto.request.PostMemberLoginRequest;
import com.ggamja.dto.request.PostMemberRegisterRequest;
import com.ggamja.dto.request.PutMyInfoRequest;
import com.ggamja.dto.response.GetHomeResponse;
import com.ggamja.dto.response.PostMemberLoginResponse;
import com.ggamja.dto.response.PostMemberRegisterResponse;
import com.ggamja.dto.response.PutMyInfoResponse;
import com.ggamja.exception.MemberException;
import com.ggamja.exception.MemberExceptionResponseStatus;
import com.ggamja.exception.UnauthorizedException;
import com.ggamja.repository.LevelRepository;
import com.ggamja.repository.MemberRepository;
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

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LevelRepository levelRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PostMemberRegisterResponse register(PostMemberRegisterRequest request) {
        if (!request.password().equals(request.passwordConfirm())) {
            throw new MemberException(MemberExceptionResponseStatus.PASSWORD_MISMATCH);
        }

        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new MemberException(MemberExceptionResponseStatus.DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Level defaultLevel = levelRepository.findById(1L)
                .orElseThrow(() -> new MemberException(MemberExceptionResponseStatus.LEVEL_NOT_FOUND));

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
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new UnauthorizedException("비밀번호가 올바르지 않습니다.");
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
                .orElseThrow(() -> new IllegalStateException("레벨 정보 없음"));

        boolean levelChanged = false;
        if (!newLevel.equals(currentLevel)) {
            member.setLevel(newLevel);
            levelChanged = true;
        }

        // 로그인 성공 시 세션에 Authentication 저장
        saveAuthenticationToSession(member, httpRequest);

        return PostMemberLoginResponse.of(member, bonusGiven, levelChanged);
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

    @Transactional
    public PutMyInfoResponse updateMyInfo(Long memberId, PutMyInfoRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionResponseStatus.MEMBER_NOT_FOUND));

        // 닉네임 변경
        if (request.nickname() != null && !request.nickname().isBlank()) {
            member.setNickname(request.nickname());
        }

        // 비밀번호 변경
        if (request.password() != null && !request.password().isBlank()) {
            if (!request.password().equals(request.passwordConfirm())) {
                throw new MemberException(MemberExceptionResponseStatus.PASSWORD_MISMATCH);
            }
            member.setPassword(passwordEncoder.encode(request.password()));
        }

        memberRepository.save(member);

        return PutMyInfoResponse.of(member);
    }

    @Transactional
    public void deleteMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionResponseStatus.MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public GetHomeResponse getHome(Long memberId) { // 홈화면 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionResponseStatus.MEMBER_NOT_FOUND));

        return GetHomeResponse.of(member);
    }
}

