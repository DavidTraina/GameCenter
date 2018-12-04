package traina.david.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import traina.david.gamecenter.GlobalManager;
import traina.david.slidingtilegame.R;

/**
 * The initial activity after user run the app
 */
public class Global extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global);
        GlobalManager.loadGlobalCenter(getApplicationContext());
        GlobalManager.saveAll(getApplicationContext());
        addSignInButtonListener();
        addSignUPButtonListener();
    }

    /**
     * Switch to the SignIn view to sign in.
     */
    private void switchToSignIn() {
        Intent tmp = new Intent(this, SignIn.class);
        startActivity(tmp);
    }

    /**
     * Switch to the SignUp view to sign up.
     */
    private void switchToSignUp() {
        Intent tmp = new Intent(this, SignUp.class);
        startActivity(tmp);
    }

    /**
     * Activate the signIn button.
     */
    private void addSignInButtonListener() {
        Button startButton = findViewById(R.id.signIn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignIn();
            }
        });
    }


    /**
     * Activate the signUp button.
     */
    private void addSignUPButtonListener() {
        Button startButton = findViewById(R.id.signUp);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignUp();
            }
        });
    }

}