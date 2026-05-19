// TEAM MEMBERS:
// [DIDEM SARIKAYA]
// [DUYGU SOGUTDALLI]
// [YAGMUR DAGDEMIR]
// [EFE YOLARTIRAN]

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//  VISITOR PATTERN  -  Visitor / ConcreteVisitors / Elements
// Visitors traverse the Department -> Team -> Employee tree and generate reports.

interface ReportVisitor {
    default void startReport() { }
    void visitEmployee(Employee employee);
    void visitTeam(Team team);
    void visitDepartment(Department department);
    default void leaveTeam(Team team) { }
    default void leaveDepartment(Department department) { }
    void printReport();
    default void finishReport() { }
}

class DiversityReportVisitor implements ReportVisitor {

    private final Map<Department, GenderCounts> departmentData = new LinkedHashMap<>();
    private final Map<Team, GenderCounts> teamData = new LinkedHashMap<>();
    private final Map<Department, List<Team>> departmentTeams = new LinkedHashMap<>();
    private GenderCounts totals;
    private Department currentDepartment;
    private Team currentTeam;

    @Override
    public void startReport() {
        departmentData.clear();
        teamData.clear();
        departmentTeams.clear();
        totals = new GenderCounts();
        currentDepartment = null;
        currentTeam = null;
    }

    @Override
    public void visitDepartment(Department department) {
        currentDepartment = department;
        currentTeam = null;
        departmentData.put(department, new GenderCounts());
        departmentTeams.put(department, new ArrayList<Team>());
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team;
        teamData.put(team, new GenderCounts());
        departmentTeams.get(currentDepartment).add(team);
    }

    @Override
    public void visitEmployee(Employee employee) {
        GenderCounts departmentCounts = departmentData.get(currentDepartment);
        GenderCounts teamCounts = teamData.get(currentTeam);
        totals.add(employee.getGender());
        if (departmentCounts != null) {
            departmentCounts.add(employee.getGender());
        }
        if (teamCounts != null) {
            teamCounts.add(employee.getGender());
        }
    }

    @Override
    public void leaveTeam(Team team) {
        currentTeam = null;
    }

    @Override
    public void printReport() {
        System.out.println("DIVERSITY REPORT");
        System.out.println("------------------------------------------------------------------------");
        for (Map.Entry<Department, GenderCounts> entry : departmentData.entrySet()) {
            Department department = entry.getKey();
            GenderCounts counts = entry.getValue();
            printCounts(department.getName(), counts);
            for (Team team : departmentTeams.get(department)) {
                printCounts("   " + team.getName(), teamData.get(team));
            }
        }
        System.out.println("------------------------------------------------------------------------");
        printCounts("Total", totals);
        System.out.println("------------------------------------------------------------------------");
    }

    private void printCounts(String label, GenderCounts counts) {
        int total = counts.total();
        double malePercent = total == 0 ? 0 : counts.male * 100.0 / total;
        double femalePercent = total == 0 ? 0 : counts.female * 100.0 / total;
        double unknownPercent = total == 0 ? 0 : counts.unknown * 100.0 / total;
        System.out.printf("%-22s Male: %d (%.1f%%)  Female: %d (%.1f%%)  Unknown: %d (%.1f%%)%n",
                label, counts.male, malePercent, counts.female, femalePercent,
                counts.unknown, unknownPercent);
    }

    private static class GenderCounts {
        int male;
        int female;
        int unknown;

        void add(Gender gender) {
            if (gender == Gender.MALE) {
                male++;
            } else if (gender == Gender.FEMALE) {
                female++;
            } else {
                unknown++;
            }
        }

        int total() {
            return male + female + unknown;
        }
    }
}

class SeniorityReportVisitor implements ReportVisitor {
    private static final int SENIORITY_THRESHOLD = 20;

    private final Map<Department, List<Employee>> departmentSeniors = new LinkedHashMap<>();
    private final Map<Team, List<Employee>> teamSeniors = new LinkedHashMap<>();
    private final Map<Department, List<Team>> departmentTeams = new LinkedHashMap<>();
    private Department currentDepartment;
    private Team currentTeam;

    @Override
    public void startReport() {
        departmentSeniors.clear();
        teamSeniors.clear();
        departmentTeams.clear();
        currentDepartment = null;
        currentTeam = null;
    }

    @Override
    public void visitDepartment(Department department) {
        currentDepartment = department;
        currentTeam = null;
        departmentSeniors.put(department, new ArrayList<Employee>());
        departmentTeams.put(department, new ArrayList<Team>());
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team;
        teamSeniors.put(team, new ArrayList<Employee>());
        departmentTeams.get(currentDepartment).add(team);
    }

    @Override
    public void visitEmployee(Employee employee) {
        if (employee.getYearsAtCompany() >= SENIORITY_THRESHOLD) {
            departmentSeniors.get(currentDepartment).add(employee);
            if (currentTeam != null) {
                teamSeniors.get(currentTeam).add(employee);
            }
        }
    }

    @Override
    public void leaveTeam(Team team) {
        currentTeam = null;
    }

    @Override
    public void printReport() {
        int totalSeniorCount = 0;
        System.out.println("SENIORITY REPORT");
        System.out.println("------------------------------------------------------------------------");
        for (Map.Entry<Department, List<Employee>> entry : departmentSeniors.entrySet()) {
            List<Employee> seniors = entry.getValue();
            if (seniors.isEmpty()) {
                continue;
            }
            totalSeniorCount += seniors.size();
            System.out.println(entry.getKey().getName() + ":");
            for (Team team : departmentTeams.get(entry.getKey())) {
                List<Employee> teamList = teamSeniors.get(team);
                if (teamList == null || teamList.isEmpty()) {
                    continue;
                }
                System.out.println("   " + team.getName() + ":");
                for (Employee employee : teamList) {
                    System.out.printf("      %-22s  %2d years  %s%n",
                            employee.getName(), employee.getYearsAtCompany(), employee.getTitle());
                }
            }
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Total senior employees : " + totalSeniorCount);
        if (totalSeniorCount == 0) {
            System.out.println("No employees with 20+ years found.");
        }
    }
}

class SalaryBandReportVisitor implements ReportVisitor {
    static final double JUNIOR_MAX = 50000.0;
    static final double MID_MAX = 100000.0;

    private final Map<Department, SalaryStats> departmentStats = new LinkedHashMap<>();
    private final Map<Team, SalaryStats> teamStats = new LinkedHashMap<>();
    private final Map<Department, List<Team>> departmentTeams = new LinkedHashMap<>();
    private SalaryStats totals;
    private Department currentDepartment;
    private Team currentTeam;

    @Override
    public void startReport() {
        departmentStats.clear();
        teamStats.clear();
        departmentTeams.clear();
        totals = new SalaryStats();
        currentDepartment = null;
        currentTeam = null;
    }

    @Override
    public void visitDepartment(Department department) {
        currentDepartment = department;
        currentTeam = null;
        departmentStats.put(department, new SalaryStats());
        departmentTeams.put(department, new ArrayList<Team>());
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team;
        teamStats.put(team, new SalaryStats());
        departmentTeams.get(currentDepartment).add(team);
    }

    @Override
    public void visitEmployee(Employee employee) {
        totals.add(employee.getSalary());
        departmentStats.get(currentDepartment).add(employee.getSalary());
        if (currentTeam != null) {
            teamStats.get(currentTeam).add(employee.getSalary());
        }
    }

    @Override
    public void leaveTeam(Team team) {
        currentTeam = null;
    }

    @Override
    public void printReport() {
        System.out.println("SALARY BAND REPORT");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Junior          : " + totals.juniorCount + " employees  (salary < 50 000)");
        System.out.println("Mid             : " + totals.midCount + " employees  (50 000 - 100 000)");
        System.out.println("Senior          : " + totals.seniorCount + " employees  (salary > 100 000)");
        System.out.println("------------------------------------------------------------------------");
        for (Map.Entry<Department, SalaryStats> entry : departmentStats.entrySet()) {
            Department department = entry.getKey();
            SalaryStats stats = entry.getValue();
            printStats(department.getName(), stats);
            for (Team team : departmentTeams.get(department)) {
                printStats("   " + team.getName(), teamStats.get(team));
            }
        }
        System.out.println("------------------------------------------------------------------------");
        printStats("Overall", totals);
        System.out.println("------------------------------------------------------------------------");
    }

    private void printStats(String label, SalaryStats stats) {
        System.out.printf("%-22s Avg: %s  Junior Avg: %s  Mid Avg: %s  Senior Avg: %s%n",
                label, average(stats.totalSalary, stats.employeeCount),
                average(stats.juniorTotal, stats.juniorCount),
                average(stats.midTotal, stats.midCount),
                average(stats.seniorTotal, stats.seniorCount));
    }

    private String average(double total, int count) {
        return count == 0 ? "0" : String.format("$%.2f", total / count);
    }

    private static class SalaryStats {
        double totalSalary;
        int employeeCount;
        double juniorTotal;
        int juniorCount;
        double midTotal;
        int midCount;
        double seniorTotal;
        int seniorCount;

        void add(double salary) {
            totalSalary += salary;
            employeeCount++;
            if (salary < JUNIOR_MAX) {
                juniorTotal += salary;
                juniorCount++;
            } else if (salary <= MID_MAX) {
                midTotal += salary;
                midCount++;
            } else {
                seniorTotal += salary;
                seniorCount++;
            }
        }
    }
}

class HeadcountReportVisitor implements ReportVisitor {

    private final Map<Department, Integer> departmentCounts = new LinkedHashMap<>();
    private final Map<Team, Integer> teamCounts = new LinkedHashMap<>();
    private final Map<Department, List<Team>> departmentTeams = new LinkedHashMap<>();
    private Department currentDepartment;
    private Team currentTeam;

    @Override
    public void startReport() {
        departmentCounts.clear();
        teamCounts.clear();
        departmentTeams.clear();
        currentDepartment = null;
        currentTeam = null;
    }

    @Override
    public void visitDepartment(Department department) {
        currentDepartment = department;
        currentTeam = null;
        departmentCounts.put(department, 0);
        departmentTeams.put(department, new ArrayList<Team>());
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team;
        teamCounts.put(team, 0);
        departmentTeams.get(currentDepartment).add(team);
    }

    @Override
    public void visitEmployee(Employee employee) {
        departmentCounts.put(currentDepartment, departmentCounts.get(currentDepartment) + 1);
        if (currentTeam != null) {
            teamCounts.put(currentTeam, teamCounts.get(currentTeam) + 1);
        }
    }

    @Override
    public void leaveTeam(Team team) {
        currentTeam = null;
    }

    @Override
    public void printReport() {
        int total = 0;
        System.out.println("HEADCOUNT REPORT");
        for (Map.Entry<Department, Integer> entry : departmentCounts.entrySet()) {
            Department department = entry.getKey();
            int count = entry.getValue();
            total += count;
            List<String> teams = new ArrayList<String>();
            for (Team team : departmentTeams.get(department)) {
                teams.add(team.getName() + " : " + teamCounts.get(team) + " employees");
            }
            String teamPart = teams.isEmpty() ? "" : " (" + String.join(" , ", teams) + ")";
            System.out.printf("%-18s : %3d employees%s%n", department.getName(), count, teamPart);
        }
        System.out.println("TOTAL              :   " + total + " employees");
    }
}

class HireDateReportVisitor implements ReportVisitor {

    private final Map<Department, TreeMap<Integer, List<String>>> departmentYears = new LinkedHashMap<>();
    private final Map<Team, TreeMap<Integer, List<String>>> teamYears = new LinkedHashMap<>();
    private final Map<Department, List<Team>> departmentTeams = new LinkedHashMap<>();
    private final TreeMap<Integer, List<String>> yearlySummary = new TreeMap<Integer, List<String>>();
    private Department currentDepartment;
    private Team currentTeam;

    @Override
    public void startReport() {
        departmentYears.clear();
        teamYears.clear();
        departmentTeams.clear();
        yearlySummary.clear();
        currentDepartment = null;
        currentTeam = null;
    }

    @Override
    public void visitDepartment(Department department) {
        currentDepartment = department;
        currentTeam = null;
        departmentYears.put(department, new TreeMap<Integer, List<String>>());
        departmentTeams.put(department, new ArrayList<Team>());
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team;
        teamYears.put(team, new TreeMap<Integer, List<String>>());
        departmentTeams.get(currentDepartment).add(team);
    }

    @Override
    public void visitEmployee(Employee employee) {
        int year = employee.getHireDate().getYear();
        addByYear(yearlySummary, year, employee.getName());
        addByYear(departmentYears.get(currentDepartment), year, employee.getName());
        if (currentTeam != null) {
            addByYear(teamYears.get(currentTeam), year, employee.getName());
        }
    }

    @Override
    public void leaveTeam(Team team) {
        currentTeam = null;
    }

    @Override
    public void printReport() {
        System.out.println("HIRE DATE REPORT");
        System.out.println("------------------------------------------------------------------------");
        for (Map.Entry<Department, TreeMap<Integer, List<String>>> entry : departmentYears.entrySet()) {
            Department department = entry.getKey();
            if (entry.getValue().isEmpty()) {
                continue;
            }
            System.out.println(department.getName() + ":");
            for (Team team : departmentTeams.get(department)) {
                TreeMap<Integer, List<String>> years = teamYears.get(team);
                if (years == null || years.isEmpty()) {
                    continue;
                }
                System.out.println("   " + team.getName() + ":");
                printYearEntries(years, "      ");
            }
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Yearly Summary:");
        for (Map.Entry<Integer, List<String>> entry : yearlySummary.entrySet()) {
            System.out.println("   " + entry.getKey() + "  :  "
                    + entry.getValue().size() + " employee(s) hired");
        }
        System.out.println("------------------------------------------------------------------------");
    }

    private void addByYear(TreeMap<Integer, List<String>> map, int year, String name) {
        if (!map.containsKey(year)) {
            map.put(year, new ArrayList<String>());
        }
        map.get(year).add(name);
    }

    private void printYearEntries(TreeMap<Integer, List<String>> years, String indent) {
        for (Map.Entry<Integer, List<String>> entry : years.entrySet()) {
            for (String name : entry.getValue()) {
                System.out.printf("%s%-22s  hired: %d%n", indent, name, entry.getKey());
            }
        }
    }
}
