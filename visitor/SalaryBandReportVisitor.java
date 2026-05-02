package visitor;

import model.Department;
import model.Employee;
import model.Team;

/**
 * SalaryBandReportVisitor - Visitor Pattern'in Concrete Visitor sınıfı.
 * Çalışanları maaş bandına göre gruplar: Junior (<50k), Mid (50k-100k), Senior (>100k).
 * Spec'te "you choose two other kinds of reporting" kapsamında seçilmiştir.
 */
public class SalaryBandReportVisitor implements ReportVisitor {

    private int juniorCount = 0;   // < 50,000
    private int midCount    = 0;   // 50,000 - 100,000
    private int seniorCount = 0;   // > 100,000
    private double totalSalary = 0;
    private int totalEmployees = 0;

    @Override
    public void visitEmployee(Employee employee) {
        double salary = employee.getSalary();
        totalSalary += salary;
        totalEmployees++;

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
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║         SALARY BAND REPORT           ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ Junior  (< $50k)   : " + juniorCount);
        System.out.println("║ Mid     ($50k-100k): " + midCount);
        System.out.println("║ Senior  (> $100k)  : " + seniorCount);
        System.out.printf( "║ Average Salary     : $%.2f%n", avg);
        System.out.println("╚══════════════════════════════════════╝");
    }
}
