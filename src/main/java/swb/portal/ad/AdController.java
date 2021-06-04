package swb.portal.ad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ads")
public class AdController {
    private final AdService adService;

    @GetMapping
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adService.getAllAds());
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
