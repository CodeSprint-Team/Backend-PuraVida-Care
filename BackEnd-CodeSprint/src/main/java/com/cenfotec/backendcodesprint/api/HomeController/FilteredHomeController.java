package com.cenfotec.backendcodesprint.api.HomeController;

import com.cenfotec.backendcodesprint.logic.FilteredHome.DTO.FilteredHomeResponseDTO;
import com.cenfotec.backendcodesprint.logic.FilteredHome.Service.FilteredHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filtered-home")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class FilteredHomeController {

    private final FilteredHomeService filteredHomeService;

    @GetMapping("/{bookingId}")
    public ResponseEntity<List<FilteredHomeResponseDTO>> getFilteredHome(
            @PathVariable Long bookingId,
            @RequestParam Long providerProfileId,
            @RequestParam(required = false) String layer) {

        List<FilteredHomeResponseDTO> response =
                filteredHomeService.getFilteredHome(bookingId, providerProfileId, layer);

        return ResponseEntity.ok(response);
    }
}
