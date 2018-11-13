package eu.epitech.epicture;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginWebviewClient extends WebViewClient {
    private LoginFragment   loginFragment;
    private ImgurToken      token;

    LoginWebviewClient (LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri requestUri = request.getUrl();

        if (isCallbackUrl(requestUri)) {
            Log.v("imgur", "url: " + request.getUrl().toString());
            handleCallbackUrl(view, requestUri);
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    private boolean isCallbackUrl(Uri requestUri) {
        String callbackUrl = loginFragment.getImgurAppCallback();

        if (callbackUrl == null)
            return false;

        Uri urlNoFragment = requestUri.buildUpon().fragment("").build();
        String fragment = requestUri.getFragment();

        return urlNoFragment.toString().equals(callbackUrl) && !TextUtils.isEmpty(fragment);
    }

    private void handleCallbackUrl(WebView view, Uri requestUri) {
        String fragment = requestUri.getFragment();
        ImgurToken handledToken = new ImgurToken(null, null, null, null).parseUri(fragment);

        if (token == null || !handledToken.equals(token)) {
            token = handledToken;
            view.stopLoading();
            view.loadUrl("about:blank");
            loginFragment.onImgurTokenReceived(token);
        }
    }
}
