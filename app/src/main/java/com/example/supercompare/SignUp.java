package com.example.supercompare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.supercompare.Common.Common;
import com.example.supercompare.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    boolean notExist = false;
    MaterialEditText edtPhoneSignUp,edtNameSignUp,edtPasswordSignUp;
    Button btnSignUpAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        edtNameSignUp = findViewById(R.id.edtNameSignUp);
        edtPhoneSignUp = findViewById(R.id.edtPhoneSignUp);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        btnSignUpAction = findViewById(R.id.btnSignUpAction);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUpAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();
                String userPhone = edtPhoneSignUp.getText().toString();
                String userPassword = edtPasswordSignUp.getText().toString();
                String userName = edtNameSignUp.getText().toString();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("userId", userPhone);
                    obj.put("username",userName);
                    obj.put("password", userPassword);
                    Log.d("signup", "onClick: " + obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AndroidNetworking.post("http://10.0.2.2:8084/signUpUser")
                        .addJSONObjectBody(obj)
                        .setTag("signUp")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                Log.d("signup", "onResponse: " + response);
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();

                                try {
                                    String nameResponse = response.getString("username");
                                    String passResponse = response.getString("password");
                                    Log.d("signup", "onResponse login: " + nameResponse +" " + passResponse);
                                    Common.currentUser = new User(nameResponse, passResponse);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //move to home page
                                Intent homeIntent = new Intent(SignUp.this, Home.class);
                                startActivity(homeIntent);
                                finish();                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d("signup", "onError: " + error);
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Error Accrued while signUp", Toast.LENGTH_SHORT).show();

                            }
                        });
                /*table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtPhoneSignUp.getText().toString()).exists()){ //check if user exists
                            mDialog.dismiss();
                            if (notExist == true)
                                Toast.makeText(SignUp.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SignUp.this, "Phone Number already registered", Toast.LENGTH_SHORT).show();
                        }
                        else if (!(dataSnapshot.child(edtPhoneSignUp.getText().toString()).exists())){
                            mDialog.dismiss();
                            User user = new User(edtNameSignUp.getText().toString(),edtPasswordSignUp.getText().toString());
                            notExist = true;
                            table_user.child(edtPhoneSignUp.getText().toString()).setValue(user);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
            }
        });

    }
}
