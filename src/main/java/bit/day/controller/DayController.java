package bit.day.controller;

import bit.day.domain.Day;
import bit.day.dto.DayRequest;
import bit.day.dto.DayResponse;
import bit.day.service.DayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/day")
@RequiredArgsConstructor
public class DayController implements DayControllerDoc{
    private final DayService dayService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DayResponse getDay(@PathVariable Long id) {
        Day day = dayService.getDay(id);
        int a = 2;
        System.out.println(a);
        return DayResponse.from(day);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DayResponse createDay(@Valid @RequestBody DayRequest dayRequest) {
        Day day = dayService.createDay(dayRequest.toCommand());
        return DayResponse.from(day);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DayResponse updateDay(@PathVariable Long id,
                                 @Valid @RequestBody DayRequest dayRequest) {
        Day day = dayService.updateDay(id, dayRequest.toCommand());
        return DayResponse.from(day);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDay(@PathVariable Long id) {
        dayService.deleteDay(id);
    }
}
