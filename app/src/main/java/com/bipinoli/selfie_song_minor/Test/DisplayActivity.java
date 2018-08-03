package com.bipinoli.selfie_song_minor.Test;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipinoli.selfie_song_minor.R;


public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";

    ImageView mImageView;
    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        mImageView = findViewById(R.id.imageView);
        mTextView = findViewById(R.id.textView_inference);

        if (getIntent() != null) {
            // Parse the gallery image url to uri
            Uri savedImageURI = Uri.parse(getIntent().getStringExtra("IMAGE_PATH"));

            // Display the saved image to ImageView
            mImageView.setImageURI(savedImageURI);

            // set inference result into the textView
            mTextView.setText(getIntent().getStringExtra("INFERENCE"));
        }
    }
}
