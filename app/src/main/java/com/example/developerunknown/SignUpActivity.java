package com.example.developerunknown;

import android.app.Activity;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;

    public Button confirmButton;
    private EditText registerEmail;
    private EditText registerUserName;
    private EditText registerPassword;
    private EditText registerFirstName;
    private EditText registerLastName;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference = db.collection("user");
    private CollectionReference unameCollectionReference = db.collection("userName");
    @Override


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        registerUserName = findViewById(R.id.registerUname);
        registerPassword = findViewById(R.id.registerPassword);
        registerEmail = findViewById(R.id.registerEmail);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);


        confirmButton = findViewById(R.id.confirm_signup);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = registerUserName.getText().toString();
                password = registerPassword.getText().toString();
                email = registerEmail.getText().toString();
                firstName=registerFirstName.getText().toString();
                lastName =registerLastName.getText().toString();

                if (userName.trim().length() == 0 ) {
                    Toast.makeText(SignUpActivity.this, "userName can't be empty", Toast.LENGTH_SHORT).show();
                }
                else if (password.trim().length() == 0 ) {
                    Toast.makeText(SignUpActivity.this, "password can't be empty", Toast.LENGTH_SHORT).show();
                }
                else if (email.trim().length()==0){
                    Toast.makeText(SignUpActivity.this, "email can't be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    DocumentReference docIdRef = userCollectionReference.document(userName);
                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("check userName", "userName exists!");
                                } else {
                                    Log.d("check userName", "userName available");
                                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()){
                                                Toast.makeText(SignUpActivity.this,"There is a error signing up",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                String user_Id = auth.getCurrentUser().getUid();
                                                HashMap<String, String> uidData = new HashMap<>();
                                                uidData.put("uid", user_Id);
                                                unameCollectionReference
                                                        .document(userName)
                                                        .set(uidData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("create user", "user has been added successfully!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("create user", "userName is not be added!" + e.toString());
                                                            }
                                                        });
                                                HashMap<String, String> userInfoData = new HashMap<>();
                                                userInfoData.put("password", password);
                                                userInfoData.put("firstName",firstName);
                                                userInfoData.put("lastName",lastName);
                                                userInfoData.put("email",email);
                                                userInfoData.put("userName",userName);
                                                userCollectionReference
                                                        .document(user_Id)
                                                        .set(userInfoData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("create user", "user information been added successfully!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("create user", "user information is not be added!" + e.toString());
                                                            }
                                                        });
                                                Intent returnIntent = new Intent();
                                                returnIntent.putExtra("result","success");
                                                setResult(Activity.RESULT_OK,returnIntent);
                                                finish();

                                            }
                                        }
                                    });

                                }
                            } else {
                                Log.d("check userName", "Failed with: ", task.getException());
                            }
                        }
                    });
}
            }
        });


    }

}