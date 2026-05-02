package factory;

import model.Department;
import model.Employee;
import model.Team;
import java.time.LocalDate;

/**
 * HRFactory - Factory Pattern'in Concrete Factory sınıfı.
 * OrgComponentFactory arayüzünü uygular.
 * Employee, Manager, Team ve Department nesnelerini oluşturur.
 * Promote işleminde Manager yaratımı bu sınıf üzerinden yapılır.
 */
public class HRFactory implements OrgComponentFactory {

    @Override
    public Employee createEmployee(String name, String gender, LocalDate hireDate,
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
    public Team createTeam(String name) {
        return new Team(name);
    }

    @Override
    public Department createDepartment(String name) {
        return new Department(name);
    }
}
