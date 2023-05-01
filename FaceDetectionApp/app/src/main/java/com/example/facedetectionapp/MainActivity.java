package com.example.facedetectionapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.facedetectionapp.Helper.GraphicOverlay;
import com.example.facedetectionapp.Helper.RectOverlay;

public class MainActivity extends AppCompatActivity {

    private Button faceDetectButton;
    private GraphicOverlay graphicOverlay;
    private CameraView cameraView;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        faceDetectButton = findViewById(R.id.detect_face_btn);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        cameraView = findViewById(R.id.camera_view);

        alertDialog = new SpotsDialog.Builder(){
            .setContext(this);
            .setMessage("Please Wait, Processing ...");
            .setCancelable(false);
            .build();

            faceDetectButton.setOnclickListener(new View.OnClickListener)
            {
                @Override
                public void onClick (View v)
                {
                    cameraView.start();
                    cameraView.captureImage();
                    graphicOverlay.clear();
                }
            }
        };

        cameraView.addCameraKitListencer(new CameraKitEventListener()){
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
                // Handle camera event
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
                // Handle camera error
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                alertDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
                // Handle captured video
            }

        };
    }

    private  void processFacedetection(Bitmap bitmap){

        FirebaseVisionImage firebaseVisionImage; = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionFaceDetectorOption firebaseVisionFaceDetectorOptions = new FirebaseVisionFaceDetectorOptions = new FirebaseVisionImage();

        FirebaseVisionFaceDetectorOption firebaseVisionFaceDetectorOptions  firebaseVisionFaceDetectorOptions = FirebaseVision.getInstance()
                .getVisionFaceDetector(firebaseVisionFaceDetectorOptions);

        FirebaseVisionFaceDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>(){
                    @Override
                    public void onSuccess < List <FirebaseVisionFace> firebaseVisionFace)
                    {
                            getFaceResult (firebaseVisionFaces);
                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void getFaceResults < List < FirebaseVisionFace>  firebaseVisionFaces
    {
        int counter = 0;
        for (FirebaseVisionFace face : firebaseVisionFaces)
        {
            Rect rect = face.getBoundingBox();
            RectOverlay rectOverlay = new RectOverlay(graphicOverlay, rect);

            graphicOverlay.add(rectOverlay);
            counter = counter + 1;

        }
        alertDialog.dismiss();
    }
    @Override
    protected void onPause(){
        super.onPause();
        cameraView.stop();

    }

    @Override
    protected void onResume(){
        super.onResume();

        cameraView.start();

    }

}