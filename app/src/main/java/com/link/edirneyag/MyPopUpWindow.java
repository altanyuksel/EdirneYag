package com.link.edirneyag;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.net.CookieManager;

import DeliveryGroup.ResponseGroup;
import adapters.AdapterRVDelivery;
import RestApi.RequestHandler;
import ServiceSetting.ServiceDefinitions;

class MyPopUpWindow {
    //region $MEMBERS
    private final String TAG = "DELIVERY_SELECT";
    private static ServiceDefinitions serviceDefinitions;
    private PopupWindow popupWindow;
    private ResponseGroup response;
    private ProgressDialog progressDialog;
    private AdapterRVDelivery mAdapterRVDelivery;
    private RecyclerView recViewDeliveryList;
    private View mainView;
    private LayoutInflater inflater;
    private ImageButton btnClose;
    private DeliveryActivity mainAct;
    private RequestHandler requestHandler;
    private TextView lblDeliveryNo;
    private TextView lblStatus;
    private TextView lblDelDate;
    private TextView lblWaybillNo;
    private AlertDialog.Builder errDialog;
    private int docType;
    //endregion

    //region $METHODS
    public MyPopUpWindow(RequestQueue mReqQueue, ServiceDefinitions serviceDefinitions, CookieManager cookieManage, final View mainView, DeliveryActivity mainAct, int docType) {
        this.serviceDefinitions = serviceDefinitions;
        this.mainView = mainView;
        this.mainAct = mainAct;
        this.docType = docType;
    }
    private void initErrorDialog() {
        errDialog = new AlertDialog.Builder(mainAct);
    }
    public void showPopupWindow() {
        //Create a View object yourself through inflater
        requestHandler = new RequestHandler();
        inflater = (LayoutInflater) mainView.getContext().getSystemService(mainView.getContext().LAYOUT_INFLATER_SERVICE);
        initViews();
        initDefinations();
        initErrorDialog();
        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;
        Display disp = mainAct.getWindowManager().getDefaultDisplay();
        Point dimensions = new Point();
        disp.getSize(dimensions);
        int width = dimensions.x - 50;
        int height = dimensions.y - 500;
        //Create a window with our parameters
        popupWindow = new PopupWindow(mainView, width, height, focusable);
        //Set the location of the window on the screen
        popupWindow.showAtLocation(mainView, Gravity.CENTER, 0, 0);
        //Initialize the elements of our window, install the handler
        fillList();
    }
    private void initViews() {
        mainView = inflater.inflate(R.layout.activity_delivery_select, null);
        recViewDeliveryList = mainView.findViewById(R.id.recViewDelivery);
        btnClose = mainView.findViewById(R.id.btnClose);
        lblDeliveryNo = mainView.findViewById(R.id.lblDeliveryNo);
        lblStatus = mainView.findViewById(R.id.lblStatus);
        lblDelDate = mainView.findViewById(R.id.lblDeliveryDate);
        initProgressDialog();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String altan2 = "";
            }
        });
        recViewDeliveryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void initDefinations() {
        response = new ResponseGroup();
    }
    private void fillList() {
        if (!serviceDefinitions.equals(null)) {
            showProgressDialog(mainView.getContext().getString(R.string.list_count_order), "");
            try {
                if (!requestHandler.checkService()) {
                    hideProgressDialog();
                    showErrorDialog(mainAct.getString(R.string.error_service));
                    return;
                }
                if (!requestHandler.checkServiceAuth(ServiceDefinitions.loginUser.get_userName(), ServiceDefinitions.loginUser.get_password())) {
                    showErrorDialog(mainAct.getString(R.string.error_credential));
                    return;
                }
 //                response = requestHandler.getAllDeliveryList();
                response = requestHandler.getAllDeliveryList(docType);
                setDeliveryListView();
                hideProgressDialog();
            } catch (Exception exception) {
                Log.d(TAG, mainView.getContext().getResources().getString(R.string.error) + exception.getMessage());
            }
        }
    }
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(mainView.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }
    private void showProgressDialog(String title, String message) {
        progressDialog.setTitle(title); // Setting Title
        progressDialog.setMessage(message); // Setting Message
        progressDialog.show(); // Display Progress Dialog
    }
    private void hideProgressDialog() {
        progressDialog.dismiss();
    }
    private void setDeliveryListView() {
        mAdapterRVDelivery = new AdapterRVDelivery(mainView.getContext(), response, mainAct);
        recViewDeliveryList.setAdapter(mAdapterRVDelivery);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recViewDeliveryList.setLayoutManager(linearLayoutManager);
    }
    public void dismissPopupWindow() {
        popupWindow.dismiss();
    }
    private void showErrorDialog(String message) {
        errDialog.setTitle(mainAct.getString(R.string.error));
        errDialog.setMessage(message);
        errDialog.setNeutralButton(mainAct.getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        errDialog.show();
    }
    //endregion
}