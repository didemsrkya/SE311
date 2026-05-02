package observer;

/**
 * OrgSubject - Observer Pattern'in Subject arayüzü.
 * Gözlemlenen nesnelerin (Department) uyguladığı arayüz.
 * Observer ekleme, çıkarma ve bildirim metotlarını tanımlar.
 */
public interface OrgSubject {
    void addObserver(OrgObserver observer);
    void removeObserver(OrgObserver observer);
    void notifyObservers(String event);
}
