package bit.couple.controller;

import bit.auth.domain.UserPrincipal;
import bit.couple.dto.CoupleCreateRequest;
import bit.couple.dto.CoupleRcodeResponseDto;
import bit.couple.dto.CoupleRequestDto;
import bit.couple.dto.CoupleResponDto;
import bit.couple.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/couple")
public class CoupleController {

    private final CoupleService coupleService;

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public void createCouple(@RequestBody CoupleCreateRequest coupleCreateRequest) {
//        coupleService.createCouple(coupleCreateRequest.toCommand());
//    }

    //NOTE: 커플 인증코드 발급 완료
    @GetMapping("/code")
    @ResponseStatus(HttpStatus.OK)
    public CoupleRcodeResponseDto createCode(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return coupleService.createCode(userPrincipal);
    }

    //NOTE: 커플 찾기
    @GetMapping("/{coupleId}")
    @ResponseStatus(HttpStatus.OK)
    public CoupleResponDto getCouple(@PathVariable Long coupleId) {
        return coupleService.getCouple(coupleId);
    }


    // NOTE: 커플 연결
    @PostMapping("/approve")
    @ResponseStatus(HttpStatus.OK)
    public void approveCouple(
            @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CoupleCreateRequest coupleCreateRequest) {
        coupleService.approveCouple(userPrincipal, coupleCreateRequest);
    }

    // TODO: coupleId -> 커플에 속한 유저 수정.
    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void refreshCouple(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CoupleRequestDto coupleRequestDto) {
        coupleService.refreshApprove(coupleRequestDto);
    }


    @DeleteMapping("/{coupleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCouple(@PathVariable Long coupleId) {
        coupleService.deleteCouple(coupleId);
    }
}
