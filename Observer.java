// TEAM MEMBERS:
// [DIDEM SARIKAYA]
// [DUYGU SOGUTDALLI]
// [YAGMUR DAGDEMIR]
// [EFE YOLARTIRAN]

//  OBSERVER PATTERN  -  Subject / Observer / ConcreteObserver
// Subject interface: OrgSubject
// Concrete Subject: Department, implemented in Composite.java
// Observer interface: OrgObserver
// Concrete Observer: CorporateHead
// Events: Department.merge() and Department.split() notify registered observers.

interface OrgObserver {
    void update(String event);
}

interface OrgSubject {
    void addObserver(OrgObserver observer);
    void removeObserver(OrgObserver observer);
    void notifyObservers(String event);
}

class CorporateHead implements OrgObserver {

    private final String name;

    public CorporateHead(String name) {
        this.name = Employee.requireText(name, "Corporate head name");
    }

    @Override
    public void update(String event) {
        System.out.println("[NOTIFICATION -> " + name + "]: " + event);
    }

    public String getName() {
        return name;
    }
}
