package com.dreamdev.kkusmartapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    //Explicit
    private Button button;
    private EditText nameEditText, phoneEditText, userEditText, passwordEditText;
    private ImageView imageView;
    private String nameString, phoneString, userString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Bind Widget
        nameEditText = (EditText) findViewById(R.id.editText);
        phoneEditText = (EditText) findViewById(R.id.editText2);
        userEditText = (EditText) findViewById(R.id.editText3);
        passwordEditText = (EditText) findViewById(R.id.editText4);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Value Form EditText
                nameString = nameEditText.getText().toString().trim();
                phoneString = phoneEditText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                //Check Space
                if (nameString.isEmpty() || phoneString.isEmpty() || userString.isEmpty() ||passwordString.isEmpty()) {
                    //Have space
                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.bird48, "Have a space", "กรุณากรอกข้อความให้ครบทุกช่อง");
                    myAlert.myDialog();
                }// IF IsEmpty

            }// Onclick
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "โปรดเลือกแอปดูภาพ"), 0);

            }//Onclick
        });

    }// Main Method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {

            Log.d(TAG, "onActivityResult: SelectImage!");

        } // if

    }// onActivity

}// Main Classs
