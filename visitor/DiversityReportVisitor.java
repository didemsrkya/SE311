package visitor;

import model.Department;
import model.Employee;
import model.Team;

/**
 * DiversityReportVisitor - Visitor Pattern'in Concrete Visitor sınıfı.
 * Organizasyon genelindeki erkek/kadın çalışan oranını hesaplar.
 * accept() çağrısıyla Composite tree'yi gezerek tüm Employee'leri toplar.
 */
public class DiversityReportVisitor implements ReportVisitor {

    private int maleCount = 0;
    private int femaleCount = 0;

    @Override
    public void visitEmployee(Employee employee) {
        if (employee.getGender().equalsIgnoreCase("Male")) {
            maleCount++;
        } else {
            femaleCount++;
        }
    }

    @Override
    public void visitTeam(Team team) {
        // Team düğümünde özel işlem yok; Employee'ler Department.accept()'te ziyaret edilir
    }

    @Override
    public void visitDepartment(Department department) {
        // Department düğümünde özel işlem yok
    }

    @Override
    public void printReport() {
        int total = maleCount + femaleCount;
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║       DIVERSITY REPORT           ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║ Total Employees : " + total);
        System.out.println("║ Male            : " + maleCount
                + (total > 0 ? " (" + String.format("%.1f", (maleCount * 100.0 / total)) + "%)" : ""));
        System.out.println("║ Female          : " + femaleCount
                + (total > 0 ? " (" + String.format("%.1f", (femaleCount * 100.0 / total)) + "%)" : ""));
        System.out.println("╚══════════════════════════════════╝");
    }
}
