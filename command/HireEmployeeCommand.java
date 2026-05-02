package command;

import model.Employee;
import model.Team;

/**
 * HireEmployeeCommand - Command Pattern'in Concrete Command sınıfı.
 * Yeni bir çalışanı belirtilen takıma ekler.
 * undo() çağrıldığında çalışanı takımdan çıkarır.
 */
public class HireEmployeeCommand implements HRCommand {

    private Team targetTeam;
    private Employee newEmployee;

    public HireEmployeeCommand(Team targetTeam, Employee newEmployee) {
        this.targetTeam  = targetTeam;
        this.newEmployee = newEmployee;
    }

    @Override
    public void execute() {
        targetTeam.addMember(newEmployee);
        System.out.println("✅ Hired: " + newEmployee.getName()
                + " → Team: " + targetTeam.getName());
    }

    @Override
    public void undo() {
        targetTeam.removeMember(newEmployee);
        System.out.println("↩️  Undo Hire: " + newEmployee.getName()
                + " removed from Team: " + targetTeam.getName());
    }

    @Override
    public String getDescription() {
        return "HIRE: " + newEmployee.getName()
                + " [" + newEmployee.getTitle() + "] → Team: " + targetTeam.getName();
    }
}
