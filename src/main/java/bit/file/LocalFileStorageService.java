package bit.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("!prod")
public class LocalFileStorageService implements FileStorageService {

    private static final String UPLOADS = "/uploads/";
    private final Path fileStoragePath;
    private final String serverAddress;

    public LocalFileStorageService(@Value("${app.file.upload-dir:./uploads}") String uploadDir,
                                   @Value("${spring.app.server.address:http://localhost:8080}") String serverAddress) {
        this.fileStoragePath = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.serverAddress = serverAddress;

        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (IOException ex) {
            throw new IllegalArgumentException("파일 업로드 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    @Override
    public FileMetadata uploadFile(MultipartFile file, String dirPath) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID() + "." + extension;

        Path targetLocation = this.fileStoragePath;

        try {
            if (dirPath != null && !dirPath.isEmpty()) {
                targetLocation = targetLocation.resolve(dirPath);
                Files.createDirectories(targetLocation);
            }

            Path filePath = targetLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String relativePath = this.fileStoragePath.relativize(filePath).toString();
            String fileUrl = serverAddress + UPLOADS + relativePath;

            return FileMetadata.of(originalFileName, uniqueFileName, fileUrl, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드에 실패했습니다.", e);
        }

    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return fileName.substring(index + 1);
    }

    @Override
    public boolean deleteFile(String fileKey) {
        try {
            Path filePath = this.fileStoragePath.resolve(fileKey);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 삭제에 실패했습니다.", e);
        }
    }

    @Override
    public String getFileUrl(String dir, String fileKey) {
        return serverAddress + UPLOADS + dir + "/" + fileKey;
    }
}
