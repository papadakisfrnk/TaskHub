package com.hub.contextawaretaskmanagement.General.Account;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.R;

public class Login extends AppCompatActivity {
    EditText name, pass;
    TextView register1, register2;
    private UserDatabase dbHelper;
    boolean state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("TaskHub");
        }
        name = findViewById(R.id.user1);
        pass = findViewById(R.id.pass1);
        register1 = findViewById(R.id.register1);
        register2 = findViewById(R.id.register2);


        dbHelper = new UserDatabase(this);
    }

    public void register(View view) {

        Intent register = new Intent(this, Register.class);
        startActivity(register);
    }

    public void login(View view) {
        String name1 = name.getText().toString();
        String pass1 = pass.getText().toString();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, null);
        TextView text = layout.findViewById(R.id.toastText);
        if (name1.isEmpty() || pass1.isEmpty()) {

            text.setText("Fields cannot be blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 300);
            toast.show();
        } else {
            boolean authenticated = dbHelper.authenticateUser(name1, pass1);
            if (authenticated) {
                Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                Intent user = new Intent(Login.this, MainActivity.class);
                user.putExtra("username", name1);
                user.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(user);
            } else {
                text.setText("Credentials are incorrect");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 300);
                toast.show();
            }
        }
    }

    public void toggle(View v) {
        if (!state) {
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pass.setSelection(pass.getText().length());
        } else {
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pass.setSelection(pass.getText().length());
        }
        state = !state;
    }
}
