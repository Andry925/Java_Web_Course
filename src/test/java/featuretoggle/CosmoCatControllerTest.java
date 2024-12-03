package featuretoggle;

import featuretoggle.service.FeatureToggleService;
import org.example.galacticmarket.DemoGalacticmarketApplication;
import org.example.galacticmarket.dto.CosmocatDTO;
import org.example.galacticmarket.service.CosmoCatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DemoGalacticmarketApplication.class)
@AutoConfigureMockMvc
class CosmoCatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CosmoCatService cosmoCatService;

    @MockBean
    private FeatureToggleService featureToggleService;

    @Test
    void testGetCosmoCatsWhenFeatureToggleIsEnabled() throws Exception {
        when(featureToggleService.check(FeatureToggles.COSMO_CATS.getFeatureName())).thenReturn(true);
        List<CosmocatDTO> cosmoCats = List.of(
                CosmocatDTO.builder().username("Galactic1234").email("galactic1234@gmail.com").build(),
                CosmocatDTO.builder().username("Galaxy5678").email("galaxy5678@gmail.com").build()
        );
        when(cosmoCatService.getCosmoCats()).thenReturn(cosmoCats);

        mockMvc.perform(get("/api/v1/cosmo-cats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Galactic1234"))
                .andExpect(jsonPath("$[0].email").value("galactic1234@gmail.com"))
                .andExpect(jsonPath("$[1].username").value("Galaxy5678"))
                .andExpect(jsonPath("$[1].email").value("galaxy5678@gmail.com"));
    }

    @Test
    void testGetCosmoCatsWithFeatureToggleDisabled() throws Exception {
        when(featureToggleService.check(FeatureToggles.COSMO_CATS.getFeatureName())).thenReturn(false);

        mockMvc.perform(get("/api/v1/cosmo-cats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value("/api/v1/cosmo-cats"))
                .andExpect(jsonPath("$.error").value("Feature Not Available"))
                .andExpect(jsonPath("$.message").value("Feature 'cosmoCats' is not enabled."))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testGetCosmoCatsWithEmptyResponse() throws Exception {
        when(featureToggleService.check(FeatureToggles.COSMO_CATS.getFeatureName())).thenReturn(true);
        when(cosmoCatService.getCosmoCats()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/cosmo-cats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetCosmoCatsInvalidEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/invalid-endpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
