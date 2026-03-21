package com.cenfotec.backendcodesprint.api.ProviderSearchController;

import com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO.ProviderSearchDTO;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO.ProviderSearchResultDTO;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.Service.ProviderSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProviderSearchController {

    private final ProviderSearchService searchService;

    @GetMapping("/providers")
    public ResponseEntity<List<ProviderSearchResultDTO>> searchProviders(
            @RequestParam(required = false) String  name,
            @RequestParam(required = false) String  zone,
            @RequestParam(required = false) Double  minPrice,
            @RequestParam(required = false) Double  maxPrice,
            @RequestParam(required = false) Double  minRating,
            @RequestParam(required = false) String  category,
            @RequestParam(required = false) Boolean verifiedOnly) {

        ProviderSearchDTO filter = new ProviderSearchDTO();
        filter.setName(name);
        filter.setZone(zone);
        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);
        filter.setMinRating(minRating);
        filter.setCategory(category);
        filter.setVerifiedOnly(verifiedOnly);

        return ResponseEntity.ok(searchService.search(filter));
    }
}