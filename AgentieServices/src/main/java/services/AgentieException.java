package services;

public class AgentieException extends RuntimeException{
    public AgentieException() {
        super();
    }

    public AgentieException(String message) {
        super(message);
    }

    public AgentieException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentieException(Throwable cause) {
        super(cause);
    }

    protected AgentieException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
