package bit.day.dto;

import bit.file.FileMetadata;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayFileUpdateData {
    private final String fileName;
    private final String fileKey;
    private final String contentType;
    private final Long fileSize;

    public static DayFileUpdateData of(FileMetadata fileMetadata) {
        return DayFileUpdateData.builder()
                .fileName(fileMetadata.getOriginalFileName())
                .fileKey(fileMetadata.getUuidFileName())
                .contentType(fileMetadata.getContentType())
                .fileSize(fileMetadata.getFileSize())
                .build();
    }
}
