package eu.epitech.epicture;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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


public class TrendingFragment extends Fragment {
    private static final String SORT_OPTION = "top";
    private static final String QUERY = "code";
    private GridView gallery_grid;
    private SearchView gallery_search;
    private List<ImgurImage> images = null;
    private ApiInterface client = null;
    private String accessToken = "accessToken";
    private GalleryListener galleryListener;

    public TrendingFragment() {}

    public static TrendingFragment newInstance(String accessToken) {
        Bundle args = new Bundle();
        TrendingFragment fragment = new TrendingFragment();

        args.putString("accessToken", accessToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gallery_search = getActivity().findViewById(R.id.gallery_search);
        gallery_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery_search.setIconified(false);
            }
        });
        gallery_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                images.clear();
                loadTrending("0", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        gallery_grid = getActivity().findViewById(R.id.gallery_grid);
        gallery_grid.setAdapter(new ImageAdapter(getContext()));
        Bundle args = getArguments();
        if (args != null)
            this.accessToken = args.getString("accessToken");
        loadTrending("0", QUERY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GalleryListener)
            galleryListener = (GalleryListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement GalleryListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        galleryListener = null;
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
        loadTrending("0", QUERY);
    }

    private void loadTrending(String page, String query) {
        if (images == null || images.isEmpty()) {
            createImgurClient();
            Call<ImageResponse> call = client.trendingGallery(accessToken, SORT_OPTION, page, "jpg", query);
            call.enqueue(new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        ImageResponse body = response.body();

                        if (response.isSuccessful() && body != null && body.getSuccess()) {
                            Log.v("imgur", "SUCCESS");
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
