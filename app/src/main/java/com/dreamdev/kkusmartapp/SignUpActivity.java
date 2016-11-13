package com.dreamdev.kkusmartapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.okhttp.*;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    //Explicit
    private Button button;
    private EditText nameEditText, phoneEditText, userEditText, passwordEditText;
    private ImageView imageView;
    private String nameString, phoneString, userString, passwordString, imagePathString, imageNameString;
    private Uri uri;
    private Boolean aBoolean;
    private String urlAddUser = "http://www.swiftcodingthai.com/kku/add_user_dreamer.php";
    private String urlImage = "http://www.swiftcodingthai.com/kku/Image";

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
        aBoolean = true;

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
                } else if (aBoolean) {
                    //Non Choose
                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.doremon48, "ยังไม่เลือกรูป", "กรุณาเลือกรูปด้วยนะครับ");
                    myAlert.myDialog();
                } else {
                    //Choose Image OK
                    uploadImageToServer();
                    upLoadStringToServer();
                }

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

    private void upLoadStringToServer() {

        AddNewUser addNewUser = new AddNewUser(SignUpActivity.this);
        addNewUser.execute(urlAddUser);

    }// Upload

    //Create Inner Class
    private class AddNewUser extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;

        public AddNewUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd","true")
                        .add("name", nameString)
                        .add("phone", phoneString)
                        .add("user", userString)
                        .add("password", passwordString)
                        .add("image", urlImage + imageNameString)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
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
            Log.d(TAG, "onPostExecute: " + s);
        }// on post
    }// AddNewUser Class


    private void uploadImageToServer() {

        //Change policy
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {

            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect("ftp.swiftcodingthai.com", 21, "kku@swiftcodingthai.com", "Abc12345");
            simpleFTP.bin();
            simpleFTP.cwd("Image");
            simpleFTP.stor(new File(imagePathString));
            simpleFTP.disconnect();

        } catch (Exception e) {
            Log.d(TAG, "uploadImageToServer: " + e.toString());
        }

    }// Upload

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {

            aBoolean = false;

            Log.d(TAG, "onActivityResult: SelectImage!");

            //Show image
            uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Find Path of Image
            imagePathString = myFindPath(uri);
            Log.d(TAG, "onActivityResult: Path = " + imagePathString);

            //Find Name of Image
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d(TAG, "onActivityResult: ImageName = " + imageNameString);


        } // if

    }// onActivity

    private String myFindPath(Uri uri) {

        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);

        if (cursor != null) {

            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(index);

        } else {
            result = uri.getPath();
        }

        return result;
    }

}// Main Classs
