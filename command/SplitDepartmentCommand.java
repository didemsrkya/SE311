package command;

import model.Department;
import model.OrgComponent;
import singleton.OrgChartManager;
import java.util.List;

/**
 * SplitDepartmentCommand - Command Pattern'in Concrete Command sınıfı.
 * Bir departmandan belirli takımları ayırarak yeni departman oluşturur.
 * Department.split() çağrısı Observer aracılığıyla CorporateHead'i bilgilendirir.
 * Spec'te "split back as if it never merged" zorunluluğunu karşılar.
 */
public class SplitDepartmentCommand implements HRCommand {

    private Department sourceDepartment;
    private String newDepartmentName;
    private List<OrgComponent> teamsToSplit;
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
        System.out.println("✂️  Split: '" + sourceDepartment.getName()
                + "' → new dept '" + newDepartmentName + "'");
    }

    @Override
    public void undo() {
        // Yeni departmanın çocuklarını kaynağa geri taşı
        for (OrgComponent child : createdDepartment.getChildren()) {
            sourceDepartment.addChild(child);
        }
        OrgChartManager.getInstance().removeDepartment(createdDepartment);
        System.out.println("↩️  Undo Split: '" + newDepartmentName
                + "' merged back into '" + sourceDepartment.getName() + "'");
    }

    @Override
    public String getDescription() {
        return "SPLIT: '" + sourceDepartment.getName()
                + "' → new department '" + newDepartmentName + "'";
    }
}
