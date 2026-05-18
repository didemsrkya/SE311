// TEAM MEMBERS:
// [DIDEM SARIKAYA]
// [DUYGU SOGUTDALLI]
// [YAGMUR DAGDEMIR]
// [EFE YOLARTIRAN]

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//  SINGLETON PATTERN  -  Single Instance / Global Access Point
// Singleton: OrgChartManager
// Managed shared state: one org chart, command history, and corporate head observer.

class OrgChartManager {

    private static OrgChartManager instance;

    private final List<Department> departments;
    private final CommandHistory commandHistory;
    private CorporateHead corporateHead;

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

    public boolean addDepartment(Department dept) {
        if (dept == null || departments.contains(dept)) {
            return false;
        }
        if (corporateHead != null) {
            dept.addObserver(corporateHead);
        }
        departments.add(dept);
        return true;
    }

    public boolean removeDepartment(Department dept) {
        if (dept == null || !departments.remove(dept)) {
            return false;
        }
        if (corporateHead != null) {
            dept.removeObserver(corporateHead);
        }
        return true;
    }

    public boolean containsDepartment(Department dept) {
        return departments.contains(dept);
    }

    public List<Department> getDepartments() {
        return Collections.unmodifiableList(departments);
    }

    public void setCorporateHead(CorporateHead head) {
        if (corporateHead != null) {
            for (Department dept : departments) {
                dept.removeObserver(corporateHead);
            }
        }
        this.corporateHead = head;
        if (head != null) {
            for (Department dept : departments) {
                dept.addObserver(head);
            }
        }
    }

    public boolean executeCommand(HRCommand command) {
        if (command == null) {
            return false;
        }
        boolean success = command.execute();
        if (success) {
            commandHistory.push(command);
        } else {
            commandHistory.recordFailure(command);
        }
        return success;
    }

    public boolean undoLastCommand() {
        HRCommand last = commandHistory.pop();
        if (last == null) {
            System.out.println("Nothing to undo.");
            return false;
        }
        last.undo();
        commandHistory.recordUndo(last);
        System.out.println("Undone: " + last.getDescription());
        return true;
    }

    public void generateReport(ReportVisitor visitor) {
        visitor.startReport();
        for (Department dept : departments) {
            dept.accept(visitor);
        }
        visitor.printReport();
        visitor.finishReport();
    }

    public void printOrgChart() {
        System.out.println("\n============ ORGANIZATIONAL CHART ============");
        for (Department dept : departments) {
            dept.printDetails("");
        }
        System.out.println("==============================================\n");
    }

    public void printAuditLog() {
        commandHistory.printAuditLog();
    }

    CommandHistory getCommandHistory() {
        return commandHistory;
    }
}
