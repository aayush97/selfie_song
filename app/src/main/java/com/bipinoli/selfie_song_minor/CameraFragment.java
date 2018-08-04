package com.bipinoli.selfie_song_minor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bipinoli.selfie_song_minor.AI.ImageClassifier;
import com.bipinoli.selfie_song_minor.Test.DisplayActivity;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Grid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class CameraFragment extends Fragment{

    public static String FILE_NAME = "snapshot.jpg";

    private static final String TAG = "CameraFragment";

    private CameraView mCameraView;
    private ImageButton mCaptureButton;

    ImageClassifier mClassifier;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        try {
            mClassifier = new ImageClassifier(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }


        mCameraView = (CameraView) v.findViewById(R.id.camera);
        mCameraView.setFacing(Facing.FRONT);
        mCameraView.setGrid(Grid.DRAW_3X3);
        mCameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(final byte[] picture) {
                Log.e(TAG, "onPictureTaken() called");
                // Create a bitmap or a file...
                // CameraUtils will read EXIF orientation for you, in a worker thread.
                CameraUtils.decodeBitmap(picture, new CameraUtils.BitmapCallback() {
                    @SuppressLint("WrongThread")
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        Log.e(TAG, "onBitmapReady() called");


                        // ***************** AI inference ****************/
                        String predictionText = "Sorry! no Inference :(";

                        if (mClassifier == null) {
                            Log.e(TAG, "Image Classifier is null!");
                        } else {
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y, false);
                            predictionText = mClassifier.classifyFrame(scaledBitmap);
                            Log.e(TAG, "AI Prediction: " + predictionText);
                        }

                        //****************************************************/



                        // save image to internal storage, PRIVATE (not visible to user or other apps)
                        File file = getContext().getDir("Images", Context.MODE_PRIVATE);
                        file = new File(file, "snapshot"+".jpg");

                        try {
                            OutputStream stream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                            stream.flush();
                            stream.close();

//                            Intent intent = new Intent(getActivity(), DisplayActivity.class);
//                            intent.putExtra("IMAGE_PATH", file.getAbsolutePath());
//                            intent.putExtra("INFERENCE", predictionText);
//                            startActivity(intent);

                            Intent intent = new Intent(getActivity(), VerificationActivity.class);
                            intent.putExtra("INFERENCE", predictionText);
                            startActivity(intent);

                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onCameraOpened(CameraOptions options) {
                super.onCameraOpened(options);
                Log.e(TAG, "onCameraOpened() called!");
            }
        });


        mCaptureButton = (ImageButton) v.findViewById(R.id.btn_capture);
        final AnimatedVectorDrawableCompat avd = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.avd_anim_button_camera);
        mCaptureButton.setImageDrawable(avd);
        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avd.start();
                mCameraView.captureSnapshot();
            }
        });

    }




    @Override
    public void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCameraView.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCameraView.destroy();
    }


}
