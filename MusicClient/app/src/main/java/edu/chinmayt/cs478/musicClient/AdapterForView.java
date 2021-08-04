package edu.chinmayt.cs478.musicClient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AdapterForView extends  RecyclerView.Adapter<AdapterForView.ViewHolder> {
    protected static final String TAG = "MediaServiceUser";
    private String[] titles; //data: the titles displayed
    private String[] artists;
    private Bitmap[] images;
    private String[] urls;
    public audioPlayer1 ap1 = new audioPlayer1();

    public AdapterForView(String[] title, String[] artist, String[] URLS, Bitmap[] image){
        //Setting all the list items to be used for the view
        titles = title;
        artists = artist;
        images = image;
        urls = URLS;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Inflating recycler view from xml file
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view); //Calling ViewHolder and passing view as parameter
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Setting Text and Images to be displayed in each view created
        holder.name.setText(titles[position]);
        holder.bands.setText(artists[position]);
        holder.image.setImageBitmap(images[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView bands;
        public ImageView image;
        private View itemView;

        public ViewHolder(@NonNull View itemView/*, RVClickListener passedListener*/) {
            super(itemView);
            //setting view for displaying songs
            name = (TextView) itemView.findViewById(R.id.recycleView1);
            bands = (TextView) itemView.findViewById(R.id.recycleView2);
            image = (ImageView) itemView.findViewById(R.id.imgView);

            this.itemView = itemView;
            itemView.setOnClickListener(this); //set click listener
        }

        @Override
        public void onClick(View v) {
            String url1 = urls[getAdapterPosition()];
            Log.i("getTitle", "URL: " + urls[getAdapterPosition()]);
            ap1.playAudio(url1); //Start media player on clicking item
        }

    }
}
