// TEAM MEMBERS:
// [DIDEM SARIKAYA]
// [DUYGU SOGUTDALLI]
// [YAGMUR DAGDEMIR]
// [EFE YOLARTIRAN]

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//  COMPOSITE PATTERN  -  Component / Leaf / Composite
// Component: OrgComponent
// Leaf: Employee
// Composite nodes: Team and Department

interface OrgComponent {
    String getName();
    void accept(ReportVisitor visitor);
    void printDetails(String indent);
}

enum Gender {
    MALE,
    FEMALE,
    UNKNOWN;

    public static Gender fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }

        String normalized = value.trim().toUpperCase();
        if (normalized.equals("MALE") || normalized.equals("M")) {
            return MALE;
        }
        if (normalized.equals("FEMALE") || normalized.equals("F")) {
            return FEMALE;
        }
        if (normalized.equals("UNKNOWN") || normalized.equals("OTHER")) {
            return UNKNOWN;
        }
        throw new IllegalArgumentException("Unsupported gender: " + value);
    }
}

class Employee implements OrgComponent {

    private final String name;
    private final Gender gender;
    private final LocalDate hireDate;
    private double salary;
    private String title;
    private boolean isManager;
    private Team parentTeam;

    public Employee(String name, String gender, LocalDate hireDate, double salary, String title) {
        this(name, Gender.fromString(gender), hireDate, salary, title);
    }

    public Employee(String name, Gender gender, LocalDate hireDate, double salary, String title) {
        this.name = requireText(name, "Employee name");
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
        if (hireDate == null) {
            throw new IllegalArgumentException("Hire date cannot be null.");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Hire date cannot be in the future.");
        }
        this.gender = gender;
        this.hireDate = hireDate;
        setSalary(salary);
        setTitle(title);
        this.isManager = false;
    }

    static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    static double requireValidSalary(double value, String fieldName) {
        if (!Double.isFinite(value) || value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive finite number.");
        }
        return value;
    }

    public long getYearsAtCompany() {
        return Period.between(hireDate, LocalDate.now()).getYears();
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
        System.out.println(indent + "Employee: " + title + ": " + name
                + " | " + gender
                + " | " + getYearsAtCompany() + " years"
                + " | $" + String.format("%.2f", salary));
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = requireValidSalary(salary, "Salary");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = requireText(title, "Title");
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    Team getParentTeam() {
        return parentTeam;
    }

    void setParentTeam(Team parentTeam) {
        this.parentTeam = parentTeam;
    }
}

class Team implements OrgComponent {

    private final String name;
    private final List<Employee> members;
    private Department parentDepartment;

    public Team(String name) {
        this.name = Employee.requireText(name, "Team name");
        this.members = new ArrayList<>();
    }

    public boolean addMember(Employee employee) {
        if (employee == null || members.contains(employee) || employee.getParentTeam() != null) {
            return false;
        }
        members.add(employee);
        employee.setParentTeam(this);
        return true;
    }

    public boolean addMember(OrgComponent component) {
        if (!(component instanceof Employee)) {
            return false;
        }
        return addMember((Employee) component);
    }

    public boolean addEmployee(Employee employee) {
        return addMember(employee);
    }

    public boolean removeMember(Employee employee) {
        if (employee == null || !members.remove(employee)) {
            return false;
        }
        employee.setParentTeam(null);
        return true;
    }

    public boolean removeMember(OrgComponent component) {
        if (!(component instanceof Employee)) {
            return false;
        }
        return removeMember((Employee) component);
    }

    public boolean removeEmployee(Employee employee) {
        return removeMember(employee);
    }

    public boolean hasMember(Employee employee) {
        return members.contains(employee);
    }

    public int getEmployeeCount() {
        return members.size();
    }

    public List<Employee> getMembers() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void accept(ReportVisitor visitor) {
        visitor.visitTeam(this);
        for (Employee member : new ArrayList<>(members)) {
            member.accept(visitor);
        }
        visitor.leaveTeam(this);
    }

    @Override
    public void printDetails(String indent) {
        System.out.println(indent + "Team: " + name);
        for (Employee member : members) {
            member.printDetails(indent + "    ");
        }
    }

    Department getParentDepartment() {
        return parentDepartment;
    }

    void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }
}

class Department implements OrgComponent, OrgSubject {

    private final String name;
    private final List<OrgComponent> children;
    private final List<OrgObserver> observers;
    private Department parentDepartment;

    public Department(String name) {
        this.name = Employee.requireText(name, "Department name");
        this.children = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public boolean addChild(OrgComponent component) {
        if (!canAcceptChild(component) || children.contains(component)) {
            return false;
        }
        if (component instanceof Team) {
            Team team = (Team) component;
            if (team.getParentDepartment() != null) {
                return false;
            }
            team.setParentDepartment(this);
        } else {
            Department department = (Department) component;
            if (department == this || department.getParentDepartment() != null) {
                return false;
            }
            department.setParentDepartment(this);
        }
        children.add(component);
        return true;
    }

    public boolean addTeam(Team team) {
        return addChild(team);
    }

    public boolean addDepartment(Department department) {
        return addChild(department);
    }

    public boolean removeChild(OrgComponent component) {
        if (component == null || !children.remove(component)) {
            return false;
        }
        clearParent(component);
        return true;
    }

    public boolean removeTeam(Team team) {
        return removeChild(team);
    }

    public boolean removeDepartment(Department department) {
        return removeChild(department);
    }

    public boolean hasChild(OrgComponent component) {
        return children.contains(component);
    }

    public List<OrgComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public boolean merge(Department other) {
        if (other == null || other == this || other.children.isEmpty()) {
            return false;
        }
        for (OrgComponent child : other.children) {
            if (!canAcceptChild(child) || children.contains(child)) {
                return false;
            }
        }

        List<OrgComponent> moved = new ArrayList<>(other.children);
        for (OrgComponent child : moved) {
            other.removeChild(child);
            addChild(child);
        }
        notifyObservers("MERGE: Department '" + other.getName()
                + "' merged into '" + this.name + "'");
        return true;
    }

    public Department split(String newDeptName, List<OrgComponent> childrenToSplit) {
        if (childrenToSplit == null || childrenToSplit.isEmpty()) {
            return null;
        }

        List<OrgComponent> uniqueChildren = new ArrayList<>();
        for (OrgComponent child : childrenToSplit) {
            if (child == null || !children.contains(child) || uniqueChildren.contains(child)) {
                return null;
            }
            uniqueChildren.add(child);
        }

        Department newDept = new Department(newDeptName);
        for (OrgObserver observer : observers) {
            newDept.addObserver(observer);
        }
        for (OrgComponent child : uniqueChildren) {
            removeChild(child);
            newDept.addChild(child);
        }

        notifyObservers("SPLIT: Department '" + this.name
                + "' split; new department '" + newDeptName + "' created");
        return newDept;
    }

    @Override
    public void addObserver(OrgObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(OrgObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event) {
        for (OrgObserver observer : new ArrayList<>(observers)) {
            observer.update(event);
        }
    }

    List<OrgObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void accept(ReportVisitor visitor) {
        visitor.visitDepartment(this);
        for (OrgComponent child : new ArrayList<>(children)) {
            child.accept(visitor);
        }
        visitor.leaveDepartment(this);
    }

    @Override
    public void printDetails(String indent) {
        System.out.println(indent + "Department: " + name);
        for (OrgComponent child : children) {
            child.printDetails(indent + "  ");
        }
    }

    Department getParentDepartment() {
        return parentDepartment;
    }

    void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    private boolean canAcceptChild(OrgComponent component) {
        return component instanceof Team || component instanceof Department;
    }

    private void clearParent(OrgComponent component) {
        if (component instanceof Team) {
            ((Team) component).setParentDepartment(null);
        } else if (component instanceof Department) {
            ((Department) component).setParentDepartment(null);
        }
    }
}
