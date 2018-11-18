package eu.epitech.epicture;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;

import java.util.List;

import eu.epitech.epicture.model.ImageResponse;
import eu.epitech.epicture.model.ImgurImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment {
    private GridView gallery_grid;
    private List<ImgurImage> images = null;
    private ApiInterface client = null;
    private String accessToken = "accessToken";
    private GalleryListener listener;

    public FavoriteFragment() {}

    public static FavoriteFragment newInstance(String accessToken) {
        Bundle args = new Bundle();
        FavoriteFragment fragment = new FavoriteFragment();

        args.putString("accessToken", accessToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SearchView gallery_search = getActivity().findViewById(R.id.gallery_search);
        gallery_search.setVisibility(View.GONE);
        gallery_grid = getActivity().findViewById(R.id.gallery_grid);
        gallery_grid.setAdapter(new ImageAdapter(getContext()));
        Bundle args = getArguments();
        if (args != null)
            this.accessToken = args.getString("accessToken");
        loadFavorite();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GalleryListener)
            listener = (GalleryListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement GalleryListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
        loadFavorite();
    }

    private void loadFavorite() {
        if (images == null || images.isEmpty()) {
            createImgurClient();
            Call<ImageResponse> call = client.favoriteGallery(accessToken);
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
