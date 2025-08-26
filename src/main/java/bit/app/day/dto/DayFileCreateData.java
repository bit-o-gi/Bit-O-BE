package bit.app.day.dto;

import bit.app.day.domain.Day;
import bit.app.file.FileMetadata;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayFileCreateData {
    private final Day day;
    private final String originalFileName;
    private final String uuidFileName;
    private final String contentType;
    private final Long fileSize;

    public static DayFileCreateData of(Day day, FileMetadata fileMetadata) {
        return DayFileCreateData.builder()
                .day(day)
                .originalFileName(fileMetadata.getOriginalFileName())
                .uuidFileName(fileMetadata.getUuidFileName())
                .contentType(fileMetadata.getContentType())
                .fileSize(fileMetadata.getFileSize())
                .build();
    }
}
