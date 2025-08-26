package bit.app.file;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileMetadata {
    private final String originalFileName;
    private final String uuidFileName;
    private final String fileUrl;
    private final String contentType;
    private final long fileSize;

    public static FileMetadata of(String fileName, String fileKey, String fileUrl, String contentType, long fileSize) {
        return FileMetadata.builder()
                .originalFileName(fileName)
                .uuidFileName(fileKey)
                .fileUrl(fileUrl)
                .contentType(contentType)
                .fileSize(fileSize)
                .build();
    }
}
