package visitor;

import model.Department;
import model.Employee;
import model.Team;

// It is concrete visitor class
public class SalaryBandReportVisitor implements ReportVisitor {

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
        System.out.println("Senior          : " + seniorCount + " employees earn greater than 100 000" );
        System.out.printf( "Average Salary  : $%.2f%n", avg);
    }
}
