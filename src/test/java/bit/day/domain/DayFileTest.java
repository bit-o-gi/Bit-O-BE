package bit.day.domain;


import static org.assertj.core.api.Assertions.assertThat;

import bit.day.dto.DayFileCreateData;
import bit.day.dto.DayFileUpdateData;
import org.junit.jupiter.api.Test;

class DayFileTest {

    @Test
    void createTest() {
        DayFileCreateData command = DayFileCreateData.builder()
                .originalFileName("fileName.jpg")
                .uuidFileName("uuid12345.jpg")
                .contentType("image/jpeg")
                .fileSize(100L)
                .build();

        DayFile dayFile = DayFile.create(command);

        assertThat(dayFile).extracting("originalFileName", "uuidFileName", "contentType", "fileSize")
                .containsExactly(command.getOriginalFileName(), command.getUuidFileName(), command.getContentType(),
                        command.getFileSize());
    }

    @Test
    void updateTest() {
        DayFile dayFile = DayFile.builder()
                .originalFileName("fileName.jpg")
                .uuidFileName("uuid12345.jpg")
                .contentType("image/jpeg")
                .fileSize(100L)
                .build();

        DayFileUpdateData command = DayFileUpdateData.builder()
                .fileName("newFileName.jpg")
                .fileKey("newUuid12345.jpg")
                .contentType("image/png")
                .fileSize(200L)
                .build();

        dayFile.update(command);

        assertThat(dayFile).extracting("originalFileName", "uuidFileName", "contentType", "fileSize")
                .containsExactly(command.getFileName(), command.getFileKey(), command.getContentType(),
                        command.getFileSize());
    }
}
