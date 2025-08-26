package bit.day.fixture;

import bit.app.couple.domain.Couple;
import bit.app.day.domain.Day;
import bit.app.day.domain.DayFile;
import java.time.LocalDate;

public class DayTestFixture {
    public static Day createDay(Long id, Couple couple, String title, LocalDate startDate, DayFile dayFile) {
        return Day.builder()
                .id(id)
                .couple(couple)
                .title(title)
                .startDate(startDate)
                .dayFile(dayFile)
                .build();
    }

    public static DayFile createDayFile(String fileName, String fileKey, String contentType, Long fileSize) {
        return DayFile.builder()
                .originalFileName(fileName)
                .uuidFileName(fileKey)
                .contentType(contentType)
                .fileSize(fileSize)
                .build();
    }
}
