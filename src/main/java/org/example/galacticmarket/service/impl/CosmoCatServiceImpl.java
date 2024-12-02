package org.example.galacticmarket.service.impl;
import featuretoggle.exceptions.FeatureNotAvailableException;
import org.example.galacticmarket.dto.CosmocatDTO;
import org.example.galacticmarket.service.CosmoCatService;
import org.springframework.stereotype.Service;
import featuretoggle.service.FeatureToggleService;
import java.util.List;

@Service
public class CosmoCatServiceImpl implements CosmoCatService {

    private final FeatureToggleService featureToggleService;

    public CosmoCatServiceImpl(FeatureToggleService featureToggleService) {
        this.featureToggleService = featureToggleService;
    }

    @Override
    public List<CosmocatDTO> getCosmoCats() {
        if (!featureToggleService.check("cosmoCats")) {
            throw new FeatureNotAvailableException("cosmoCats");
        }

        return List.of(
                CosmocatDTO.builder().username("Galactic1234").email("galactic1234@gmail.com").build(),
                CosmocatDTO.builder().username("Galaxy000").email("galaxy000@gmail.com").build()
        );
    }
}
