package edu.chinmayt.cs478.musicClient;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import edu.chinmayt.cs478.musicCommon.MusicService;


public class MusicClientMain extends AppCompatActivity {
    public String[] artists = new String[5];
    public String[] URL = new String[5];
    public String[] titles = new String[5];
    public Bundle images = new Bundle();
    protected static final String TAG = "MediaServiceUser";
    private MusicService mMediaService;
    private boolean mIsBound = false;
    public audioPlayer1 ap = new audioPlayer1();
    TextView titleV;
    TextView artistV;
    ImageView imageV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //Initializing text views and image views for display
        TextView labelOutput = (TextView) findViewById(R.id.labeloutput);
        TextView txt1 = (TextView) findViewById(R.id.txtView);
        TextView txt2 = (TextView) findViewById(R.id.textView);
        TextView txt3 = (TextView) findViewById(R.id.textView2);
        TextView txt4 = (TextView) findViewById(R.id.textView3);
        TextView txt5 = (TextView) findViewById(R.id.textView4);
        //Initializing view for current song details
        titleV = (TextView) findViewById(R.id.TitleView);
        artistV = (TextView) findViewById(R.id.artistView);
        imageV = (ImageView) findViewById(R.id.imageView);
        //Initializing Buttons
        final Button bindButton = (Button) findViewById(R.id.bind_button);
        final Button listAllButton = (Button) findViewById(R.id.listButton);
        final Button unBindButton = (Button) findViewById(R.id.unbind_button);
        //Graying out buttons and views before binding
        unBindButton.setEnabled(false);
        listAllButton.setEnabled(false);
        labelOutput.setText("Service is unbound");
        txt1.setEnabled(false);
        txt2.setEnabled(false);
        txt3.setEnabled(false);
        txt4.setEnabled(false);
        txt5.setEnabled(false);
        //OnClickListener for Bind button to bind service and further operations
        bindButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkBindingAndBind();
                mIsBound = true;
                try {
                    if (mIsBound) {
                        //Enabling disabled buttons after binding service
                        listAllButton.setEnabled(true);
                        unBindButton.setEnabled(true);
                        txt1.setEnabled(true);
                        txt2.setEnabled(true);
                        txt3.setEnabled(true);
                        txt4.setEnabled(true);
                        txt5.setEnabled(true);
                        bindButton.setEnabled(false);
                        labelOutput.setText("Service is bound");//Displaying Service is bound

                        Bundle allDetails = new Bundle();
                        //Getting bundle containing all details of songs from Service
                        allDetails = mMediaService.getAllDetails();
                        //Setting text views to list of songs obtained from service
                        txt1.setText(allDetails.getStringArray("titles")[0]);
                        txt2.setText(allDetails.getStringArray("titles")[1]);
                        txt3.setText(allDetails.getStringArray("titles")[2]);
                        txt4.setText(allDetails.getStringArray("titles")[3]);
                        txt5.setText(allDetails.getStringArray("titles")[4]);
                        //Storing obtained details in string array and bundle
                        titles = allDetails.getStringArray("titles");
                        artists = allDetails.getStringArray("artists");
                        URL = allDetails.getStringArray("urls");
                        images = allDetails.getBundle("imageBundle");//Contains all images as byteArray
                    } else {
                        Log.i(TAG, "Service was not bound!");
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        //OnClickListeners for all text views displayed for songs list
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting media player on Click
                startMedia(0);
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting media player on Click
                startMedia(1);
            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting media player on Click
                startMedia(2);
            }
        });
        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting media player on Click
                startMedia(3);
            }
        });
        txt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting media player on Click
                startMedia(4);
            }
        });
        //OnClickListener for listing all song details in new activity
        listAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                //Sending all data obtained from service with intent to new activity
                intent.putExtra("artist",artists);
                intent.putExtra("urls", URL);
                intent.putExtra("titles", titles);
                intent.putExtra("images",images);
                startActivity(intent);
                //Setting Current song display to blank
                titleV.setText("");
                imageV.setImageBitmap(null);
                artistV.setText("");
            }
        });
        //OnClickListener for unbinding service
        unBindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBound) {
                    //Unbinding service
                    unbindService(mConnection);
                    mIsBound = false;
                    labelOutput.setText("Service is unbound");
                    //Disabling buttons and views except bind button
                    unBindButton.setEnabled(false);
                    listAllButton.setEnabled(false);
                    bindButton.setEnabled(true);
                    txt1.setEnabled(false);
                    txt2.setEnabled(false);
                    txt3.setEnabled(false);
                    txt4.setEnabled(false);
                    txt5.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Starting and binding service onStart of the application
        checkBindingAndBind();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unbinding service after activity is destroyed
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    protected void checkBindingAndBind() {
        if (!mIsBound) {
            ComponentName c;
            boolean b = false;
            Intent i = new Intent(MusicService.class.getName());
            // Must make intent explicit or lower target API level to 20.
            ResolveInfo info = getPackageManager().resolveService(i, 0);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
            //Starting service as foreground service
            c = startForegroundService(i);
            Log.i(TAG, "checkBindingAndBind: " + c);
            i.setComponent(c);
            //Binding service to client
            b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
            if (b) {
                Log.i(TAG, "bindService() succeeded!");
            } else {
                Log.i(TAG, "bindService() failed!");
            }
        }
    }

    //Method used to start media player after user clicks on song item
    public void startMedia(int id) {
        Bundle solo = new Bundle();
        try {
            //Getting all details(title, artist and image) of song clicked by user from service
            solo = mMediaService.getAll(id);
            String title = solo.getString("title");
            String artist = solo.getString("artist");
            byte[] imgs = solo.getByteArray("image");//Getting image as byteArray from service
            Bitmap img = BitmapFactory.decodeByteArray(imgs, 0,imgs.length);
            //Getting URL from service for the song clicked by user
            String url1 = mMediaService.getURLOfOne(id);
            //Displaying details(Title, artist, image) for song being played
            titleV.setText(title);
            artistV.setText(artist);
            imageV.setImageBitmap(img);
            // Starting Media Player using object created of class containing media player
            ap.playAudio(url1);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    //Service Connection for service
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iservice) {
            Log.i(TAG, "onServiceConnected");
            mMediaService = MusicService.Stub.asInterface(iservice);
            mIsBound = true; //True after binding
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i(TAG, "onServiceDisconnected ");
            mMediaService = null;
            ap.killMediaPlayer(); //Kill media player is service gets disconnected by OS
            mIsBound = false; //false after unbinding
        }
    };
}