package co.fullstacklabs.battlemonsters.challenge.exceptions;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class InexistentMonsterException extends RuntimeException {

    private static final long serialVersionUID = -3810197994862153L;

    public InexistentMonsterException(final String message) {
        super(message);
    }
}
