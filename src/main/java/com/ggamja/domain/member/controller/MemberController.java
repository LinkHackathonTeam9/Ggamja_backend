package com.ggamja.domain.member.controller;

import com.ggamja.domain.member.dto.response.*;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.member.dto.request.PostMemberLoginRequest;
import com.ggamja.domain.member.dto.request.PostMemberRegisterRequest;
import com.ggamja.domain.member.dto.request.PutMyInfoRequest;
import com.ggamja.domain.member.repository.MemberRepository;
import com.ggamja.domain.member.service.MemberService;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Operation(summary = "회원가입", description = "새로운 멤버를 등록합니다.")
    @DocumentedApiErrors({ DUPLICATED_EMAIL, PASSWORD_MISMATCH, LEVEL_NOT_FOUND })
    @PostMapping("/register")
    public ResponseEntity<PostMemberRegisterResponse> register(
            @Valid @RequestBody PostMemberRegisterRequest request) {
        PostMemberRegisterResponse response = memberService.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인", description = "세션 기반 로그인")
    @DocumentedApiErrors({ MEMBER_NOT_FOUND, LEVEL_NOT_FOUND })
    @PostMapping("/login")
    public ResponseEntity<PostMemberLoginResponse> login(
            @RequestBody PostMemberLoginRequest request,
            HttpServletRequest httpRequest   // 세션 저장을 위해 필요
    ) {
        PostMemberLoginResponse response = memberService.login(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 회원 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = GetHomeResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })    @GetMapping("/me")
    public ResponseEntity<GetMyInfoResponse> getMyInfo(
            @AuthenticationPrincipal Member member
    ) {
        // 세션에서 꺼낸 member 대신 DB에서 다시 조회
        Member fresh = memberRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return ResponseEntity.ok(GetMyInfoResponse.of(fresh));
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 세션을 종료합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = GetHomeResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })    @PostMapping("/logout")
    public void logout() {
        // Swagger 노출용 껍데기
        // 실제 처리는 Spring Security LogoutFilter가 수행
    }

    @Operation(summary = "내 정보 수정", description = "현재 로그인된 사용자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PutMyInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (비밀번호 불일치, 중복 닉네임 등)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/me")
    public ResponseEntity<PutMyInfoResponse> updateMyInfo(
            @AuthenticationPrincipal Member member,
            @RequestBody PutMyInfoRequest request
    ) {
        PutMyInfoResponse response = memberService.updateMyInfo(member.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 회원을 탈퇴 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 성공",
                    content = @Content(schema = @Schema(implementation = DeleteMemberResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/me")
    public ResponseEntity<DeleteMemberResponse> deleteMyInfo(
            @AuthenticationPrincipal Member member
    ) {
        memberService.deleteMyInfo(member.getId());
        return ResponseEntity.ok(DeleteMemberResponse.success());
    }

    @Operation(summary = "홈 화면 조회", description = "현재 로그인된 회원의 닉네임, 레벨, 캐릭터 이미지 URL을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = GetHomeResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/home")
    public ResponseEntity<GetHomeResponse> getHome(
            @AuthenticationPrincipal Member member
    ) {
        GetHomeResponse response = memberService.getHome(member.getId());
        return ResponseEntity.ok(response);
    }
}

