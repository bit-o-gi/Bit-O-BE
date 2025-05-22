package bit.day.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/day")
@RequiredArgsConstructor
public class DayController implements DayControllerDoc {
    private final DayService dayService;

    @GetMapping("/{id}")
    public ResponseEntity<DayResponse> getDay(@PathVariable Long id) {
        return ResponseEntity.ok().body(dayService.getDay(id));
    }

    @GetMapping
    public ResponseEntity<DayResponse> getDayByCouple(@RequestParam Long coupleId) {
        return ResponseEntity.ok().body(dayService.getDayByCouple(coupleId));
    }

    @PostMapping
    public ResponseEntity<Long> createDay(@Valid @RequestBody DayRequest dayRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dayService.createDay(dayRequest.toCommand()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DayResponse> updateDay(@PathVariable Long id,
                                                 @Valid @RequestBody DayRequest dayRequest) {
        return ResponseEntity.ok().body(dayService.updateDay(id, dayRequest.toCommand()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDay(@PathVariable Long id) {
        dayService.deleteDay(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
