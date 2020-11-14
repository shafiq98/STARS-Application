import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// import UI
import UI.AdminUI;
import UI.StudentUI;

// import Data
import Data.AccountData;
import Data.CourseData;
import Data.IndexesData;
import Data.LessonData;
import Data.StudentCourseData;
import Data.StudentData;

// import Managers
import Managers.CalendarMgr;
import Managers.DataListMgr;
import Managers.UserValidationMgr;

// import Account and Student
// all other Entities will be called through other classes and methods
import Entities.Account;
import Entities.Student;

public class main {

    public static void main(String[] args) throws ParseException, IOException, NoSuchAlgorithmException
    {

        StudentData.initStudents();
        CourseData.initCourses();
        IndexesData.initIndexes();
        LessonData.initLessons();
        StudentCourseData.initStudentCourses();
        AccountData.initAccounts();

        do {

            String username;
            String password;
            String accountType = "";
            int choice = 0;
            Account loggedInAcc;

            @SuppressWarnings("resource")
            Scanner sc = new Scanner(System.in);
            Console cnsl = System.console();
            mainLoop:
            while (true) {
                domainLoop:

                while(true){
                    System.out.println("********Select Domain********");
                    System.out.println("(1) Student");
                    System.out.println("(2) Staff");
                    System.out.println("(3) Exit");
                    System.out.print("> ");
                    try {
                        choice = Integer.parseInt(sc.nextLine());
                        switch (choice) {
                            case 1:
                                accountType = "Student";
                                break domainLoop;
                            case 2:
                                accountType = "Staff";
                                break domainLoop;
                            case 3:
                                System.out.println("Exiting program...");
                                System.exit(0);
                            default:
                                System.out.println("Invalid Input! Please re-enter!");
                                System.out.println();
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid Input! Please re-enter!");
                        System.out.println();
                    }
                }

                System.out.print("Username: ");
                username = sc.nextLine();
                System.out.print("Password: ");
                password = sc.nextLine();

                loggedInAcc = UserValidationMgr.compareUserPass(username, password, accountType);
                if (!(loggedInAcc == null)) {
                    break;
                }

                System.out.println();
                System.out.println("Incorrect username or password! Please re-enter!");
                System.out.println();
            }

            System.out.println();

            if (loggedInAcc.getAccountType().equals("Student"))
            {
                // iterate through list of students and get their access time to ensure we can log in
                ArrayList<Student> studentList = DataListMgr.getStudents();
                for(Student loggedInStudent: studentList)
                {
                    if (loggedInStudent.getUserName().equals(loggedInAcc.getUsername()))
                    {

                        // get current time in Calendar Object format
                        Calendar c = Calendar.getInstance();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        LocalDateTime now = LocalDateTime.now();
                        c = CalendarMgr.stringToCalendar(dtf.format(now));

                        // returns +ve if start time is after todays date
                        int compareAccessStart = c.compareTo(loggedInStudent.getAccessStart());
                        // returns +ve if end time is before todays date
                        int compareAccessEnd = loggedInStudent.getAccessEnd().compareTo(c);

                        // if one of the terms is negative, means we're outside our access period
                        if (compareAccessStart < 0 || compareAccessEnd < 0){
                            System.out.println("Unable to login! Your access time is from "
                                    + CalendarMgr.calendarToString(loggedInStudent.getAccessStart()) + " to "
                                    + CalendarMgr.calendarToString(loggedInStudent.getAccessEnd()));
                            System.out.println();
                        }
                        else{
                            System.out.println("Hello, " + loggedInAcc.getUsername() + "!");
                            StudentUI.showStudentOption(loggedInStudent);
                        }
                    }
                }
            }
            else if (loggedInAcc.getAccountType().equals("Staff"))
            {
                AdminUI.showStaffOption();
            }
        } while (true);
    }
}