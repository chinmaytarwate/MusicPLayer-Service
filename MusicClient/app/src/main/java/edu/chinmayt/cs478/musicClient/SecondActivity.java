package edu.chinmayt.cs478.musicClient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SecondActivity extends Activity {
    String[] artists;
    String[] titles;
    String[] URLs;
    Bundle image = new Bundle();
    byte[] images1;
    byte[] images2;
    byte[] images3;
    byte[] images4;
    byte[] images5;
    Bitmap[] img;
    public audioPlayer1 ap1 = new audioPlayer1();
    private RecyclerView mainView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        //Recycler view for displaying all songs and its details
        mainView = (RecyclerView) findViewById(R.id.recycler);
        //Getting title, artists, URL and images from intent passed to this activity
        titles = getIntent().getStringArrayExtra("titles");
        artists = getIntent().getStringArrayExtra("artist");
        URLs = getIntent().getStringArrayExtra("urls");
        image = getIntent().getBundleExtra("images");
        //Getting bitmap of images
        images1 = image.getByteArray("img1");
        Bitmap img1 = BitmapFactory.decodeByteArray(images1, 0, images1.length);
        images2 = image.getByteArray("img2");
        Bitmap img2 = BitmapFactory.decodeByteArray(images2, 0, images2.length);
        images3 = image.getByteArray("img3");
        Bitmap img3 = BitmapFactory.decodeByteArray(images3, 0, images3.length);
        images4 = image.getByteArray("img4");
        Bitmap img4 = BitmapFactory.decodeByteArray(images4, 0, images4.length);
        images5 = image.getByteArray("img5");
        Bitmap img5 = BitmapFactory.decodeByteArray(images5, 0, images5.length);
        img = new Bitmap[]{img1,img2,img3,img4,img5};
        //Adapter for recycler view passed with titles artists URL and images
        AdapterForView adapter = new AdapterForView(titles, artists, URLs,img);
        mainView.setHasFixedSize(true);
        mainView.setAdapter(adapter);
        mainView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStop(){
        super.onStop();
        ap1.killMediaPlayer();
    }
}
