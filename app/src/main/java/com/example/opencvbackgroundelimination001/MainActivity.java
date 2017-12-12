package com.example.opencvbackgroundelimination001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.Camera2Renderer;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.features2d.FlannBasedMatcher;
import org.opencv.imgproc.LineSegmentDetector;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.videoio.VideoCapture;
import org.opencv.video.BackgroundSubtractorMOG2;

import static org.opencv.video.Video.createBackgroundSubtractorKNN;
import static org.opencv.video.Video.createBackgroundSubtractorMOG2;


/*
* A Java interpretation of an example from
* https://docs.opencv.org/3.1.0/d1/dc5/tutorial_background_subtraction.html
* */

public class MainActivity extends AppCompatActivity  implements CameraBridgeViewBase.CvCameraViewListener2{

    private CameraBridgeViewBase mOpenCvCameraView;
    private BackgroundSubtractorKNN backgroundSubtractorKNN = null;
    //private BackgroundSubtractorMOG2 backgroundSubtractorMOG2 =  null;// = createBackgroundSubtractorMOG2();
    private Mat aFrame;
    private Mat aMaskedFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
        //Internal OpenCV library not found. Using OpenCV Manager for initialization
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Library har inte laddats, ger managern en spark i röven...");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV är igång och fungerar");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    //nya grejor
                    /*
                    if(backgroundSubtractorMOG2==null) {
                        backgroundSubtractorMOG2 = createBackgroundSubtractorMOG2();
                    }
                    */
                    if(backgroundSubtractorKNN==null) {
                        backgroundSubtractorKNN = createBackgroundSubtractorKNN();
                    }
                    //slut på nya grejor
                    //Log.i(TAG, "OpenCV ld sucsly");
                    mOpenCvCameraView.enableView();
                    //mOpenCvCameraView.SetCaptureFormat();
                    CameraBridgeViewBase SetCaptureFormat;
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }


    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //org.opencv.android
        this.aFrame = inputFrame.gray();
        this.aMaskedFrame = new Mat();
        //nya grejor
        //@ToDo: uppdateraw masken på något sätt
        //this.backgroundSubtractorMOG2.apply(this.aFrame, this.aMaskedFrame);
        this.backgroundSubtractorKNN.apply(this.aFrame, this.aMaskedFrame);
        //slut på nya grejor
        return this.aMaskedFrame;
        //return inputFrame.rgba();
    }


}
