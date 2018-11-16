package eu.epitech.epicture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;

import eu.epitech.epicture.model.BasicResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        LoginButtonFragment.LoginListener,
        LoginFragment.ImgurTokenListener,
        GalleryFragment.GalleryListener,
        TrendingFragment.GalleryListener {
    private static final String TRENDING_TAG = "trending_button";
    private static final String LOGIN_BUTTON_TAG = "login_button";
    private static final String LOGIN_TAG = "login";
    private static final String GALLERY_TAG = "gallery";
    private String SHARED_TOKEN = "sharedToken";
    private Integer PICK_IMAGE = 1;
    private ApiInterface client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
            return;
        ImgurToken token = getToken();
        if (token != null)
            OnImgurTokenReceived(token);
        else
            logout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem uploadImage = menu.findItem(R.id.upload_button);
        MenuItem logoutButton = menu.findItem(R.id.logout_button);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f;

        if ((f = fm.findFragmentByTag(LOGIN_BUTTON_TAG)) != null && f.isVisible()) {
            if (uploadImage != null)
                uploadImage.setVisible(false);
            if (logoutButton != null)
                logoutButton.setVisible(false);
        } else if ((f = fm.findFragmentByTag(LOGIN_TAG)) != null && f.isVisible()) {
            if (uploadImage != null)
                uploadImage.setVisible(false);
            if (logoutButton != null)
                logoutButton.setVisible(false);
        } else if ((f = fm.findFragmentByTag(GALLERY_TAG)) != null && f.isVisible()) {
            if (uploadImage != null)
                uploadImage.setVisible(true);
            if (logoutButton != null)
                logoutButton.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_button:
                pickImage();
                return true;
            case R.id.trending_button:
                showTrendings();
                return true;
            case R.id.logout_button:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            uploadImage(uri);
        }

    }

    private void saveToken(ImgurToken token) {
        Gson gson = new Gson();
        String authJson = gson.toJson(token);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this );
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SHARED_TOKEN, authJson);
        editor.apply();
    }

    private void clearToken() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(SHARED_TOKEN);
        editor.apply();
    }

    private ImgurToken getToken() {
        ImgurToken token = null;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String authJson = settings.getString(SHARED_TOKEN, null);

        if (!TextUtils.isEmpty(authJson)) {
            Gson gson = new Gson();
            try {
                token = gson.fromJson(authJson, ImgurToken.class);
            } catch (JsonSyntaxException e) {
                Log.e("imgur", "Error while retrieving Token - " + e.toString());
            }
        }
        return token;
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() != 0)
            fm.popBackStackImmediate();
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose image"), PICK_IMAGE);
    }

    private void uploadImage(Uri uri) {
        ImgurToken token = getToken();

        if (token == null)
            logout();
        createImgurClient();
        File file = new File(uri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Log.v("imgur", "uploadFile: uri = "+ uri.getPath()); // TODO remove log
        Log.v("imgur", "uploadFile: file name = "+ file.getName()); // TODO remove log
        Call<BasicResponse> call = client.uploadImage(token.toAuthHeader(), body);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                BasicResponse body = response.body();
                Log.v("imgur", "API CALL SUCCESSFUL");
                if (response.isSuccessful() && body != null && body.getSuccess()) {
                    Log.v("imgur", "Upload was a success");

                    // tell the image gallery to refresh, which will load the new image
                    GalleryFragment gallery = (GalleryFragment) getSupportFragmentManager().findFragmentByTag(GALLERY_TAG);
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

    private void showTrendings() {
        ImgurToken token = getToken();

        if (token == null)
            logout();
        TrendingFragment newFragment = TrendingFragment.newInstance(token.toAuthHeader());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_main, newFragment, TRENDING_TAG);
        transaction.addToBackStack(TRENDING_TAG);
        transaction.commit();
    }

    private void createImgurClient()
    {
        if (client == null)
            client = ApiClient.getClient().create(ApiInterface.class);
    }

    private void logout() {
        clearToken();
        clearBackStack();

        LoginButtonFragment newFragment = new LoginButtonFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_main, newFragment, LOGIN_BUTTON_TAG);
        transaction.commit();
    }

    @Override
    public void onLoginButtonClicked() {
        LoginFragment newFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_main, newFragment, LOGIN_TAG);
        transaction.addToBackStack(LOGIN_TAG);
        transaction.commit();
    }

    @Override
    public void OnImgurTokenReceived(ImgurToken token) {
        saveToken(token);
        clearBackStack();

        GalleryFragment newFragment = GalleryFragment.newInstance(token.toAuthHeader());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_main, newFragment, GALLERY_TAG);
        transaction.commit();
    }

    @Override
    public void onGalleryClicked() {
        Log.v("imgur", "Gallery clicked"); // TODO remove log
    }
}
