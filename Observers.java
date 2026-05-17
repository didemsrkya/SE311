// TEAM MEMBERS:
// [DİDEM SARIKAYA]
// [DUYGU SÖĞÜTDALLI]
// [YAĞMUR DAĞDEMİR]
// [EFE YOLARTIRAN]

//  OBSERVER PATTERN  —  Subject / Observer / ConcreteObserver
// Participant mapping:
// Subject interface: OrgSubject
// Concrete Subject: Department, implemented in Models.java
// Observer interface: OrgObserver
// Concrete Observer: CorporateHead
// Events: Department.merge() and Department.split() notify registered observers

interface OrgObserver {
    void update(String event);
}

// ────────────────────────────────────────────────────────

interface OrgSubject {
    void addObserver(OrgObserver observer);
    void removeObserver(OrgObserver observer);
    void notifyObservers(String event);
}

// ────────────────────────────────────────────────────────

class CorporateHead implements OrgObserver {

    private String name;

    public CorporateHead(String name) {
        this.name = name;
    }

    @Override
    public void update(String event) {
        System.out.println("\n📢 [NOTIFICATION → " + name + "]: " + event + "\n");
    }

    public String getName() {
        return name;
    }
}
