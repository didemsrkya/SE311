package singleton;

import command.CommandHistory;
import command.HRCommand;
import model.Department;
import observer.CorporateHead;
import visitor.ReportVisitor;
import java.util.ArrayList;
import java.util.List;

 /**
  *  * OrgChartManager - Singleton Pattern.
  *  * Single entry point for the entire organization tree.
  *  * Only one instance can exist, preventing inconsistent state.
  *  * Handles command execution, report generation, and CorporateHead management.
  *  */

public class OrgChartManager {

    private static OrgChartManager instance;

    private List<Department> departments;
    private CommandHistory commandHistory;
    private CorporateHead corporateHead;

     // Private constructor - cannot be instantiated from outside -
    private OrgChartManager() {
        this.departments    = new ArrayList<>();
        this.commandHistory = new CommandHistory();
    }

     public static synchronized OrgChartManager getInstance() {
         if (instance == null) {
             instance = new OrgChartManager();
         }
         return instance;
     }

    //Department Management

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

     //CorporateHead Assignment

    public void setCorporateHead(CorporateHead head) {
        this.corporateHead = head;
        // Add as observer to all existing departments
        for (Department dept : departments) {
            dept.addObserver(head);
        }
    }

     //Command Execution (including audit log)

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

     //Report Generation

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

     //Org Chart Printing

    public void printOrgChart() {
        System.out.println("\n════════════ ORGANIZATIONAL CHART ════════════");
        for (Department dept : departments) {
            dept.printDetails("");
        }
        System.out.println("═══════════════════════════════════════════════\n");
    }

    //Audit Log

    public void printAuditLog() {
        commandHistory.printAuditLog();
    }
}
