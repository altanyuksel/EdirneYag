package RestApi;

import Models.Delivery.Palet;
import Models.Delivery.ResponseDelivery;
import Models.Delivery.User;
import DeliveryGroup.ResponseGroup;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RestApi {
    @GET("api/Ping")
    Call<Boolean> getPing();

    @GET("api/ping/auth")
    Call<User> getAuth(@Header("Authorization") String basicAuth);

    @GET("api/delivery")
    Call<ResponseDelivery> getDelivery(@Header("Authorization") String basicAuth, @Query("type") int type, @Query("deliveryNo") String deliveryNo, @Query("page") int page, @Query("pageSize") int pageSize);

    @GET("api/delivery/BeginDelivery")
    Call<String> setDeliveryStart(@Header("Authorization") String basicAuth, @Query("type") int type, @Query("deliveryNo") String deliveryNo);

    @POST("api/delivery/FinishDeliveryGroup")
    Call<String> setDeliveryFinish(@Header("Authorization") String basicAuth, @Query("type") int type, @Query("deliveryNo") String deliveryNo, @Body RequestBody requestBody);

    @GET("api/delivery/UndoDelivery")
    Call<String> setDeliveryUndo(@Header("Authorization") String basicAuth, @Query("type") int type, @Query("deliveryNo") String deliveryNo);

    @GET("api/delivery/palet")
    Call<Palet> getPalet(@Header("Authorization") String basicAuth, @Query("paletNo") String paletNo);

    @POST("api/delivery/PalletQuantityDelivery")
    Call<String> setDeliveryPalletQuantity(@Header("Authorization") String basicAuth, @Query("type") int type, @Query("deliveryNo") String deliveryNo, @Body RequestBody requestBody);

    @GET("api/deliverygroupproduct")
    Call<ResponseGroup> getDeliveryGroupProduct(@Header("Authorization") String basicAuth, @Query("type") int type, @Query("deliveryNo") String deliveryNo, @Query("page") int page, @Query("pageSize") int pageSize);
}