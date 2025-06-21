package bit.day.controller;

import bit.auth.domain.UserPrincipal;
import bit.day.dto.DayDeleteCommand;
import bit.day.dto.DayFileRequest;
import bit.day.dto.DayFileResponse;
import bit.day.dto.DayRegisterRequest;
import bit.day.dto.DayResponse;
import bit.day.dto.DayUpdateRequest;
import bit.day.facade.DayFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/day")
@RequiredArgsConstructor
public class DayController implements DayControllerDoc {
    private final DayFacade dayFacade;

    @GetMapping("/{id}")
    public ResponseEntity<DayResponse> getDay(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id) {
        return ResponseEntity.ok().body(dayFacade.getDay(id));
    }

    @GetMapping("/user")
    public ResponseEntity<DayResponse> getDayByCouple(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok().body(dayFacade.getDayByCouple(userPrincipal.getId()));
    }

    @PostMapping
    public ResponseEntity<Long> createDay(@RequestBody @Valid DayRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dayFacade.createDay(request.toCommand()));
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DayFileResponse> uploadDayFile(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                         @ModelAttribute @Valid DayFileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dayFacade.uploadThumbnail(request.toCommand(userPrincipal.getId())));
    }

    @PutMapping
    public ResponseEntity<Long> updateDay(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid DayUpdateRequest dayUpdateRequest) {
        return ResponseEntity.ok().body(dayFacade.updateDay(dayUpdateRequest.toCommand(userPrincipal.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDay(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long id) {
        dayFacade.deleteDay(new DayDeleteCommand(userPrincipal.getId(), id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
