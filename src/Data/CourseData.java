package Data;

import java.io.*;
import java.text.*;
import java.util.*;

import Managers.CalendarMgr;
import Managers.IO;
import Entities.Course;

public class CourseData {

    public static final String SEPARATOR = "|";

    public static ArrayList<Course> courseList = new ArrayList<Course>();
    /** Initialise the courses before application starts
     * @param filename
     * @throws IOException
     * @throws ParseException
     */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static ArrayList<Course> initCourses() throws IOException, ParseException {
        // read String from text file
        ArrayList<String> stringArray = (ArrayList) IO.read("src/Data/courses.txt");

        Calendar examDate = Calendar.getInstance();

        for (int i = 0 ; i < stringArray.size() ; i++) {

            String field = (String) stringArray.get(i);

            // get individual 'fields' of the string separated by SEPARATOR
            // pass in the string to the string tokenizer using delimiter "," 
            StringTokenizer tokenizer = new StringTokenizer(field, SEPARATOR);

            //first to fifth tokens
            String  courseCode = tokenizer.nextToken().trim();
            String  courseName = tokenizer.nextToken().trim();
            int AU = Integer.parseInt(tokenizer.nextToken().trim());
            String school = tokenizer.nextToken().trim();
            String courseType = tokenizer.nextToken().trim();
            examDate = CalendarMgr.stringToCalendar(tokenizer.nextToken().trim());

            // create Course object from file data
            Course course = new Course(courseCode, courseName, AU, school,courseType, examDate);
            // add to Courses list 
            courseList.add(course) ;
        }
        return courseList ;
    }



    /** Save the courses that has been added during the session
     * @param CourseToUpdate
     * @throws IOException
     */
    public static void saveCourses(ArrayList<Course> CourseToUpdate) throws IOException {
        ArrayList <String> courseListRename = new ArrayList<String>() ;// to store Courses data

        for (int i = 0 ; i < CourseToUpdate.size() ; i++) {
            Course course = (Course) CourseToUpdate.get(i);
            StringBuilder stringBuild =  new StringBuilder() ;
            stringBuild.append(course.getCourseCode().trim().toUpperCase());
            stringBuild.append(SEPARATOR);
            stringBuild.append(course.getCourseName().trim());
            stringBuild.append(SEPARATOR);
            stringBuild.append(course.getAU());
            stringBuild.append(SEPARATOR);
            stringBuild.append(course.getSchool());
            stringBuild.append(SEPARATOR);
            stringBuild.append(course.getCourseType());
            stringBuild.append(SEPARATOR);
            stringBuild.append(CalendarMgr.calendarToString(course.getExamDate()));

            courseListRename.add(stringBuild.toString()) ;
        }
        IO.write("src/Data/courses.txt",courseListRename);
    }
}

