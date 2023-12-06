package com.link.edirneyag;

import static Utils.UtilsCommon.KEY_PASSWORD;
import static Utils.UtilsCommon.KEY_USERNAME;
import static Utils.UtilsCommon.PREF_CREDENTIAL;
import static Utils.UtilsCommon.serviceDefinitions;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;

import Models.Delivery.Palet;
import adapters.AdapterRVDeliveryItem;
import Models.Delivery.Delivery;
import RestApi.RequestHandler;
import ServiceSetting.ServiceDefinitions;
import Utils.DecimalInputFilter;

public class DeliveryActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    //region $MEMBERS
    private static final String TAG = "DeliveryActivity";
    private RequestQueue mRequestQueue;
    private Context mContext;
    private ProgressDialog progressDialog;
    //    private Button btnAdd, btnRemove, btnStart, btnUndo, btnFinish, btnUpdate, btnDeliveryNo, btnChangeUser;
    private Button btnStart, btnUndo, btnFinish, btnUpdate, btnDeliveryNo, btnChangeUser;
    private EditText editNumber;

    AlertDialog.Builder errDialog;
    private CookieManager cookieManage; //Yeni nesil servisi cookie kullandığından cookie set ediyoruz. Bunu yapmazsak çalışmıyor.
    private MyPopUpWindow popUpClass;
    public RequestHandler requestHandler;

    private SurfaceView surfaceViewDelivery;
    private SurfaceHolder mSurfaceHolder;
    private TextView txtBarcodeValue, txtVehicle, txtUsername, txtMalAdi, txtPaletMiktar;

    public static String strCountDeliveryNo, barcodeString;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private ArrayList<Integer> selectedItemList; //Okunan barkodların index'lerini tutuyor.
    private Integer selectedIndex; //Okunan barkodların index'lerini tutuyor.
    private AdapterRVDeliveryItem mAdapterRVDeliveryItem;
    private Delivery response;
    private RecyclerView recViewDeliveryList;
    private boolean canReadBarcode = true;
    private static final String KEY_DELIVERY = "KEY_DELIVERY";
    private static final String KEY_DELIVERYITEMS_ADAPTER = "KEY_DELIVERYITEMS_ADAPTER";
    private static final String KEY_DELIVERYITEMS_SELECTED = "KEY_DELIVERYITEMS_SELECTED";
    private static final String KEY_DELIVERYNO = "KEY_DELIVERYNO";
    private Switch cameraSwitch;
    private boolean isCameraRunning = true;
    //endregion

    //region $ACTIVITY OVERRIDE METHODS
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initDefinitions(savedInstanceState);
        checkService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeDetectorsAndSources();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.exit))
                .setCancelable(false)
                .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton(getApplicationContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //endregion

    //region $METHODS
    private void initViews() {
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_delivery);

        btnDeliveryNo = findViewById(R.id.txtDeliveryNo);
        btnStart = findViewById(R.id.btnStart);
        btnUndo = findViewById(R.id.btnUndo);
        btnFinish = findViewById(R.id.btnFinish);
        surfaceViewDelivery = findViewById(R.id.surfaceViewDelivery);
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        btnUpdate = findViewById(R.id.btnUpdate);
        txtVehicle = findViewById(R.id.txtVehicle);
        txtUsername = findViewById(R.id.txtUsername);
        recViewDeliveryList = findViewById(R.id.recViewDeliveryItemList);
        btnChangeUser = findViewById(R.id.btnChangeUser);
        cameraSwitch = findViewById(R.id.swCamera);
        txtMalAdi = findViewById(R.id.txtMalAdi);
        txtPaletMiktar = findViewById(R.id.txtPaletMiktar);
        editNumber = findViewById(R.id.editSayim);

        mSurfaceHolder = surfaceViewDelivery.getHolder();

        initProgressDialog();
        initErrorDialog();

        btnStart.setOnClickListener(this::onClickBtnStart);
        btnUndo.setOnClickListener(this::onClickBtnUndo);
        btnFinish.setOnClickListener(this::onClickBtnFinish);
//        btnAdd.setOnClickListener(this::onClickBtnAdd);
//        btnRemove.setOnClickListener(this::onClickBtnRemove);
        btnDeliveryNo.setOnClickListener(this::onClickBtnDeliveryNo);
        btnUpdate.setOnClickListener(this::onClickBtnUpdate);
        btnChangeUser.setOnClickListener(this::onClickBtnChangeUser);

//        btnStart.setBackgroundColor(Color.RED);
//        btnUndo.setBackgroundColor(Color.RED);
//        btnFinish.setBackgroundColor(Color.RED);

        btnStart.setEnabled(false);
        btnUndo.setEnabled(false);
        btnFinish.setEnabled(false);

        txtUsername.setText(ServiceDefinitions.loginUser.get_userName());

        editNumber.setFilters(new InputFilter[]{new DecimalInputFilter(3)});
        editNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    String text = s.toString();
                    int dotIndex = text.indexOf(".");
                    if (dotIndex != -1 && text.length() - dotIndex > 4) {
                        // Noktadan sonra 3 haneli sayılarla sınırla
                        editNumber.setText(text.substring(0, dotIndex + 4));
                        editNumber.setSelection(editNumber.getText().length());
                    }
                }
            }
        });

        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Switch açıkken, kamerayı başlat
                    startCamera();
                } else {
                    // Switch kapalıysa, kamerayı durdur
                    stopCamera();
                }
            }
        });
    }

    private void initDefinitions(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);
        cookieManage = new CookieManager();
        CookieHandler.setDefault(cookieManage);
        requestHandler = new RequestHandler();
        selectedItemList = new ArrayList<>();
        selectedIndex = -1;
        if (savedInstanceState != null) {
            // Eğer önceki bir durum kaydedilmişse, verileri geri yükle
            strCountDeliveryNo = savedInstanceState.getString(KEY_DELIVERYNO);
            response = savedInstanceState.getParcelable(KEY_DELIVERY);
            setDeliveryListView();
            mAdapterRVDeliveryItem.listDelivery = savedInstanceState.getParcelableArrayList(KEY_DELIVERYITEMS_ADAPTER);
            selectedItemList = savedInstanceState.getIntegerArrayList(KEY_DELIVERYITEMS_SELECTED);
            mAdapterRVDeliveryItem.listReadedBarcodeList = selectedItemList;
//            mAdapterRVDeliveryItem.notifyDataSetChanged();
            fillList(response);
        }
    }

    private void checkService() {
        try {
            if (serviceDefinitions == null) {
                showErrorDialog(mContext.getString(R.string.error_config));
            }
            if (!requestHandler.checkService()) {
                showErrorDialog(mContext.getString(R.string.error_service));
            }
            if (!requestHandler.checkServiceAuth(ServiceDefinitions.loginUser.get_userName(), ServiceDefinitions.loginUser.get_password())) {
                showErrorDialog(mContext.getString(R.string.error_credential));
            }
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void startDelivery(String deliveryNo) throws Exception {
        if (deliveryNo != null && deliveryNo != "") {
            requestHandler.setDeliveryStatus(deliveryNo, 1, this, response.get_deliveryItem());
            response.set_status(1);
            selectedItemList = new ArrayList<>();
            showToastMessage(getString(R.string.start_delivery), Toast.LENGTH_LONG, Gravity.TOP);
        }
    }

    private void undoDelivery(String deliveryNo) throws Exception {
        if (deliveryNo != null && deliveryNo != "") {
            requestHandler.setDeliveryStatus(deliveryNo, 0, this, response.get_deliveryItem());
            response.set_status(1);
            selectedItemList = new ArrayList<>();
            showToastMessage(getString(R.string.undo_delivery), Toast.LENGTH_LONG, Gravity.TOP);
        }
    }

    private void finishDelivery(String deliveryNo) throws Exception {
        if (response.get_status() == 1) {
            requestHandler.setDeliveryStatus(deliveryNo, 2, this, response.get_deliveryItem());
        } else {
            showErrorDialog(getString(R.string.error_finish));
        }
    }

    private void initializeDetectorsAndSources() {
        barcodeString = "";
        Log.d(TAG, "initializeDetectorsAndSources: Barcode scan is start.");
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();
        mSurfaceHolder.addCallback(this);
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                if (!barcodeString.equals("")) {
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.barcode_read), Toast.LENGTH_SHORT).show();
                    showToastMessage(getString(R.string.barcode_read), Toast.LENGTH_LONG, Gravity.TOP);
                }
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    if (canReadBarcode) {
                        canReadBarcode = false;
                        txtBarcodeValue.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                canReadBarcode = true;
                                if (response != null && response.get_status() == 1) {
                                    barcodeString = barcodes.valueAt(0).displayValue;
                                    txtBarcodeValue.setText(barcodeString);
                                    Log.d(TAG, "initializeDetectorsAndSources: barcode was scanned. scanned string : " + barcodeString);
                                    setDeliveryRows(barcodeString);
                                    showToastMessage(barcodeString, Toast.LENGTH_LONG, Gravity.TOP);
                                }
                            }
                        }, 2000);
                    }
                }
            }
        });
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(DeliveryActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    private void initErrorDialog() {
        errDialog = new AlertDialog.Builder(DeliveryActivity.this);
    }

    private void showProgressDialog(String title, String message) {
        progressDialog.setTitle(title); // Setting Title
        progressDialog.setMessage(message); // Setting Message
        progressDialog.show(); // Display Progress Dialog
    }

    private void showErrorDialog(String message) {
        errDialog.setTitle(getResources().getString(R.string.error));
        errDialog.setMessage(message);
        errDialog.setNeutralButton(getApplicationContext().getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        errDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    public void setDeliveryRows(String readedBarcode) {
        Palet palet = requestHandler.getPalet(readedBarcode);
        if (palet != null){
            txtMalAdi.setText(palet.getMateryalAdi());
            txtPaletMiktar.setText(String.valueOf(palet.getMiktar()));
            editNumber.setText("0");
        }else{
            showToastMessage(getString(R.string.couldnot_find_pallet), Toast.LENGTH_LONG, Gravity.TOP);
        }

        //        if (!response.equals(null)){
//            for (DeliveryItem item: response.get_deliveryItem()) {
//                for (String barcode: item.getMateryalKodu()) {
//                    if (barcode.equalsIgnoreCase(readedBarcode)){
//                        int index = response.get_deliveryItem().indexOf(item);
//                        if (!selectedItemList.contains(index)) {
//                            selectedItemList.add(index);
//                        }
//                        selectedIndex = index;
//                        if (miktar == 1) {
//                            miktar += mAdapterRVDeliveryItem.listDelivery.get(selectedIndex).getMiktar2();
//                        }
//
//                        if (mAdapterRVDeliveryItem.listDelivery.get(selectedIndex).getMiktar() >= miktar){
//                            SoundManager.playSound(this, R.raw.beep);
//                            mAdapterRVDeliveryItem.setSayimMiktar(selectedIndex, miktar, seriNo);
//                            editNumber.setText("0.000");
//                        }
//                        mAdapterRVDeliveryItem.listReadedBarcodeList = selectedItemList;
//                        for (int i = 0; i <= mAdapterRVDeliveryItem.listDelivery.size()-1; i++){
//                            mAdapterRVDeliveryItem.notifyItemChanged(i);
//                        }
//                        mAdapterRVDeliveryItem.notifyDataSetChanged();
//                        return true;
//                    }
//                }
//            }
//        }
    }

    private void openPopUpWindow(View v) {
        popUpClass = new MyPopUpWindow(mRequestQueue, serviceDefinitions, cookieManage, v, DeliveryActivity.this);
        popUpClass.showPopupWindow();
    }

    public void closePopupWindow(String emirNo) {
        popUpClass.dismissPopupWindow();
        strCountDeliveryNo = emirNo;
        fillList(null);
    }

    private void showToastMessage(String message, int duration, int gravity) {
        Toast toast = Toast.makeText(getApplicationContext(), message, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    private void setDeliveryListView() {
        mAdapterRVDeliveryItem = new AdapterRVDeliveryItem(response.get_deliveryItem(), DeliveryActivity.this);
        recViewDeliveryList.setAdapter(mAdapterRVDeliveryItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recViewDeliveryList.setLayoutManager(linearLayoutManager);
    }

    private void fillList(Delivery res) {
        try {
            if (res == null) {
                response = requestHandler.getDelivery(strCountDeliveryNo);
                selectedItemList = new ArrayList<>();
                selectedIndex = -1;
                setDeliveryListView();

                if (!requestHandler.checkService()) {
                    hideProgressDialog();
                    showErrorDialog(DeliveryActivity.this.getString(R.string.error_service));
                    return;
                }
                if (!requestHandler.checkServiceAuth(ServiceDefinitions.loginUser.get_userName(), ServiceDefinitions.loginUser.get_password())) {
                    showErrorDialog(DeliveryActivity.this.getString(R.string.error_credential));
                    return;
                }
            }
            btnDeliveryNo.setText(response.get_deliveryNo());
            String info = "Plaka: " + response.get_plate();
            info += ", Soför Ad - Soyad: " + response.get_driverName() + " " + response.get_driverSurname();
            info += ", Soför TCKN: " + response.get_driverTCKN();
            info += ", Soför Tel: " + response.get_driverPhone();
            txtVehicle.setText(info);
            hideProgressDialog();
            if (response.get_status() == 0) {
                btnStart.setBackgroundColor(Color.GREEN);
//                btnUndo.setBackgroundColor(Color.RED);
//                btnFinish.setBackgroundColor(Color.RED);

                btnStart.setEnabled(true);
                btnUndo.setEnabled(false);
                btnFinish.setEnabled(false);
            } else if (response.get_status() == 1) {
//                btnStart.setBackgroundColor(Color.RED);
                btnUndo.setBackgroundColor(Color.GREEN);
                btnFinish.setBackgroundColor(Color.GREEN);

                btnStart.setEnabled(false);
                btnUndo.setEnabled(true);
                btnFinish.setEnabled(true);
            }
        } catch (Exception exception) {
            Log.d(TAG, getString(R.string.error) + exception.getMessage());
        }
    }

    public void clearAll(String waybillNo, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.finish_delivery) + "(" + getString(R.string.waybillno) + ":" + waybillNo + ")";
        if (!errorMessage.equals("")) {
            message = getString(R.string.error) + errorMessage;
        }
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNeutralButton(getApplicationContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    selectedItemList = new ArrayList<>();
                    clearPage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void clearPage() {
        btnDeliveryNo.setText(getString(R.string.delivery_no));
        txtVehicle.setText("");
        strCountDeliveryNo = "";
        selectedItemList.clear();
        mAdapterRVDeliveryItem.listReadedBarcodeList.clear();
        response = new Delivery();
        selectedIndex = -1;

        int size = mAdapterRVDeliveryItem.listDelivery.size();
        mAdapterRVDeliveryItem.listDelivery.clear();
        mAdapterRVDeliveryItem.notifyItemRangeRemoved(0, size);

//        btnStart.setBackgroundColor(Color.RED);
//        btnUndo.setBackgroundColor(Color.RED);
//        btnFinish.setBackgroundColor(Color.RED);

        btnStart.setEnabled(false);
        btnUndo.setEnabled(false);
        btnFinish.setEnabled(false);
    }

    private String controlConditions() {
        String errMessage = "";
        double toplamSayimMiktar = 0;
        for (int i = 0; i <= response.get_deliveryItem().size() - 1; i++) {
            toplamSayimMiktar += response.get_deliveryItem().get(i).getMiktar2();
        }
//        if (selectedItemList.size() != response.get_deliveryItem().size()){
//            errMessage = "Tüm satırlar okutulmalıdır.";
//        }
        if (toplamSayimMiktar <= 0) {
            errMessage = "En az bir satır okutulmalıdır.";
        }
        return errMessage;
    }
    //endregion

    //region $SURFACE OVERRIDE METHODS
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (ActivityCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(mSurfaceHolder);
            } else {
                ActivityCompat.requestPermissions(DeliveryActivity.this, new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            if (ActivityCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(mSurfaceHolder);
            } else {
                ActivityCompat.requestPermissions(DeliveryActivity.this, new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cameraSource.stop();
        cameraSource.release();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Verileri kaydet
        super.onSaveInstanceState(outState);
        if (response != null) {
            outState.putParcelable(KEY_DELIVERY, response);
            outState.putIntegerArrayList(KEY_DELIVERYITEMS_SELECTED, selectedItemList);
            outState.putParcelableArrayList(KEY_DELIVERYITEMS_ADAPTER, (ArrayList<? extends Parcelable>) mAdapterRVDeliveryItem.listDelivery);
            outState.putString(KEY_DELIVERYNO, strCountDeliveryNo);
        }
    }
    //endregion

    //region $EVENTS
    public void onClickBtnDeliveryNo(View v) {
        if (btnDeliveryNo.getId() == v.getId()) {
            showProgressDialog("", getApplicationContext().getString(R.string.read));
            openPopUpWindow(v);
            hideProgressDialog();
        }
    }

    public void onClickBtnStart(View v) {
        try {
            if (response == null || response.get_deliveryNo() == "") {
                showErrorDialog(getString(R.string.error_select_delivery));
            } else if (response.get_status() == 0 || response.get_status() == -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.is_delivery_start));
                builder.setCancelable(false);
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            startDelivery(strCountDeliveryNo);
                            selectedIndex = -1;
//                            btnStart.setBackgroundColor(Color.RED);
                            btnUndo.setBackgroundColor(Color.GREEN);
                            btnFinish.setBackgroundColor(Color.GREEN);

                            btnStart.setEnabled(false);
                            btnUndo.setEnabled(true);
                            btnFinish.setEnabled(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(getApplicationContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
            String a = e.getMessage();
        }
    }

    public void onClickBtnUndo(View v) {
        try {
            if (response == null || response.get_deliveryNo() == "") {
                showErrorDialog(getString(R.string.error_select_delivery));
            } else if (response.get_status() == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.is_delivery_undo));
                builder.setCancelable(false);
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            undoDelivery(strCountDeliveryNo);
                            selectedIndex = -1;
                            clearPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(getApplicationContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
            String a = e.getMessage();
        }
    }

    public void onClickBtnFinish(View v) {
        if (response != null && response.get_status() == 1) {
            String errMessage = controlConditions();
            if (errMessage.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.is_delivery_finish));
                builder.setCancelable(false);
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            finishDelivery(strCountDeliveryNo);
                            response.set_status(2);
                            selectedIndex = -1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(getApplicationContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                showErrorDialog(errMessage);
            }
        } else {
            showErrorDialog(getString(R.string.error_finish));
        }
    }

    public void onClickBtnUpdate(View v) {
        try {
            float dNumber = 0;
            if (!String.valueOf(editNumber.getText()).equals("")) {
                dNumber = Float.valueOf(String.valueOf(editNumber.getText()));

                if (!response.equals(null)) {
                    mAdapterRVDeliveryItem.setSayimMiktar(selectedIndex, dNumber, "");
                    editNumber.setText("0.000");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickBtnChangeUser(View v) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.logout_user));
            builder.setCancelable(false);
            builder.setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    try {
                        DeleteUser();
                        startActivity(new Intent(DeliveryActivity.this, MainActivity.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton(getApplicationContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteUser() {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_CREDENTIAL, MODE_PRIVATE).edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    private void startCamera() {
        // Kamera başlatma kodları buraya eklenecek
        if (!isCameraRunning) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cameraSource.start(mSurfaceHolder);
                isCameraRunning = true;
            } catch (IOException e) {
                e.printStackTrace();
                // Hata durumunu ele alabilirsiniz
            }
        }
    }

    private void stopCamera() {
        // Kamera durdurma kodları buraya eklenecek
        if (isCameraRunning) {
            cameraSource.stop();
            isCameraRunning = false;
        }
    }
    //endregion
}