package exceptions;

import java.io.FileNotFoundException;

public class RuntimeFileNotFoundException extends RuntimeException {
    public RuntimeFileNotFoundException(FileNotFoundException e) {
        super(e);
    }

    public RuntimeFileNotFoundException(String msg) {
        super(msg);
    }
}
