package com.example.clutterrevision;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.media.CamcorderProfile.get;

public class AdapterBooks extends RecyclerView.Adapter<AdapterBooks.ViewHolder> {
    private Context context;
    private List<PojoBook> data = new ArrayList();
    private ViewModelBook viewModelBook;

    public AdapterBooks(Context context, ViewModelBook viewModelBook) {
        this.context = context;
        this.viewModelBook = viewModelBook;
    }

    @NonNull
    @Override
    public AdapterBooks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBooks.ViewHolder holder, int position) {
        PojoBook pojoBook = data.get(position);
        holder.setUrl(pojoBook.getTitle());
        holder.title.setText(pojoBook.getTitle());
        String authors = "";
        for (int i = 0; i < pojoBook.getAuthors().size(); i++) {
            if (i < pojoBook.getAuthors().size() - 1) {
                authors += pojoBook.getAuthors().get(i) + ", ";
            } else {
                authors += pojoBook.getAuthors().get(i);
            }
        }
        holder.author.setText(authors);
        holder.description.setText(pojoBook.getDescription());
        String publish;
        if (pojoBook.getPublisher() == "") {
            publish = pojoBook.getPublisher() + pojoBook.getPublishedDate();
        } else {
            publish = pojoBook.getPublisher() + ", " + pojoBook.getPublishedDate();
        }
        holder.publish.setText(publish);
        String https = data.get(position).getSmallThumbnail().replace("http", "https");
        Picasso.get().load(Uri.parse(https))
                .placeholder(R.drawable.rotate_animation)
                .fit().centerInside()
                .into(holder.thumb, new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Success");
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Drawable drawable;

        TextView title;
        TextView author;
        TextView description;
        TextView publish;
        ImageView thumb;
        ImageView button;
        AnimationDrawable animationDrawable;
        Handler handler = new Handler();

        String url;

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.text_title);
            author = itemView.findViewById(R.id.text_authors);
            description = itemView.findViewById(R.id.text_description);
            publish = itemView.findViewById(R.id.text_publish);
            button = itemView.findViewById(R.id.button_web);
            animationDrawable = (AnimationDrawable) button.getDrawable();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setBackground(context.getResources().getDrawable(R.drawable.referenece, null));
                    animationDrawable.start();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animationDrawable.stop();
                            button.setBackground(context.getResources().getDrawable(R.drawable.basic, null));
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, url + " book");
                            context.startActivity(intent);
                        }
                    }, 300);
                }
            });
            thumb = itemView.findViewById(R.id.thumb_book);
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    public void setData(List<PojoBook> books) {
        data = books;
        notifyDataSetChanged();
    }

}
