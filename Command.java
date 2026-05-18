// TEAM MEMBERS:
// [DIDEM SARIKAYA]
// [DUYGU SOGUTDALLI]
// [YAGMUR DAGDEMIR]
// [EFE YOLARTIRAN]

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//  COMMAND PATTERN  -  Command / ConcreteCommands / History
// Command: HRCommand
// Receivers: Team, Employee, Department, and OrgChartManager
// Invoker: OrgChartManager.executeCommand()
// Audit support: CommandHistory records executed and undo operations.

interface HRCommand {
    boolean execute();
    void undo();
    String getDescription();
}

class CommandHistory {

    private final List<HRCommand> history = new ArrayList<>();
    private final List<String> auditLog = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void push(HRCommand command) {
        history.add(command);
        addAudit("EXECUTE: " + command.getDescription());
    }

    public HRCommand pop() {
        if (history.isEmpty()) {
            return null;
        }
        return history.remove(history.size() - 1);
    }

    public void recordUndo(HRCommand command) {
        addAudit("UNDO: " + command.getDescription());
    }

    public void recordFailure(HRCommand command) {
        addAudit("REJECTED: " + command.getDescription());
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
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

    private void addAudit(String message) {
        String entry = "[" + LocalDateTime.now().format(FORMATTER) + "] " + message;
        auditLog.add(entry);
        System.out.println("[AUDIT LOG] " + entry);
    }
}

class HireEmployeeCommand implements HRCommand {

    private final Team targetTeam;
    private final Employee newEmployee;
    private boolean executed;

    public HireEmployeeCommand(Team targetTeam, Employee newEmployee) {
        this.targetTeam = targetTeam;
        this.newEmployee = newEmployee;
    }

    @Override
    public boolean execute() {
        if (targetTeam == null || newEmployee == null || targetTeam.hasMember(newEmployee)) {
            return false;
        }
        executed = targetTeam.addMember(newEmployee);
        if (executed) {
            System.out.println("Hired: " + newEmployee.getName()
                    + " -> Team: " + targetTeam.getName());
        }
        return executed;
    }

    @Override
    public void undo() {
        if (!executed) {
            return;
        }
        targetTeam.removeMember(newEmployee);
        executed = false;
        System.out.println("Undo Hire: " + newEmployee.getName()
                + " removed from Team: " + targetTeam.getName());
    }

    @Override
    public String getDescription() {
        return "HIRE: " + describeEmployee(newEmployee)
                + " -> Team: " + describeTeam(targetTeam);
    }

    private String describeEmployee(Employee employee) {
        if (employee == null) {
            return "<null employee>";
        }
        return employee.getName() + " [" + employee.getTitle() + "]";
    }

    private String describeTeam(Team team) {
        return team == null ? "<null team>" : team.getName();
    }
}

class PromoteEmployeeCommand implements HRCommand {

    private final Employee employee;
    private final String newTitle;
    private final double salaryIncrease;
    private String oldTitle;
    private double oldSalary;
    private boolean oldManagerStatus;
    private boolean executed;

    public PromoteEmployeeCommand(Employee employee, String newTitle, double salaryIncrease) {
        this.employee = employee;
        this.newTitle = newTitle;
        this.salaryIncrease = salaryIncrease;
    }

    @Override
    public boolean execute() {
        if (employee == null || newTitle == null || newTitle.trim().isEmpty()
                || !Double.isFinite(salaryIncrease) || salaryIncrease <= 0) {
            return false;
        }

        oldTitle = employee.getTitle();
        oldSalary = employee.getSalary();
        oldManagerStatus = employee.isManager();
        double newSalary = oldSalary + salaryIncrease;
        Employee.requireValidSalary(newSalary, "Promoted salary");

        employee.setTitle(newTitle);
        employee.setSalary(newSalary);
        employee.setManager(true);
        executed = true;

        System.out.println("Promoted: " + employee.getName()
                + " | " + oldTitle + " -> " + newTitle
                + " | $" + String.format("%.2f", oldSalary)
                + " -> $" + String.format("%.2f", newSalary));
        return true;
    }

    @Override
    public void undo() {
        if (!executed) {
            return;
        }
        employee.setTitle(oldTitle);
        employee.setSalary(oldSalary);
        employee.setManager(oldManagerStatus);
        executed = false;
        System.out.println("Undo Promote: " + employee.getName()
                + " reverted to " + oldTitle);
    }

    @Override
    public String getDescription() {
        String name = employee == null ? "<null employee>" : employee.getName();
        return "PROMOTE: " + name + " -> " + newTitle
                + " (increase: $" + String.format("%.2f", salaryIncrease) + ")";
    }
}

class MergeDepartmentCommand implements HRCommand {

    private final Department target;
    private final Department source;
    private final OrgChartManager manager;
    private List<OrgComponent> movedChildren = new ArrayList<>();
    private boolean sourceWasRegistered;
    private boolean executed;

    public MergeDepartmentCommand(Department target, Department source) {
        this.target = target;
        this.source = source;
        this.manager = OrgChartManager.getInstance();
    }

    @Override
    public boolean execute() {
        if (target == null || source == null || target == source
                || !manager.containsDepartment(target) || !manager.containsDepartment(source)
                || source.getChildren().isEmpty()) {
            return false;
        }
        for (OrgComponent child : source.getChildren()) {
            if (target.hasChild(child)) {
                return false;
            }
        }

        movedChildren = new ArrayList<>(source.getChildren());
        sourceWasRegistered = manager.containsDepartment(source);
        executed = target.merge(source);
        if (executed && sourceWasRegistered) {
            manager.removeDepartment(source);
            System.out.println("Merged: '" + source.getName()
                    + "' into '" + target.getName() + "'");
        }
        return executed;
    }

    @Override
    public void undo() {
        if (!executed) {
            return;
        }
        for (OrgComponent child : movedChildren) {
            target.removeChild(child);
            source.addChild(child);
        }
        if (sourceWasRegistered) {
            manager.addDepartment(source);
        }
        executed = false;
        System.out.println("Undo Merge: '" + source.getName()
                + "' restored from '" + target.getName() + "'");
    }

    @Override
    public String getDescription() {
        return "MERGE: Department '" + describeDepartment(source)
                + "' -> '" + describeDepartment(target) + "'";
    }

    private String describeDepartment(Department department) {
        return department == null ? "<null department>" : department.getName();
    }
}

class SplitDepartmentCommand implements HRCommand {

    private final Department sourceDepartment;
    private final String newDepartmentName;
    private final List<OrgComponent> childrenToSplit;
    private final OrgChartManager manager;
    private Department createdDepartment;
    private boolean executed;

    public SplitDepartmentCommand(Department sourceDepartment,
                                  String newDepartmentName,
                                  List<OrgComponent> childrenToSplit) {
        this.sourceDepartment = sourceDepartment;
        this.newDepartmentName = newDepartmentName;
        this.childrenToSplit = childrenToSplit == null
                ? Collections.emptyList()
                : new ArrayList<>(childrenToSplit);
        this.manager = OrgChartManager.getInstance();
    }

    @Override
    public boolean execute() {
        if (sourceDepartment == null || !manager.containsDepartment(sourceDepartment)
                || newDepartmentName == null || newDepartmentName.trim().isEmpty()
                || childrenToSplit.isEmpty()) {
            return false;
        }
        for (OrgComponent child : childrenToSplit) {
            if (child == null || !sourceDepartment.hasChild(child)
                    || Collections.frequency(childrenToSplit, child) > 1) {
                return false;
            }
        }

        createdDepartment = sourceDepartment.split(newDepartmentName, childrenToSplit);
        if (createdDepartment == null) {
            return false;
        }
        executed = manager.addDepartment(createdDepartment);
        if (!executed) {
            for (OrgComponent child : new ArrayList<>(createdDepartment.getChildren())) {
                createdDepartment.removeChild(child);
                sourceDepartment.addChild(child);
            }
            return false;
        }
        System.out.println("Split: '" + sourceDepartment.getName()
                + "' -> new dept '" + newDepartmentName + "'");
        return true;
    }

    @Override
    public void undo() {
        if (!executed || createdDepartment == null) {
            return;
        }
        for (OrgComponent child : new ArrayList<>(createdDepartment.getChildren())) {
            createdDepartment.removeChild(child);
            sourceDepartment.addChild(child);
        }
        manager.removeDepartment(createdDepartment);
        executed = false;
        System.out.println("Undo Split: '" + newDepartmentName
                + "' merged back into '" + sourceDepartment.getName() + "'");
    }

    Department getCreatedDepartment() {
        return createdDepartment;
    }

    @Override
    public String getDescription() {
        String sourceName = sourceDepartment == null ? "<null department>" : sourceDepartment.getName();
        return "SPLIT: '" + sourceName
                + "' -> new department '" + newDepartmentName + "'";
    }
}

class LayOffEmployeeCommand implements HRCommand {

    private final Team sourceTeam;
    private final Employee employee;
    private boolean executed;

    public LayOffEmployeeCommand(Team sourceTeam, Employee employee) {
        this.sourceTeam = sourceTeam;
        this.employee = employee;
    }

    @Override
    public boolean execute() {
        if (sourceTeam == null || employee == null || !sourceTeam.hasMember(employee)) {
            return false;
        }
        executed = sourceTeam.removeMember(employee);
        if (executed) {
            System.out.println("Laid off: " + employee.getName()
                    + " from Team: " + sourceTeam.getName());
        }
        return executed;
    }

    @Override
    public void undo() {
        if (!executed) {
            return;
        }
        sourceTeam.addMember(employee);
        executed = false;
        System.out.println("Undo LayOff: " + employee.getName()
                + " re-added to Team: " + sourceTeam.getName());
    }

    @Override
    public String getDescription() {
        String employeeInfo = employee == null
                ? "<null employee>"
                : employee.getName() + " [" + employee.getTitle() + "]";
        String teamName = sourceTeam == null ? "<null team>" : sourceTeam.getName();
        return "LAYOFF: " + employeeInfo + " from Team: " + teamName;
    }
}
