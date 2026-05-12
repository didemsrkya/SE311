package command;

import model.Department;
import model.OrgComponent;
import java.util.ArrayList;
import java.util.List;

// concrete command class, it merges two departments
public class MergeDepartmentCommand implements HRCommand {

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
        target.merge(source); //calls the target departments merge() method 
        System.out.println(" Merged: '" + source.getName()
                + "' into '" + target.getName() + "'");
    }

    @Override
    public void undo() {
        // Taşınan çocukları target'tan geri al
        for (OrgComponent child : savedSourceChildren) {
            target.getChildren().remove(child); /*getChildren (composite pattern method) (gets the moved source's children 
                                                 and puts them back to the source (addChildren()))*/ 
            source.addChild(child);
        }
        System.out.println("↩️  Undo Merge: '" + source.getName()
                + "' restored from '" + target.getName() + "'");
    }

    @Override
    public String getDescription() {
        return "MERGE: Department '" + source.getName()
                + "' → '" + target.getName() + "'";
    }
}
