// TEAM MEMBERS:
// [DİDEM SARIKAYA]
// [DUYGU SÖĞÜTDALLI]
// [YAĞMUR DAĞDEMİR]
// [EFE YOLARTIRAN]

import java.util.ArrayList;
import java.util.List;

//  SINGLETON PATTERN  —  Single Instance / Global Access Point
// Participant mapping:
// Singleton: OrgChartManager
// Unique instance holder: private static OrgChartManager instance
// Private constructor: prevents direct object creation from outside
// Global access point: OrgChartManager.getInstance()
// Managed shared state: departments, command history, and corporate head observer
// Client: Main obtains the single manager instance through getInstance()

class OrgChartManager {

    private static OrgChartManager instance;

    private List<Department> departments;
    private CommandHistory commandHistory;
    private CorporateHead corporateHead;

    // Private constructor prevents direct instantiation from outside this class.
    private OrgChartManager() {
        this.departments = new ArrayList<>();
        this.commandHistory = new CommandHistory();
    }

    public static synchronized OrgChartManager getInstance() {
        if (instance == null) {
            instance = new OrgChartManager();
        }
        return instance;
    }

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

    public void setCorporateHead(CorporateHead head) {
        this.corporateHead = head;
        for (Department dept : departments) {
            dept.addObserver(head);
        }
    }

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

    public void generateReport(ReportVisitor visitor) {
        for (Department dept : departments) {
            dept.accept(visitor);
        }
        visitor.printReport();
    }

    public void printOrgChart() {
        System.out.println("\n════════════ ORGANIZATIONAL CHART ════════════");
        for (Department dept : departments) {
            dept.printDetails("");
        }
        System.out.println("═══════════════════════════════════════════════\n");
    }

    public void printAuditLog() {
        commandHistory.printAuditLog();
    }
}
