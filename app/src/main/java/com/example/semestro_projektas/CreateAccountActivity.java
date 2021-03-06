package com.example.semestro_projektas;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateAccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private EditText Name;
    private EditText Password;
    private String Type = "";
    private Button Create;
    private FirebaseAuth mAuth;
    private Spinner spinner;
    private ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String[] paths = {"Pasirinkti", "Cecho vadovas", "Administratorius", "Inžinierius"};


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        getSupportActionBar().setTitle("Vartotojo kūrimas");
        Name = (EditText) findViewById(R.id.edusername);
        Password = (EditText) findViewById(R.id.atpass);
        mAuth = FirebaseAuth.getInstance();
        spinner = findViewById(R.id.spinner1);
        Create = (Button) findViewById(R.id.btnCreate);
        mProgress = new ProgressDialog(this);
        spinner = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(CreateAccountActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Create.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                startRegister();
            }
        });
        getSupportActionBar().setTitle("Vartotojo kūrimas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                Type = "";
                break;
            case 1:
                Type = "1";
                break;
            case 2:
                Type = "2";
                break;
            case 3:
                Type = "3";
                break;
        }
    }

    private void startRegister() {
        final String name = Type;
        final String email = Name.getText().toString().trim();
        final String password = Password.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgress.setMessage("Vartotojas kuriamas");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.dismiss();
                            if (task.isSuccessful()) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                                DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                                Toast.makeText(CreateAccountActivity.this, " Vartotojas "+email+" sukurtas ", Toast.LENGTH_SHORT).show();
                                currentUserDB.child("Tipas").setValue(name);
                                currentUserDB.child("Email").setValue(email);
                                currentUserDB.child("Slaptažodis").setValue(password);
                            } else
                                Toast.makeText(CreateAccountActivity.this, "error registering user", Toast.LENGTH_SHORT).show();

                        }
                    });
        }else
            Toast.makeText(CreateAccountActivity.this, "Įveskite visus duomenis", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}



