package command;

import model.Employee;
import model.Team;

/**
 * LayOffEmployeeCommand - Command Pattern'in Concrete Command sınıfı.
 * Bir çalışanı takımdan çıkarır (işten çıkarma).
 * undo() çağrıldığında çalışanı geri ekler.
 */
public class LayOffEmployeeCommand implements HRCommand {

    private Team sourceTeam;
    private Employee employee;

    public LayOffEmployeeCommand(Team sourceTeam, Employee employee) {
        this.sourceTeam = sourceTeam;
        this.employee   = employee;
    }

    @Override
    public void execute() {
        sourceTeam.removeMember(employee);
        System.out.println("❌ Laid off: " + employee.getName()
                + " from Team: " + sourceTeam.getName());
    }

    @Override
    public void undo() {
        sourceTeam.addMember(employee);
        System.out.println("↩️  Undo LayOff: " + employee.getName()
                + " re-added to Team: " + sourceTeam.getName());
    }

    @Override
    public String getDescription() {
        return "LAYOFF: " + employee.getName()
                + " [" + employee.getTitle() + "] from Team: " + sourceTeam.getName();
    }
}
