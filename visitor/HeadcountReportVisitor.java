package visitor;

import model.Department;
import model.Employee;
import model.Team;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HeadcountReportVisitor - Visitor Pattern'in Concrete Visitor sınıfı.
 * Her departmandaki çalışan sayısını raporlar.
 * Spec'te "you choose two other kinds of reporting" kapsamında seçilmiştir.
 */
public class HeadcountReportVisitor implements ReportVisitor {

    // Departman adı → çalışan sayısı
    private Map<String, Integer> headcountMap = new LinkedHashMap<>();
    private String currentDepartment = "";

    @Override
    public void visitDepartment(Department department) {
        currentDepartment = department.getName();
        headcountMap.put(currentDepartment, 0);
    }

    @Override
    public void visitTeam(Team team) { }

    @Override
    public void visitEmployee(Employee employee) {
        if (!currentDepartment.isEmpty()) {
            headcountMap.put(currentDepartment,
                    headcountMap.getOrDefault(currentDepartment, 0) + 1);
        }
    }

    @Override
    public void printReport() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║          HEADCOUNT REPORT                ║");
        System.out.println("╠══════════════════════════════════════════╣");
        int total = 0;
        for (Map.Entry<String, Integer> entry : headcountMap.entrySet()) {
            System.out.printf("║  %-25s : %3d employees ║%n",
                    entry.getKey(), entry.getValue());
            total += entry.getValue();
        }
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  %-25s : %3d employees ║%n", "TOTAL", total);
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
