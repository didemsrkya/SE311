package command;

/**
 * COMMAND INTERFACE
 */
public interface HRCommand {
    void execute();
    void undo();
    String getDescription();
}
