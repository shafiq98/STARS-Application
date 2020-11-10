package Managers;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

import Entities.Account;

public class UserValidationMgr
{

    private static byte[] hashBytes;
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static Account compareUserPass(String username, String passwordToBeHash, String accountType) throws NoSuchAlgorithmException
    {
        String salt;
        ArrayList<Account> accountList = DataListMgr.getAccounts();
        String securePassword;

        for (int i = 0; i < accountList.size(); i++)
        {

            //create user object to iterate
            Account user = (Account) accountList.get(i);

            //retrieve salt from text data
            salt = user.getSalt();

            //hash user password input with salt
            securePassword = passwordToBeHash;
//            securePassword = hashing(passwordToBeHash);
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

    public static String generateSalt() {
        String salt;
        // random alphanumeric generator
        salt = UUID.randomUUID().toString();
        return salt;
    }
//
//    public static String hashing(String passwordToBeHash, String salt)
//    {
//        String hashPassword = "";
//
//        try {
//            //generate salt
//            if (salt.equals(""))
//            {
//                salt = generateSalt();
//            }
//
//            StringBuilder sb = new StringBuilder(passwordToBeHash);
//
//            // Prepend salt to passwordToBeHash
//            String passwordWithSaltToBeHash = sb.insert(0, salt).toString();
//
//            // create SHA-512 Message Digest Instance
//            MessageDigest sha512algo = MessageDigest.getInstance("SHA-512");
//
//            // Get hash in decimal format
//            StringBuilder sb2 = new StringBuilder();
//            hashBytes = sha512algo.digest(passwordWithSaltToBeHash.getBytes());
//
//            for (int i = 0; i < hashBytes.length; i++) {
//                // convert it to hexadecimal format
//                sb2.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//
//            hashPassword = sb2.toString();
//
//        } catch (Exception e) {
//            System.out.println("IOException > " + e.getMessage());
//        }
//
//        System.out.println(hashPassword);
//        return hashPassword;
//    }
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
            System.out.println(result);
        } catch (Exception e)
        {
            System.out.println("IOException > " + e.getMessage());
        }
        return result;
    }


}