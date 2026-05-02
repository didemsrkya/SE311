package command;

/**
 * HRCommand - Command Pattern'in temel arayüzü.
 * Tüm HR operasyonları (hire, layoff, promote, merge, split) bu arayüzü uygular.
 * execute() işlemi gerçekleştirir, undo() geri alır.
 * CommandHistory audit log için her execute'u kaydeder.
 */
public interface HRCommand {
    void execute();
    void undo();
    String getDescription();
}
