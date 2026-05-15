package command;

import model.Employee;

//concrete command class, promotes an employee
public class PromoteEmployeeCommand implements HRCommand {

    private Employee employee; 
    private String oldTitle;
    private String newTitle;
    private double oldSalary; //it gets old salary and old title in order to undo operation
    private double newSalary;

    public PromoteEmployeeCommand(Employee employee, String newTitle, double salaryIncrease) {
        this.employee   = employee;
        this.oldTitle   = employee.getTitle();
        this.newTitle   = newTitle;
        this.oldSalary  = employee.getSalary();
        this.newSalary  = employee.getSalary() + salaryIncrease;
    }

    @Override
    public void execute() {
        employee.setTitle(newTitle);
        employee.setSalary(newSalary);
        employee.setManager(true);
        System.out.println("#  Promoted: " + employee.getName()
                + " | " + oldTitle + " -> " + newTitle
                + " | $" + oldSalary + "-> $" + newSalary);
    }

    @Override
    public void undo() {
        employee.setTitle(oldTitle);
        employee.setSalary(oldSalary);
        employee.setManager(false);
        System.out.println("  Undo Promote: " + employee.getName()
                + " reverted to " + oldTitle);
    }

    @Override
    public String getDescription() {
        return "PROMOTE: " + employee.getName()
                + " -> " + newTitle + " (salary: $" + newSalary + ")";
    }
}
