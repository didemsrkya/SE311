// TEAM MEMBERS:
// [YOUR NAME 1]
// [YOUR NAME 2]
// [YOUR NAME 3]

import command.*;
import factory.HRFactory;
import factory.OrgComponentFactory;
import model.*;
import observer.CorporateHead;
import singleton.OrgChartManager;
import visitor.*;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Main - SE311 HR Application Demo
 * Tüm 6 design pattern'in entegre çalışmasını gösterir:
 * Composite, Observer, Visitor, Command, Singleton, Factory
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║     SE311 - HR Application Demo           ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        // ─── 1. SINGLETON ────────────────────────────────────────────
        System.out.println("━━━ [SINGLETON] OrgChartManager ━━━");
        OrgChartManager manager = OrgChartManager.getInstance();
        System.out.println("OrgChartManager instance created: " + (manager != null));
        System.out.println("Same instance? " + (manager == OrgChartManager.getInstance()) + "\n");

        // ─── 2. OBSERVER ─────────────────────────────────────────────
        System.out.println("━━━ [OBSERVER] CorporateHead setup ━━━");
        CorporateHead ceo = new CorporateHead("Alice Johnson (CEO)");
        manager.setCorporateHead(ceo);

        // ─── 3. FACTORY ──────────────────────────────────────────────
        System.out.println("━━━ [FACTORY] Creating org components ━━━");
        OrgComponentFactory factory = new HRFactory();

        // Departments
        Department engineering = factory.createDepartment("Engineering");
        Department marketing   = factory.createDepartment("Marketing");
        Department finance     = factory.createDepartment("Finance");

        // Teams
        Team backendTeam  = factory.createTeam("Backend Team");
        Team frontendTeam = factory.createTeam("Frontend Team");
        Team adsTeam      = factory.createTeam("Ads Team");
        Team financeTeam  = factory.createTeam("Finance Team");

        // Employees
        Employee emp1 = factory.createEmployee("Bob Smith",   "Male",   LocalDate.of(2001, 3, 15), 95000, "Senior Developer");
        Employee emp2 = factory.createEmployee("Carol White", "Female", LocalDate.of(2018, 7, 1),  72000, "Developer");
        Employee emp3 = factory.createEmployee("David Brown", "Male",   LocalDate.of(1999, 1, 20), 88000, "Developer");
        Employee emp4 = factory.createEmployee("Emma Davis",  "Female", LocalDate.of(2020, 5, 10), 65000, "Junior Developer");
        Employee emp5 = factory.createEmployee("Frank Lee",   "Male",   LocalDate.of(2015, 9, 5),  78000, "Marketing Lead");
        Employee emp6 = factory.createEmployee("Grace Kim",   "Female", LocalDate.of(2003, 2, 28), 91000, "Senior Analyst");
        Employee mgr1 = factory.createManager("Henry Clark",  "Male",   LocalDate.of(1998, 6, 12), 130000);

        // ─── 4. COMPOSITE ────────────────────────────────────────────
        System.out.println("\n━━━ [COMPOSITE] Building hierarchy ━━━");

        backendTeam.addMember(emp1);
        backendTeam.addMember(emp3);
        backendTeam.addMember(mgr1);

        frontendTeam.addMember(emp2);
        frontendTeam.addMember(emp4);

        adsTeam.addMember(emp5);
        financeTeam.addMember(emp6);

        engineering.addChild(backendTeam);
        engineering.addChild(frontendTeam);
        marketing.addChild(adsTeam);
        finance.addChild(financeTeam);

        manager.addDepartment(engineering);
        manager.addDepartment(marketing);
        manager.addDepartment(finance);

        manager.printOrgChart();

        // ─── 5. COMMAND ──────────────────────────────────────────────
        System.out.println("━━━ [COMMAND] HR Operations ━━━\n");

        // Hire
        Employee newHire = factory.createEmployee("Ivan Torres", "Male", LocalDate.of(2024, 1, 15), 60000, "Junior Developer");
        manager.executeCommand(new HireEmployeeCommand(frontendTeam, newHire));

        // Promote
        manager.executeCommand(new PromoteEmployeeCommand(emp2, "Senior Developer", 15000));

        // Merge (Observer tetiklenir → CEO bildirim alır)
        System.out.println("\n--- Merge Operation ---");
        manager.executeCommand(new MergeDepartmentCommand(engineering, marketing));

        // Split (Observer tetiklenir → CEO bildirim alır)
        System.out.println("\n--- Split Operation ---");
        manager.executeCommand(new SplitDepartmentCommand(
                engineering, "Product Team", Arrays.asList(adsTeam)));

        // Lay off
        manager.executeCommand(new LayOffEmployeeCommand(backendTeam, emp3));

        manager.printOrgChart();

        // ─── Undo Demo ───────────────────────────────────────────────
        System.out.println("━━━ [COMMAND] Undo last operation ━━━");
        manager.undoLastCommand();
        manager.printOrgChart();

        // ─── 6. VISITOR - Reports ────────────────────────────────────
        System.out.println("\n━━━ [VISITOR] Generating Reports ━━━\n");

        System.out.println("--- Report 1: Diversity ---");
        manager.generateReport(new DiversityReportVisitor());

        System.out.println("\n--- Report 2: Seniority (20+ years) ---");
        manager.generateReport(new SeniorityReportVisitor());

        System.out.println("\n--- Report 3: Salary Band ---");
        manager.generateReport(new SalaryBandReportVisitor());

        System.out.println("\n--- Report 4: Headcount by Department ---");
        manager.generateReport(new HeadcountReportVisitor());

        // ─── Audit Log ───────────────────────────────────────────────
        System.out.println("\n━━━ [COMMAND] Audit Log ━━━");
        manager.printAuditLog();

        System.out.println("\n✅ Demo complete.");
    }
}
