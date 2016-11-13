package com.dreamdev.kkusmartapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private Button signInButton, signUpButton;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind widget
        signInButton = (Button) findViewById(R.id.signIn);
        signUpButton = (Button) findViewById(R.id.signUp);
        userEditText = (EditText) findViewById(R.id.editText5);
        passwordEditText = (EditText) findViewById(R.id.editText6);

        //Sign in Controller
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Value Form Edit Text
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                // Check Space
                if (userString.isEmpty() || passwordString.isEmpty()) {
                    //Have Space
                    MyAlert myAlert = new MyAlert(MainActivity.this, R.drawable.nobita48,
                            getResources().getString(R.string.haveSpace),
                            getResources().getString(R.string.haveSpaceMsg));
                    myAlert.myDialog();
                } else {
                    // No Space
                    MyConstant myConstant = new MyConstant();
                    SynUser synUser = new SynUser(MainActivity.this);
                    synUser.execute(myConstant.getUrlGetUser());
                }

            }// Onclick
        });

        //Sign Up Controller
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

    }// Main Method

    private class SynUser extends AsyncTask<String, Void, String> {

        private Context context;

        public SynUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d(TAG, "doInBackground: "+e.toString());
                return null;
            }
        }// do Background

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: "+s);

            try {
                JSONArray jsonArray = new JSONArray(s);
//                JSONObject jsonObject = jsonArray.getJSONObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }// on Post
    } // SynUser


} // Main class
