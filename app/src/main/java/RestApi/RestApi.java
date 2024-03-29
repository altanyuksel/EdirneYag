package RestApi;

import Models.Delivery.Palet;
import Models.Delivery.User;
import DeliveryGroup.ResponseGroup;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {
    @GET("api/Ping")
    Call<Boolean> getPing();

    @GET("api/ping/auth")
    Call<User> getAuth(@Header("Authorization") String basicAuth);

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
    @GET("api/stock/gift")
    Call<ResponseGroup> getStockGift(@Header("Authorization") String basicAuth, @Query("documentNo") String documentNo, @Query("page") int page, @Query("pageSize") int pageSize);
    @GET("api/stock/internaluse")
    Call<ResponseGroup> getStockInternalUse(@Header("Authorization") String basicAuth, @Query("documentNo") String documentNo, @Query("page") int page, @Query("pageSize") int pageSize);
    @GET("api/stock/retailsale")
    Call<ResponseGroup> getStockRetailSale(@Header("Authorization") String basicAuth, @Query("documentNo") String documentNo, @Query("page") int page, @Query("pageSize") int pageSize);
    @GET("api/stock/BeginGift")
    Call<String> setGiftStart(@Header("Authorization") String basicAuth, @Query("documentno") String documentNo);
    @GET("api/stock/Begininternaluse")
    Call<String> setInternalUseStart(@Header("Authorization") String basicAuth, @Query("documentno") String documentNo);
    @GET("api/stock/Beginretailsale")
    Call<String> setRetailsaleStart(@Header("Authorization") String basicAuth, @Query("documentno") String documentNo);
    @POST("api/stock/FinishGift")
    Call<String> setGiftFinish(@Header("Authorization") String basicAuth, @Query("documentNo") String documentNo, @Body RequestBody requestBody);
    @POST("api/stock/FinishInternalUse")
    Call<String> setInternalUseFinish(@Header("Authorization") String basicAuth, @Query("documentNo") String documentNo, @Body RequestBody requestBody);
    @POST("api/stock/FinishRetailSale")
    Call<String> setRetailSaleFinish(@Header("Authorization") String basicAuth, @Query("documentNo") String documentNo, @Body RequestBody requestBody);
    @GET("api/stock/UndoGift")
    Call<String> setGiftUndo(@Header("Authorization") String basicAuth, @Query("documentNo") String deliveryNo);
    @GET("api/stock/UndoInternalUse")
    Call<String> setInernalUseUndo(@Header("Authorization") String basicAuth, @Query("documentNo") String deliveryNo);
    @GET("api/stock/UndoRetailSale")
    Call<String> setRetailSaleUndo(@Header("Authorization") String basicAuth, @Query("documentNo") String deliveryNo);
}