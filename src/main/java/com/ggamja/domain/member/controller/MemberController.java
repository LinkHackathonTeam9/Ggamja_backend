package com.ggamja.domain.member.controller;

import com.ggamja.domain.member.dto.response.*;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.member.dto.request.PostMemberLoginRequest;
import com.ggamja.domain.member.dto.request.PostMemberRegisterRequest;
import com.ggamja.domain.member.dto.request.PutMyInfoRequest;
import com.ggamja.domain.member.service.MemberService;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "새로운 멤버를 등록합니다.")
    @DocumentedApiErrors({ DUPLICATED_EMAIL, PASSWORD_MISMATCH, LEVEL_NOT_FOUND })
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<PostMemberRegisterResponse>> register(
            @Valid @RequestBody PostMemberRegisterRequest request) {
        PostMemberRegisterResponse response = memberService.register(request);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }

    @Operation(summary = "로그인", description = "세션 기반 로그인")
    @DocumentedApiErrors({ MEMBER_NOT_FOUND, LEVEL_NOT_FOUND })
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<PostMemberLoginResponse>> login(
            @RequestBody PostMemberLoginRequest request,
            HttpServletRequest httpRequest   // 세션 저장을 위해 필요
    ) {
        PostMemberLoginResponse response = memberService.login(request, httpRequest);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 회원 정보 조회")
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED})
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<GetMyInfoResponse>> getMyInfo(
            @AuthenticationPrincipal Member member
    ) {
        GetMyInfoResponse response = memberService.getMyInfo(member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 세션을 종료합니다.")
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED})
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout() {
        // Swagger 노출용 껍데기
        // 실제 처리는 Spring Security LogoutFilter가 수행
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @Operation(summary = "내 정보 수정", description = "현재 로그인된 사용자의 정보를 수정합니다.")
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED, PASSWORD_MISMATCH})
    @PutMapping("/me")
    public ResponseEntity<BaseResponse<PutMyInfoResponse>> updateMyInfo(
            @AuthenticationPrincipal Member member,
            @RequestBody PutMyInfoRequest request,
            HttpServletRequest httpRequest
    ) {
        PutMyInfoResponse response = memberService.updateMyInfo(member, request, httpRequest);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 회원을 탈퇴 처리합니다.")
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED})
    @DeleteMapping("/me")
    public ResponseEntity<BaseResponse<Void>> deleteMyInfo(
            @AuthenticationPrincipal Member member
    ) {
        memberService.deleteMyInfo(member);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @Operation(summary = "홈 화면 조회", description = "현재 로그인된 회원의 닉네임, 레벨, 캐릭터 이미지 URL을 반환합니다.")
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED})
    @GetMapping("/home")
    public ResponseEntity<BaseResponse<GetHomeResponse>> getHome(
            @AuthenticationPrincipal Member member
    ) {
        GetHomeResponse response = memberService.getHome(member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }
}

