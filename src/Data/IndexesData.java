package Data;

import java.io.*;
import java.text.*;
import java.util.*;

import Managers.IO;
import Entities.Index;

public class IndexesData {
    public static final String SEPARATOR = "|";

    public static ArrayList <Index> indexList = new ArrayList<Index>() ;

    /* load indexes.txt */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static ArrayList<Index> initIndexes() throws IOException, ParseException {
        // read String from text file
        System.out.println("Reading from text file");
        ArrayList<String> stringArray = (ArrayList) IO.read("src/Data/indexes.txt");

        for (int i = 0 ; i < stringArray.size() ; i++) {

            String field = stringArray.get(i);

            // get individual 'fields' of the string separated by SEPARATOR
            // pass in the string to the string tokenizer using delimiter ","
            StringTokenizer tokenizer = new StringTokenizer(field, SEPARATOR);

            //first to fifth tokens
            String  courseCode = tokenizer.nextToken().trim();
            int  indexNumber = Integer.parseInt(tokenizer.nextToken().trim());
            String tutorialGroup = tokenizer.nextToken().trim();
            int vacancies = Integer.parseInt(tokenizer.nextToken().trim());
            int waitingList = Integer.parseInt(tokenizer.nextToken().trim());

            // create Course object from file data
            Index index = new Index(courseCode, indexNumber, tutorialGroup, vacancies, waitingList);
            // add to Courses list
            indexList.add(index) ;
        }
        return indexList ;
    }

    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static void searchVacancy(String CourseCode,int indexNumber)throws IOException
    {
        ArrayList<String> stringArray = (ArrayList) IO.read("src/Data/indexes.txt");
        for (int i = 0 ; i < stringArray.size() ; i++) {

            String field = stringArray.get(i);

            // get individual 'fields' of the string separated by SEPARATOR
            // pass in the string to the string tokenizer using delimiter ","
            StringTokenizer tokenizer = new StringTokenizer(field, SEPARATOR);

            //first to fifth tokens
            String  courseCode = tokenizer.nextToken().trim();
            int  indexNumber1 = Integer.parseInt(tokenizer.nextToken().trim());
            int vacancies = Integer.parseInt(tokenizer.nextToken().trim());

            if(courseCode.equalsIgnoreCase(CourseCode))
            {
                if(indexNumber == indexNumber1)
                    System.out.println("Index Number: "+indexNumber1+" \t Vacancies: "+vacancies);
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static void showIndex(String CourseCode)throws IOException
    {
        ArrayList<String> stringArray = (ArrayList) IO.read("src/Data/indexes.txt");
        int t=0;
        for (int i = 0 ; i < stringArray.size() ; i++) {

            String field = stringArray.get(i);

            // get individual 'fields' of the string separated by SEPARATOR
            // pass in the string to the string tokenizer using delimiter ","
            StringTokenizer tokenizer = new StringTokenizer(field, SEPARATOR);

            //first to fifth tokens
            String  courseCode = tokenizer.nextToken().trim();
            String  indexNumber = tokenizer.nextToken().trim();

            if(courseCode.equalsIgnoreCase(CourseCode))
            {

                System.out.println(t+1+") Index Number: "+indexNumber);
                t++;
            }
        }

    }


    public static void saveIndexes(ArrayList<Index> IndexToUpdate) throws IOException {
        ArrayList <String> data = new ArrayList<String>() ;// to store Courses data

        for (int i = 0 ; i < IndexToUpdate.size() ; i++) {
            Index index = IndexToUpdate.get(i);
            StringBuilder partial =  new StringBuilder() ;
            partial.append(index.getCourseCode().trim().toUpperCase());
            partial.append(SEPARATOR);
            partial.append(index.getIndexNumber());
            partial.append(SEPARATOR);
            partial.append(index.getTutorialGroup());
            partial.append(SEPARATOR);
            partial.append(index.getVacancy());
            partial.append(SEPARATOR);
            partial.append(index.getWaitingList());

            data.add(partial.toString()) ;
        }
        IO.write("src/Data/indexes.txt",data);
    }
}