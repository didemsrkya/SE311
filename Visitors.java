// TEAM MEMBERS:
// [DİDEM SARIKAYA]
// [DUYGU SÖĞÜTDALLI]
// [YAĞMUR DAĞDEMİR]
// [EFE YOLARTIRAN]
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//  VISITOR PATTERN  —  Visitor / ConcreteVisitors / Elements
// Participant mapping:
// Visitor: ReportVisitor
// Concrete Visitors: DiversityReportVisitor, SeniorityReportVisitor,
// SalaryBandReportVisitor, HeadcountReportVisitor, HireDateReportVisitor
// Element: OrgComponent
// Concrete Element : Employee, Team, Department
// Object Structure: OrgChartManager stores departments and starts traversal
// Client: Main selects which report visitor to run

//This is our visitor interface
interface ReportVisitor {
    void visitEmployee(Employee employee);
    void visitTeam(Team team);
    void visitDepartment(Department department);
    void printReport();
}

// It is concrete visitor class
class DiversityReportVisitor implements ReportVisitor {

    private int maleCount = 0;
    private int femaleCount = 0;
    private String currentDept = ""; //we need this to know which department we are in
    private String currentTeam = ""; //we need this to know which team we are in

    private Map<String, int[]> deptData = new LinkedHashMap<>(); //we keep department name and [male count, female count]
    private Map<String, int[]> teamData = new LinkedHashMap<>(); // we keep team name in the department and [male count, female count]
    private Map<String, List<String>> deptTeams = new LinkedHashMap<>(); //we keep teams of each department here

    @Override
    public void visitDepartment(Department department) {
        currentDept = department.getName(); //we are updating the department we are in
        currentTeam = ""; //we are resetting the team information
        deptData.put(currentDept, new int[]{0, 0}); //new department and an empty array for male and female numbers
        deptTeams.put(currentDept, new ArrayList<>()); //we are adding a new list here to add team names in visitTeam
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team.getName(); //we are updating the team we are in.
        teamData.put(currentDept + "|" + currentTeam, new int[]{0, 0}); //we are adding the team name and a new array for the team male and female count.
        deptTeams.get(currentDept).add(currentTeam); //adding the information of this team is in this department.
    }

    @Override
    public void visitEmployee(Employee employee) {
        if (employee.getGender().equalsIgnoreCase("Male")) { //if employee is male
            maleCount++; //increase the total male count in the company
            deptData.get(currentDept)[0]++; //increase the male count in the department
            if (teamData.containsKey(currentDept + "|" + currentTeam)) 
                teamData.get(currentDept + "|" + currentTeam)[0]++; //increase the male count the team
        } else {
            femaleCount++; //increase the total female count in the company
            deptData.get(currentDept)[1]++; //increase the female count in the department
            if (teamData.containsKey(currentDept + "|" + currentTeam))
                teamData.get(currentDept + "|" + currentTeam)[1]++;  //increase the female count the team
        }
    }

    @Override
    public void printReport() {
        int total = maleCount + femaleCount; //all employees
        System.out.println(" DIVERSITY REPORT");
        System.out.println("------------------------------------------------------------------------");

        //print department and team breakdown
        for (String dept : deptData.keySet()) {
            int deptMaleCount = deptData.get(dept)[0];
            int deptFemaleCount = deptData.get(dept)[1];
            int deptTotalCount = deptMaleCount + deptFemaleCount;
            double deptMalePercent = 0;
            double deptFemalePercent = 0;
           if(deptTotalCount > 0) {
                deptMalePercent = deptMaleCount * 100.0 / deptTotalCount;
                deptFemalePercent = deptFemaleCount * 100.0 / deptTotalCount;
            }
            System.out.printf("%n%-18s Male: %d (%.1f%%)  Female: %d (%.1f%%)%n",
                    dept, deptMaleCount, deptMalePercent, deptFemaleCount, deptFemalePercent);

            //print team breakdown under the department
            for (String teamName : deptTeams.get(dept)) {
                 int[] teamCounts = teamData.get(dept + "|" + teamName);
                int teamTotalCount = teamCounts[0] + teamCounts[1];
                double teamMalePercent = 0;
                double teamFemalePercent = 0;
                if(teamTotalCount > 0) {
                    teamMalePercent = teamCounts[0] * 100.0 / teamTotalCount;
                    teamFemalePercent = teamCounts[1] * 100.0 / teamTotalCount;
                }
                System.out.printf("   %-18s Male: %d (%.1f%%)  Female: %d (%.1f%%)%n",
                         teamName, teamCounts[0], teamMalePercent, teamCounts[1], teamFemalePercent);
            }
        }

        //print total at the bottom
        System.out.println("------------------------------------------------------------------------");
        // We are calculating the percentage of male and female employees.
        double percentageOfMen = 0;
        double percentageOfFemale = 0;
        if(total > 0) {
            percentageOfMen = maleCount * 100.0 / total;
            percentageOfFemale = femaleCount * 100.0 / total;
        }
        System.out.printf("Total Male            : %d (%.1f%%)%n", maleCount, percentageOfMen);
        System.out.printf("Total Female          : %d (%.1f%%)%n", femaleCount, percentageOfFemale);
        System.out.println("------------------------------------------------------------------------");
    }
}

// ────────────────────────────────────────────────────────

// It is concrete visitor class
class SeniorityReportVisitor implements ReportVisitor {
 private static final int SENIORITY_THRESHOLD = 20;
    private String currentDept = "";
    private String currentTeam = "";

    private Map<String, List<Employee>> deptSeniorMap = new LinkedHashMap<>(); //we keep senior employees for each department
    private Map<String, List<Employee>> teamSeniorMap = new LinkedHashMap<>(); //we keep senior employees for each team
    private Map<String, List<String>> deptTeams = new LinkedHashMap<>(); //we keep which teams are in which department

    @Override
    public void visitDepartment(Department department) {
        currentDept = department.getName();
        currentTeam = "";
        deptSeniorMap.put(currentDept, new ArrayList<>());
        deptTeams.put(currentDept, new ArrayList<>());
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team.getName();
        teamSeniorMap.put(currentDept + "|" + currentTeam, new ArrayList<>());
        deptTeams.get(currentDept).add(currentTeam);
    }

    @Override
    public void visitEmployee(Employee employee) {
        //We are checking if the employee worked more than 20 years
        if (employee.getYearsAtCompany() >= SENIORITY_THRESHOLD) {
            deptSeniorMap.get(currentDept).add(employee);
            if (teamSeniorMap.containsKey(currentDept + "|" + currentTeam))
                teamSeniorMap.get(currentDept + "|" + currentTeam).add(employee);
        }
    }

    @Override
    public void printReport() {
        System.out.println(" SENIORITY REPORT");
        System.out.println("------------------------------------------------------------------------");

        int totalSeniorCount = 0;
        for (String dept : deptSeniorMap.keySet()) {
            List<Employee> deptSeniors = deptSeniorMap.get(dept);
            if(deptSeniors.size() == 0) //if there are no seniors in this department skip it
                continue;
            totalSeniorCount = totalSeniorCount + deptSeniors.size();
            System.out.println(dept + ":");
            for (String teamName : deptTeams.get(dept)) {
                List<Employee> teamSeniors = teamSeniorMap.get(dept + "|" + teamName);
                if(teamSeniors.size() == 0) //if there are no seniors in this team skip it
                    continue;
                System.out.println("   " + teamName + ":");
                for (int i = 0; i < teamSeniors.size(); i++) {
                    Employee e = teamSeniors.get(i);
                    System.out.printf("      %-22s  %2d years  %s%n", e.getName(), e.getYearsAtCompany(), e.getTitle());
                }
            }
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Total senior employees : " + totalSeniorCount);
        if(totalSeniorCount == 0)
            System.out.println(" No employees with 20+ years found.");
    }
}

// ────────────────────────────────────────────────────────

// It is concrete visitor class
class SalaryBandReportVisitor implements ReportVisitor {

    private int juniorCount = 0;   // If the salary is less than 50 000
    private int midCount    = 0;   // If the salary is between 50 000 and 100 000
    private int seniorCount = 0;   // If the salary is greater than 100 000
    private double totalSalary = 0;
    private int totalEmployees = 0;
    private String currentDept = "";
    private String currentTeam = "";

    // overall band salary totals for final summary
    private double juniorTotalSalary = 0;
    private double midTotalSalary = 0;
    private double seniorTotalSalary = 0;

    // for department the datas we are keeping dept, [totalSalary, count, juniorTotal, juniorCount, midTotal, midCount, seniorTotal, seniorCount]
    private Map<String, double[]> deptSalaryMap = new LinkedHashMap<>();
    // for team the datas we are keeping in that department dept|team, [totalSalary, count, juniorTotal, juniorCount, midTotal, midCount, seniorTotal, seniorCount]
    private Map<String, double[]> teamSalaryMap = new LinkedHashMap<>();
    private Map<String, List<String>> deptTeams = new LinkedHashMap<>(); //we keep which teams are in which department

    private String formatAverage(double totalSalary, double employeeCount) {
        if (employeeCount == 0) {
            return "N/A";
        }
        return String.format("$%.2f", totalSalary / employeeCount);
    }

    @Override
    public void visitDepartment(Department department) {
        currentDept = department.getName();
        currentTeam = "";
        deptSalaryMap.put(currentDept, new double[]{0, 0, 0, 0, 0, 0, 0, 0});
        deptTeams.put(currentDept, new ArrayList<>());
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team.getName();
        teamSalaryMap.put(currentDept + "|" + currentTeam, new double[]{0, 0, 0, 0, 0, 0, 0, 0});
        deptTeams.get(currentDept).add(currentTeam);
    }

    @Override
    public void visitEmployee(Employee employee) {
        double salary = employee.getSalary();
        totalSalary += salary;
        totalEmployees++;
        //We are deciding the employees position, totalIdx and countIdx is used for the array [totalSalary, count, juniorTotal, juniorCount, midTotal, midCount, seniorTotal, seniorCount]
        int totalIdx, countIdx;
        if (salary < 50000) {
            juniorCount++;
            juniorTotalSalary += salary;
            totalIdx = 2; 
            countIdx = 3;
        } else if (salary <= 100000) {
            midCount++;
            midTotalSalary += salary;
            totalIdx = 4; 
            countIdx = 5;
        } else {
            seniorCount++;
            seniorTotalSalary += salary;
            totalIdx = 6; 
            countIdx = 7;
        }
        deptSalaryMap.get(currentDept)[0] += salary;
        deptSalaryMap.get(currentDept)[1]++;
        deptSalaryMap.get(currentDept)[totalIdx] += salary;
        deptSalaryMap.get(currentDept)[countIdx]++;
        if (teamSalaryMap.containsKey(currentDept + "|" + currentTeam)) {
            teamSalaryMap.get(currentDept + "|" + currentTeam)[0] += salary;
            teamSalaryMap.get(currentDept + "|" + currentTeam)[1]++;
            teamSalaryMap.get(currentDept + "|" + currentTeam)[totalIdx] += salary;
            teamSalaryMap.get(currentDept + "|" + currentTeam)[countIdx]++;
        }
    }

    @Override
    public void printReport() {
        System.out.println(" SALARY BAND REPORT");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Junior          : " + juniorCount + " employees  (salary < 50 000)");
        System.out.println("Mid             : " + midCount + " employees  (50 000 - 100 000)");
        System.out.println("Senior          : " + seniorCount + " employees  (salary > 100 000)");
        System.out.println("------------------------------------------------------------------------");

        //print department and team salary breakdown
        for (String dept : deptSalaryMap.keySet()) {
            double[] d = deptSalaryMap.get(dept);
            String deptAvg = formatAverage(d[0], d[1]);
            String deptJuniorAvg = formatAverage(d[2], d[3]);
            String deptMidAvg = formatAverage(d[4], d[5]);
            String deptSeniorAvg = formatAverage(d[6], d[7]);

            System.out.printf("%n%-18s -> Avg: %s  Junior Avg: %s  Mid Avg: %s  Senior Avg: %s%n%n",
                    dept, deptAvg, deptJuniorAvg, deptMidAvg, deptSeniorAvg);

            //print team breakdown under the department
            for (String teamName : deptTeams.get(dept)) {
                double[] t = teamSalaryMap.get(dept + "|" + teamName);
                String teamAvg = formatAverage(t[0], t[1]);
                String teamJuniorAvg = formatAverage(t[2], t[3]);
                String teamMidAvg = formatAverage(t[4], t[5]);
                String teamSeniorAvg = formatAverage(t[6], t[7]);
                System.out.printf("      %-18s -> Avg: %s  Junior Avg: %s  Mid Avg: %s  Senior Avg: %s%n",
                        teamName, teamAvg, teamJuniorAvg, teamMidAvg, teamSeniorAvg);
            }
        }

        System.out.println("------------------------------------------------------------------------");
        String avgSalary = formatAverage(totalSalary, totalEmployees);
        String overallJuniorAvg = formatAverage(juniorTotalSalary, juniorCount);
        String overallMidAvg = formatAverage(midTotalSalary, midCount);
        String overallSeniorAvg = formatAverage(seniorTotalSalary, seniorCount);
        System.out.printf("Total Employees : %d%n", totalEmployees);
        System.out.printf("Overall Avg     : %s%n", avgSalary);
        System.out.printf("Junior Avg      : %s%n", overallJuniorAvg);
        System.out.printf("Mid Avg         : %s%n", overallMidAvg);
        System.out.printf("Senior Avg      : %s%n", overallSeniorAvg);
        System.out.println("------------------------------------------------------------------------");
    }
}

// It is concrete visitor class
class HeadcountReportVisitor implements ReportVisitor {

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
            headcountMap.put(currentDepartment, headcountMap.getOrDefault(currentDepartment, 0) + 1);
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

            String teamPart = teams.isEmpty() ? "" : " (" + String.join(" , ", teams) + ")";
            System.out.printf("%-15s : %3d employees%-60s %n", deptName, count, teamPart);
        }
        System.out.println("TOTAL           :   " + total + " employees");
    }
}

// ────────────────────────────────────────────────────────

// It is concrete visitor class
class HireDateReportVisitor implements ReportVisitor {

    private String currentDept = "";
    private String currentTeam = "";

    private Map<String, List<String>> deptTeams = new LinkedHashMap<>(); //we keep which teams are in which department

    // year and list of employee names hired that year
    private Map<Integer, List<String>> yearMap = new LinkedHashMap<>();

    // we keep the employees hired in each department by year
    private Map<String, Map<Integer, List<String>>> deptYearMap = new LinkedHashMap<>();

    // we keep the employees hired in each team by year
    private Map<String, Map<Integer, List<String>>> teamYearMap = new LinkedHashMap<>();

    @Override
    public void visitDepartment(Department department) {
        currentDept = department.getName();
        currentTeam = "";
        deptYearMap.put(currentDept, new LinkedHashMap<>());
        deptTeams.put(currentDept, new ArrayList<>());
    }

    @Override
    public void visitTeam(Team team) {
        currentTeam = team.getName();
        teamYearMap.put(currentDept + "|" + currentTeam, new LinkedHashMap<>());
        deptTeams.get(currentDept).add(currentTeam);
    }

    @Override
    public void visitEmployee(Employee employee) {
        int hireYear = employee.getHireDate().getYear();
        String employeeName = employee.getName();

        //add to overall year map
        if (!yearMap.containsKey(hireYear))
            yearMap.put(hireYear, new ArrayList<>());
        yearMap.get(hireYear).add(employeeName);

        //add to department year map
        Map<Integer, List<String>> deptYears = deptYearMap.get(currentDept);
        if (!deptYears.containsKey(hireYear))
            deptYears.put(hireYear, new ArrayList<>());
        deptYears.get(hireYear).add(employeeName);

        //add to team year map
        String teamKey = currentDept + "|" + currentTeam;
        if (teamYearMap.containsKey(teamKey)) {
            Map<Integer, List<String>> teamYears = teamYearMap.get(teamKey);
            if (!teamYears.containsKey(hireYear))
                teamYears.put(hireYear, new ArrayList<>());
            teamYears.get(hireYear).add(employeeName);
        }
    }

    @Override
    public void printReport() {
        System.out.println(" HIRE DATE REPORT");
        System.out.println("------------------------------------------------------------------------");

        //print department and team breakdown by year
        for (String dept : deptYearMap.keySet()) {
            Map<Integer, List<String>> deptYears = deptYearMap.get(dept);
            if (deptYears.isEmpty())
                continue;
            System.out.println(dept + ":");

            for (String teamName : deptTeams.get(dept)) {
                Map<Integer, List<String>> teamYears = teamYearMap.get(dept + "|" + teamName);
                if (teamYears == null || teamYears.isEmpty())
                    continue;
                System.out.println("   " + teamName + ":");
                for (int year : teamYears.keySet()) {
                    List<String> names = teamYears.get(year);
                    for (int i = 0; i < names.size(); i++) {
                        System.out.printf("      %-22s  hired: %d%n", names.get(i), year);
                    }
                }
            }
        }

        //print overall yearly summary at the bottom
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Yearly Summary:");
        for (int year : yearMap.keySet()) {
            int count = yearMap.get(year).size();
            System.out.println("   " + year + "  :  " + count + " employee(s) hired");
        }
        System.out.println("------------------------------------------------------------------------");
    }
}
