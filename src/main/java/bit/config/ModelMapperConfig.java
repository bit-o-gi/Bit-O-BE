package bit.config;

import bit.app.anniversary.dto.AnDto;
import bit.app.anniversary.entity.Anniversary;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        modelMapper.addMappings(new PropertyMap<AnDto, Anniversary>() {
            @Override
            protected void configure() {
                using(ctx -> {
                    String dateStr = (String) ctx.getSource();
                    return dateStr != null ? LocalDateTime.parse(dateStr) : null;
                }).map(source.getAnniversaryDate(), destination.getAnniversaryDate());
            }
        });

        modelMapper.addMappings(new PropertyMap<Anniversary, AnDto>() {
            @Override
            protected void configure() {
                using(ctx -> {
                    LocalDateTime date = (LocalDateTime) ctx.getSource();
                    return date != null ? date.toString() : null;
                }).map(source.getAnniversaryDate(), destination.getAnniversaryDate());
            }
        });

        return modelMapper;
    }
}
