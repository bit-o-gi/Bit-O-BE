package bit.app.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.app.server.address}")
    private String serverAddress;

    @Override
    public FileMetadata uploadFile(MultipartFile file, String dirPath) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = getExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID() + "." + extension;

        String s3Key = uniqueFileName;
        if (dirPath != null && !dirPath.isEmpty()) {
            if (!dirPath.endsWith("/")) {
                dirPath += "/";
            }
            s3Key = dirPath + uniqueFileName;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3Key, file.getInputStream(),
                    metadata);

            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드에 실패했습니다.", e);
        }

        String fileUrl = serverAddress + s3Key;

        return FileMetadata.of(originalFilename, uniqueFileName, fileUrl, file.getContentType(), file.getSize());
    }

    private String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }


    @Override
    public boolean deleteFile(String fileKey) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("파일 삭제에 실패했습니다.", e);
        }
    }

    @Override
    public Optional<String> getFileUrl(String dir, String fileKey) {
        if (fileKey == null || fileKey.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(serverAddress + dir + "/" + fileKey);
    }
}
