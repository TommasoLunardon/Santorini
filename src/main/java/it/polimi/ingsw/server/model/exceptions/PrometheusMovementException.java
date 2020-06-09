package it.polimi.ingsw.server.model.exceptions;
/**
 * Exception thrown when the Prometheus movement performed isn't valid
 */
public class PrometheusMovementException extends Exception {
    public PrometheusMovementException() {
        super("You went to an upper level during Prometheus special movement!");
    }
}
