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


public class GalleryFragment extends Fragment {
    private GridView gallery_grid;
    private GalleryListener mListener;

    public GalleryFragment() {}

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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GalleryListener) {
            mListener = (GalleryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    public interface GalleryListener {
        void onGalleryClicked();
    }
}
