package clueGame;

/*
 * Should extend exception not runTimeException because a bad configuration is a checked exception which is expected and should be handled
 */
public class BadConfigFormatException extends Exception {
    public BadConfigFormatException() {
        super("Bad configuration format in input file.");
    }

    public BadConfigFormatException(String message) {
        super(message);
    }
}