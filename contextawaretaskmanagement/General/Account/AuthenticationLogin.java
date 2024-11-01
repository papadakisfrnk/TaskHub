package com.hub.contextawaretaskmanagement.General.Account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.R;

import java.util.List;
import java.util.concurrent.Executor;


public class AuthenticationLogin extends AppCompatActivity {
    private UserDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        dbHelper = new UserDatabase(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("Authentication");
        }

        TextView msgtex = findViewById(R.id.msgtext);
        final Button loginbutton = findViewById(R.id.login);
        Button login_user = findViewById(R.id.loginuser);

        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (dbHelper.getAllUsernames().isEmpty()) {
                    intent = new Intent(AuthenticationLogin.this, Register.class);
                    startActivity(intent);
                } else {

                        intent = new Intent(AuthenticationLogin.this, Login.class);
                        startActivity(intent);

                }
            }
        });

        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG);

        switch (canAuthenticate) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("Select the Fingerprint icon to log in using the fingerprint sensor.");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("This device doesn't have a Fingerprint sensor");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("The biometric sensor is currently unavailable");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("No biometric credentials are currently enrolled. Please check your security settings.");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            default:
                msgtex.setText("Biometric authentication is not available.");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        final BiometricPrompt biometricPrompt = new BiometricPrompt(AuthenticationLogin.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication Success", Toast.LENGTH_SHORT).show();

                // Fetch the first created user from the database
                List<String> usernames = dbHelper.getAllUsernames();
                String firstUser = usernames.isEmpty() ? "Unknown User" : usernames.get(0); // Get first username or "Unknown User"
                Intent inte = new Intent(AuthenticationLogin.this, MainActivity.class);
                inte.putExtra("username", firstUser);
                startActivity(inte);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Context-Aware Task Management")
                .setDescription("Use your fingerprint to Authenticate")
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build();

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.getAllUsernames().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You have to register first in order to use the fingerprint login.", Toast.LENGTH_SHORT).show();
                }else {
                    biometricPrompt.authenticate(promptInfo);
                }
            }
        });
    }

}