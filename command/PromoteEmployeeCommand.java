package command;

import model.Employee;

/**
 * PromoteEmployeeCommand - Command Pattern'in Concrete Command sınıfı.
 * Bir çalışanı Manager unvanına terfi ettirir.
 * undo() çağrıldığında eski unvanına geri döner.
 * Factory Pattern ile birlikte çalışır (terfi işlemi nesne yaratmaz, sadece state değiştirir).
 */
public class PromoteEmployeeCommand implements HRCommand {

    private Employee employee;
    private String oldTitle;
    private String newTitle;
    private double oldSalary;
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
        System.out.println("⬆️  Promoted: " + employee.getName()
                + " | " + oldTitle + " → " + newTitle
                + " | $" + oldSalary + " → $" + newSalary);
    }

    @Override
    public void undo() {
        employee.setTitle(oldTitle);
        employee.setSalary(oldSalary);
        employee.setManager(false);
        System.out.println("↩️  Undo Promote: " + employee.getName()
                + " reverted to " + oldTitle);
    }

    @Override
    public String getDescription() {
        return "PROMOTE: " + employee.getName()
                + " → " + newTitle + " (salary: $" + newSalary + ")";
    }
}
