package Managers;

import java.io.*;
import java.text.*;
import java.util.*;

import Data.*;
import Entities.Index;
import Entities.Lesson;
import Entities.Student;
import Entities.StudentCourse;

public class StudentCourseMgr
{
    /**
     * Create a new course with the necessary information
     *
     * @throws ParseException
     * @throws IOException
     */
    public static void registerCourse(Student s, int indexNumber) throws IOException, ParseException
    {
        ArrayList<Index> indexList = DataListMgr.getIndexes();
        for (Index i : indexList)
        {
            if (i.getIndexNumber() == indexNumber)
            {
                int vacancy = i.getVacancy();
                int waitingList = i.getWaitingList();
                String registerStatus = "On Waiting List";
                String courseCode = i.getCourseCode();

                // check for conflict
                boolean result = checkConflict(s, indexNumber);
                if (result)
                {
                    return;
                }

                if (i.getVacancy() <= 0)
                {
                    waitingList++;
                }
                else if (i.getVacancy() > 0)
                {
                    vacancy--;
                    registerStatus = "Registered";
                }

                // Adding
                StudentCourse newStudentCourse = new StudentCourse(s.getUserName(), courseCode, indexNumber, registerStatus);
                DataListMgr.writeObject(newStudentCourse);

                // Update new vacancy & waiting list
                indexList.remove(i);
                Index newIndex = new Index(i.getCourseCode(), indexNumber, i.getTutorialGroup(), vacancy, waitingList);
                DataListMgr.writeObject(newIndex);

                System.out.println();
                if (registerStatus.equals("On Waiting List"))
                {
                    System.out.println("Due to lack of vacancy, your Index " + indexNumber + " (" + courseCode + ") will be put into waiting list.");
                } else if (registerStatus.equals("Registered"))
                {
                    System.out.println("Index " + indexNumber + " (" + courseCode + ") has been successfully added!");
                }

                return;
            }
        }
    }

    public static void removeCourse(Student s, int indexNumber) throws IOException, ParseException
    {
        ArrayList<StudentCourse> studentCourseList = DataListMgr.getStudentCourses();
        ArrayList<Index> indexList = DataListMgr.getIndexes();

        for (StudentCourse course : studentCourseList)
        {
            if (course.getIndexNumber() == indexNumber && course.getUserName().equals(s.getUserName()))
            {
                studentCourseList.remove(course);
                StudentCourseData.saveStudentCourses(studentCourseList);

                System.out.println("Index " + course.getIndexNumber() + " (" + course.getCourseCode() + ") has been removed!");

                for (Index i : indexList)
                {
                    int vacancy = i.getVacancy();
                    int waitingList = i.getWaitingList();

                    if (course.getRegisterStatus().equals("Registered"))
                    {
                        vacancy++;
                    } else if (course.getRegisterStatus().equals("On Waiting List"))
                    {
                        waitingList--;
                    }

                    if (i.getIndexNumber() == indexNumber)
                    {
                        // Update new vacancy & waiting list
                        indexList.remove(i);
                        Index newIndex = new Index(i.getCourseCode(), indexNumber, i.getTutorialGroup(), vacancy, waitingList);
                        DataListMgr.writeObject(newIndex);

                        return;
                    }
                }
            }
        }
    }

    private static boolean checkConflict(Student s, int indexNumber) throws IOException, ParseException
    {
        ArrayList<StudentCourse> studentCourseArrayList = DataListMgr.getStudentCourses();
        ArrayList<Lesson> lessonArrayList = DataListMgr.getLessons();

        String newDay;
        String oldDay;

        int newStart, newEnd, oldStart, oldEnd;

        // iterate through all the lessons available
        for (Lesson l : lessonArrayList)
        {
            // find the lesson that matches the index we are registering for
            if (l.getIndexNumber() == indexNumber)
            {
                // return day, start and end time of that lesson
                ArrayList<String> newResult = returnDate(indexNumber);
                newDay = newResult.get(0);
                newStart = Integer.parseInt(newResult.get(1));
                newEnd = Integer.parseInt(newResult.get(2));
                System.out.println();
                // iterate through all student course registrations
                for (StudentCourse sc : studentCourseArrayList)
                {
                    // if the student course row refers to the student who's checking this, then we continue with this check
                    if (sc.getUserName().equals(s.getUserName()))
                    {
                        // return day, start and end time of old index that student has already registered for
                        ArrayList<String> oldResult = returnDate(sc.getIndexNumber());
                        oldDay = oldResult.get(0);
                        oldStart = Integer.parseInt(oldResult.get(1));
                        oldEnd= Integer.parseInt(oldResult.get(2));
                        // if the day is same AND (newStart time < oldEnd time OR newEnd > oldStart)
                        // return true for conflict
                        if (newDay.equals(oldDay))
                        {
//                            System.out.println("Day Clash!");
                            if (newStart >= oldEnd || newEnd <= oldStart)
                            {
                                System.out.println("Sorry, theres a conflict");
                                System.out.println();

                                System.out.println("Previously Registered Course");
                                PrintMgr.printIndexInfo(sc.getIndexNumber());

                                System.out.println("New Course you're trying to register");
                                PrintMgr.printIndexInfo(indexNumber);
                                return true;
                            }

                            if (newStart == oldStart || newEnd == oldEnd)
                            {
                                System.out.println("Sorry, theres a conflict");
                                System.out.println();
                                System.out.println("Previously Registered Course");
                                PrintMgr.printIndexInfo(sc.getIndexNumber());
                                System.out.println();
                                System.out.println("New Course you're trying to register");
                                PrintMgr.printIndexInfo(indexNumber);
                                return true;
                            }
                        }
                    }
                }
                System.out.println();
            }
        }
        return false;
    }

    private static ArrayList<String> returnDate(int indexNumber)
    {
        ArrayList<Lesson> lessonArrayList = DataListMgr.getLessons();
        ArrayList<String> result = new ArrayList<String>();
        for (Lesson l : lessonArrayList)
        {
            String lessonDay, lessonStart, lessonEnd, time;
            if (l.getIndexNumber() == indexNumber)
            {
                lessonDay = l.getLessonDay();
                result.add(lessonDay);
                time = l.getLessonTime();
                StringTokenizer star = new StringTokenizer(time, "-");
                lessonStart = star.nextToken().trim();
                result.add(lessonStart);
                lessonEnd = star.nextToken().trim();
                result.add(lessonEnd);
            }
        }
        return result;
    }

}