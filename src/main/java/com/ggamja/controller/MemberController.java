package com.ggamja.controller;

import com.ggamja.domain.Member;
import com.ggamja.dto.request.PostMemberLoginRequest;
import com.ggamja.dto.request.PostMemberRegisterRequest;
import com.ggamja.dto.response.GetMyInfoResponse;
import com.ggamja.dto.response.PostMemberLoginResponse;
import com.ggamja.dto.response.PostMemberRegisterResponse;
import com.ggamja.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "새로운 멤버를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @ApiResponse(responseCode = "400", description = "요청 값 오류")
    @PostMapping("/register")
    public ResponseEntity<PostMemberRegisterResponse> register(
            @Valid @RequestBody PostMemberRegisterRequest request) {
        PostMemberRegisterResponse response = memberService.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인", description = "세션 기반 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @PostMapping("/login")
    public ResponseEntity<PostMemberLoginResponse> login(
            @RequestBody PostMemberLoginRequest request,
            HttpServletRequest httpRequest   // 세션 저장을 위해 필요
    ) {
        PostMemberLoginResponse response = memberService.login(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 회원 정보 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/me")
    public ResponseEntity<GetMyInfoResponse> getMyInfo(
            @AuthenticationPrincipal Member member
    ) {
        if (member == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(GetMyInfoResponse.of(member));
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 세션을 종료합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public void logout() {
        // Swagger 노출용 껍데기
        // 실제 처리는 Spring Security LogoutFilter가 수행
    }


}

