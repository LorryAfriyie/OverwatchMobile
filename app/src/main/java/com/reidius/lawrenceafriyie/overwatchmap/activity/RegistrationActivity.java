package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.services.SharedPrefManager;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

            final EditText personID = (EditText) findViewById(R.id.id);
            personID.requestFocus();
            final EditText name = (EditText) findViewById(R.id.name);
            final EditText surname = (EditText) findViewById(R.id.surname);
            final EditText email = (EditText) findViewById(R.id.email);
            final EditText contactNo = (EditText) findViewById(R.id.contact);
            final EditText username = (EditText) findViewById(R.id.username);
            final EditText password = (EditText) findViewById(R.id.password);
            final EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);
            final String deviceToken = SharedPrefManager.getmInstance(this).getKeyAccessToken();
            final String personType = "Student";



            findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(password.getText().toString().equals("") || password.getText().toString() == null)
                        {
                            password.setError("Enter password");
                            password.requestFocus();
                        }
                        else if(password.getText().toString().length() < 8)
                        {
                            password.setError("Password must be at least 8 characters long");
                            password.requestFocus();
                        }
                        else if(confirmPassword.getText().toString().equals("") || confirmPassword.getText().toString() == null)
                        {
                            confirmPassword.setError("Re-enter password!");
                            confirmPassword.requestFocus();
                        }

                        else if(personID.getText().toString().equals("") || personID.getText().toString() == null){
                            personID.setError("Enter student No!");
                            personID.requestFocus();
                        }
                        else if(name.getText().toString().equals("") || name.getText().toString() == null){
                            name.setError("Enter your name!");
                            name.requestFocus();
                        }
                        else if(surname.getText().toString().equals("") || surname.getText().toString() == null){
                            surname.setError("Enter your surname!");
                            surname.requestFocus();
                        }
                        else if(name.getText().toString().equals("") || name.getText().toString() == null){
                            name.setError("Enter your name!");
                            name.requestFocus();
                        }
                         else if(email.getText().toString().equals("") || email.getText().toString() == null){
                            email.setError("Enter your email!");
                            email.requestFocus();
                        }

                        else if (password.getText().toString().equals(confirmPassword.getText().toString()))
                        {
                            Intent intent = new Intent(RegistrationActivity.this, FinalRegistrationActivity.class);
                            Bundle extras = new Bundle();

                            extras.putString("personID", personID.getText().toString());
                            extras.putString("name", name.getText().toString());
                            extras.putString("surname", surname.getText().toString());
                            extras.putString("email", email.getText().toString());
                            extras.putString("contactNo", contactNo.getText().toString());
                            extras.putString("username", username.getText().toString());
                            extras.putString("password", password.getText().toString());
                            extras.putString("deviceToken", deviceToken.toString());
                            extras.putString("personType", personType.toString());
                            intent.putExtras(extras);
                            startActivity(intent);

                        }
                        else {
                                confirmPassword.setError("Password Doesn't Match!");
                                confirmPassword.requestFocus();
                            }

                        String number = contactNo.getText().toString().trim();
                        if (number.isEmpty() || number.length() < 12) {
                            contactNo.setError("Valid Number Required!");
                            contactNo.requestFocus();
                            return;
                        }
                        else
                            contactNo.setText(number);
                    }
                    catch (Exception e)
                    {

                    }



                }

            });
        }
    }

