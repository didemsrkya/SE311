// TEAM MEMBERS:
// [DİDEM SARIKAYA]
// [DUYGU SÖĞÜTDALLI]
// [YAĞMUR DAĞDEMİR]
// [EFE YOLARTIRAN]


// ════════════════════════════════════════════════════════
//  OBSERVER PATTERN  —  Subject / Observer / ConcreteObserver
// ════════════════════════════════════════════════════════
// Participant mapping:
// Subject interface: OrgSubject
// Concrete Subject: Department, implemented in Models.java
// Observer interface: OrgObserver
// Concrete Observer: CorporateHead
// Events: Department.merge() and Department.split() notify registered observers

/**
 * OrgObserver - Observer Pattern'in Observer arayüzü.
 * Organizasyonel değişikliklerden haberdar olmak isteyen
 * sınıflar bu arayüzü uygular. (Örn: CorporateHead)
 */
interface OrgObserver {
    void update(String event);
}

// ────────────────────────────────────────────────────────

/**
 * OrgSubject - Observer Pattern'in Subject arayüzü.
 * Gözlemlenen nesnelerin (Department) uyguladığı arayüz.
 * Observer ekleme, çıkarma ve bildirim metotlarını tanımlar.
 */
interface OrgSubject {
    void addObserver(OrgObserver observer);
    void removeObserver(OrgObserver observer);
    void notifyObservers(String event);
}

// ────────────────────────────────────────────────────────

/**
 * CorporateHead - Observer Pattern'in Concrete Observer sınıfı.
 * Departman merge ve split olaylarında otomatik bildirim alır.
 * update() metodu her bildirimde çağrılır ve olayı loglar.
 */
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
