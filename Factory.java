// TEAM MEMBERS:
// [DIDEM SARIKAYA]
// [DUYGU SOGUTDALLI]
// [YAGMUR DAGDEMIR]
// [EFE YOLARTIRAN]
import java.time.LocalDate;
import java.util.Arrays;

//  FACTORY PATTERN  —  AbstractFactory / ConcreteFactory
// Participant mapping:
// Abstract Factory: OrgComponentFactory
// Concrete Factory: HRFactory
// Abstract Products: OrgComponent-compatible organization objects
// Concrete Products: Employee, Team, Department
// Client: Main requests objects through OrgComponentFactory instead of using constructors directly

/**
 * OrgComponentFactory - Interface of the Factory Pattern.
 * Abstracts the creation responsibility of all organization objects.
 * Separates object creation logic from client code.
 */
interface OrgComponentFactory {
    Employee createEmployee(String name, String gender, LocalDate hireDate,
                            double salary, String title);
    Employee createEmployee(String name, Gender gender, LocalDate hireDate,
                            double salary, String title);
    Employee createManager(String name, String gender, LocalDate hireDate,
                           double salary);
    Employee createManager(String name, Gender gender, LocalDate hireDate,
                           double salary);
    Team createTeam(String name);
    Department createDepartment(String name);
}

// ────────────────────────────────────────────────────────

/**
 * HRFactory - Concrete Factory class of the Factory Pattern.
 * Implements the OrgComponentFactory interface.
 * Responsible for creating Employee, Manager, Team, and Department objects.
 * Manager creation during promotion is handled through this class.
 */
class HRFactory implements OrgComponentFactory {

    @Override
    public Employee createEmployee(String name, String gender, LocalDate hireDate,
                                   double salary, String title) {
        return new Employee(name, gender, hireDate, salary, title);
    }

    @Override
    public Employee createEmployee(String name, Gender gender, LocalDate hireDate,
                                   double salary, String title) {
        return new Employee(name, gender, hireDate, salary, title);
    }

    @Override
    public Employee createManager(String name, String gender, LocalDate hireDate,
                                  double salary) {
        Employee manager = new Employee(name, gender, hireDate, salary, "Manager");
        manager.setManager(true);
        return manager;
    }

    @Override
    public Employee createManager(String name, Gender gender, LocalDate hireDate,
                                  double salary) {
        Employee manager = new Employee(name, gender, hireDate, salary, "Manager");
        manager.setManager(true);
        return manager;
    }

    @Override
    public Team createTeam(String name) {
        return new Team(name);
    }

    @Override
    public Department createDepartment(String name) {
        return new Department(name);
    }
}

class Main {

    public static void main(String[] args) {

        System.out.println("=== SE311 - HR Application Demo ===\n");

        System.out.println("(SINGLETON) OrgChartManager");
        OrgChartManager manager = OrgChartManager.getInstance();
        System.out.println("Same instance? " + (manager == OrgChartManager.getInstance()) + "\n");

        System.out.println("(OBSERVER) CorporateHead setup");
        CorporateHead ceo = new CorporateHead("Alice Johnson (CEO)");
        manager.setCorporateHead(ceo);

        System.out.println("(FACTORY) Creating org components");
        OrgComponentFactory factory = new HRFactory();

        Department engineering = factory.createDepartment("Engineering");
        Department marketing = factory.createDepartment("Marketing");
        Department finance = factory.createDepartment("Finance");

        Team backendTeam = factory.createTeam("Backend Team");
        Team frontendTeam = factory.createTeam("Frontend Team");
        Team adsTeam = factory.createTeam("Ads Team");
        Team financeTeam = factory.createTeam("Finance Team");

        Employee emp1 = factory.createEmployee("Duygu Sogutdalli", "Female",
                LocalDate.of(2001, 3, 15), 95000, "Senior Developer");
        Employee emp2 = factory.createEmployee("Yagmur Dagdemir", "Female",
                LocalDate.of(2018, 7, 1), 72000, "Developer");
        Employee emp3 = factory.createEmployee("Didem Sarikaya", "Female",
                LocalDate.of(1999, 1, 20), 88000, "Developer");
        Employee emp4 = factory.createEmployee("Efe Yolartiran", "Male",
                LocalDate.of(2020, 5, 10), 65000, "Junior Developer");
        Employee emp5 = factory.createEmployee("Semih Yagci", "Male",
                LocalDate.of(2015, 9, 5), 78000, "Marketing Lead");
        Employee emp6 = factory.createEmployee("Tony Stark", "Male",
                LocalDate.of(2003, 2, 28), 91000, "Senior Analyst");
        Employee mgr1 = factory.createManager("Ufuk Celikkan", "Male",
                LocalDate.of(1998, 6, 12), 130000);

        System.out.println("\n(COMPOSITE) Building hierarchy");
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

        System.out.println("(COMMAND) HR Operations\n");
        Employee newHire = factory.createEmployee("Adriana Lima", "Female",
                LocalDate.of(2024, 1, 15), 60000, "Junior Developer");
        manager.executeCommand(new HireEmployeeCommand(frontendTeam, newHire));
        manager.executeCommand(new PromoteEmployeeCommand(emp2, "Senior Developer", 15000));

        System.out.println("\n--- Merge Operation ---");
        manager.executeCommand(new MergeDepartmentCommand(engineering, marketing));

        System.out.println("\n--- Split Operation ---");
        manager.executeCommand(new SplitDepartmentCommand(
                engineering, "Product Team", Arrays.asList(adsTeam)));

        manager.executeCommand(new LayOffEmployeeCommand(backendTeam, emp3));
        manager.printOrgChart();

        System.out.println("(COMMAND) Undo last operation");
        manager.undoLastCommand();
        manager.printOrgChart();

        System.out.println("\n(VISITOR) Generating Reports\n");
        System.out.println("--- Report 1: Diversity ---");
        manager.generateReport(new DiversityReportVisitor());

        System.out.println("\n--- Report 2: Seniority (20+ years) ---");
        manager.generateReport(new SeniorityReportVisitor());

        System.out.println("\n--- Report 3: Salary Band ---");
        manager.generateReport(new SalaryBandReportVisitor());

        System.out.println("\n--- Report 4: Headcount by Department ---");
        manager.generateReport(new HeadcountReportVisitor());

        System.out.println("\n--- Report 5: Hire Date by Year ---");
        manager.generateReport(new HireDateReportVisitor());

        System.out.println("\n(COMMAND) Audit Log");
        manager.printAuditLog();

        System.out.println("\nDemo complete");
    }
}
