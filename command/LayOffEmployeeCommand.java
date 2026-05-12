package command;

import model.Employee;
import model.Team;

// Concrete Command 
public class LayOffEmployeeCommand implements HRCommand {

    private Team sourceTeam; //Team object reference for laying off an employee from a team
    private Employee employee; //Employee reference as a parameter for removeMember() method

    public LayOffEmployeeCommand(Team sourceTeam, Employee employee) {
        this.sourceTeam = sourceTeam;
        this.employee   = employee;
    }

    @Override
    public void execute() {
        sourceTeam.removeMember(employee);
        System.out.println("Laid off: " + employee.getName()
                + " from Team: " + sourceTeam.getName());
    }

    @Override
    public void undo() {
        sourceTeam.addMember(employee);
        System.out.println("Undo LayOff: " + employee.getName()
                + " re-added to Team: " + sourceTeam.getName());
    }

    @Override
    public String getDescription() {
        return "LAYOFF: " + employee.getName()
                + " [" + employee.getTitle() + "] from Team: " + sourceTeam.getName();
    }
}
