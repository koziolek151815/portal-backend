package swb.portal.ad;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ads")
public class AdController {
    private final AdService adService;

/*    @GetMapping
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adService.getAllAds());
    }*/
    @GetMapping
    ResponseEntity<Page<AdResponseDto>> getAllAds(
            @PageableDefault()
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "adCreatedDate", direction = Sort.Direction.DESC)
            }) Pageable pageable) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(adService.getAllAds( pageable));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adService.getAdById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody AdRequestDto adRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adService.createAd(adRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody AdRequestDto adRequestDto, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adService.updateAd(adRequestDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        adService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
