// TEAM MEMBERS:
// [DİDEM SARIKAYA]
// [DUYGU SÖĞÜTDALLI]
// [YAĞMUR DAĞDEMİR]
// [EFE YOLARTIRAN]


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// ════════════════════════════════════════════════════════
//  VISITOR PATTERN  —  Visitor Interface / ConcreteVisitors
// ════════════════════════════════════════════════════════

//This is our visitor interface
interface ReportVisitor {
    void visitEmployee(Employee employee);
    void visitTeam(Team team);
    void visitDepartment(Department department);
    void printReport();
}

// ────────────────────────────────────────────────────────

// It is concrete visitor class
class DiversityReportVisitor implements ReportVisitor {

    private int maleCount = 0;
    private int femaleCount = 0;

    @Override
    public void visitEmployee(Employee employee) {
        //In here we visit each employee and count the number of male and female employees
        if (employee.getGender().equalsIgnoreCase("Male")) {
            maleCount++;
        } else {
            femaleCount++;
        }
    }

    @Override
    public void visitTeam(Team team) {}

    @Override
    public void visitDepartment(Department department) {}

    @Override
    public void printReport() {
        int total = maleCount + femaleCount; //all employees
        System.out.println(" DIVERSITY REPORT");
        System.out.println("Total Employees : " + total);
        // We are calculating the percentage of male and female employees.
        double percentageOfMen = 0;
        double percentageOfFemale = 0;
        if (total > 0) {
            percentageOfMen = maleCount * 100.0 / total;
            percentageOfFemale = femaleCount * 100.0 / total;
        }
        System.out.printf("Male            : %d (%.1f%%)%n", maleCount, percentageOfMen);
        System.out.printf("Female          : %d (%.1f%%)%n", femaleCount, percentageOfFemale);
    }
}

// ────────────────────────────────────────────────────────

// It is concrete visitor class
class SeniorityReportVisitor implements ReportVisitor {

    private static final int SENIORITY_THRESHOLD = 20;
    private List<Employee> seniorEmployees = new ArrayList<>();

    @Override
    public void visitEmployee(Employee employee) {
        //We are checking if the employee worked more than 20 years
        if (employee.getYearsAtCompany() >= SENIORITY_THRESHOLD) {
            seniorEmployees.add(employee);
        }
    }

    @Override
    public void visitTeam(Team team) { }

    @Override
    public void visitDepartment(Department department) { }

    @Override
    public void printReport() {
        System.out.println(" SENIORITY REPORT");
        if (seniorEmployees.isEmpty()) {
            System.out.println(" No employees with 20+ years found.");
        } else {
            for (Employee e : seniorEmployees) {
                System.out.printf("%-20s | %2d years | %-10s %n",
                        e.getName(), e.getYearsAtCompany(), e.getTitle());
            }
        }
    }
}

// ────────────────────────────────────────────────────────

// It is concrete visitor class
class SalaryBandReportVisitor implements ReportVisitor {

    private int juniorCount = 0;   // If the salary is less than 50 000
    private int midCount    = 0;   // If the salary is between 50 000 and 100 000
    private int seniorCount = 0;   // If the salary is greater than 100 000
    private double totalSalary = 0;
    private int totalEmployees = 0;

    @Override
    public void visitEmployee(Employee employee) {
        double salary = employee.getSalary();
        totalSalary += salary;
        totalEmployees++;
        //We are deciding the employees position
        if (salary < 50_000) {
            juniorCount++;
        } else if (salary <= 100_000) {
            midCount++;
        } else {
            seniorCount++;
        }
    }

    @Override
    public void visitTeam(Team team) { }

    @Override
    public void visitDepartment(Department department) { }

    @Override
    public void printReport() {
        double avg = totalEmployees > 0 ? totalSalary / totalEmployees : 0;
        System.out.println(" SALARY BAND REPORT");
        System.out.println("Junior          : " + juniorCount + " employees earn less than 50 000");
        System.out.println("Mid             : " + midCount + " employees earn between 50 000 and 100 000");
        System.out.println("Senior          : " + seniorCount + " employees earn greater than 100 000");
        System.out.printf( "Average Salary  : $%.2f%n", avg);
    }
}

// ────────────────────────────────────────────────────────

// It is concrete visitor class
class HeadcountReportVisitor implements ReportVisitor {

    private Map<String, Integer> headcountMap = new LinkedHashMap<>(); //We hold department name and employee number in the department in this list
    private Map<String, List<String>> teamDetails = new LinkedHashMap<>(); // We hold department name and teams information ( team name and  number of employees in teams) in the department in this list
    private String currentDepartment = "";

    @Override
    public void visitDepartment(Department department) {
        currentDepartment = department.getName();
        headcountMap.put(currentDepartment, 0);
        teamDetails.put(currentDepartment, new ArrayList<>());
    }

    @Override
    public void visitTeam(Team team) {
        //We add team name and number of employee in the team in here
        if (!currentDepartment.isEmpty()) {
            String info = team.getName() + " : " + team.getMembers().size() + " employees";
            teamDetails.get(currentDepartment).add(info);
        }
    }

    @Override
    public void visitEmployee(Employee employee) {
        if (!currentDepartment.isEmpty()) {
            headcountMap.put(currentDepartment, headcountMap.getOrDefault(currentDepartment, 0) + 1);
        }
    }

    @Override
    public void printReport() {
        System.out.println(" HEADCOUNT REPORT");
        int total = 0;
        for (Map.Entry<String, Integer> entry : headcountMap.entrySet()) {
            String deptName = entry.getKey();
            int count = entry.getValue();
            total += count;
            List<String> teams = teamDetails.get(deptName);

            // This is the part where we formatting the output, We used AI in the next 2 lines to format output.
            String teamPart = teams.isEmpty() ? "" : " (" + String.join(" , ", teams) + ")";
            System.out.printf("%-15s : %3d employees%-60s %n", deptName, count, teamPart);
        }
        System.out.println("TOTAL           :   " + total + " employees");
    }
}

// ════════════════════════════════════════════════════════
//  SINGLETON PATTERN  —  OrgChartManager
// ════════════════════════════════════════════════════════

/**
 *  * OrgChartManager - Singleton Pattern.
 *  * Single entry point for the entire organization tree.
 *  * Only one instance can exist, preventing inconsistent state.
 *  * Handles command execution, report generation, and CorporateHead management.
 */
class OrgChartManager {

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
