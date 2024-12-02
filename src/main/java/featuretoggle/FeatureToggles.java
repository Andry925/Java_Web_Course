package featuretoggle;

import lombok.Getter;

@Getter
public enum FeatureToggles {
    COSMO_CATS("cosmoCats");

    private final String featureName;

    FeatureToggles(String featureName) {
        this.featureName = featureName;
    }

}

