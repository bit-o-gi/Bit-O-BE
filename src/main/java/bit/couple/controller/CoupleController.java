package bit.couple.controller;

import bit.auth.domain.UserPrincipal;
import bit.couple.dto.CoupleCreateRequest;
import bit.couple.dto.CoupleRcodeResponseDto;
import bit.couple.dto.CoupleRequestDto;
import bit.couple.dto.CoupleResponDto;
import bit.couple.service.CoupleService;
import bit.couple.swagger.CoupleControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/couple")
public class CoupleController implements CoupleControllerDocs {

    private final CoupleService coupleService;

    // NOTE: 커플 인증코드 발급 완료
    @PostMapping("/code")
    public ResponseEntity<CoupleRcodeResponseDto> createCode(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CoupleRcodeResponseDto response = coupleService.createCode(userPrincipal.getUser());
        return ResponseEntity.status(201).body(response);
    }

    // NOTE: 커플 인증코드 조회
    @GetMapping("/code")
    public ResponseEntity<CoupleRcodeResponseDto> searchCode(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CoupleRcodeResponseDto response = coupleService.getCodeByUser(userPrincipal.getUser());
        return ResponseEntity.ok(response);
    }

    // NOTE: 커플 찾기
    @GetMapping("/{coupleId}")
    public ResponseEntity<CoupleResponDto> getCouple(@PathVariable Long coupleId) {
        CoupleResponDto response = coupleService.getCouple(coupleId);
        return ResponseEntity.ok(response);
    }

    // NOTE: 커플 연결
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmCouple(
            @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CoupleCreateRequest coupleCreateRequest) {
        coupleService.confirmCouple(userPrincipal.getUser(), coupleCreateRequest);
        return ResponseEntity.status(201).build();
    }

    // NOTE: 커플에 속한 유저 수정
    @PutMapping("/")
    public ResponseEntity<Void> updateCouple(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CoupleRequestDto coupleRequestDto) {
        coupleService.refreshCouple(userPrincipal.getUser(), coupleRequestDto);
        return ResponseEntity.ok().build();
    }

    // TODO: coupleId -> 커플에 속한 유저 수정
    @PutMapping("/{coupleId}")
    public ResponseEntity<Void> approveCouple(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long coupleId) {
        coupleService.coupleApprove(userPrincipal.getUser(), coupleId);
        return ResponseEntity.ok().build();
    }

    // NOTE: 커플 삭제
    @DeleteMapping("/{coupleId}")
    public ResponseEntity<Void> deleteCouple(@PathVariable Long coupleId) {
        coupleService.deleteCouple(coupleId);
        return ResponseEntity.noContent().build();
    }
}
