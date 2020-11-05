package Managers;

import java.io.IOException;
import java.util.ArrayList;

import Data.AccountData;
import Data.CourseData;
import Data.IndexesData;
import Data.LessonData;
import Data.StudentCourseData;
import Data.StudentData;
import Entities.Account;
import Entities.Course;
import Entities.Index;
import Entities.Lesson;
import Entities.Student;
import Entities.StudentCourse;

public class DataListMgr {

    public static ArrayList<Student> studentList = StudentData.studentList;
    public static ArrayList<Course> courseList = CourseData.courseList;
    public static ArrayList<Index> indexList = IndexesData.indexList;
    public static ArrayList<Lesson> lessonList = LessonData.lessonList;
    public static ArrayList<StudentCourse> studentCourseList = StudentCourseData.studentCourseList;
    public static ArrayList<Account> accountList = AccountData.accountList;

    // Student
    public static ArrayList<Student> getStudents(){ return studentList; }

    // Course
    public static ArrayList<Course> getCourses(){ return courseList; }

    // Index
    public static ArrayList<Index> getIndexes(){ return indexList; }

    // Lesson
    public static ArrayList<Lesson> getLessons(){ return lessonList; }

    // StudentCourse
    public static ArrayList<StudentCourse> getStudentCourses(){ return studentCourseList; }

    // Account
    public static ArrayList<Account> getAccounts(){ return accountList; }

    // WriteObject
    public static void writeObject(Object newObj) throws IOException{
        if (newObj instanceof Student){
            studentList.add((Student) newObj);
            StudentData.saveStudents(studentList);
        }
        else if (newObj instanceof Course){
            courseList.add((Course) newObj);
            CourseData.saveCourses(courseList);
        }
        else if (newObj instanceof Index){
            indexList.add((Index) newObj);
            IndexesData.saveIndexes(indexList);
        }
        else if (newObj instanceof Lesson){
            lessonList.add((Lesson) newObj);
            LessonData.saveLessons(lessonList);
        }
        else if (newObj instanceof StudentCourse){
            studentCourseList.add((StudentCourse) newObj);
            StudentCourseData.saveStudentCourses(studentCourseList);
        }
        else if (newObj instanceof Account){
            accountList.add((Account) newObj);
            AccountData.saveAccounts(accountList);
        }
    }
}
