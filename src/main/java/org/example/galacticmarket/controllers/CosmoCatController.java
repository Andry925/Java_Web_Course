package org.example.galacticmarket.controllers;


import featuretoggle.FeatureToggles;
import featuretoggle.annotation.FeatureToggleAnnotation;
import org.example.galacticmarket.dto.CosmocatDTO;
import org.example.galacticmarket.service.CosmoCatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/v1/cosmo-cats")
public class CosmoCatController {
    private final CosmoCatService cosmoCatService;

    public CosmoCatController(CosmoCatService cosmoCatService) {
        this.cosmoCatService = cosmoCatService;
    }

    @GetMapping
    @FeatureToggleAnnotation(FeatureToggles.COSMO_CATS)
    public ResponseEntity<List<CosmocatDTO>> getAllCosmoCats() {
        List<CosmocatDTO> cosmoCats = cosmoCatService.getCosmoCats();
        return ResponseEntity.ok(cosmoCats);
    }
}