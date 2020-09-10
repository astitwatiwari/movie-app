package com.example.moviesapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.moviesapp.R;

import lombok.SneakyThrows;

public class ProfilePageActivity extends AppCompatActivity {

    ImageButton nameEditEnable;
    ImageButton aboutEditEnable;
    ImageButton phoneEditEnable;
    ImageButton openCamera;
    ImageView profilePic;
    EditText name;
    EditText phone;
    EditText about;

    Uri imageUri;
    String outputFileUri;
    Uri permImageUri;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private final int REQUEST_ALL_PERMISSIONS = 1;
    private final int TAKE_PICTURE = 123;
    private final int OPEN_GALLERY = 234;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        openCamera = findViewById(R.id.profilePageCameraButton);
        nameEditEnable = findViewById(R.id.nameEditEnableButton);
        aboutEditEnable = findViewById(R.id.aboutEditEnableButton);
        phoneEditEnable = findViewById(R.id.phoneEditEnableButton);
        name = findViewById(R.id.profilePageName);
        phone = findViewById(R.id.profilePagePhone);
        about = findViewById(R.id.profilePageAbout);
        profilePic = findViewById(R.id.profilePagePic);


        about.setImeOptions(EditorInfo.IME_ACTION_DONE);
        about.setRawInputType(InputType.TYPE_CLASS_TEXT);

        nameEditEnable.setOnClickListener(nameEditEnableClickListener);
        aboutEditEnable.setOnClickListener(aboutEditEnableCLickListener);
        phoneEditEnable.setOnClickListener(phoneEditEnableClickListener);
        openCamera.setOnClickListener(openCameraClickListener);

        sharedPreferences = getPreferences(0);
        editor = sharedPreferences.edit();

        name.setText(sharedPreferences.getString("name", null));
        phone.setText(sharedPreferences.getString("phone", null));
        about.setText(sharedPreferences.getString("about", null));
        outputFileUri = sharedPreferences.getString("uri", null);

      if (outputFileUri != null) {
            permImageUri = Uri.parse(outputFileUri);
           profilePic.setImageURI(permImageUri);
        }


        name.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                doneAction(name);
                editor.putString("name", name.getText().toString());
                editor.commit();
                handled = true;
            }
            return handled;
        });
        phone.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                doneAction(phone);
                editor.putString("phone", phone.getText().toString());
                editor.commit();
                handled = true;
            }
            return handled;
        });
        about.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                doneAction(about);
                editor.putString("about", about.getText().toString());
                editor.commit();
                handled = true;
            }
            return handled;
        });
    }

    public View.OnClickListener nameEditEnableClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (name.isEnabled()) {
                doneAction(name);
                editor.putString("name", name.getText().toString());
                editor.commit();
            } else {
                name.setSelection(name.getText().length());
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.requestFocus();
                imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            }

        }
    };

    public View.OnClickListener aboutEditEnableCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (about.isEnabled()) {
                doneAction(about);
                editor.putString("about", about.getText().toString());
                editor.commit();


            } else {
                about.setSelection(about.getText().length());
                about.setEnabled(true);
                about.setFocusableInTouchMode(true);
                about.requestFocus();
                imm.showSoftInput(about, InputMethodManager.SHOW_IMPLICIT);
            }

        }
    };

    public View.OnClickListener phoneEditEnableClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (phone.isEnabled()) {
                doneAction(phone);
                editor.putString("phone", phone.getText().toString());
                editor.commit();

            } else {
                phone.setSelection(phone.getText().length());
                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.requestFocus();
                imm.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
            }

        }
    };

    public View.OnClickListener openCameraClickListener = v -> {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ALL_PERMISSIONS);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an option");
            String[] options = {"Camera", "Gallery"};
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intentCamera, TAKE_PICTURE);
                            break;
                        case 1:
                            Intent intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intentGallery.setType("image/*");
                            startActivityForResult(intentGallery, OPEN_GALLERY);
                            break;

                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();


        }
    };

    @SneakyThrows
    public void doneAction(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.setSelection(0);
        editText.setEnabled(false);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @SneakyThrows
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            if (imageUri != null) {
                profilePic.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri));
                editor.putString("uri", imageUri.toString());
                editor.commit();
            }
        } else if (requestCode == OPEN_GALLERY && resultCode == RESULT_OK) {
            profilePic.setImageURI(data.getData());
            editor.putString("uri", data.getData().toString());
            editor.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            openCamera.performClick();
        }

    }
}
