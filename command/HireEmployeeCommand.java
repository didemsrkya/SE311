package command;

import model.Employee;
import model.Team;

//concrete command class
public class HireEmployeeCommand implements HRCommand {

    private Team targetTeam; //takes a Team object reference as an attribute just to run addMember() method
    private Employee newEmployee; //takes an Employee object  reference as an attribute just to pass it as a parameter to addMember() method

    public HireEmployeeCommand(Team targetTeam, Employee newEmployee) {
        this.targetTeam  = targetTeam;
        this.newEmployee = newEmployee;
    }

    @Override
    public void execute() { //triggers receiver (Team)
        targetTeam.addMember(newEmployee);
        System.out.println("Hired: " + newEmployee.getName()  
                + " -> Team: " + targetTeam.getName());
    }

    @Override
    public void undo() { //removes last added employee
        targetTeam.removeMember(newEmployee);
        System.out.println("  Undo Hire: " + newEmployee.getName()
                + " removed from Team: " + targetTeam.getName());
    }

    @Override
    public String getDescription() { //prints the hired employee's information
        return "HIRE: " + newEmployee.getName()
                + " [" + newEmployee.getTitle() + "] -> Team: " + targetTeam.getName();
    }
}
