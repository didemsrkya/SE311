package singleton;

import command.CommandHistory;
import command.HRCommand;
import model.Department;
import observer.CorporateHead;
import visitor.ReportVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * OrgChartManager - Singleton Pattern.
 * Tüm organizasyon ağacının tek giriş noktasıdır.
 * Sadece bir instance var olabilir; bu sayede tutarsız durum oluşmaz.
 * Command çalıştırma, rapor üretme ve CorporateHead yönetimi buradan yapılır.
 */
public class OrgChartManager {

    private static OrgChartManager instance;

    private List<Department> departments;
    private CommandHistory commandHistory;
    private CorporateHead corporateHead;

    // Private constructor — dışarıdan new ile oluşturulamaz
    private OrgChartManager() {
        this.departments    = new ArrayList<>();
        this.commandHistory = new CommandHistory();
    }

    // Thread-safe değil, ama akademik proje için yeterli
    public static OrgChartManager getInstance() {
        if (instance == null) {
            instance = new OrgChartManager();
        }
        return instance;
    }

    // ── Department Yönetimi ─────────────────────────────────────

    public void addDepartment(Department dept) {
        if (corporateHead != null) {
            dept.addObserver(corporateHead);
        }
        departments.add(dept);
    }

    public void removeDepartment(Department dept) {
        departments.remove(dept);
    }

    public List<Department> getDepartments() {
        return departments;
    }

    // ── CorporateHead Atama ─────────────────────────────────────

    public void setCorporateHead(CorporateHead head) {
        this.corporateHead = head;
        // Mevcut tüm departmanlara observer olarak ekle
        for (Department dept : departments) {
            dept.addObserver(head);
        }
    }

    // ── Command Çalıştırma (audit log dahil) ────────────────────

    public void executeCommand(HRCommand command) {
        command.execute();
        commandHistory.push(command);
    }

    public void undoLastCommand() {
        HRCommand last = commandHistory.pop();
        if (last != null) {
            last.undo();
            System.out.println("↩️  Undone: " + last.getDescription());
        } else {
            System.out.println("⚠️  Nothing to undo.");
        }
    }

    // ── Rapor Üretme ────────────────────────────────────────────

    /**
     * Tüm organizasyon ağacı üzerinde Visitor çalıştırır.
     * Her departman accept() ile Visitor'ı karşılar, tree traverse edilir.
     */
    public void generateReport(ReportVisitor visitor) {
        for (Department dept : departments) {
            dept.accept(visitor);
        }
        visitor.printReport();
    }

    // ── Organizasyon Şeması Yazdırma ────────────────────────────

    public void printOrgChart() {
        System.out.println("\n════════════ ORGANIZATIONAL CHART ════════════");
        for (Department dept : departments) {
            dept.printDetails("");
        }
        System.out.println("═══════════════════════════════════════════════\n");
    }

    // ── Audit Log ───────────────────────────────────────────────

    public void printAuditLog() {
        commandHistory.printAuditLog();
    }
}
