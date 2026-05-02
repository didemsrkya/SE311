package command;

import model.Department;
import model.OrgComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * MergeDepartmentCommand - Command Pattern'in Concrete Command sınıfı.
 * İki departmanı birleştirir. Department.merge() metodunu çağırır,
 * bu da Observer aracılığıyla CorporateHead'e bildirim gönderir.
 * undo() için merge öncesi state saklanır.
 */
public class MergeDepartmentCommand implements HRCommand {

    private Department target;
    private Department source;
    private List<OrgComponent> savedSourceChildren;

    public MergeDepartmentCommand(Department target, Department source) {
        this.target = target;
        this.source = source;
        // undo için kaydet
        this.savedSourceChildren = new ArrayList<>(source.getChildren());
    }

    @Override
    public void execute() {
        target.merge(source);
        System.out.println("🔀 Merged: '" + source.getName()
                + "' into '" + target.getName() + "'");
    }

    @Override
    public void undo() {
        // Taşınan çocukları target'tan geri al
        for (OrgComponent child : savedSourceChildren) {
            target.getChildren().remove(child);
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
