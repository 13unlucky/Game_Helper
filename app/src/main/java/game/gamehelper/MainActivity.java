package game.gamehelper;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import org.opencv.core.CvType;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
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
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Mat canny;
    private Mat gray;
    private Mat hierarchy;
    private Mat blur;
    private Mat rgba;

    private String CurrentPhotoPath;

    private Button pictureButton;
    private Button pictureButtonGray;
    private Button pictureButtonBlur;
    private Button pictureButtonCanny;
    private Button pictureButtonProc;
    private Button countButton;

    private TextView countText;
    private ImageView picture;

    private Bitmap bitmapRgba;
    private Bitmap bitmapGray;
    private Bitmap bitmapBlur;
    private Bitmap bitmapCanny;



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
        pictureButtonGray = (Button) findViewById(R.id.showPictureGray);
        pictureButtonGray.setOnClickListener(this);
        pictureButtonProc = (Button) findViewById(R.id.showPictureProcessed);
        pictureButtonProc.setOnClickListener(this);
        pictureButtonBlur = (Button) findViewById(R.id.showPictureBlur);
        pictureButtonBlur.setOnClickListener(this);
        pictureButtonCanny = (Button) findViewById(R.id.showPictureCanny);
        pictureButtonCanny.setOnClickListener(this);
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
                countText.setText(CurrentPhotoPath);
                break;
            case R.id.showPictureGray:
                bitmapGray = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(gray, bitmapGray);
                picture.setImageBitmap(bitmapGray);
                countText.setText("Gray");
                break;
            case R.id.showPictureBlur:
                bitmapBlur = Bitmap.createBitmap(blur.cols(), blur.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(blur, bitmapBlur);
                picture.setImageBitmap(bitmapBlur);
                countText.setText("Processed");
                break;
            case R.id.showPictureCanny:
                bitmapCanny = Bitmap.createBitmap(canny.cols(), canny.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(canny, bitmapCanny);
                picture.setImageBitmap(bitmapCanny);
                countText.setText("Processed");
                break;
            case R.id.showPictureProcessed:
                bitmapRgba = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(rgba, bitmapRgba);
                picture.setImageBitmap(bitmapRgba);
                countText.setText("Processed");
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
    /*
    @Override
    public void onStop()
    {

        gray.release();
        circles.release();
    }
    */
    public int countCircles(){

        File file = new File(CurrentPhotoPath);
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap myBitmap32 = myBitmap.copy(Bitmap.Config.ARGB_8888, true);

        rgba = new Mat(myBitmap32.getWidth(),myBitmap32.getHeight(), CvType.CV_8UC1);
        gray = new Mat(myBitmap32.getWidth(),myBitmap32.getHeight(), CvType.CV_8UC1);
        canny = new Mat(myBitmap32.getWidth(),myBitmap32.getHeight(), CvType.CV_8UC1);
        blur = new Mat(myBitmap32.getWidth(),myBitmap32.getHeight(), CvType.CV_8UC1);
        hierarchy = new Mat();

        Utils.bitmapToMat(myBitmap32,rgba,true);


        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(gray, blur, new Size(3, 3), new Point(0, 0), 2);
        Imgproc.Canny(blur, canny, 120, 250);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();


        Imgproc.findContours(canny, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.drawContours(rgba, contours, - 1, new Scalar(255,0,0,255));









        return 0;
    }


}
