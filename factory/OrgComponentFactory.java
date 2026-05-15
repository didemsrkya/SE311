package factory;

import model.Department;
import model.Employee;
import model.Team;
import java.time.LocalDate;

/**
 * OrgComponentFactory - Interface of the Factory Pattern.
 * Abstracts the creation responsibility of all organization objects.
 * Separates object creation logic from client code.
 */

public interface OrgComponentFactory {
    Employee createEmployee(String name, String gender, LocalDate hireDate,
                            double salary, String title);
    Employee createManager(String name, String gender, LocalDate hireDate,
                           double salary);
    Team createTeam(String name);
    Department createDepartment(String name);
}
