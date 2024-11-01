package com.hub.contextawaretaskmanagement.General.Account;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.R;

import java.util.List;

public class Register extends AppCompatActivity {
    private EditText name, pass, email, pass2;
    private UserDatabase dbHelper;
    private List<String> usernames;
    private boolean state = false;
    private ProgressBar passwordStrengthBar;
    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the UserDatabase helper before using it
        dbHelper = new UserDatabase(this);

        // Retrieve all usernames after initializing dbHelper
        usernames = dbHelper.getAllUsernames();
        passwordStrengthBar = findViewById(R.id.passwordStrengthBar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("TaskHub");
        }

        name = findViewById(R.id.user2);
        email = findViewById(R.id.email2);
        pass = findViewById(R.id.pass2);
        pass2 = findViewById(R.id.pass3);

        startPasswordStrengthThread();
    }

    private void startPasswordStrengthThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            code = pass.getText().toString();
                            int strength = calculatePasswordStrength(code);
                            passwordStrengthBar.setProgress(strength);
                        }
                    });

                    try {
                        // Sleep the thread for 500ms to avoid continuous UI updates
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.setDaemon(true);  // Set as daemon thread so it exits with the app
        thread.start();
    }

    public void register(View view) {
        String name1 = name.getText().toString();
        String pass1 = pass.getText().toString();
        String email1 = email.getText().toString();

        if (usernames != null && usernames.contains(name1)) {
            Toast.makeText(Register.this, "The Username is already registered!", Toast.LENGTH_SHORT).show();
        } else {
            if (name1.isEmpty() || pass1.isEmpty() || email1.isEmpty()) {
                Toast.makeText(Register.this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();
            } else if (!pass1.equals(pass2.getText().toString())) {
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Insert user data into SQLite database
                long result = dbHelper.insertUser(name1, pass1, email1);

                if (result != -1) {
                    Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                    Intent user = new Intent(Register.this, MainActivity.class);
                    user.putExtra("username", name1);
                    user.putExtra("email", email1);
                    user.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(user);
                } else {
                    Toast.makeText(Register.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private int calculatePasswordStrength(String password) {
        int strengthPercentage = 0;

        if (password.length() >= 8) {
            strengthPercentage += 25;
        }
        if (password.matches("(?=.*[0-9]).*")) {
            strengthPercentage += 25;
        }
        if (password.matches("(?=.*[a-z]).*")) {
            strengthPercentage += 25;
        }
        if (password.matches("(?=.*[A-Z]).*")) {
            strengthPercentage += 25;
        }
        if (password.matches("(?=.*[!@#\\$%\\^&\\*]).*")) {
            strengthPercentage += 25;
        }

        return Math.min(strengthPercentage, 100);
    }

    public void toggle(View v) {
        if (!state) {
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pass.setSelection(pass.getText().length());
            pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pass2.setSelection(pass2.getText().length());
        } else {
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pass.setSelection(pass.getText().length());
            pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pass2.setSelection(pass2.getText().length());
        }
        state = !state;
    }
}