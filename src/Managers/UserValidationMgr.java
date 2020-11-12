package Managers;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

import Entities.Account;

public class UserValidationMgr
{

    private static byte[] hashBytes;
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static void main(String[] args)
    {
        System.out.println(hashing("stud33"));
    }

    public static Account compareUserPass(String username, String passwordToBeHash, String accountType) throws NoSuchAlgorithmException
    {
        String salt;
        ArrayList<Account> accountList = DataListMgr.getAccounts();
        String securePassword;

        for (int i = 0; i < accountList.size(); i++)
        {

            //create user object to iterate
            Account user = (Account) accountList.get(i);

            //hash user password input with salt
            securePassword = hashing(passwordToBeHash);
            //compare user input hash with hash retrieved from text data
            if (username.toLowerCase().equals(user.getUsername().toLowerCase()) && securePassword.equals(user.getPassword()))
            {
                if (user.getAccountType().equals(accountType))
                {
                    return user;
                }
            }
        }
        return null;
    }

    public static String bytes2String(byte[] data)
    {
        char[] hexChars = new char[data.length * 2];
        for (int i = 0; i < data.length; i++)
        {
            int v = data[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String hashing(String data)
    {
        // hashing method without any salt involved
        // less secure but simpler to apply
        String result = "";
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            hashBytes = digest.digest(data.getBytes());
            result = bytes2String(hashBytes);
        } catch (Exception e)
        {
            System.out.println("IOException > " + e.getMessage());
        }
        return result;
    }


}