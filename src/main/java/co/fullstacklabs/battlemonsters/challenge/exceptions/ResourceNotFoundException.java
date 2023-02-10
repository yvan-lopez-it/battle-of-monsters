package co.fullstacklabs.battlemonsters.challenge.exceptions;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3894595137664320531L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
