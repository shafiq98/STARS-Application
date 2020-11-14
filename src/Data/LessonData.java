package Data;

import java.io.*;
import java.text.*;
import java.util.*;

import Managers.IO;
import Entities.Lesson;

public class LessonData {
    public static final String SEPARATOR = "|";

    public static ArrayList <Lesson> lessonList = new ArrayList<Lesson>() ;

    /* load lessons.txt before program starts */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static ArrayList<Lesson> initLessons() throws IOException, ParseException {
        // read String from text file
        ArrayList<String> stringArray = (ArrayList) IO.read("src/Data/lessons.txt");

        if (stringArray.size() == 0){
            return new ArrayList<Lesson>();
        }

        for (int i = 0 ; i < stringArray.size() ; i++) {
            String field = stringArray.get(i);

            // get individual 'fields' of the string separated by SEPARATOR
            // pass in the string to the string tokenizer using delimiter "," 
            StringTokenizer tokenizer = new StringTokenizer(field, SEPARATOR);

            //first to fifth tokens
            int  indexNumber = Integer.parseInt(tokenizer.nextToken().trim());
            String lessonType = tokenizer.nextToken().trim();
            String lessonDay = tokenizer.nextToken().trim();
            String lessonTime = tokenizer.nextToken().trim();
            String lessonVenue = tokenizer.nextToken().trim();

            // create Lesson object from file data
            Lesson lesson = new Lesson(indexNumber, lessonType, lessonDay, lessonTime, lessonVenue);
            // add to Lesson list 
            lessonList.add(lesson);
        }
        return lessonList;
    }


    public static void saveLessons(ArrayList<Lesson> LessonToUpdate) throws IOException {
        ArrayList <String> data = new ArrayList<String>() ;// to store Courses data

        for (int i = 0 ; i < LessonToUpdate.size() ; i++) {
            Lesson lesson = LessonToUpdate.get(i);
            StringBuilder partial =  new StringBuilder() ;
            partial.append(lesson.getIndexNumber());
            partial.append(SEPARATOR);
            partial.append(lesson.getLessonType());
            partial.append(SEPARATOR);
            partial.append(lesson.getLessonDay());
            partial.append(SEPARATOR);
            partial.append(lesson.getLessonTime());
            partial.append(SEPARATOR);
            partial.append(lesson.getLessonVenue());

            data.add(partial.toString()) ;
        }
        IO.write("src/Data/lessons.txt",data);
    }
}
