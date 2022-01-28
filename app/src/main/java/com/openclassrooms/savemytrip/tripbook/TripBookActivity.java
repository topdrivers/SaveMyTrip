package com.openclassrooms.savemytrip.tripbook;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;






import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.openclassrooms.savemytrip.R;
import com.openclassrooms.savemytrip.base.BaseActivity;
import com.openclassrooms.savemytrip.utils.StorageUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class TripBookActivity extends BaseActivity {

    //FOR DESIGN
    @BindView(R.id.trip_book_activity_external_choice)    LinearLayout linearLayoutExternalChoice;
    @BindView(R.id.trip_book_activity_internal_choice) LinearLayout linearLayoutInternalChoice;
    @BindView(R.id.trip_book_activity_radio_external)    RadioButton radioButtonExternalChoice;
    @BindView(R.id.trip_book_activity_radio_public) RadioButton radioButtonExternalPublicChoice;
    @BindView(R.id.trip_book_activity_radio_volatile) RadioButton radioButtonInternalVolatileChoice;
    @BindView(R.id.trip_book_activity_edit_text)  EditText editText;


    // 1 - FILE PURPOSE


    private static final String FILENAME = "tripBook.txt";


    private static final String FOLDERNAME = "bookTrip";

    private static final String AUTHORITY="com.openclassrooms.savemytrip.fileprovider";


    @Override


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trip_book);
        this.configureToolbar();
        ButterKnife.bind(this);


        // 6 - Read from storage when starting


        readFromStorage();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public int getLayoutContentViewID() {
         return R.layout.activity_trip_book;
    }

// -------------------

// UI

// -------------------

    private void initView() {

        CompoundButton.OnCheckedChangeListener checkedChangeListener = (button, isChecked) -> {

            if (isChecked) {


            }

            readFromStorage();

        };

    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_share) {

            shareFile();

            return true;

        } else if (itemId == R.id.action_save) {

            save();

            return true;

        }

        return super.onOptionsItemSelected(item);

    }


    // --------------------


    // ACTIONS


    // --------------------

    // 4 - Save after user clicked on button


    private void save() {

        if (this.radioButtonExternalPublicChoice.isChecked()) {
        //if (binding.radioButtonExternalPublicChoice.isChecked()) {
            this.writeOnExternalStorage(); //Save on external storage

        } else {

            // TODO : Save on internal storage
            this.writeOnInternalStorage();

        }

    }

    // ----------------------------------


    // UTILS - STORAGE


    // ----------------------------------

    // 2 - Read from storage


    private void readFromStorage() {
        if (checkWriteExternalStoragePermission()) return;

        if (this.radioButtonExternalChoice.isChecked()) {

            if (StorageUtils.isExternalStorageReadable()) {

                File directory;

                // EXTERNAL

                if (this.radioButtonExternalPublicChoice.isChecked()) {

                    // External - Public

                    directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

                } else {

                    // External - Private

                    directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

                }



                this.editText.setText(StorageUtils.getTextFromStorage(directory, this, FILENAME, FOLDERNAME));

            }

        } else {

            // TODO : READ FROM INTERNAL STORAGE
            File directory;
            if (this.radioButtonInternalVolatileChoice.isChecked()){

                directory = getCacheDir();

            }else {

                directory = getFilesDir();

            }

            this.editText.setText(StorageUtils.getTextFromStorage(directory, this, FILENAME, FOLDERNAME));




        }

    }

    // 3 - Write on external storage


    private void writeOnExternalStorage() {

        if (StorageUtils.isExternalStorageWritable()) {

            File directory;

            if (this.radioButtonExternalPublicChoice.isChecked()) {

                directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            } else {

                directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            }

            if (this.radioButtonInternalVolatileChoice.isChecked()){

                directory = getCacheDir();

            }else {

                directory = getFilesDir();

            }

            StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, this.editText.getText().toString());



        } else {

            Toast.makeText(this, getString(R.string.external_storage_impossible_create_file), Toast.LENGTH_LONG).show();

        }
    }


    private void writeOnInternalStorage(){
        File directory;
        if (this.radioButtonInternalVolatileChoice.isChecked()){

            directory = getCacheDir();

        }else {

            directory = getFilesDir();

        }

        StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, this.editText.getText().toString());



    }

    // SHARE FILE


// ----------------------------------


// 2 - Share the internal file


    private void shareFile(){


        File internalFile = StorageUtils.getFileFromStorage(getFilesDir(),this, FILENAME, FOLDERNAME);



        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), AUTHORITY, internalFile);



        Intent sharingIntent = new Intent(Intent.ACTION_SEND);


        sharingIntent.setType("text/*");


        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);


        startActivity(Intent.createChooser(sharingIntent, getString(R.string.trip_book_share)));


    }

    // 1 - PERMISSION PURPOSE


    private static final int RC_STORAGE_WRITE_PERMS = 100;


    @Override


    // 2 - After permission granted or refused


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == RC_STORAGE_WRITE_PERMS) {

            if (grantResults.length > 0 &&

                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                readFromStorage();

            }

        }

    }


    // ----------------------------------


    // UTILS - STORAGE


    // ----------------------------------


    private boolean checkWriteExternalStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,

                    new String[]{WRITE_EXTERNAL_STORAGE},

                    RC_STORAGE_WRITE_PERMS);

            return true;

        }

        return false;

    }


}