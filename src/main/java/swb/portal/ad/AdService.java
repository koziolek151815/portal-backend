package swb.portal.ad;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import swb.portal.entities.AdEntity;
import swb.portal.entities.UserEntity;
import swb.portal.user.UserService;
import static java.util.stream.Collectors.toList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdService {
    private final AdRepository adRepository;
    private final AdFactory adFactory;
    private final UserService userService;

/*    public List<AdResponseDto> getAllAds() {
        return adRepository.findAll()
                .stream()
                .map(adFactory::entityToAdResponseDto)
                .collect(Collectors.toList());
    }*/

    public Page<AdResponseDto> getAllAds(Pageable pageable) {
        List<AdResponseDto> list = adRepository.findAll(pageable)
                .stream()
                .map(adFactory::entityToAdResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(list);
    }

    public AdResponseDto getAdById(Long adId) {
        return adFactory.entityToAdResponseDto(adRepository
                .findById(adId).orElseThrow(() -> new RuntimeException("Not found such ad")));
    }

    public AdResponseDto createAd(AdRequestDto adRequestDto) {
        UserEntity user = userService.getCurrentUser();
        AdEntity adEntity = adRepository.save(adFactory.adRequestDtoToEntity(adRequestDto, user));
        return adFactory.entityToAdResponseDto(adEntity);
    }

    public AdResponseDto updateAd(AdRequestDto adRequestDto, Long adId) {
        AdEntity adToUpdate = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Not found such ad"));
        UserEntity user = userService.getCurrentUser();
        if (!adToUpdate.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not owner of this ad");
        }
        return adFactory.entityToAdResponseDto(adRepository
                .save(adFactory.updateEntityFromAdRequestDto(adToUpdate, adRequestDto)));
    }

    public void deleteAd(Long adId) {
        adRepository.delete(adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Not found such ad")));
    }
}
