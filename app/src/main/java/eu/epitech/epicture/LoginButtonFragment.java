package eu.epitech.epicture;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginButtonFragment extends Fragment implements View.OnClickListener {
    private Button loginButton;
    private LoginListener listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_login_button, container, false);

        loginButton = mainView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        return mainView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                onButtonPressed();
                break;
            default:
                Log.e("imgur", "Unknown button clicked" + v.getId());
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener)
            listener = (LoginListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        if (loginButton != null) {
            loginButton.setOnClickListener(null);
            loginButton = null;
        }
    }

    public void onButtonPressed() {
        if (listener != null)
            listener.onLoginButtonClicked();
    }

    public interface LoginListener {
        void onLoginButtonClicked();
    }
}
