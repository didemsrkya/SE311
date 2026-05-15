package visitor;

import model.Department;
import model.Employee;
import model.Team;
import java.util.ArrayList;
import java.util.List;
// It is concrete visitor class
public class SeniorityReportVisitor implements ReportVisitor {

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
