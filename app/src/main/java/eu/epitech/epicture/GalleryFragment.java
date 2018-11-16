package eu.epitech.epicture;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

import eu.epitech.epicture.model.ImageResponse;
import eu.epitech.epicture.model.ImgurImage;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GalleryFragment extends Fragment {
    private GridView gallery_grid;
    private List<ImgurImage> images = null;
    private ApiInterface client = null;
    private String accessToken = "accessToken";
    private GalleryListener mListener;

    public GalleryFragment() {}

    public static GalleryFragment newInstance(String accessToken) {
        Bundle args = new Bundle();
        GalleryFragment fragment = new GalleryFragment();

        args.putString("accessToken", accessToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView =  inflater.inflate(R.layout.fragment_gallery, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gallery_grid = getActivity().findViewById(R.id.gallery_grid);
        gallery_grid.setAdapter(new ImageAdapter(getContext()));
        Bundle args = getArguments();
        if (args != null)
            this.accessToken = args.getString("accessToken");
        loadUserGallery();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GalleryListener)
            mListener = (GalleryListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement GalleryListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    public void refreshList() {
        images = null;
        final ImageAdapter adapter = (ImageAdapter) gallery_grid.getAdapter();
        if (adapter != null) {
            gallery_grid.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
            });
        }
        loadUserGallery();
    }

    private void loadUserGallery() {
        if (images == null || images.isEmpty()) {
            createImgurClient();
            Call<ImageResponse> call = client.userGallery(accessToken);
            call.enqueue(new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        ImageResponse body = response.body();

                        if (response.isSuccessful() && body != null && body.getSuccess()) {
                            images = body.getData();
                            if (body.getSuccess()) {
                                final ImageAdapter adapter = (ImageAdapter) gallery_grid.getAdapter();
                                if (adapter != null) {
                                    gallery_grid.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.setItemsAndNotify(images);
                                        }
                                    });

                                }
                            }
                        } else {
                            // TODO send error message
                        }
                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        // TODO send error message
                    }
                });
        } else {
            Log.v("imgur", "I have the images that I need");
            // we have cached images, so just display those, instead of loading new ones.
            final ImageAdapter adapter = (ImageAdapter) gallery_grid.getAdapter();
            if (adapter != null) {
                gallery_grid.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItemsAndNotify(images);
                    }
                });
            }
        }
    }

    private void createImgurClient()
    {
        if (client == null)
            client = ApiClient.getClient().create(ApiInterface.class);
    }

    public interface GalleryListener {
        void onGalleryClicked();
    }
}
