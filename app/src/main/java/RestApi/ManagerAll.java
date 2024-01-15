package RestApi;

import android.util.Base64;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

import Models.Delivery.Palet;
import Models.Delivery.PalletsInfo;
import Models.Delivery.User;
import ServiceSetting.ServiceDefinitions;
import DeliveryGroup.ResponseGroup;
import DeliveryGroup.DeliveryGroupItem;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ManagerAll extends BaseManager {
    private static ManagerAll mInstance = new ManagerAll();
    public static synchronized ManagerAll getInstance() {
        return mInstance;
    }
    Gson gson = new Gson();

    public Call<Boolean> checkPing() {
        Call<Boolean> call = getRestApiClient().getPing();
        return call;
    }
    public Call<User> checkAuth(String userName, String password) {
        String basicAuth = "";
        try {
            byte[] encrpt= (userName+":"+password).getBytes("UTF-8");
            basicAuth = Base64.encodeToString(encrpt, Base64.NO_WRAP);
//            Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP)
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            basicAuth = "";
        }
        Call<User> call = getRestApiClient().getAuth("Basic " + basicAuth);
        return call;
    }

    public Call<ResponseGroup> getDeliveryList(int type, String deliveryNo, int page, int pageSize) {
        String basicAuth = "";
        try {
            byte[] encrpt= (ServiceDefinitions.loginUser.get_userName() +":"+ ServiceDefinitions.loginUser.get_password()).getBytes("UTF-8");
            basicAuth = Base64.encodeToString(encrpt, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            basicAuth = "";
        }
        basicAuth = "Basic " + basicAuth;

        Call<ResponseGroup> call = getRestApiClient().getDeliveryGroupProduct(basicAuth, type,deliveryNo,page,pageSize);
        if (type == 500){
            call = getRestApiClient().getDeliveryGroupProduct(basicAuth, type,deliveryNo,page,pageSize);
        } else if (type == 55) {
            call = getRestApiClient().getStockGift(basicAuth, deliveryNo,page,pageSize);
        } else if (type == 52) {
            call = getRestApiClient().getStockInternalUse(basicAuth, deliveryNo,page,pageSize);
        } else if (type == 1) {
            call = getRestApiClient().getStockRetailSale(basicAuth, deliveryNo,page,pageSize);
        }
        return call;
    }

    public Call<ResponseGroup> getDeliveryListGroup(int type, String deliveryNo, int page, int pageSize) {
        String basicAuth = "";
        try {
            byte[] encrpt= (ServiceDefinitions.loginUser.get_userName() +":"+ ServiceDefinitions.loginUser.get_password()).getBytes("UTF-8");
            basicAuth = Base64.encodeToString(encrpt, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            basicAuth = "";
        }
        basicAuth = "Basic " + basicAuth;

        Call<ResponseGroup> call = null;
        if (type == 500){
            call = getRestApiClient().getDeliveryGroupProduct(basicAuth, type,deliveryNo,page,pageSize);
        } else if(type == 55){
            call = getRestApiClient().getStockGift(basicAuth, deliveryNo,page,pageSize);
        } else if(type == 52){
            call = getRestApiClient().getStockInternalUse(basicAuth, deliveryNo,page,pageSize);
        } else if(type == 1){
            call = getRestApiClient().getStockRetailSale(basicAuth, deliveryNo,page,pageSize);
        }
        return call;
    }

    public Call<String> setDeliveryStatus(int type, String deliveryNo, int status, List<DeliveryGroupItem> items) {
        String basicAuth = "";
        try {
            byte[] encrpt= (ServiceDefinitions.loginUser.get_userName() +":"+ ServiceDefinitions.loginUser.get_password()).getBytes("UTF-8");
            basicAuth = Base64.encodeToString(encrpt, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            basicAuth = "";
        }
        basicAuth = "Basic " + basicAuth;

        Call<String> call = null;
        if (status == 0){
            if (type == 500){
                call = getRestApiClient().setDeliveryUndo(basicAuth, type, deliveryNo);
            } else if (type == 55) {
                call = getRestApiClient().setGiftUndo(basicAuth, deliveryNo);
            } else if (type == 52) {
                call = getRestApiClient().setInernalUseUndo(basicAuth, deliveryNo);
            } else if (type == 1) {
                call = getRestApiClient().setRetailSaleUndo(basicAuth, deliveryNo);
            }
        }else if(status == 1) {
            if (type == 500){
                call = getRestApiClient().setDeliveryStart(basicAuth, type, deliveryNo);
            } else if (type == 55) {
                call = getRestApiClient().setGiftStart(basicAuth, deliveryNo);
            } else if (type == 52) {
                call = getRestApiClient().setInternalUseStart(basicAuth, deliveryNo);
            } else if (type == 1) {
                call = getRestApiClient().setRetailsaleStart(basicAuth, deliveryNo);
            }
        } else if(status == 2) {
            gson = new Gson();
            String json = gson.toJson(items);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

            if (type == 500){
                call = getRestApiClient().setDeliveryFinish(basicAuth, type, deliveryNo, requestBody);
            } else if (type == 55) {
                call = getRestApiClient().setGiftFinish(basicAuth, deliveryNo, requestBody);
            } else if (type == 52) {
                call = getRestApiClient().setInternalUseFinish(basicAuth, deliveryNo, requestBody);
            } else if (type == 1) {
                call = getRestApiClient().setRetailSaleFinish(basicAuth, deliveryNo, requestBody);
            }
        }
        return call;
    }
    public Call<String> setDeliveryPalletQuantity(int type, String deliveryNo, int status, List<PalletsInfo> items) {
        String basicAuth = "";
        try {
            byte[] encrpt= (ServiceDefinitions.loginUser.get_userName() +":"+ ServiceDefinitions.loginUser.get_password()).getBytes("UTF-8");
            basicAuth = Base64.encodeToString(encrpt, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            basicAuth = "";
        }
        basicAuth = "Basic " + basicAuth;

        Call<String> call = null;
        if (status == 2) {
            gson = new Gson();
            String json = gson.toJson(items);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
            call = getRestApiClient().setDeliveryPalletQuantity(basicAuth, type, deliveryNo, requestBody);
        }
        return call;
    }
    public Call<Palet> getPalet(String paletNo) {
        String basicAuth = "";
        try {
            byte[] encrpt= (ServiceDefinitions.loginUser.get_userName() +":"+ ServiceDefinitions.loginUser.get_password()).getBytes("UTF-8");
            basicAuth = Base64.encodeToString(encrpt, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            basicAuth = "";
        }
        basicAuth = "Basic " + basicAuth;

        Call<Palet> call = getRestApiClient().getPalet(basicAuth, paletNo);
        return call;
    }
}
