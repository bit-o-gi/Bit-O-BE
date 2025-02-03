package bit.couple.scheduled;


import bit.couple.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CoupleScheduled {

    private final CoupleService coupleService;

    // 5분마다 코드 삭제 메서드 실행
    @Scheduled(fixedRate = 300000) // 5분 (300,000ms)마다 실행
    public void scheduledRemoveExpiredCodes() {
        coupleService.removeExpiredCodes();
    }
}
