package featuretoggle.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeatureToggleService {

    private final Environment environment;

    public FeatureToggleService(Environment environment) {
        this.environment = environment;
    }

    public boolean check(String featureName) {
        return Optional.ofNullable(environment.getProperty("application.feature.toggles." + featureName, Boolean.class))
                .orElse(false);
    }
}