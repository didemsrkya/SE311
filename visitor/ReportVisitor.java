package visitor;

import model.Department;
import model.Employee;
import model.Team;

/**
 * ReportVisitor - Visitor Pattern'in temel arayüzü.
 * Her rapor tipi bu arayüzü uygular.
 * Composite tree üzerindeki her düğüm tipi için ayrı bir visit metodu vardır.
 * Yeni rapor eklemek için sadece yeni bir Visitor sınıfı oluşturmak yeterlidir,
 * mevcut model sınıflarına dokunulmaz. (Open/Closed Principle)
 */
public interface ReportVisitor {
    void visitEmployee(Employee employee);
    void visitTeam(Team team);
    void visitDepartment(Department department);
    void printReport();
}
