package com.example.smartalert;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Submission extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LocationListener {

    ActivityResultLauncher<Intent> activityResultLauncher;



    private static final int PICK_IMAGE_REQUEST=1;
    private Spinner spinner;
    private Uri mImageUri;
    Button button9,button10;
    DatabaseReference userdatabaseref;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;
    ImageView myimageview;
    LocationManager locationManager;

    String locat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        myimageview=findViewById(R.id.imageView);
        calendar = Calendar.getInstance();
        simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy HH:mm::ss");
        Date =simpleDateFormat.format(calendar.getTime());
        locationManager =(LocationManager) getSystemService(LOCATION_SERVICE);
        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()==RESULT_OK && null !=result.getData()){
                            mImageUri=result.getData().getData();
                            myimageview.setImageURI(mImageUri);
                        }
                    }
                });

        button9=findViewById(R.id.buttonInsert);
        go1(this.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextInputEditText comments=findViewById(R.id.textInputEditTextComments);
                userdatabaseref= FirebaseDatabase.getInstance().getReference().child("Incident");
                if(TextUtils.isEmpty(comments.getText().toString())){
                    Toast.makeText(Submission.this, "Enter comments to proceed!", Toast.LENGTH_SHORT).show();
                }else {
                    Incident incident=new Incident();
                    incident.comments=comments.getText().toString();
                    incident.type=spinner.getSelectedItem().toString();
                    incident.location=locat;
                    incident.photo=mImageUri.toString();
                    incident.timestamp=Date;
                    userdatabaseref.push().setValue(incident);
                    Toast.makeText(Submission.this, "Data inserted successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Submission.this, Index.class);
                    startActivity(intent);

                }
            }
        });

        button10=findViewById(R.id.buttonUpload);
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent,"Title"),SELECT_IMAGE_CODE);
                activityResultLauncher.launch(intent);

                //openFileChooser();
            }
        });


        spinner = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.danger, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==1) {
            Uri uri=data.getData();
            myimageview.setImageURI(uri);
            button10.setText("Done");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void go1(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locat=location.getLatitude()+","+location.getLongitude();
    }
}