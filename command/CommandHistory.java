package command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** CommandHistory is an additional class for audit log system. It gets all the executed commands with their timestamps, it satisfies the
 * "logged for audit purposes" requirement. At the same time, it holds the list of the history of commands to undo() operation
 */ 
public class CommandHistory {

    private List<HRCommand> history = new ArrayList<>();
    private List<String> auditLog  = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void push(HRCommand command) {
        history.add(command);
        String entry = "[" + LocalDateTime.now().format(FORMATTER) + "] "
                + command.getDescription();
        auditLog.add(entry);
        System.out.println("[AUDIT LOG] " + entry);
    }

    public HRCommand pop() {
        if (history.isEmpty()) return null;
        return history.remove(history.size() - 1); //removes the last command
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public void printAuditLog() {
        System.out.println("--- AUDIT LOG ---");
        if (auditLog.isEmpty()) {
            System.out.println("No operations recorded.");
        } else {
            for (String entry : auditLog) {
                System.out.println(entry);
            }
        }
        System.out.println("-----------------");
    }
}
