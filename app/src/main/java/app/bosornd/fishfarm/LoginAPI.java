package app.bosornd.fishfarm;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by jun on 17. 9. 14.
 */

public interface LoginAPI {
    @FormUrlEncoded
    @POST("/login/login.php")
    public void checkUser(
            @Field("username") String username,
            @Field("password") String password,
            Callback<Response> callback);
}
