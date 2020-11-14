package Entities;

import java.io.*;

public class Account implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String username ;
    private final String password ;
    private final String accountType ;

    public Account(String username, String password, String accountType)  {
        this.username = username ;
        this.password = password ;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType() {
        return accountType;
    }


    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account acc = (Account)obj;
            return (getUsername().equals(acc.getUsername()));
        }
        return false;
    }
}
