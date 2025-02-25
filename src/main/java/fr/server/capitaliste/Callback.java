package fr.server.capitaliste;

@FunctionalInterface
public interface Callback {
    void call(Object... args);
}
