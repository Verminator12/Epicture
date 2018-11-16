package eu.epitech.epicture;

import eu.epitech.epicture.model.BasicResponse;
import eu.epitech.epicture.model.ImageResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @GET("account/me/images/0")
    Call<ImageResponse> userGallery(@Header("Authorization") String Authorization);

    @Multipart
    @POST("image")
    Call<BasicResponse> uploadImage(@Header("Authorization") String Authorization, @Part MultipartBody.Part image);
}
