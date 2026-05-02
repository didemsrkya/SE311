package observer;

/**
 * CorporateHead - Observer Pattern'in Concrete Observer sınıfı.
 * Departman merge ve split olaylarında otomatik bildirim alır.
 * update() metodu her bildirimde çağrılır ve olayı loglar.
 */
public class CorporateHead implements OrgObserver {

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
