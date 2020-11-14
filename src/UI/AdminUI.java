package UI;

import java.text.*;
import java.util.*;
import java.io.*;

import Managers.DataListMgr;
import Managers.PrintMgr;
import Managers.CalendarMgr;
import Entities.Account;
import Entities.Course;
import Entities.Index;
import Managers.UserValidationMgr;
import Entities.Student;

/**
 * The UI displayed to the staff as the admin.
 * @version 1.0
 * @since 2017-03-22
 */

public class AdminUI {

    private static final Scanner sc = new Scanner(System.in);

    /**
     * Display options user can perform on restaurant food menu.
     * User can Add new food, new promotion package or remove 
     * menu item from menu
     * @throws ParseException
     * @throws IOException
     */
    public static void showStaffOption() throws IOException, ParseException{
        int choice;

        StaffWhileLoop:
        while(true){
            System.out.println("***Welcome to the Admin Interface***");
            System.out.println("Please select an action:");
            System.out.println("(1) Edit student access period");
            System.out.println("(2) Add a student");
            System.out.println("(3) Add/Update a course");
            System.out.println("(4) Check available (vacancy) slot for an index number");
            System.out.println("(5) Print student list by index number");
            System.out.println("(6) Print student list by course");
            System.out.println("(7) Logout");

            System.out.print("> ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1: // Edit student access period
                    editAccessTimeUI();
                    break;
                case 2: // Add a student
                    addNewStudentUI();
                    break;
                case 3: // Add/Update a course
                    CourseUI.addUpdateCourseUI();
                    break;
                case 4: // Check available vacancy for an index
                    checkVacancyUI();
                    break;
                case 5: // Print student list by index number
                    printStudentListIndexUI();
                    break;
                case 6: // Print student list by course
                    printStudentListCourseCodeUI();
                    break;
                case 7: // Logout
                    System.out.println("Successfully Logged Out!");
                    System.out.println();
                    break StaffWhileLoop;
                default:
                    System.out.println("Invalid Input! Please re-enter!");
            }
            System.out.println();
        }
    }

    /** Show a UI that prompts Staff of the students'
     * details to add new student
     * @throws ParseException
     */
    private static void addNewStudentUI() throws ParseException, IOException{
        ArrayList<Student> studentList = DataListMgr.getStudents();

        String username = "";
        String matricNumber = "";
        boolean flag;

        do {
            flag = false;
            System.out.print("Enter the student's username: ");
            username = sc.nextLine();
            for (Student s : studentList) {
                if (s.getUserName().equals(username)) {
                    System.out.println("Username already exists! Please re-enter.");
                    flag = true;
                    break;
                }
            }
        } while (flag);

        System.out.print("Enter the student's first name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter the student's last name: ");
        String lastName = sc.nextLine();

        do {
            flag = false;
            System.out.print("Enter the student's matric number: ");
            matricNumber = sc.nextLine();
            for (Student s : studentList) {
                if (s.getMatricNumber().equals(matricNumber)) {
                    System.out.println("Matriculation Number already exists! Please re-enter.");
                    flag = true;
                    break;
                }
            }
        } while (flag);

        System.out.print("Enter the student's gender (M/F): ");
        char gender = sc.next().charAt(0);
        sc.nextLine();
        System.out.print("Enter the student's Email Address: "); String email = sc.nextLine();

        Calendar accessStart = CalendarMgr.getValidDateTime("access start");
        Calendar accessEnd = CalendarMgr.getValidDateTime("access end");

        // Adding New Account (Note: Password = Matric Number)
//        String password = UserValidationMgr.hashing(matricNumber,salt);
        String password = UserValidationMgr.hashing(matricNumber);
        // set new accounts to have their matric number as password
//        String password = matricNumber;
        Account newAccount = new Account(username, password, "Student");
        DataListMgr.writeObject(newAccount);
        System.out.println("New Account successfully made");

        // Adding New Student
        Student newStud = new Student(username, firstName, lastName,
                matricNumber, gender, email, accessStart,
                accessEnd, 3);
        DataListMgr.writeObject(newStud);
        System.out.println("New Student Successfully made");

        PrintMgr.printStudentList();
    }

    /* Show a UI to update the students' access period
     * done by the Staff
     */
    private static void editAccessTimeUI() throws IOException, ParseException{
        ArrayList<Student> studentList = DataListMgr.getStudents();

        System.out.print("\nEnter the Student's Matriculation Number: ");
        String matricNumber = sc.nextLine();

        for(Student s: studentList){
            if (s.getMatricNumber().equals(matricNumber)){
                Calendar newAccessStart = CalendarMgr.getValidDateTime("new access start access time");
                Calendar newAccessEnd = CalendarMgr.getValidDateTime("new access endaccess time");

                // Updating
                studentList.remove(s);
                Student newStud = new Student(s.getUserName(), s.getFirstName(), s.getLastName(),
                        s.getMatricNumber(), s.getGender(), s.getEmail(), newAccessStart, newAccessEnd, s.getNotiMode());
                DataListMgr.writeObject(newStud);

                return;
            }
        }

        System.out.println();
        System.out.println("Matriculation Number is not found!");
    }

    private static void checkVacancyUI() throws IOException, ParseException{
        ArrayList<Index> indexList = DataListMgr.getIndexes();

        System.out.print("\nPlease enter the index number to check: "); int indexNumber = sc.nextInt();
        sc.nextLine();

        for(Index index : indexList){
            if (index.getIndexNumber() == indexNumber){
                System.out.println();
                System.out.println("Index Number\tVacancy\t\tWaiting List");
                System.out.println("----------------------------------------");

                System.out.print(index.getIndexNumber() + "\t\t\t");
                System.out.print(index.getVacancy() + "\t\t\t");
                System.out.print(index.getWaitingList());
                System.out.println();

                return;
            }
        }
    }

    private static void printStudentListIndexUI() throws IOException, ParseException {
        ArrayList<Index> indexList = DataListMgr.getIndexes();

        int indexNumber = 0;
        while(true){
            try{
                System.out.print("Enter the Index Number: "); indexNumber = sc.nextInt();
                sc.nextLine();
                break;
            } catch (Exception e){
                sc.nextLine();
                System.out.println("Invalid input! Index Number must be a number!");
            }
        }

        // To check if the index number input by the user exists in the database or not
        boolean foundIndexNumber = false;
        for(Index i: indexList){
            if(i.getIndexNumber() == indexNumber){
                foundIndexNumber = true;
            }
        }
        if(!foundIndexNumber){
            System.out.println();
            System.out.println("Index Number you entered is not found!");
            return;
        }

        PrintMgr.printStudentList(indexNumber);
    }

    private static void printStudentListCourseCodeUI() throws IOException, ParseException {
        ArrayList<Course> courseList = DataListMgr.getCourses();

        System.out.print("Enter a Course Code: "); String courseCode = sc.nextLine().toUpperCase();

        // To check if the course code input by the user exists in the database or not
        boolean foundCourseCode = false;
        for(Course c: courseList){
            if(c.getCourseCode() == courseCode){
                foundCourseCode = true;
            }
        }
        if(!foundCourseCode){
            System.out.println();
            System.out.println("Course Code you entered is not found!");
            return;
        }

        PrintMgr.printStudentList(courseCode);
    }
}