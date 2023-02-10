package co.fullstacklabs.battlemonsters.challenge.exceptions;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class UndefinedMonsterException extends RuntimeException {

    private static final long serialVersionUID = -2456479608126013825L;

    public UndefinedMonsterException(final String message) {
        super(message);
    }
}
