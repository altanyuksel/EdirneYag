package RestApi;

import android.os.StrictMode;
import com.link.edirneyag.DeliveryActivity;
import java.util.List;

import DeliveryGroup.DeliveryGroup;
import Models.Delivery.Paging;
import Models.Delivery.Palet;
import Models.Delivery.PalletsInfo;
import Models.Delivery.User;
import ServiceSetting.ServiceDefinitions;
import DeliveryGroup.ResponseGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import DeliveryGroup.DeliveryGroupItem;

public class RequestHandler {
    public RequestHandler() {
        ignoreUIThread();
    }
    private DeliveryActivity deliveryActivity;
    public void ignoreUIThread() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public boolean checkService() {
        boolean isServiceOK = false;
        Call<Boolean> call = ManagerAll.getInstance().checkPing();
        Boolean returnedVal = false;
        try {
            returnedVal = call.execute().body();
            if (returnedVal) {
                isServiceOK = true;
            }
        } catch (Exception exception) {
            isServiceOK = false;
        }
        return isServiceOK;
    }

    public boolean checkServiceAuth(String userName, String password) {
        boolean isServiceOK = false;
        if (userName.equals("") || password.equals("")){
            return  isServiceOK;
        }
        Call<User> call = ManagerAll.getInstance().checkAuth(userName,password);
        User returnedVal = null;
        try {
            returnedVal = call.execute().body();
            if (!returnedVal.get_userName().equals("") && (!returnedVal.get_userType().equals(""))) {
                ServiceDefinitions.loginUser.set_userType(returnedVal.get_userType());
                isServiceOK = true;
            }
        } catch (Exception exception) {
            isServiceOK = false;
        }
        return isServiceOK;
    }

    public ResponseGroup getAllDeliveryList(int deliveryType) {
        ResponseGroup responseDelivery = null;
        ResponseGroup tempRes = null;
        int page = 0, pageSize = 100;
        boolean bExist = true;
        Call<ResponseGroup> call;
        String res = "";
//        if (ServiceDefinitions.loginUser.get_userType().equals("Fabrika")){
//            deliveryType = 500;
//        } else if(ServiceDefinitions.loginUser.get_userType().equals("Şube")){
//            deliveryType = 600;
//        }
        try {
            while (bExist){
                call = ManagerAll.getInstance().getDeliveryList(deliveryType,"",page,pageSize);
                tempRes = call.execute().body();
                if(!tempRes.equals(null) || !tempRes.getListItem().equals(null)){
                    if(responseDelivery == null){
                        responseDelivery = tempRes;
                        page++;
                    } else {
                        boolean b= responseDelivery.getListItem().addAll(tempRes.getListItem());
                        int newPageSize = responseDelivery.getListItem().size();
                        responseDelivery.setPaging(new Paging(0,newPageSize));
                        if (tempRes.getPaging().getPageSize() < pageSize){
                            bExist = false;
                        }else{
                            page++;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            res = exception.getMessage();
        }
        return responseDelivery;
    }

    public DeliveryGroup getDelivery(String deliveryNo, int deliveryType) {
        ResponseGroup responseGroup = null;
//        if (ServiceDefinitions.loginUser.get_userType().equals("Fabrika")){
//            deliveryType = 500;
//        } else if(ServiceDefinitions.loginUser.get_userType().equals("Şube")){
//            deliveryType = 600;
//        }
        Call<ResponseGroup> call = ManagerAll.getInstance().getDeliveryListGroup(deliveryType,deliveryNo,0,10);
        String res = "";
        try {
            responseGroup = call.execute().body();
        } catch (Exception exception) {
            res = exception.getMessage();
            return  null;
        }
        if (responseGroup == null || responseGroup.getListItem().size() == 0) return null;
        return responseGroup.getListItem().get(0);
    }

    public void setDeliveryStatus(String orderNo, int status, DeliveryActivity activity, List<DeliveryGroupItem> items, List<PalletsInfo> itemsPallets, int deliveryType) throws Exception {
        deliveryActivity = activity;
        Call<String> call = ManagerAll.getInstance().setDeliveryStatus(deliveryType, orderNo, status, items);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = "";
                if (!response.isSuccessful()) {
                    try {
                        res =  response.errorBody().string();
                        Clear("", res);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    res = response.body();
                    try {
                        setDeliveryPalletQuantity(orderNo, status, activity, itemsPallets);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Clear(res, "");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                try {
                    throw new Exception("İşlem başarısız. Hata: " + t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setDeliveryPalletQuantity(String orderNo, int status, DeliveryActivity activity, List<PalletsInfo> items) throws Exception {
        deliveryActivity = activity;
        int deliveryType = -1;
        if (ServiceDefinitions.loginUser.get_userType().equals("Fabrika")){
            deliveryType = 500;
        } else if(ServiceDefinitions.loginUser.get_userType().equals("Şube")){
            deliveryType = 600;
        }
        Call<String> call = ManagerAll.getInstance().setDeliveryPalletQuantity(deliveryType, orderNo, status, items);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = "";
                if (!response.isSuccessful()) {
                    try {
                        res =  response.errorBody().string();
                        Clear("", res);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                try {
                    throw new Exception("İşlem başarısız. Hata: " + t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void Clear(String docNo, String errorMessage){
        deliveryActivity.clearAll(docNo, errorMessage);
    }
    public Palet getPalet(String paletNo) {
        Palet resPalet = null;
        Call<Palet> call = ManagerAll.getInstance().getPalet(paletNo);
        String res = "";
        try {
            resPalet = call.execute().body();
        } catch (Exception exception) {
            res = exception.getMessage();
            return  null;
        }
        return resPalet;
    }
}