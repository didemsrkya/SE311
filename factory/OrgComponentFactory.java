package factory;

import model.Department;
import model.Employee;
import model.Team;
import java.time.LocalDate;

/**
 * OrgComponentFactory - Factory Pattern'in arayüzü.
 * Tüm organizasyon nesnelerinin yaratım sorumluluğunu soyutlar.
 * Nesne oluşturma mantığını istemci kodundan ayırır.
 */
public interface OrgComponentFactory {
    Employee createEmployee(String name, String gender, LocalDate hireDate,
                            double salary, String title);
    Employee createManager(String name, String gender, LocalDate hireDate,
                           double salary);
    Team createTeam(String name);
    Department createDepartment(String name);
}
