package bit.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileMetadata uploadFile(MultipartFile file, String dirPath);

    boolean deleteFile(String fileKey);

    String getFileUrl(String dir, String fileKey);
}
