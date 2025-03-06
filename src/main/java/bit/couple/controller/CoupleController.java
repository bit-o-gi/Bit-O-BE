package bit.couple.controller;

import bit.auth.domain.UserPrincipal;
import bit.couple.dto.CoupleRcodeReqestDto;
import bit.couple.dto.CoupleRcodeResponseDto;
import bit.couple.dto.CoupleRequestDto;
import bit.couple.dto.CoupleResponseDto;
import bit.couple.service.CoupleService;
import bit.couple.swagger.CoupleControllerDocs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/couple")
public class CoupleController implements CoupleControllerDocs {

    private final CoupleService coupleService;

    // NOTE: 현재 로그인한 사용자의 커플 정보 조회
    @GetMapping("")
    public ResponseEntity<CoupleResponseDto> getCoupleInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CoupleResponseDto response = coupleService.getCoupleByUserId(userPrincipal.getId());
        return ResponseEntity.ok(response);
    }

    // NOTE: 커플 인증코드 발급 완료
    @PostMapping("/code")
    public ResponseEntity<CoupleRcodeResponseDto> createCode(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CoupleRcodeResponseDto response = coupleService.createCode(userPrincipal.getId());
        return ResponseEntity.status(201).body(response);
    }

    // NOTE: 커플 인증코드 조회
    @GetMapping("/code")
    public ResponseEntity<CoupleRcodeResponseDto> searchCode(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CoupleRcodeResponseDto response = coupleService.getCodeByUser(userPrincipal.getId());
        return ResponseEntity.ok(response);
    }

    // NOTE: 커플 찾기
    @GetMapping("/{coupleId}")
    public ResponseEntity<CoupleResponseDto> getCouple(@PathVariable Long coupleId) {
        CoupleResponseDto response = coupleService.getCouple(coupleId);
        return ResponseEntity.ok(response);
    }

    // NOTE: 커플 연결
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmCouple(
            @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CoupleRcodeReqestDto coupleCreateRequest) {
        coupleService.confirmCouple(userPrincipal.getId(), coupleCreateRequest);
        return ResponseEntity.status(201).build();
    }

    // NOTE: 커플에 속한 유저 수정
    @PutMapping("/")
    public ResponseEntity<Void> updateCouple(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CoupleRequestDto coupleRequestDto) {
        coupleService.updateCouple(userPrincipal.getId(), coupleRequestDto);
        return ResponseEntity.ok().build();
    }

    // TODO: coupleId -> 커플에 속한 유저 수정
    @PutMapping("/{coupleId}")
    public ResponseEntity<Void> approveCouple(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long coupleId) {
        coupleService.coupleApprove(userPrincipal.getId(), coupleId);
        return ResponseEntity.ok().build();
    }

    // NOTE: 커플 삭제
    @DeleteMapping("/{coupleId}")
    public ResponseEntity<Void> deleteCouple(@PathVariable Long coupleId) {
        coupleService.deleteCouple(coupleId);
        return ResponseEntity.noContent().build();
    }
}
