package eu.epitech.epicture;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class LoginFragment extends Fragment {
    private WebView loginWebview;
    private String IMGUR_AUTH_CALLBACK = "https://api.imgur.com/oauth2/authorize";
    private ImgurAppInfo appInfo = new ImgurAppInfo();
    private ImgurTokenListener listener;

    public String getImgurAppCallback() {
        return appInfo.getImgurAppCallback();
    }

    public LoginFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_login, container, false);

        loginWebview = mainView.findViewById(R.id.login_webview);

        WebSettings webSettings = loginWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        loginWebview.setWebViewClient(new LoginWebviewClient(this));
        loginWebview.loadUrl(buildWebviewUrl().toString());
        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImgurTokenListener)
            listener = (ImgurTokenListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        if (loginWebview != null) {
            clearHistoryAndCookies();
            loginWebview.destroy();
            loginWebview = null;
        }
    }

    private Uri buildWebviewUrl() {
        Uri.Builder loginUri = Uri.parse(IMGUR_AUTH_CALLBACK).buildUpon();

        loginUri.appendQueryParameter("client_id", appInfo.getImgurAppId());
        loginUri.appendQueryParameter("response_type", "token");

        return loginUri.build();
    }

    public void clearHistoryAndCookies() {
        loginWebview.clearCache(true);
        loginWebview.clearHistory();

        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    public void OnImgurTokenReceived(ImgurToken token) {
        if (listener != null)
            listener.OnImgurTokenReceived(token);
    }

    public interface ImgurTokenListener {
        void OnImgurTokenReceived(ImgurToken token);
    }
}
