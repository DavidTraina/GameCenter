package traina.david.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.slidingtilegame.R;

public class SignIn extends AppCompatActivity {

    /**
     * The entered username to be updated from the editText for username
     */
    private String userName = "";

    /**
     * The entered password to be updated from the editText for password
     */
    private String password = "";

    /**
     * the current GlobalCenter used to track all GameCenter data
     */
    private final GlobalCenter globalCenter = GlobalManager.getGlobalCenter();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        addLoginButtonListener();
        addCancelButtonListener();
    }

    /**
     * Activate the cancel button.
     */
    private void addCancelButtonListener() {
        Button CancelButton = findViewById(R.id.cancel_signin);
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    /**
     * Activate the login button.
     */
    private void addLoginButtonListener() {
        Button loginButton = findViewById(R.id.signin_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText User = findViewById(R.id.signin_username);
                EditText Password = findViewById(R.id.signin_password);
                userName = User.getText().toString();
                password = Password.getText().toString();
                if (globalCenter.signIn(userName, password)) {
                    localCenter();
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Switch back to the Global view.
     */
    private void cancel() {
        Intent tmp = new Intent(this, Global.class);
        startActivity(tmp);
    }

    /**
     * Switch to the LocalGameCenterActivity view for the current user that just logs in.
     */
    private void localCenter() {
        Intent tmp = new Intent(this, LocalGameCenterActivity.class);
        startActivity(tmp);
    }
}
