package UI;

import java.io.*;
import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.*;

import Entities.*;
import Managers.*;

/**
 * The UI displayed to the student as the user.
 * @version 1.0
 * @since 2020-11-11
 */

public class StudentUI {

    private static Scanner sc = new Scanner(System.in);
    private static Student loggedInStudent;


    public static void showStudentOption(Student s){

        loggedInStudent = s;
        int choice;

        StudentWhileLoop:
        while (true){
            System.out.println("***Welcome to Student panel!***");
            System.out.println("Please select an action:");
            System.out.println("(1) Register Course");
            System.out.println("(2) Drop Course");
            System.out.println("(3) Check/Print Courses Registered");
            System.out.println("(4) Check Vacancies Available");
            System.out.println("(5) Change Index Number of Course");
            System.out.println("(6) Swop Index Number with Another Student");
            System.out.println("(7) Select Notification Mode");
            System.out.println("(8) Logout");

            System.out.print("> ");
            try {
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1: // Register Course
                        registerCourseUI();
                        break;
                    case 2: // Drop Course
                        dropCourseUI();
                        break;
                    case 3: // Check/Print Courses Registered
                        PrintMgr.printRegisteredCourses(s);
                        break;
                    case 4: // Check Vacancies Available
                        checkVacancyUI();
                        break;
                    case 5: // Change Index Number of Course
                        changeIndexNumberUI();
                        break;
                    case 6: // Swop Index Number with Another Student
                        swopIndexNumberUI();
                        break;
                    case 7: // Select Notification Mode
                        selectNotiModeUI();
                        break;
                    case 8: // Logout
                        System.out.println("Successfully Logged Out!");
                        System.out.println();
                        break StudentWhileLoop;
                    default:
                        System.out.println("Invalid Input! Please re-enter!");
                }
            } catch (Exception e) {
                System.out.println("Invalid Input! Please re-enter!");
            }
            System.out.println();
        }
    }
    /** Show a UI that prompts Staff of the students'
     * details to add new student
     * @throws ParseException
     * @throws IOException
     */
    private static void registerCourseUI() throws ParseException, IOException{
        ArrayList<StudentCourse> studentCourseList = DataListMgr.getStudentCourses();
        ArrayList<Index> indexList = DataListMgr.getIndexes();

        // Verify that the user can register more courses without overloading
        int auCount = 0;
        final int maxAU = 6;
        for(StudentCourse sc: studentCourseList){
            if(sc.getUserName().equals(loggedInStudent.getUserName())){
                auCount += courseAU(sc.getIndexNumber());
            }
        }
        if (auCount >= maxAU)
        {
            System.out.println("Current AU\tMaximum AU");
            System.out.println(auCount + "\t\t\t" + maxAU);
            return;
        }

        // User enters the index they wish to register for
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

        // To check if the student has already registered to the course's index number
        for(StudentCourse sc: studentCourseList){
            if(sc.getUserName().equals(loggedInStudent.getUserName()) && sc.getIndexNumber() == indexNumber){
                System.out.println();
                System.out.println("You have already registered to the course's index number!");
                return;
            }
        }

        PrintMgr.printIndexInfo(indexNumber);

        System.out.println();
        System.out.print("Confirm to Add Course? (Y/N): ");
        char choice = sc.nextLine().charAt(0);
        if (choice == 'Y' || choice == 'y'){
            StudentCourseMgr.registerCourse(loggedInStudent, indexNumber);
        }
    }

    private static void dropCourseUI() throws ParseException, IOException{
        PrintMgr.printRegisteredCourses(loggedInStudent);

        System.out.println();
        System.out.print("Enter the index number to drop: "); int indexNumber = sc.nextInt();
        sc.nextLine();

        PrintMgr.printIndexInfo(indexNumber);

        System.out.println();
        System.out.print("Confirm to Drop Course? (Y/N): ");
        char choice = sc.nextLine().charAt(0);
        if (choice == 'Y' || choice == 'y'){
            StudentCourseMgr.removeCourse(loggedInStudent, indexNumber);

            //NotificationMgr.sendAlertWaitlist(indexNumber);
        }
    }

    private static void checkVacancyUI() throws IOException, ParseException{
        ArrayList<Index> indexList = DataListMgr.getIndexes();

        int indexNumber = 0;
        while(true){
            try{
                System.out.print("Please enter the index number to check: "); indexNumber = sc.nextInt();
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

        PrintMgr.printIndexInfo(indexNumber);

        for(Index index : indexList){
            if (index.getIndexNumber() == indexNumber){

                System.out.println();
                System.out.print("Vacancy: " + index.getVacancy());
                System.out.print("\t\tWaiting List: " + index.getWaitingList());
                System.out.println();

                return;
            }
        }
    }

    private static void changeIndexNumberUI() throws IOException, ParseException{
        System.out.print("\nEnter Current Index Number: "); int currentIndexNumber = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Index Number: "); int newIndexNumber = sc.nextInt();
        sc.nextLine();

        System.out.println();
        System.out.println("Current Index Information");
        System.out.println("=========================");
        PrintMgr.printIndexInfo(currentIndexNumber);

        System.out.println();
        System.out.println("New Index Information");
        System.out.println("=====================");
        PrintMgr.printIndexInfo(newIndexNumber);

        System.out.println();
        System.out.print("Confirm to Change Index Number? (Y/N): ");
        char choice = sc.nextLine().charAt(0);
        if (choice == 'Y' || choice == 'y'){
            StudentCourseMgr.removeCourse(loggedInStudent, currentIndexNumber);
            StudentCourseMgr.registerCourse(loggedInStudent, newIndexNumber);

            System.out.println("Index Number " + currentIndexNumber + " has been changed to " + newIndexNumber);

            //NotificationMgr.sendAlertWaitlist(currentIndexNumber);
        }
    }

    private static void swopIndexNumberUI() throws IOException, ParseException, NoSuchAlgorithmException
    {
        System.out.print("\nEnter Peer's Username: "); String peerUsername = sc.nextLine();
        System.out.print("Enter Peer's Password: "); String peerPassword = sc.nextLine();

        Account peerAcc = UserValidationMgr.compareUserPass(peerUsername, peerPassword, "Student");
        ArrayList<Student> studList = DataListMgr.getStudents();
        if (!(peerAcc == null)) { // Successfully logged in
            for (Student peer : studList){
                if (peer.getUserName().equals(peerAcc.getUsername())){
                    System.out.print("Enter Your Index Number: "); int yourIndexNumber = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Peer's Index Number: "); int peerIndexNumber = sc.nextInt();
                    sc.nextLine();

                    System.out.println();
                    System.out.println("Student #1 (" + loggedInStudent.getMatricNumber() + ")'s Index Information");
                    System.out.println("================================================");
                    PrintMgr.printIndexInfo(yourIndexNumber);

                    System.out.println();
                    System.out.println("Student #2 (" + peer.getMatricNumber() + ")'s Index Information");
                    System.out.println("================================================");
                    PrintMgr.printIndexInfo(peerIndexNumber);

                    System.out.println();
                    System.out.print("Confirm to Change Index Number? (Y/N): ");
                    char choice = sc.nextLine().charAt(0);
                    if (choice == 'Y' || choice == 'y'){
                        StudentCourseMgr.removeCourse(loggedInStudent, yourIndexNumber);
                        StudentCourseMgr.registerCourse(loggedInStudent, peerIndexNumber);

                        StudentCourseMgr.removeCourse(peer, peerIndexNumber);
                        StudentCourseMgr.registerCourse(peer, yourIndexNumber);

                        System.out.println(loggedInStudent.getMatricNumber() + "-Index Number " + yourIndexNumber + " has been successfully swopped with " + peer.getMatricNumber() + "-Index Number " + peerIndexNumber);
                    }
                }
            }
        }else{
            System.out.println();
            System.out.println("Incorrect peer's username or password!");
        }
    }

    private static void selectNotiModeUI() throws IOException, ParseException{
        System.out.println("Please select your notification mode:");
        System.out.println("=====================================");
        System.out.println("(1) Send SMS");
        System.out.println("(2) Send Email");
        System.out.println("(3) Send both");
        int choice = sc.nextInt();
        sc.nextLine();

        ArrayList<Student> studentList = DataListMgr.getStudents();
        System.out.println("Size: " + studentList.size());
        for(Student s : studentList){
            if (s.getUserName().equals(loggedInStudent.getUserName())){
                // Updating
                studentList.remove(s);
                Student newStud = new Student(s.getUserName(), s.getFirstName(), s.getLastName(),
                        s.getMatricNumber(), s.getGender(), s.getEmail(), s.getAccessStart(), s.getAccessEnd(), choice);
                DataListMgr.writeObject(newStud);

                // necessary to prevent re-looping of updated textfile
                return;
            }
        }
    }

    private static int courseAU(int indexNumber) throws IOException
    {
        // load up all indexes and courses
        ArrayList <Index> indexArrayList = DataListMgr.getIndexes();
        ArrayList <Course> courseArrayList = DataListMgr.getCourses();

        // iterate through index list to find the course we want
        for (Index i : indexArrayList)
        {
            if (i.getIndexNumber() == (indexNumber))
            {
                // find the course code for that index
                String courseCode = i.getCourseCode();
                for (Course c : courseArrayList)
                {
                    // return the AU for that course code
                    if (c.getCourseCode().equals(courseCode))
                    {
//                        System.out.println("Index: " + i.getIndexNumber() + "\tCourse Name: " + c.getCourseName());
//                        System.out.println(c.getAU());
                        return c.getAU();
                    }
                }
            }

        }
        System.out.println("Sorry, either the index or the corresponding course have been deleted from their respective files");
        System.out.println("Contact Admin for support");
        return 0;
    }
}
