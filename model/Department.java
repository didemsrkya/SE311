package model;

import observer.OrgObserver;
import observer.OrgSubject;
import visitor.ReportVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Department - Composite Pattern'in kök düğümü.
 * Team nesnelerini gruplar. OrgSubject arayüzünü de uygular,
 * bu sayede merge/split olaylarında Observer'lara bildirim gönderir.
 * Observer Pattern ile doğrudan bağlantılıdır.
 */
public class Department implements OrgComponent, OrgSubject {

    private String name;
    private List<OrgComponent> children;
    private List<OrgObserver> observers;

    public Department(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    // ── Composite Metotları ──────────────────────────────────────

    public void addChild(OrgComponent component) {
        children.add(component);
    }

    public void removeChild(OrgComponent component) {
        children.remove(component);
    }

    public List<OrgComponent> getChildren() {
        return children;
    }

    /**
     * merge: Başka bir departmanın tüm çocuklarını bu departmana taşır.
     * İşlem sonunda Observer'lara bildirim gönderir.
     */
    public void merge(Department other) {
        for (OrgComponent child : other.getChildren()) {
            this.children.add(child);
        }
        other.getChildren().clear();
        notifyObservers("MERGE: Department '" + other.getName()
                + "' merged into '" + this.name + "'");
    }

    /**
     * split: Bu departmandan belirli takımları ayırarak yeni bir departman oluşturur.
     * İşlem sonunda Observer'lara bildirim gönderir.
     */
    public Department split(String newDeptName, List<OrgComponent> teamsToSplit) {
        Department newDept = new Department(newDeptName);
        for (OrgComponent team : teamsToSplit) {
            newDept.addChild(team);
            this.children.remove(team);
        }
        // Yeni dept aynı observer'ları paylaşır
        for (OrgObserver obs : observers) {
            newDept.addObserver(obs);
        }
        notifyObservers("SPLIT: Department '" + this.name
                + "' split — new department '" + newDeptName + "' created");
        return newDept;
    }

    // ── Observer Metotları ───────────────────────────────────────

    @Override
    public void addObserver(OrgObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(OrgObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event) {
        for (OrgObserver observer : observers) {
            observer.update(event);
        }
    }

    // ── OrgComponent Metotları ───────────────────────────────────

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void accept(ReportVisitor visitor) {
        visitor.visitDepartment(this);
        for (OrgComponent child : children) {
            child.accept(visitor);
        }
    }

    @Override
    public void printDetails(String indent) {
        System.out.println(indent + "🏢 Department: " + name);
        for (OrgComponent child : children) {
            child.printDetails(indent + "  ");
        }
    }
}
