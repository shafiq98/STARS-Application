package Data;

import java.io.*;
import java.text.*;
import java.util.*;

import Managers.IO;
import Entities.StudentCourse;

public class StudentCourseData {

    public static final String SEPARATOR = "|";

    public static ArrayList <StudentCourse> studentCourseList = new ArrayList<StudentCourse>() ;

    /* load data from studentCourses.txt */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static ArrayList<StudentCourse> initStudentCourses() throws IOException, ParseException {
        // read String from text file
        ArrayList<String> stringArray = (ArrayList) IO.read("src/Data/studentCourses.txt");

        if (stringArray.size() == 0){
            return new ArrayList<StudentCourse>();
        }
        for (int i = 0 ; i < stringArray.size() ; i++) {

            String field = stringArray.get(i);

            // get individual 'fields' of the string separated by SEPARATOR
            // pass in the string to the string tokenizer using delimiter ","
            StringTokenizer tokenizer = new StringTokenizer(field, SEPARATOR);

            //first to fifth tokens
            String  userName = tokenizer.nextToken().trim();
            String  courseCode = tokenizer.nextToken().trim();
            int indexNumber = Integer.parseInt(tokenizer.nextToken().trim());
            String registerStatus = tokenizer.nextToken().trim();

            // create Course object from file data
            StudentCourse course = new StudentCourse(userName, courseCode, indexNumber, registerStatus);
            // add to Courses list
            studentCourseList.add(course) ;
        }
        return studentCourseList ;
    }


    public static void saveStudentCourses(ArrayList<StudentCourse> CourseToUpdate) throws IOException {
        ArrayList <String> data = new ArrayList<String>() ;// to store Courses data

        for (int i = 0 ; i < CourseToUpdate.size() ; i++) {
            StudentCourse course = CourseToUpdate.get(i);
            StringBuilder partial =  new StringBuilder() ;
            partial.append(course.getUserName().trim());
            partial.append(SEPARATOR);
            partial.append(course.getCourseCode().trim());
            partial.append(SEPARATOR);
            partial.append(course.getIndexNumber());
            partial.append(SEPARATOR);
            partial.append(course.getRegisterStatus());

            data.add(partial.toString()) ;
        }
        IO.write("src/Data/studentCourses.txt", data);
    }
}