package Managers;

import java.io.*;
import java.text.*;
import java.util.*;

import Data.CourseData;
import Data.IndexesData;
import Data.LessonData;
import Data.StudentCourseData;
import Entities.Course;
import Entities.Index;
import Entities.Lesson;
import Entities.StudentCourse;

public class AdminMgr
{

    private static final Scanner sc = new Scanner(System.in);
    private static boolean update = false;

    public static void removeCourse(String courseCode) throws IOException, ParseException{
        ArrayList<Course> courseList = DataListMgr.getCourses();
        ArrayList<Index> indexList = DataListMgr.getIndexes();
        ArrayList<StudentCourse> studentCourseList = DataListMgr.getStudentCourses();
        ArrayList<Lesson> lessonList = DataListMgr.getLessons();

        for(Course c : courseList){
            if (c.getCourseCode().equals(courseCode)){
                courseList.remove(c);
                CourseData.saveCourses(courseList);
                System.out.println("Course " + c.getCourseName() + " (" + courseCode + ") has been removed!");

                // remove indexes from indexes.txt
                int indexCounter = 0;
                while(indexCounter != indexList.size()){
                    for(Index i : indexList) {
                        if (i.getCourseCode().equals(courseCode)){
                            indexList.remove(i);
                            IndexesData.saveIndexes(indexList);

                            // remove lessons from lessons.txt
                            int lessonCounter = 0;
                            while(lessonCounter != lessonList.size()){
                                for(Lesson l : lessonList){
                                    if(l.getIndexNumber() == i.getIndexNumber()){
                                        lessonList.remove(l);
                                        LessonData.saveLessons(lessonList);

                                        lessonCounter = 0;
                                        break;
                                    }
                                    lessonCounter++;
                                }
                            }
                            indexCounter = 0;
                            break;
                        }
                        indexCounter++;
                    }
                }

                //remove courses from studentCourses.txt
                int studCourseCounter = 0;
                while(studCourseCounter != studentCourseList.size()){
                    for(StudentCourse sc : studentCourseList){
                        if(sc.getCourseCode().equals(courseCode)){
                            studentCourseList.remove(sc);
                            StudentCourseData.saveStudentCourses(studentCourseList);

                            studCourseCounter = 0;
                            break;
                        }
                        studCourseCounter++;
                    }
                }

                // necessary to prevent re-looping of updated textfile
                break;
            }
        }
    }

    public static void updateCourseCode(String courseCode) throws IOException, ParseException{
        ArrayList<Course> courseList = DataListMgr.getCourses();
        String newCourseCode = null;

        if(courseList.size() <= 0)
        {
            System.out.println("No match found!");
        }
        else
        {
            for (Course c : courseList)
            {
                if(c.getCourseCode().equals(courseCode)){
                    System.out.println("Course Code\t Course Name");
                    System.out.println("----------------------------------------");
                    System.out.print(c.getCourseCode() + "\t\t ");
                    System.out.println(c.getCourseName());
                    System.out.println();
                    System.out.print("Please enter new course code:"); newCourseCode = sc.next().toUpperCase();

                    update = true;

                    //updating indexes
                    ArrayList<Index> indexList = DataListMgr.getIndexes();
                    for(Index i : indexList) {
                        if (i.getCourseCode().equals(courseCode)){
                            indexList.remove(i);
                            Index newIndex = new Index(newCourseCode, i.getIndexNumber(),
                                    i.getTutorialGroup(), i.getVacancy(), i.getWaitingList());
                            DataListMgr.writeObject(newIndex);
                        }
                    }

                    //updating Course Code in studentCourses.txt
                    ArrayList<StudentCourse> studentCourseList = DataListMgr.getStudentCourses();
                    for(StudentCourse sc : studentCourseList){
                        if(sc.getCourseCode().equals(courseCode)){
                            studentCourseList.remove(sc);
                            StudentCourse newSc = new StudentCourse(sc.getUserName(), newCourseCode, sc.getIndexNumber(), sc.getRegisterStatus());
                            DataListMgr.writeObject(newSc);
                        }
                    }

                    //updating course info data
                    courseList.remove(c);
                    Course newCourse = new Course(newCourseCode, c.getCourseName(),
                            c.getAU(), c.getSchool(),c.getCourseType(),
                            c.getExamDate());
                    DataListMgr.writeObject(newCourse);

                    break;
                }
            }
        }

        if(!update){
            System.out.println("Sorry, the course code is not valid, no match found.\n");
        }

    }
    public static void updateSchool(String courseCode) throws IOException, ParseException
    {
        ArrayList<Course> courseList = DataListMgr.getCourses();
        boolean status = false;

        if(courseList.size() <= 0)
        {
            System.out.println("No match found!");
        }
        else
        {
            for (Course c : courseList)
            {
                if(c.getCourseCode().equals(courseCode)){
                    System.out.println("Course Code\t Course Name");
                    System.out.println("----------------------------------------");
                    System.out.print(c.getCourseCode() + "\t\t ");
                    System.out.println(c.getSchool());
                    System.out.println();
                    System.out.print("Please enter new school name:"); String newSchoolName = sc.next();
                    courseList.remove(c);

                    Course newCourse = new Course(c.getCourseCode(), c.getCourseName(),
                            c.getAU(), newSchoolName, c.getCourseType(),
                            c.getExamDate());
                    DataListMgr.writeObject(newCourse);

                    status = true;
                    break;
                }
            }
        }
        // if update status hasn't changed, raise error message
        if(!status)
        {
            System.out.println("Sorry, the course code is not valid, no match found.\n");
        }
    }
}