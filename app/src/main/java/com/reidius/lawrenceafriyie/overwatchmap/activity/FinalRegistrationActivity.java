package com.reidius.lawrenceafriyie.overwatchmap.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiClient;
import com.reidius.lawrenceafriyie.overwatchmap.apiclient.ApiInterface;
import com.reidius.lawrenceafriyie.overwatchmap.models.Login;
import com.reidius.lawrenceafriyie.overwatchmap.models.Person;
import com.reidius.lawrenceafriyie.overwatchmap.models.Student;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalRegistrationActivity extends AppCompatActivity {

    final static String TAG = "FinalRegistration";

    private String verificationId;
    private FirebaseAuth mAuth;

    // Textbox
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_registration);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                FirebaseCrash.report(ex);
            }
        });

        try{

            mAuth = FirebaseAuth.getInstance();
            editText = findViewById(R.id.editText);

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();

            final String personID = extras.getString("personID");
            final String name = extras.getString("name");
            final String surname = extras.getString("surname");
            final String email = extras.getString("email");
            final String contactNo = extras.getString("contactNo");
            final String username = extras.getString("username");
            final String password = extras.getString("password");
            final String deviceToken = extras.getString("deviceToken");
            final String personType = extras.getString("personType");

            sendVerificationCode(contactNo);

            findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    try{
                        String code = editText.getText().toString().trim();

                        if (code.isEmpty() || code.length() < 6) {

                            editText.setError("Enter code...");
                            editText.requestFocus();
                            return;
                        }

                        verifyCode(code);

                            Person person = new Person(
                                    Integer.parseInt(personID.toString()), name.toString(), surname.toString(),
                                    email.toString(), contactNo.toString(), deviceToken.toString(), personType.toString());

                            Login login = new Login(Integer.parseInt(personID.toString()), "",
                                password.toString());
                            Student student = new Student(Integer.parseInt(personID.toString()));
                            sendPostRequest(person);
                            sendLogin(login);
                    }
                    catch(Exception ex)
                    {
                        FirebaseCrash.report(new Exception("App Name : My first Android non-fatal error"));
                        finish();
                        startActivity(getIntent());
                    }
                }

                private void sendLogin(Login login) {
                    // creating a retrofit instance
                    try{
                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        final Call<Login> call = apiService.login(login);


                        call.enqueue(new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {
                            }
                            @Override
                            public void onFailure(Call<Login> call, Throwable t) { }
                        });
                    }
                    catch(Throwable t){
                        t.printStackTrace();
                    }
                }

                private void sendPostRequest(Person person) {
                    // creating a retrofit instance
                    try{
                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        final Call<Person> call = apiService.person(person);

                        call.enqueue(new Callback<Person>() {
                            @Override
                            public void onResponse(Call<Person> call, Response<Person> response) {
                            }
                            @Override
                            public void onFailure(Call<Person> call, Throwable t) { }
                        });
                    }
                    catch(Throwable t){
                        t.printStackTrace();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            FirebaseCrash.report(new Exception("App Name : My first Android non-fatal error"));
            finish();
            startActivity(getIntent());
        }
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(FinalRegistrationActivity.this, SigninActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(FinalRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(FinalRegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
