package command;

import model.Department;
import model.OrgComponent;
import singleton.OrgChartManager;
import java.util.List;

//concrete command class, it separates a team from a departmen to create a new department
public class SplitDepartmentCommand implements HRCommand {

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
