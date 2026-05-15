package visitor;

import model.Department;
import model.Employee;
import model.Team;
// It is concrete visitor class
public class DiversityReportVisitor implements ReportVisitor {

    private int maleCount = 0;
    private int femaleCount = 0;

    @Override
    public void visitEmployee(Employee employee) {
        //In here we visit each employee and count the number of male and female employees
        if (employee.getGender().equalsIgnoreCase("Male")) {
            maleCount++;
        } else {
            femaleCount++;
        }
    }

    @Override
    public void visitTeam(Team team) {}

    @Override
    public void visitDepartment(Department department) {}

    @Override
    public void printReport() {
        int total = maleCount + femaleCount; //all employees
        System.out.println(" DIVERSITY REPORT");
        System.out.println("Total Employees : " + total);
        // We are calculating the percentage of male and female employees.
        double percentageOfMen =0;
        double percentageOfFemale = 0;
        if(total>0){
            percentageOfMen= maleCount * 100.0 / total;
            percentageOfFemale = femaleCount * 100.0 / total;
        }
        System.out.printf("Male            : %d (%.1f%%)%n", maleCount, percentageOfMen);
        System.out.printf("Female          : %d (%.1f%%)%n", femaleCount, percentageOfFemale);
    }
}
