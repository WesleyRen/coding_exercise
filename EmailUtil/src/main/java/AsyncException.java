public class AsyncException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AsyncException(final String message) {
        super(message);
    }

    public AsyncException(final Exception exception) {
        super(exception);
    }

    public AsyncException(final String message, final Exception exception) {
        super(message, exception);
    }
}
