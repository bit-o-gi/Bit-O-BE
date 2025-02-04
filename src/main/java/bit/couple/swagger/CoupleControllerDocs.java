package bit.couple.swagger;

import bit.auth.domain.UserPrincipal;
import bit.couple.dto.CoupleCreateRequest;
import bit.couple.dto.CoupleRcodeResponseDto;
import bit.couple.dto.CoupleRequestDto;
import bit.couple.dto.CoupleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Couple API", description = "커플 관련 API")
public interface CoupleControllerDocs {

    @Operation(summary = "커플 인증 코드 생성", description = "유저가 커플 인증 코드를 생성합니다.",
            responses = {@ApiResponse(responseCode = "201", description = "커플 인증 코드 생성 완료")})
    ResponseEntity<CoupleRcodeResponseDto> createCode(@AuthenticationPrincipal UserPrincipal userPrincipal);

    @Operation(summary = "커플 인증 코드 조회", description = "유저가 발급받은 커플 인증 코드를 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "커플 인증 코드 조회 성공")})
    ResponseEntity<CoupleRcodeResponseDto> searchCode(@AuthenticationPrincipal UserPrincipal userPrincipal);

    @Operation(summary = "커플 정보 조회", description = "커플 ID를 통해 커플 정보를 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "커플 정보 조회 성공")})
    ResponseEntity<CoupleResponseDto> getCouple(@PathVariable Long coupleId);

    @Operation(summary = "커플 연결", description = "커플 코드를 입력하여 커플을 확정합니다.",
            responses = {@ApiResponse(responseCode = "201", description = "커플 연결 성공")})
    ResponseEntity<Void> confirmCouple(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CoupleCreateRequest coupleCreateRequest);

    @Operation(summary = "커플 정보 수정", description = "커플 ID에 속한 유저 정보를 수정합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "커플 정보 수정 성공")})
    ResponseEntity<Void> updateCouple(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CoupleRequestDto coupleRequestDto);

    @Operation(summary = "커플 정보 수정", description = "커플 ID에 속한 유저 정보를 수정합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "커플 정보 수정 성공")})
    ResponseEntity<Void> approveCouple(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long coupleId);


    @Operation(summary = "커플 삭제", description = "커플 정보를 삭제합니다.",
            responses = {@ApiResponse(responseCode = "204", description = "커플 삭제 성공")})
    ResponseEntity<Void> deleteCouple(@PathVariable Long coupleId);
}
