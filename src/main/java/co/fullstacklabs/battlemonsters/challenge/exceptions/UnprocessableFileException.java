package co.fullstacklabs.battlemonsters.challenge.exceptions;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class UnprocessableFileException extends RuntimeException {

    private static final long serialVersionUID = -8345633438651819675L;

    public UnprocessableFileException(String message) {
        super(message);
    }
}
