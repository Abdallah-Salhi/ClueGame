package clueGame;

public class BadConfigFormatException extends Exception {
    public BadConfigFormatException() {
        super("Bad configuration format in input file.");
    }

    public BadConfigFormatException(String message) {
        super(message);
    }
}