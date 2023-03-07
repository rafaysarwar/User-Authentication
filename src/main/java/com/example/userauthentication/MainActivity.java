package com.example.userauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.userauthentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView signUp, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.login);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextEmail.setOnClickListener(this);

        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPassword.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUp:
                startActivity(new Intent(MainActivity.this, RegisterUser.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("Password length should not be less than 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

