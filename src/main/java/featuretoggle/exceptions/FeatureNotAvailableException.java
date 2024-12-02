package featuretoggle.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FeatureNotAvailableException extends RuntimeException {
    public FeatureNotAvailableException(String featureName) {
        super("Feature '" + featureName + "' is not enabled.");
    }
}
