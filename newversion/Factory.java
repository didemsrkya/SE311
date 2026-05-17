// TEAM MEMBERS:
// [DİDEM SARIKAYA]
// [DUYGU SÖĞÜTDALLI]
// [YAĞMUR DAĞDEMİR]
// [EFE YOLARTIRAN]

package newversion;
import java.time.LocalDate;

// ════════════════════════════════════════════════════════
//  FACTORY PATTERN  —  AbstractFactory / ConcreteFactory
// ════════════════════════════════════════════════════════

/**
 * OrgComponentFactory - Interface of the Factory Pattern.
 * Abstracts the creation responsibility of all organization objects.
 * Separates object creation logic from client code.
 */
interface OrgComponentFactory {
    Employee createEmployee(String name, String gender, LocalDate hireDate,
                            double salary, String title);
    Employee createManager(String name, String gender, LocalDate hireDate,
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
    public Employee createManager(String name, String gender, LocalDate hireDate,
                                  double salary) {
        // Title as "Manager" — role is also flagged via setManager()
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
