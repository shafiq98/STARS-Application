package Data;

import java.io.*;
import java.text.*;
import java.util.*;

import Managers.CalendarMgr;
import Managers.IO;
import Entities.Student;

public class StudentData {
    public static final String SEPARATOR = "|";

    public static ArrayList<Student> studentList = new ArrayList<Student>();
    /* load students.txt on application startup */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static ArrayList<Student> initStudents() throws IOException, ParseException {
        // read String from text file
        ArrayList<String> stringArray = (ArrayList) IO.read("src/Data/students.txt");

        for (int i = 0; i < stringArray.size(); i++) {
            String st = stringArray.get(i);

            // get individual 'fields' of the string separated by SEPARATOR
            // pass in the string to the string tokenizer using delimiter ","
            StringTokenizer star = new StringTokenizer(st, SEPARATOR);

            String userName = star.nextToken().trim(); // first token
            String firstName = star.nextToken().trim(); // first token
            String lastName = star.nextToken().trim(); // second token
            String matricNum = star.nextToken().trim(); // third token
            char gender = (star.nextToken().trim()).charAt(0); // fourth token
//            String nationality = star.nextToken().trim(); // fifth token
//            int mobileNo = Integer.parseInt((star.nextToken().trim())); //sixth token
            String email = star.nextToken().trim(); // seventh token
            Calendar accessStart = CalendarMgr.stringToCalendar(star.nextToken().trim()); // eight token
            Calendar accessEnd = CalendarMgr.stringToCalendar(star.nextToken().trim()); // nine token
            int notiMode = Integer.parseInt((star.nextToken().trim())); //tenth token

            Student std = new Student(userName, firstName, lastName, matricNum, gender, email, accessStart, accessEnd, notiMode);

            // add to Students list
            studentList.add(std);
        }
        return studentList;
    }

    // an example of saving
    public static void saveStudents(ArrayList<Student> al) throws IOException {
        ArrayList<String> data = new ArrayList<String>();// to store Students data

        for (int i = 0; i < al.size(); i++) {
            Student std = al.get(i);
            StringBuilder partial = new StringBuilder();
            partial.append(std.getUserName().trim());
            partial.append(SEPARATOR);
            partial.append(std.getFirstName().trim());
            partial.append(SEPARATOR);
            partial.append(std.getLastName().trim());
            partial.append(SEPARATOR);
            partial.append(std.getMatricNumber().trim());
            partial.append(SEPARATOR);
            partial.append(std.getGender());
            partial.append(SEPARATOR);
            partial.append(std.getEmail());
            partial.append(SEPARATOR);
            partial.append(CalendarMgr.calendarToString(std.getAccessStart()));
            partial.append(SEPARATOR);
            partial.append(CalendarMgr.calendarToString(std.getAccessEnd()));
            partial.append(SEPARATOR);
            partial.append(std.getNotiMode());

            data.add(partial.toString());
        }
        IO.write("src/Data/students.txt", data);
    }
}