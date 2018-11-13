package eu.epitech.epicture;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;


public class LoginFragment extends Fragment {
    private WebView loginWebview;
    private String IMGUR_AUTH_CALLBACK = "https://api.imgur.com/oauth2/authorize";
    private String imgurAppCallback = new ImgurAppInfo().getImgurAppCallback();

    public String getImgurAppCallback() {
        return imgurAppCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_login, container, false);

        loginWebview = mainView.findViewById(R.id.login_webview);
        loginWebview.setWebViewClient(new LoginWebviewClient(this));
        loginWebview.loadUrl(buildWebviewUrl().toString());
        return mainView;
    }

    private Uri buildWebviewUrl() {
        Uri.Builder loginUri = Uri.parse(IMGUR_AUTH_CALLBACK).buildUpon();
        String AppId = new ImgurAppInfo().getImgurAppId();

        loginUri.appendQueryParameter("client_id", AppId);
        loginUri.appendQueryParameter("response_type", "token");

        return loginUri.build();
    }

    public void clearHistoryAndCookies() {
        loginWebview.clearCache(true);
        loginWebview.clearHistory();

        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    public void onImgurTokenReceived(ImgurToken token) {
        clearHistoryAndCookies();
        Log.e("imgur", "token received");
        Log.e("imgur", token.toAuthHeader());
    }
}
