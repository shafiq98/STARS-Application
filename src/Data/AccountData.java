package Data;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import Managers.IO;
import Entities.Account;

public class AccountData
{
    public static final String SEPARATOR = "|";

    public static ArrayList<Account> accountList = new ArrayList<Account>();

    /* load accounts.txt before program starts */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static ArrayList<Account> initAccounts() throws IOException {
        // read String from text file
        ArrayList<String> stringArray = (ArrayList) IO.read("src/Data/accounts.txt");


        for (int i = 0; i < stringArray.size(); i++) {
            String st = stringArray.get(i);
            // read in file line by line, and then split up the data by '|' character
            StringTokenizer star = new StringTokenizer(st, SEPARATOR);

            String username = star.nextToken().trim(); // first item
            String password = star.nextToken().trim(); // second item
            String accountType = star.nextToken().trim(); // third item
            // create Account object from file data
            Account acc = new Account(username, password, accountType);
            // add to Account list
            accountList.add(acc);
        }
        return accountList;
    }

    // save new entry
    public static void saveAccounts(ArrayList<Account> al) throws IOException {
        ArrayList <String> data = new ArrayList<String>() ;

        for (int i = 0; i < al.size(); i++) {
            Account acc = al.get(i);
            StringBuilder partial = new StringBuilder();
            partial.append(acc.getUsername().trim());
            partial.append(SEPARATOR);
            partial.append(acc.getPassword().trim());
            partial.append(SEPARATOR);
            partial.append(acc.getAccountType().trim());

            data.add(partial.toString());
        }
        IO.write("src/Data/accounts.txt", data);
    }
}
