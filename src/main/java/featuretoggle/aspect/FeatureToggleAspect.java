package featuretoggle.aspect;


import featuretoggle.FeatureToggles;
import featuretoggle.annotation.FeatureToggleAnnotation;
import featuretoggle.exceptions.FeatureNotAvailableException;
import featuretoggle.service.FeatureToggleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    @Before("@annotation(featureToggleAnnotation)")
    public void checkFeatureToggle(FeatureToggleAnnotation featureToggleAnnotation) {
        FeatureToggles toggle = featureToggleAnnotation.value();
        String featureName = toggle.getFeatureName();

        if (!featureToggleService.check(featureName)) {
            log.warn("Feature toggle '{}' is disabled. Access denied.", featureName);
            throw new FeatureNotAvailableException(featureName);
        }

        log.info("Feature toggle '{}' is enabled. Proceeding with execution.", featureName);
    }
}
