package bit.app.anniversary.controller;

import bit.app.anniversary.dto.AnReqDto;
import bit.app.anniversary.dto.AnResDto;
import bit.app.anniversary.service.AnService;
import bit.app.auth.domain.UserPrincipal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AnController {

    private final AnService anniversaryService;

    // 기념일 생성
    @MutationMapping
    public AnResDto createAnniversary(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @Argument("anDto") AnReqDto anReqDto) {
        log.info("anReqDto ==> {}", anReqDto);
        return anniversaryService.createAnniversary(userPrincipal, anReqDto);
    }

    // 기념일 업데이트
    @MutationMapping
    public AnResDto updateAnniversary(@AuthenticationPrincipal UserPrincipal userPrincipal, @Argument("id") Long id,
                                      @Argument("anDto") AnReqDto anReqDto) {
        return anniversaryService.updateAnniversary(userPrincipal, id, anReqDto);
    }

    // 기념일 삭제
    @MutationMapping
    public Long deleteAnniversary(@Argument("id") Long id) {
        anniversaryService.deleteAnniversary(id);
        return id;
    }

    // 특정 기념일 조회
    @QueryMapping
    public AnResDto getAnniversary(@Argument Long id) {
        return anniversaryService.getAnniversary(id);
    }

    // 날짜 범위로 기념일 목록 조회
    @QueryMapping
    public List<AnResDto> getAnniversariesInRange(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @Argument LocalDateTime startDate, @Argument LocalDateTime endDate,
                                                  @Argument int page,
                                                  @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return anniversaryService.findAnniversariesInRange(userPrincipal, startDate, endDate, pageable);
    }

}
