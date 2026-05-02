package model;

import visitor.ReportVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Team - Composite Pattern'in ara düğümü.
 * Employee nesnelerini gruplar. Bir Department altında yer alır.
 * accept() metoduyla Visitor tüm üyelerini ziyaret edebilir.
 */
public class Team implements OrgComponent {

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
