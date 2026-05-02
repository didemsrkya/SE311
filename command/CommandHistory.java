package command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * CommandHistory - Command Pattern'in audit log bileşeni.
 * Tüm execute edilen komutları zaman damgasıyla kaydeder.
 * Spec'te "logged for audit purposes" zorunluluğunu karşılar.
 * Aynı zamanda undo için geçmiş komut listesini tutar.
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
        System.out.println("📋 [AUDIT LOG] " + entry);
    }

    public HRCommand pop() {
        if (history.isEmpty()) return null;
        return history.remove(history.size() - 1);
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public void printAuditLog() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                     AUDIT LOG                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        if (auditLog.isEmpty()) {
            System.out.println("║  No operations recorded.                                ║");
        } else {
            for (String entry : auditLog) {
                System.out.println("║  " + entry);
            }
        }
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
}
