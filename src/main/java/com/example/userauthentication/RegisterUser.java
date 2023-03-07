package com.example.userauthentication;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView register;
    private EditText editTextFullName, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullname);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        String email = editTextEmail.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
            return;
        }

        if(fullName.matches(".*(1|2|3|4|5|6|7|8|9|0).*")){
            editTextFullName.setError("No Digits!");
            editTextFullName.requestFocus();
            return;
        }

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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener((new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterUser.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else{
                                                Toast.makeText(RegisterUser.this, "Registration Failed, Try Again!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }));
                        }else{
                            Toast.makeText(RegisterUser.this, "Registration Failed, Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

