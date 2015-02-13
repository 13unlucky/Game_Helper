package game.gamehelper;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MotionEvent;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {


    private Mat gray;
    private Mat circles;
    private int numCircles;
    private Mat rgba;
    private String CurrentPhotoPath;
    private Button pictureButton;
    private Button countButton;
    private TextView countText;
    private ImageView picture;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {

                }
                break;
                default:
                {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countText = (TextView) findViewById(R.id.countText);
        pictureButton = (Button) findViewById(R.id.pictureButton);
        pictureButton.setOnClickListener(this);
        countButton = (Button) findViewById(R.id.countButton);
        countButton.setOnClickListener(this);
        picture = (ImageView) findViewById(R.id.imageView);
        pictureButton = (Button) findViewById(R.id.showPicture);
        pictureButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.countButton:
                String count = new String();
                count = Integer.toString(countCircles());
                countText.setText(count);
                break;
            case R.id.pictureButton:
                dispatchTakePictureIntent();
                break;
            case R.id.showPicture:
                Bitmap bitmap = BitmapFactory.decodeFile(CurrentPhotoPath);
                picture.setImageBitmap(bitmap);
                System.out.print(CurrentPhotoPath);
                countText.setText(CurrentPhotoPath);
                break;
        }
    }



    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent,1);
            }
        }

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        CurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }


    public int countCircles(){
        gray = new Mat();
        circles = new Mat();
        rgba = new Mat();
        File file = new File(CurrentPhotoPath);
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap myBitmap32 = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32,rgba,true);

        Size sizeRgba = rgba.size();





        int rows = (int) sizeRgba.height;
        int cols = (int) sizeRgba.width;



        Size GB = new Size(9,9);


        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur( gray, gray, GB, 2, 2 );



        Imgproc.HoughCircles( gray, circles,Imgproc.CV_HOUGH_GRADIENT, 1, gray.rows()/8 , 200, 100, 0, 0 );
        /*
        for (int x = 0; x < circles.cols(); x++)
        {
            double vCircle[]=circles.get(0,x);

            Point center=new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
            int radius = (int)Math.round(vCircle[2]);
            // draw the circle center
            Core.circle(rgba, center, 3,new Scalar(0,255,0), -1, 8, 0 );
            // draw the circle outline
            Core.circle( rgba, center, radius, new Scalar(0,0,255), 3, 8, 0 );

        }
        */
        gray.release();
        circles.release();
        numCircles = circles.cols();
        return numCircles;
    }


}
