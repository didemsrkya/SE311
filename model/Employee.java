package model;

import visitor.ReportVisitor;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Employee - Composite Pattern'in Leaf (yaprak) düğümü.
 * Alt eleman barındırmaz. Bir çalışanın tüm bilgilerini tutar.
 * Factory Pattern aracılığıyla oluşturulur.
 */
public class Employee implements OrgComponent {

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

    // Kaç yıldır şirkette olduğunu döner
    public long getYearsAtCompany() {
        return ChronoUnit.YEARS.between(hireDate, LocalDate.now());
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
