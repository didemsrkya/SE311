package model;

import visitor.ReportVisitor;

/**
 * OrgComponent - Composite Pattern'in temel arayüzü.
 * Department, Team ve Employee sınıfları bu arayüzü uygular.
 * Tüm organizasyon elemanları bu tip üzerinden yönetilir.
 */
public interface OrgComponent {
    String getName();
    void accept(ReportVisitor visitor); //This is the participant of our visitor pattern. It is the Element. 
    void printDetails(String indent);
}
