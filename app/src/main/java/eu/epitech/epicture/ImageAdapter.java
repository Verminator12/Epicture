package eu.epitech.epicture;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import eu.epitech.epicture.model.ImgurImage;

public class ImageAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<ImgurImage> images = new ArrayList<>();

    ImageAdapter (Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = view.findViewById(R.id.item_image);
            holder.progressBar = view.findViewById(R.id.progress_bar);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        ImgurImage item = images.get(position);
        String link;
        //link = "https://i.imgur.com/" + item.getId() + "m.jpg"; // TODO remove line
        link = item.getLink();
        Glide.with(context)
                .load(link)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
        return view;
    }

    public void setItemsAndNotify(List<ImgurImage> images) {
        this.images = images;
        for (int i = 0; i < images.size(); i++ )
            if (images.get(i).getMp4_size() != null)
                images.remove(i);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }
}
