package com.uw.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(null);
    }

    public void uploadClick(View view) {
        try {
            //1. Create a Reference
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference buckyRef = storageRef.child("images/bucky.png");

            //2. Convert Image to byte stream
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.bucky);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] buckyByteStream = baos.toByteArray();

            //3. Start upload class
            UploadTask uploadTask = buckyRef.putBytes(buckyByteStream);
            uploadTask.addOnFailureListener((exception) -> {
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("ImageUpload", "Image successfully uploaded to Firebase");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Error", "Image Upload Failed");
        }
    }

    public void downloadClick(View view) {
        //1. Create a Reference to object uploaded
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference buckyRef = storageReference.child("images/bucky.png");

        //2. Get ImageView object
        final ImageView imageView = findViewById(R.id.imageView);
        final long ONE_MEGABYTE = 1024 * 1024 * 3;

        //3. Download Image into byte stream, set image in imageview
        buckyRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //4. Data for 'images/bucky.png' returned. Turn into bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                //5. Set the image in imageView
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.i("Error", "Image Download Failed");
            }
        });

    }
}
