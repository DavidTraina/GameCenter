package traina.david.gamecenter;

import java.io.Serializable;

/**
 * Store a user's password and username as an Account.
 */
public class Account implements Serializable {

    private final String username;
    private final String password;

    Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * A getter for the account's username.
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }


    /**
     * Return whether password_attempt matches this account's password.
     * @param password_attempt String
     * @return Boolean
     */
    Boolean signIn(String password_attempt) {
        return (password_attempt.equals(this.password));
    }
}