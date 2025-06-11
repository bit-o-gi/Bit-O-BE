package bit.day.service;

import static bit.day.facade.DayFacade.DAY_DIRECTORY_PATH;

import bit.couple.domain.Couple;
import bit.day.domain.Day;
import bit.day.domain.DayFile;
import bit.day.dto.DayFileCreateData;
import bit.day.dto.DayFileUpdateData;
import bit.file.FileMetadata;
import bit.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DayFileService {

    private final FileStorageService fileStorageService;

    @Transactional
    public String uploadThumbnail(Couple couple, Day day, MultipartFile multipartFile) {
        day.checkCouple(couple);
        DayFile dayFile = day.getDayFile();

        if (dayFile != null) {
            return updateDayFile(day, multipartFile);
        }

        return createDayFile(day, multipartFile);
    }

    private String updateDayFile(Day day, MultipartFile multipartFile) {
        DayFile dayFile = day.getDayFile();

        if (fileStorageService.deleteFile(dayFile.getUuidFileName())) {
            throw new IllegalStateException("file delete fail");
        }

        FileMetadata fileMetadata = fileStorageService.uploadFile(multipartFile, DAY_DIRECTORY_PATH);
        dayFile.update(DayFileUpdateData.of(fileMetadata));
        day.changeThumbnailUrl(dayFile.getUuidFileName());

        return fileMetadata.getFileUrl();
    }

    private String createDayFile(Day day, MultipartFile multipartFile) {
        FileMetadata fileMetadata = fileStorageService.uploadFile(multipartFile, DAY_DIRECTORY_PATH);
        DayFile dayFile = DayFile.create(DayFileCreateData.of(day, fileMetadata));
        day.registerDayFile(dayFile);
        day.changeThumbnailUrl(dayFile.getUuidFileName());

        return fileMetadata.getFileUrl();
    }
}
