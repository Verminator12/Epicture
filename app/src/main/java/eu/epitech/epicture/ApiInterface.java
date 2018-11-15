package eu.epitech.epicture;

import eu.epitech.epicture.model.ImageResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiInterface {
    @GET("account/me/images/0")
    Call<ImageResponse> userGallery(@Header("Client-ID") String ClientID);
}
