// TEAM MEMBERS:
// [DİDEM SARIKAYA]
// [DUYGU SÖĞÜTDALLI]
// [YAĞMUR DAĞDEMİR]
// [EFE YOLARTIRAN]
package newversion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


// ════════════════════════════════════════════════════════
//  COMPOSITE PATTERN  —  Component / Leaf / Composite
// ════════════════════════════════════════════════════════

/**
 * OrgComponent - Composite Pattern'in temel arayüzü.
 * Department, Team ve Employee sınıfları bu arayüzü uygular.
 * Tüm organizasyon elemanları bu tip üzerinden yönetilir.
 */
interface OrgComponent {
    String getName();
    void accept(ReportVisitor visitor); //This is the participant of our visitor pattern. It is the Element.
    void printDetails(String indent);
}

// ────────────────────────────────────────────────────────

/**
 * Employee - Composite Pattern'in Leaf (yaprak) düğümü.
 * Alt eleman barındırmaz. Bir çalışanın tüm bilgilerini tutar.
 * Factory Pattern aracılığıyla oluşturulur.
 */
//It is our the concrete element of our visitor pattern.
class Employee implements OrgComponent {

    private String name;
    private String gender;       // "Male" veya "Female"
    private LocalDate hireDate;
    private double salary;
    private String title;
    private boolean isManager;

    public Employee(String name, String gender, LocalDate hireDate, double salary, String title) {
        this.name = name;
        this.gender = gender;
        this.hireDate = hireDate;
        this.salary = salary;
        this.title = title;
        this.isManager = false;
    }

    public long getYearsAtCompany() {
        int currentYear = LocalDate.now().getYear();
        int hireYear = hireDate.getYear();
        return currentYear - hireYear;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void accept(ReportVisitor visitor) {
        visitor.visitEmployee(this);
    }

    @Override
    public void printDetails(String indent) {
        System.out.println(indent + "👤 " + title + ": " + name
                + " | " + gender
                + " | " + getYearsAtCompany() + " years"
                + " | $" + salary);
    }

    // Getters & Setters
    public String getGender()       { return gender; }
    public LocalDate getHireDate()  { return hireDate; }
    public double getSalary()       { return salary; }
    public void setSalary(double s) { this.salary = s; }
    public String getTitle()        { return title; }
    public void setTitle(String t)  { this.title = t; }
    public boolean isManager()      { return isManager; }
    public void setManager(boolean m) { this.isManager = m; }
}

// ────────────────────────────────────────────────────────

/**
 * Team - Composite Pattern'in ara düğümü.
 * Employee nesnelerini gruplar. Bir Department altında yer alır.
 * accept() metoduyla Visitor tüm üyelerini ziyaret edebilir.
 */
//It is our the concrete element of our visitor pattern.
class Team implements OrgComponent {

    private String name;
    private List<OrgComponent> members;

    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public void addMember(OrgComponent component) {
        members.add(component);
    }

    public void removeMember(OrgComponent component) {
        members.remove(component);
    }

    public List<OrgComponent> getMembers() {
        return members;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void accept(ReportVisitor visitor) {
        visitor.visitTeam(this);
        for (OrgComponent member : members) {
            member.accept(visitor);
        }
    }

    @Override
    public void printDetails(String indent) {
        System.out.println(indent + "👥 Team: " + name);
        for (OrgComponent member : members) {
            member.printDetails(indent + "    ");
        }
    }
}

// ────────────────────────────────────────────────────────

/**
 * Department - Composite Pattern'in kök düğümü.
 * Team nesnelerini gruplar. OrgSubject arayüzünü de uygular,
 * bu sayede merge/split olaylarında Observer'lara bildirim gönderir.
 * Observer Pattern ile doğrudan bağlantılıdır.
 */
//It is our the concrete element of our visitor pattern.
class Department implements OrgComponent, OrgSubject {

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
        System.out.println(indent + " Department: " + name);
        for (OrgComponent child : children) {
            child.printDetails(indent + "  ");
        }
    }
}
