# SE311 Project

## Human Resources Application
### Project #2
### DEADLINE: 22 May 2026 18:00 PM (No late submissions)

In this project you will be modeling a Human Resources Application which will be used by the Human Resources department which manages the corporate organizational chart. You will use the design patterns covered in class. This system manages the corporate hierarchy (Departments, Teams, and Employees) to generate several reports and reassign staff.

Periodically structural changes happen in the organization. For example,
- a. An employee is promoted to Manager, or
- b. Departments are merged or
- c. An employee is laid off or
- d. A new employee has been hired.

When one such request is given to the HR department, the organizational chart is updated. In rare occasions a previously merged department splits back again into two as if it never merged. This operation must be validated and logged for audit purposes.

When departments are merged or split the corporate head gets a notification.

The corporate head every week asks for reports such as
- a. A Diversity Report (Male and Female Employee Ratio)
- b. A Seniority Report (workers who are with the company more than 20 years)
- c. And other reports (you choose two other kinds of reporting)

**DO NOT CHANGE CODE AFTER YOU SUBMITTED YOUR PROJECT. WHAT YOU WILL BE PRESENTING MUST BE SAME AS WHAT YOU HAVE SUBMITTED. NO EXCEPTION.**

---

## Notes:
- Use at least 5 design patterns to solve this problem. Use only the ones that are listed in the course syllabus.
- **You will not write a real-world application.** You will simulate/model various functions of it.
- We will discuss some of the patterns in the upcoming weeks. So either you can wait for the lecture, or you can proactively study the pattern and use it if it applies to your problem.
- The whole design must be a coherent one. Do not just simply copy and paste things from your lecture examples. Adapt the examples. Use class examples as an inspiration or skeleton.
- **Patterns used must be connected to each other. They must not be isolated from each other.**
- If you encounter any vagueness in the project description, and you cannot reach me feel free to make any assumption you want, if you state them very clearly. Be careful. Do not make unrealistic, farfetched assumptions. Do not add new functionality to make things unnecessarily complex.
- Please be creative. Choose meaningful names for your methods and classes.
- You will be graded proportional to the elegance of the solution.
- KEEP IT SIMPLE.
- **IF YOU USED AI, DISCLOSE YOUR AI USE. EXPLAIN HOW YOU HAVE USED AI.**

---

## ACADEMIC INTEGRITY

This is a team project. All team projects must be completed by the members of the team without the aid of non-team members. If a team member does not contribute to the implementation and the project report, his/her name should not appear on the work submitted for evaluation. Plagiarism, copying, cheating, outsourcing the project to another person or organization for pay or without pay are considered as actions of academic dishonesty. Failure to maintain academic honesty may result in disciplinary action according to the Izmir University of Economics' disciplinary bylaw for students of institutions of higher education (https://www.ieu.edu.tr/en/bylaws/type/read/id/13).

**AI Use Warning:** The goal of this project is to develop your foundational understanding of Software Architecture and Design Patterns. Therefore, the use of generative AI tools (such as ChatGPT, Claude, or GitHub Copilot) to generate project code, core logic, project architecture or report content is prohibited and will be treated as academic misconduct. We may use specialized tools to audit submissions, and you will be asked to:

1. Explain a specific block of code 1-on-1
2. Change the logic/code on the fly (e.g. "How do you do this", "What happens if...")

You must be able to explain the "why" behind your implementation.

However, you are permitted to use AI tools for **debugging** or as a "**smart documentation**" resource. Specifically, you may use AI to:
1. Generate syntax examples for specific API calls
2. Explain the parameters or return values of unfamiliar library functions.
3. Java language syntax and constructs
4. English issues, terminology, domain knowledge (i.e. OS, finance) etc.

---

## Project Submission Guidelines

You will be providing:
- a) a report that contains
  - i. a detailed account (in writing) of your thought process i.e. The patterns you choose and what made you choose a pattern, how did you map participants etc.
  - ii. Please pay attention to the format and appearance of the report. Use a spell checker, align the margins, use the same font and size, proper headings double spacing, use captions for the figures.
  - iii. UML diagram of the solution for the scenarios. You must use software. The diagrams must be readable. Do not use tools that automatically generate UMLs from the source code.
  - iv. An explanation in English of what each class does. Explanation is also needed for key methods in the classes.
- b) a Java implementation.

1. Members of the group are all expected to know all aspects of the solution intimately. I will schedule presentations with group members and ask questions.
2. I must receive running programs. So, include a screen dump of your program.
3. You can develop your code on any IDE. However, please pay attention to the fact that Java versions may have functionality that may not exist in earlier releases. Please stick to the least common denominator.
4. **I will be paying attention to good programming style, i.e., indentation, comments, meaningful variable names etc.**
5. Submit a ZIP or JAR file.
6. Format of the submitted file: `<lastname1-lastname2-lastname3>PROJECT<number>.zip/.jar` (Example: tekke-buruk-turan-PROJECT1.zip)
7. Do not send project meta files. Your zip/jar file must contain only java files. Please do not send the jdk/jre or IDE related jar files in the zip. ONLY YOUR source files. No such file like Project2(1).java, PROJETC1.txt, PROJECT.java, main.java, jre.jar etc.
8. Please limit the number of Java files. The number of Java files should not exceed the number of patterns used.
9. Write the names and last names of the group members on the first page of the source code listing in comments. Example:
```
// FATIH TEKKE
// OKAN BURUK
// ARDA TURAN
// Player Performance Monitoring System
```
10. One submission for each group suffices.
11. **IMPORTANT: Submit only once. Do not send two versions of the Project.**

