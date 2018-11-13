package eu.epitech.epicture;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginButtonFragment extends Fragment {
    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View mainView = inflater.inflate(R.layout.fragment_login_button, container, false);
         loginButton = mainView.findViewById(R.id.login_button);
         loginButton.setOnClickListener(listener);
         return mainView;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginFragment newFragment = new LoginFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_main, newFragment, "login");
            transaction.addToBackStack("login"); // TODO replace strings with DEFINES
            transaction.commit();
        }
    };
}
