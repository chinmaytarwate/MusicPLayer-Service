package edu.chinmayt.cs478.musicClient;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
//Class for media player
public class audioPlayer1 {
    public static MediaPlayer mPlayer = new MediaPlayer();
    public static boolean isPlayingAudio = false;

    public void playAudio(String url1){
        try {
            //Kill existing media player before starting a new one
            killMediaPlayer();
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(url1);//passing url to media player to stream
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPlayingAudio=true;
                    mp.start(); //Starting media player
                }
            });


        } catch (Exception e) {
            Log.e("AudioPlay",e.toString());
        }
    }
    public void killMediaPlayer() {
        isPlayingAudio=false;
        if(mPlayer.isPlaying()) {
            try {
                mPlayer.stop(); //Stop media player
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
