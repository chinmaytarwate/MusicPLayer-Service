package edu.chinmayt.cs478.musiccentral;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.core.app.NotificationCompat;
import java.io.ByteArrayOutputStream;
import edu.chinmayt.cs478.musicCommon.MusicService;

public class MusicCentralMain extends Service {
    String[] resTitle;
    String[] resArtist;
    String[] resURL;
    String oneURL;
    Bundle imgBundle = new Bundle();
    String image1;
    private Notification notification;
    private static String CHANNEL_ID = "Music player style" ;
    private static final int NOTIFICATION_ID = 1;
    private final MusicService.Stub mBinder = new MusicService.Stub() {
        //Method for getting URL of the song clicked by user
        @Override
        public String getURLOfOne(int urlID) throws RemoteException {
            oneURL = resURL[urlID]; //Getting URL for required song
            return oneURL;
        }

        @Override
        public Bundle getAllDetails() throws RemoteException {
            Bundle allBundle = new Bundle(); //Bundle to store all details of all songs
            //Getting all string arrays from resources
            resTitle = getResources().getStringArray(R.array.titles);
            resArtist = getResources().getStringArray(R.array.artists);
            resURL = getResources().getStringArray(R.array.urls);
            //Putting titles, artists, images and urls in bundle
            allBundle.putStringArray("titles",resTitle);
            allBundle.putStringArray("artists", resArtist);
            allBundle.putStringArray("urls", resURL);
            //Converting drawable to Bitmap to store in Bundle
            Drawable d1 = getResources().getDrawable(R.drawable.ukulele_foreground, null);
            Bitmap img1 = ((BitmapDrawable)d1).getBitmap();
            Drawable d2 = getResources().getDrawable(R.drawable.memories_foreground, null);
            Bitmap img2 = ((BitmapDrawable)d2).getBitmap();
            Drawable d3 = getResources().getDrawable(R.drawable.memories_foreground, null);
            Bitmap img3 = ((BitmapDrawable)d3).getBitmap();
            Drawable d4 = getResources().getDrawable(R.drawable.beginning_foreground, null);
            Bitmap img4 = ((BitmapDrawable)d4).getBitmap();
            Drawable d5 = getResources().getDrawable(R.drawable.punky_foreground, null);
            Bitmap img5 = ((BitmapDrawable)d5).getBitmap();

            //Storing images as byteArray in bundle
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray1 = stream.toByteArray();

            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            img2.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
            byte[] byteArray2 = stream1.toByteArray();

            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            img3.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            byte[] byteArray3 = stream2.toByteArray();

            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            img4.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
            byte[] byteArray4 = stream3.toByteArray();

            ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
            img5.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
            byte[] byteArray5 = stream4.toByteArray();

            imgBundle.putByteArray("img1", byteArray1);
            imgBundle.putByteArray("img2", byteArray2);
            imgBundle.putByteArray("img3", byteArray3);
            imgBundle.putByteArray("img4", byteArray4);
            imgBundle.putByteArray("img5", byteArray5);
            allBundle.putBundle("imageBundle", imgBundle); //Putting image bundle in the bundle containing all details
            return allBundle;
        }

        //Get method for all values of selected song by user
        @Override
        public Bundle getAll(int id) throws RemoteException {
            //Getting bitmap bundle keys for song selected by user
            if(id == 0) {
                image1 = "img1";
            } else if (id == 1) {
                image1 = "img2";
            } else if (id == 2){
                image1 = "img3";
            } else if (id ==3) {
                image1 = "img4";
            } else {
                image1 = "img5";
            }
            //Storing all values in bundle
            Bundle b = new Bundle();
            b.putString("title",resTitle[id]);
            b.putString("artist", resArtist[id]);
            b.putByteArray("image",imgBundle.getByteArray(image1));
            b.putString("url",oneURL);
            return b;
        }

    };

    //Creating notification for service
    @Override
    public void onCreate() {
        super.onCreate();
        this.createNotificationChannel();
        final Intent notificationIntent = new Intent(getApplicationContext(), MusicCentralMain.class);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0) ;

        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Music Playing")
                .setContentText("Click to Access Music Player")
                .setTicker("Music is playing!")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Show service", pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "Music player notification";
        String description = "The channel for music player notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = null;
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}