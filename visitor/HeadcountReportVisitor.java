package visitor;

import model.Department;
import model.Employee;
import model.Team;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

// It is concrete visitor class
public class HeadcountReportVisitor implements ReportVisitor {

    private Map<String, Integer> headcountMap = new LinkedHashMap<>(); //We hold department name and employee number in the department in this list
    private Map<String, List<String>> teamDetails = new LinkedHashMap<>(); // We hold department name and teams information ( team name and  number of employees in teams) in the department in this list
    private String currentDepartment = "";

    @Override
    public void visitDepartment(Department department) {
        currentDepartment = department.getName();
        headcountMap.put(currentDepartment, 0);
        teamDetails.put(currentDepartment, new ArrayList<>());
    }

    @Override
    public void visitTeam(Team team) {
        //We add team name and number of employee in the team in here
        if (!currentDepartment.isEmpty()) {
            String info = team.getName() + " : " + team.getMembers().size() + " employees";
            teamDetails.get(currentDepartment).add(info);
        }
    }

    @Override
    public void visitEmployee(Employee employee) {
        if (!currentDepartment.isEmpty()) {
            headcountMap.put(currentDepartment,headcountMap.getOrDefault(currentDepartment, 0) + 1);
        }
    }

    @Override
    public void printReport() {
        System.out.println(" HEADCOUNT REPORT");
        int total = 0;
        for (Map.Entry<String, Integer> entry : headcountMap.entrySet()) {
            String deptName = entry.getKey();
            int count = entry.getValue();
            total += count;
            List<String> teams = teamDetails.get(deptName);

            // This is the part where we formatting the output, We used AI in the next 2 lines to format output.
            String teamPart = teams.isEmpty() ? "" : " (" + String.join(" , ", teams) + ")";
            System.out.printf("%-15s : %3d employees%-60s %n",deptName, count, teamPart);
        }
        
        System.out.println("TOTAL           :   "+ total+ " employees");
    }
}