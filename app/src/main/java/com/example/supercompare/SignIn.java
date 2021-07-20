package com.example.supercompare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignIn extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignInAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignInAction = (Button)findViewById(R.id.btnSignInAction);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignInAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                String userId = edtPhone.getText().toString();
                String userPassword = edtPassword.getText().toString();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("password", userPassword);
                    Log.d("signup", "signin onClick: " + obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AndroidNetworking.get("http://10.0.2.2:8084/loginUser/{userId}/{password}")
                        .addPathParameter("userId", userId)
                        .addPathParameter("password", userPassword)
                        .setTag("signIn")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                Log.d("signup", "onResponse: " + response);
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "Signed In Successfully!", Toast.LENGTH_SHORT).show();
                                try {
                                    String nameResponse = response.getString("username");
                                    String passResponse = response.getString("password");
                                    Log.d("signup", "onResponse login: " + nameResponse +" " + passResponse);
                                    Common.currentUser = new User(nameResponse, passResponse);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //move to home intent
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                startActivity(homeIntent);
                                finish();
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d("signup", "onError: " + error);
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "Error Accrued while signIn", Toast.LENGTH_SHORT).show();

                            }
                        });
                /*table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) { //check if user already exist
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword()!=null) {
                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    Common.currentUser = user;
                                    Toast.makeText(SignIn.this, "Sign In Successfully!", Toast.LENGTH_SHORT).show();

                                    //move to home intent
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    Toast.makeText(SignIn.this, "Incorrect Password.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                        else {
                            mDialog.dismiss();
                            //user doesn't exist
                            Toast.makeText(SignIn.this, "User doesn't exist in database!", Toast.LENGTH_SHORT).show();
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
