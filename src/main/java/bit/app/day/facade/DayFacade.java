package bit.app.day.facade;

import bit.app.couple.domain.Couple;
import bit.app.couple.service.CoupleService;
import bit.app.day.domain.Day;
import bit.app.day.dto.DayDeleteCommand;
import bit.app.day.dto.DayFileRegisterCommand;
import bit.app.day.dto.DayFileResponse;
import bit.app.day.dto.DayRegisterCommand;
import bit.app.day.dto.DayResponse;
import bit.app.day.dto.DayUpdateCommand;
import bit.app.day.service.DayFileService;
import bit.app.day.service.DayService;
import bit.app.file.FileStorageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DayFacade {
    public static final String DAY_DIRECTORY_PATH = "day";

    private final DayService dayService;
    private final DayFileService dayFileService;
    private final CoupleService coupleService;
    private final FileStorageService fileStorageService;

    public DayResponse getDay(Long id) {
        Day day = dayService.getDayById(id);
        Optional<String> fileUrlOptional = fileStorageService.getFileUrl(DAY_DIRECTORY_PATH, day.getThumbnailUrl());
        String fileUrl = fileUrlOptional.orElse(null);

        return DayResponse.of(day, fileUrl);
    }

    @Transactional(readOnly = true)
    public DayResponse getDayByCouple(Long userId) {
        Couple couple = coupleService.getCoupleEntityByUserId(userId);
        Day day = dayService.getDayByCouple(couple);
        Optional<String> fileUrlOptional = fileStorageService.getFileUrl(DAY_DIRECTORY_PATH, day.getThumbnailUrl());
        String fileUrl = fileUrlOptional.orElse(null);

        return DayResponse.of(day, fileUrl);
    }

    @Transactional
    public Long createDay(DayRegisterCommand command) {
        Couple couple = coupleService.getCoupleEntityById(command.coupleId());

        return dayService.createDay(couple, command);
    }

    @Transactional
    public DayFileResponse uploadThumbnail(DayFileRegisterCommand command) {
        Couple couple = coupleService.getCoupleEntityByUserId(command.getUserId());
        Day day = dayService.getDayById(command.getDayId());

        String thumbnailUrl = dayFileService.uploadThumbnail(couple, day, command.getFile());

        return DayFileResponse.of(day.getId(), thumbnailUrl);
    }

    @Transactional
    public Long updateDay(DayUpdateCommand command) {
        Couple couple = coupleService.getCoupleEntityByUserId(command.userId());
        Day day = dayService.getDayByCouple(couple);

        return dayService.updateDay(day, command);
    }

    @Transactional
    public void deleteDay(DayDeleteCommand command) {
        Couple couple = coupleService.getCoupleEntityByUserId(command.userId());

        dayService.deleteDay(command.dayId(), couple);
    }
}
