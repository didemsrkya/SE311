// TEAM MEMBERS:
// [DİDEM SARIKAYA]
// [DUYGU SÖĞÜTDALLI]
// [YAĞMUR DAĞDEMİR]
// [EFE YOLARTIRAN]
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// ════════════════════════════════════════════════════════
//  COMMAND PATTERN  —  Command / ConcreteCommands / History
// ════════════════════════════════════════════════════════
// Participant mapping:
// Command: HRCommand
// Concrete Commands: HireEmployeeCommand, PromoteEmployeeCommand,
// MergeDepartmentCommand, SplitDepartmentCommand, LayOffEmployeeCommand
// Receivers: Team, Employee, Department, and OrgChartManager
// Invoker: OrgChartManager.executeCommand()
// Client: Main creates command objects and sends them to OrgChartManager
// History/Audit support: CommandHistory stores executed commands and log entries

/**
 * COMMAND INTERFACE
 */
interface HRCommand {
    void execute();
    void undo();
    String getDescription();
}

// ────────────────────────────────────────────────────────

/** CommandHistory is an additional class for audit log system. It gets all the executed commands with their timestamps, it satisfies the
 * "logged for audit purposes" requirement. At the same time, it holds the list of the history of commands to undo() operation
 */
class CommandHistory {

    private List<HRCommand> history = new ArrayList<>();
    private List<String> auditLog  = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void push(HRCommand command) {
        history.add(command);
        String entry = "[" + LocalDateTime.now().format(FORMATTER) + "] "
                + command.getDescription();
        auditLog.add(entry);
        System.out.println("[AUDIT LOG] " + entry);
    }

    public HRCommand pop() {
        if (history.isEmpty()) return null;
        return history.remove(history.size() - 1); //removes the last command
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public void printAuditLog() {
        System.out.println("--- AUDIT LOG ---");
        if (auditLog.isEmpty()) {
            System.out.println("No operations recorded.");
        } else {
            for (String entry : auditLog) {
                System.out.println(entry);
            }
        }
        System.out.println("-----------------");
    }
}

// ────────────────────────────────────────────────────────

//concrete command class
class HireEmployeeCommand implements HRCommand {

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

// ────────────────────────────────────────────────────────

//concrete command class, promotes an employee
class PromoteEmployeeCommand implements HRCommand {

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

// ────────────────────────────────────────────────────────

// concrete command class, it merges two departments
class MergeDepartmentCommand implements HRCommand {

    private Department target; //target class object reference (the department which will contain 2 departments)
    private Department source; //the departmen whose children will be moved to the target department
    private List<OrgComponent> savedSourceChildren;

    public MergeDepartmentCommand(Department target, Department source) {
        this.target = target;
        this.source = source;
        // saving for undo() operation
        this.savedSourceChildren = new ArrayList<>(source.getChildren());
    }

    @Override
    public void execute() {
        target.merge(source);
        OrgChartManager.getInstance().removeDepartment(source); // ekle
        System.out.println("Merged: '" + source.getName()
                + "' into '" + target.getName() + "'");;
    }

    @Override
    public void undo() {
        for (OrgComponent child : savedSourceChildren) {
            target.getChildren().remove(child);
            source.addChild(child);
        }
        OrgChartManager.getInstance().addDepartment(source); // ekle
        System.out.println("Undo Merge: '" + source.getName()
                + "' restored from '" + target.getName() + "'");
    }

    @Override
    public String getDescription() {
        return "MERGE: Department '" + source.getName()
                + "' -> '" + target.getName() + "'";
    }
}

// ────────────────────────────────────────────────────────

//concrete command class, it separates a team from a departmen to create a new department
class SplitDepartmentCommand implements HRCommand {

    private Department sourceDepartment;
    private String newDepartmentName;
    private List<OrgComponent> teamsToSplit; //the reference to the team which will be splitted
    private Department createdDepartment;

    public SplitDepartmentCommand(Department sourceDepartment,
                                   String newDepartmentName,
                                   List<OrgComponent> teamsToSplit) {
        this.sourceDepartment  = sourceDepartment;
        this.newDepartmentName = newDepartmentName;
        this.teamsToSplit      = teamsToSplit;
    }

    @Override
    public void execute() {
        createdDepartment = sourceDepartment.split(newDepartmentName, teamsToSplit);
        OrgChartManager.getInstance().addDepartment(createdDepartment);
        System.out.println("Split: '" + sourceDepartment.getName()
                + "' -> new dept '" + newDepartmentName + "'");
    }

    @Override
    public void undo() {
        // moving the new deparment's children to the source
        for (OrgComponent child : createdDepartment.getChildren()) {
            sourceDepartment.addChild(child);
        }
        OrgChartManager.getInstance().removeDepartment(createdDepartment);
        System.out.println("  Undo Split: '" + newDepartmentName
                + "' merged back into '" + sourceDepartment.getName() + "'");
    }

    @Override
    public String getDescription() {
        return "SPLIT: '" + sourceDepartment.getName()
                + "' -> new department '" + newDepartmentName + "'";
    }
}

// ────────────────────────────────────────────────────────

// Concrete Command
class LayOffEmployeeCommand implements HRCommand {

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
