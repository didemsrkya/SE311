package visitor;

import model.Department;
import model.Employee;
import model.Team;

//This is our visitor interface
public interface ReportVisitor {
    void visitEmployee(Employee employee);
    void visitTeam(Team team);
    void visitDepartment(Department department);
    void printReport();
}
