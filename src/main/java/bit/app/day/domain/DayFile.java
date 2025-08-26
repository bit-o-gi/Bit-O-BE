package bit.app.day.domain;

import bit.app.day.dto.DayFileCreateData;
import bit.app.day.dto.DayFileUpdateData;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id")
    private Day day;

    private String originalFileName;

    private String uuidFileName;

    private String contentType;

    private long fileSize;

    public static DayFile create(DayFileCreateData dto) {
        return DayFile.builder()
                .day(dto.getDay())
                .originalFileName(dto.getOriginalFileName())
                .uuidFileName(dto.getUuidFileName())
                .contentType(dto.getContentType())
                .fileSize(dto.getFileSize())
                .build();
    }

    public void update(DayFileUpdateData command) {
        this.originalFileName = command.getFileName();
        this.uuidFileName = command.getFileKey();
        this.contentType = command.getContentType();
        this.fileSize = command.getFileSize();
    }
}
