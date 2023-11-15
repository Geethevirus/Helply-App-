package com.example.helply;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class ImageScanner extends AppCompatActivity {

    private Button snapBtn;
    private Button detectBtn;
    private TextView outcomeText;
    private ImageView snapImage;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scanner);


        snapBtn = findViewById(R.id.button);
        detectBtn = findViewById(R.id.button2);
        outcomeText = findViewById(R.id.outcomeText);
        snapImage = findViewById(R.id.imageView);
        toolbar = findViewById(R.id.scannerToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Text Recognition");


        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectText();

            }
        });

        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission())
                {
                    captureImage();

                }else{

                    requestPermission();
                }

            }
        });


    }

    //Check the permission to access is granted or not
    private boolean checkPermission()
    {

        int camerPermissions = ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        return camerPermissions == PackageManager.PERMISSION_GRANTED;

    }

    //request permission to access camera
    private void requestPermission()
    {
        //permission code integer variable
        int PERMISSION_CODE = 200;

        ActivityCompat.requestPermissions(this, new String[]{CAMERA},PERMISSION_CODE);

    }

    //used to capture the image
    private void captureImage()
    {

        Intent takeImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takeImage.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(takeImage,REQUEST_IMAGE_CAPTURE);

        }

    }
    //check the results when the permission is asked from the user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0)
        {
            //Checks if permission is granted or not
            boolean camerPermisssion = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (camerPermisssion)
            {
                Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT).show();


                captureImage();
            }else{
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //checks if the permission it has been granted by user or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            snapImage.setImageBitmap(imageBitmap);
        }
    }

    //Method to detect text from image
    public void detectText()
    {
        InputImage image = InputImage.fromBitmap(imageBitmap,0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        Task<Text> text = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {

                StringBuilder result = new StringBuilder();
                //Extracting the data

                for(Text.TextBlock block : text.getTextBlocks())
                {
                    String blockText = block.getText();
                    Point[] blockCornerPoint = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();

                    for (Text.Line line: block.getLines())
                    {
                        String lineText = line.getText();
                        Point[] lineCornerPoint = line.getCornerPoints();
                        Rect linRect = line.getBoundingBox();
                        for(Text.Element element : line.getElements())
                        {
                            String elementText = element.getText();
                            result.append(elementText);

                        }
                        outcomeText.setText(blockText);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImageScanner.this,"Failure to detect",Toast.LENGTH_SHORT).show();
            }
        });
    }
}