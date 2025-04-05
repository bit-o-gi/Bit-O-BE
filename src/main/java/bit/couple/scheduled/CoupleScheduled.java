package bit.couple.scheduled;


import bit.couple.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CoupleScheduled {

    private final CoupleService coupleService;

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void scheduledRemoveExpiredCodes() {
        coupleService.removeExpiredCodes();
    }
}
