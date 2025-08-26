package bit.app.file;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileMetadata uploadFile(MultipartFile file, String dirPath);

    boolean deleteFile(String fileKey);

    Optional<String> getFileUrl(String dir, String fileKey);
}
