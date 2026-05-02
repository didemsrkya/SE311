package observer;

/**
 * OrgObserver - Observer Pattern'in Observer arayüzü.
 * Organizasyonel değişikliklerden haberdar olmak isteyen
 * sınıflar bu arayüzü uygular. (Örn: CorporateHead)
 */
public interface OrgObserver {
    void update(String event);
}
