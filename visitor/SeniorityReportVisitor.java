package visitor;

import model.Department;
import model.Employee;
import model.Team;
import java.util.ArrayList;
import java.util.List;

/**
 * SeniorityReportVisitor - Visitor Pattern'in Concrete Visitor sınıfı.
 * Şirkette 20 yıldan fazla çalışan kıdemli çalışanları listeler.
 * Spec'te doğrudan istenmiş bir rapordur.
 */
public class SeniorityReportVisitor implements ReportVisitor {

    private static final int SENIORITY_THRESHOLD = 20;
    private List<Employee> seniorEmployees = new ArrayList<>();

    @Override
    public void visitEmployee(Employee employee) {
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
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║             SENIORITY REPORT (20+ years)         ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        if (seniorEmployees.isEmpty()) {
            System.out.println("║  No employees with 20+ years found.             ║");
        } else {
            for (Employee e : seniorEmployees) {
                System.out.printf("║  %-20s | %2d years | %-10s ║%n",
                        e.getName(), e.getYearsAtCompany(), e.getTitle());
            }
        }
        System.out.println("╚══════════════════════════════════════════════════╝");
    }
}
