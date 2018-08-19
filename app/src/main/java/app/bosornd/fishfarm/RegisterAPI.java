package app.bosornd.fishfarm;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by jun on 17. 9. 14.
 */

public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/login/insert.php")
    public void insertUser(
            @Field("name") String name,
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("fishfarm") String fishfarm,
            Callback<Response> callback);
}