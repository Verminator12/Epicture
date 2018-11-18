package eu.epitech.epicture;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Objects;

import eu.epitech.epicture.model.BasicResponse;
import eu.epitech.epicture.model.ImageResponse;
import eu.epitech.epicture.model.ImgurImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FullscreenFragment extends Fragment {
    private ImageView imageView;
    private String accessToken = "accessToken";
    private String imageLink = "imageLink";
    private String imageId = "imageId";
    private String deleteHash = "deleteHash";
    private onDeleteListener listener;
    private ApiInterface client;

    public FullscreenFragment() {}

    public static FullscreenFragment newInstance(String accessToken, String imageLink,
                                                 String imageId, String deleteHash) {
        FullscreenFragment fragment = new FullscreenFragment();
        Bundle args = new Bundle();

        args.putString("accessToken", accessToken);
        args.putString("imageLink", imageLink);
        args.putString("imageId", imageId);
        args.putString("deleteHash", deleteHash);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fullscreen, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView = getActivity().findViewById(R.id.fullscreen_image);
        ImageButton favorite_button = getActivity().findViewById(R.id.favorite_image);
        ImageButton remove_button = getActivity().findViewById(R.id.remove_image);

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteImage(imageId);
            }
        });
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage(deleteHash);
            }
        });
        Bundle args = getArguments();
        if (args != null) {
            this.accessToken = args.getString("accessToken");
            this.imageLink = args.getString("imageLink");
            this.imageId = args.getString("imageId");
            this.deleteHash = args.getString("deleteHash");
        }
        loadImage(imageLink);
    }

    private void loadImage(String imageLink) {
        Glide.with(this)
                .load(imageLink)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ProgressBar progressBar = getActivity().findViewById(R.id.fullscreen_progress_bar);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressBar progressBar = getActivity().findViewById(R.id.fullscreen_progress_bar);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    private void favoriteImage(String imageId) {
        createImgurClient();
        Call<BasicResponse> call = client.favoriteImage(accessToken, imageId);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                BasicResponse body = response.body();
                if (response.isSuccessful() && body != null && body.getSuccess()) {
                    FavoriteFragment gallery = (FavoriteFragment) getActivity().getSupportFragmentManager().findFragmentByTag("favorite");
                    if (gallery != null) {
                        gallery.refreshList();
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });
    }

    private void removeImage(String deleteHash) {
        createImgurClient();
        Call<BasicResponse> call = client.deleteImage(accessToken, deleteHash);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
            }
        });
        listener.onDeletedImage();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onDeleteListener)
            listener = (onDeleteListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement onDeleteListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface onDeleteListener {
        void onDeletedImage();
    }

    private void createImgurClient() {
        if (client == null)
            client = ApiClient.getClient().create(ApiInterface.class);
    }
}
