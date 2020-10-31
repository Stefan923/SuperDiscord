package me.stefan923.superdiscord.exceptions;

public class MissingPermissionException extends Exception {

    public MissingPermissionException() {
        super("");
    }

    public MissingPermissionException(final String string) {
        super(string);
    }

}
