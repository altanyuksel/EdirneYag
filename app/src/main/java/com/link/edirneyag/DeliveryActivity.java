package com.link.edirneyag;

import static Utils.UtilsCommon.KEY_PASSWORD;
import static Utils.UtilsCommon.KEY_USERNAME;
import static Utils.UtilsCommon.PREF_CREDENTIAL;
import static Utils.UtilsCommon.serviceDefinitions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
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
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import DeliveryGroup.DeliveryGroup;
import Models.Delivery.Palet;
import Models.Delivery.PalletsInfo;
import ServiceSetting.SoundManager;
import adapters.AdapterRVDeliveryItem;
import DeliveryGroup.DeliveryGroupItem;
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
//    private Button btnStart, btnUndo, btnFinish, btnUpdate, btnDeliveryNo, btnChangeUser, btnPalet, btnClear, btnPalet2;
    private Button btnStart, btnUndo, btnFinish, btnUpdate, btnDeliveryNo;
    private EditText editNumber, editBarcode;

    AlertDialog.Builder errDialog;
    private CookieManager cookieManage; //Yeni nesil servisi cookie kullandığından cookie set ediyoruz. Bunu yapmazsak çalışmıyor.
    private MyPopUpWindow popUpClass;
    public RequestHandler requestHandler;
    private SurfaceHolder mSurfaceHolder;
    private TextView txtBarcodeValue, txtMalAdi, txtPaletMiktar;
    private WebView webViewVehicle;
    public static String strCountDeliveryNo, barcodeString;
    public static Palet readedPalet;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private ArrayList<Integer> selectedItemList; //Okunan barkodların index'lerini tutuyor.
    private Integer selectedIndex; //Okunan barkodların index'lerini tutuyor.
    private AdapterRVDeliveryItem mAdapterRVDeliveryItem;
    private DeliveryGroup response;
    private RecyclerView recViewDeliveryList;
    private boolean canReadBarcode = true;
    private static final String KEY_DELIVERY = "KEY_DELIVERY";
    private static final String KEY_DELIVERYITEMS_ADAPTER = "KEY_DELIVERYITEMS_ADAPTER";
    private static final String KEY_DELIVERYITEMS_SELECTED = "KEY_DELIVERYITEMS_SELECTED";
    private static final String KEY_DELIVERYNO = "KEY_DELIVERYNO";
    private static final String KEY_SAVE_DELIVERYNO = "KEY_SAVE_DELIVERYNO";
    private static final String KEY_SAVE_RESPONSE = "KEY_SAVE_RESPONSE";
    private static final String KEY_SAVE_ADAPTER_LIST = "KEY_SAVE_ADAPTER_LIST";
    private static final String KEY_SAVE_SELECTEDITEMLIST = "KEY_SAVE_SELECTEDITEMLIST";
    private static final String KEY_SAVE_PALLETS_INFO = "KEY_SAVE_PALLETS_INFO";
    private Switch cameraSwitch;
    private boolean isCameraRunning = true;
    public List<PalletsInfo> mPalletsInfoList;

    public List<Palet> mReadedPallets;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public DeliveryActivity() {
    }
    //endregion

    //region $ACTIVITY OVERRIDE METHODS
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initDefinitions();
        checkService();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        LoadDeliveryList();
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
        saveDeliveryList();
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
//                        SaveDeliveryList();
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
    @SuppressLint("MissingInflatedId")
    private void initViews() {
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_delivery);

        btnDeliveryNo = findViewById(R.id.txtDeliveryNo);
        btnStart = findViewById(R.id.btnStart);
        btnUndo = findViewById(R.id.btnUndo);
        btnFinish = findViewById(R.id.btnFinish);
        SurfaceView surfaceViewDelivery = findViewById(R.id.surfaceViewDelivery);
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        btnUpdate = findViewById(R.id.btnUpdate);
//        txtVehicle = findViewById(R.id.txtVehicle);
        webViewVehicle = findViewById(R.id.webViewVehicle);
//        TextView txtUsername = findViewById(R.id.txtUsername);
        recViewDeliveryList = findViewById(R.id.recViewDeliveryItemList);
//        btnChangeUser = findViewById(R.id.btnChangeUser);
        cameraSwitch = findViewById(R.id.swCamera);
//        cameraSwitch.setVisibility(View.GONE);
        txtMalAdi = findViewById(R.id.txtMalAdi);
        txtPaletMiktar = findViewById(R.id.txtPaletMiktar);
        editNumber = findViewById(R.id.editSayim);
        editBarcode = findViewById(R.id.editBarcode);
//        btnPalet = findViewById(R.id.btnPallet);
//        btnClear = findViewById(R.id.btnClear);
//        btnPalet2 = findViewById(R.id.btnPallet2);

        mSurfaceHolder = surfaceViewDelivery.getHolder();

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.common_open_on_phone, R.string.barcode_read);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view);
        TextView txtMenuUsername = navigationView.getHeaderView(0).findViewById(R.id.txtMenuUsername);
        setupDrawerContent(navigationView);


        initProgressDialog();
        initErrorDialog();

        btnStart.setOnClickListener(this::onClickBtnStart);
        btnUndo.setOnClickListener(this::onClickBtnUndo);
        btnFinish.setOnClickListener(this::onClickBtnFinish);
//        btnAdd.setOnClickListener(this::onClickBtnAdd);
//        btnRemove.setOnClickListener(this::onClickBtnRemove);
        btnDeliveryNo.setOnClickListener(this::onClickBtnDeliveryNo);
        btnUpdate.setOnClickListener(this::onClickBtnUpdate);
//        btnChangeUser.setOnClickListener(this::onClickBtnChangeUser);
//        btnPalet.setOnClickListener(this::onClickBtnPalet);
//        btnClear.setOnClickListener(this::onClickBtnClear);
//        btnPalet2.setOnClickListener(this::onClickBtnPalet2);
//        btnStart.setBackgroundColor(Color.RED);
//        btnUndo.setBackgroundColor(Color.RED);
//        btnFinish.setBackgroundColor(Color.RED);

        btnStart.setEnabled(false);
        btnUndo.setEnabled(false);
        btnFinish.setEnabled(false);

//        txtUsername.setText(ServiceDefinitions.loginUser.get_userName());
        txtMenuUsername.setText(ServiceDefinitions.loginUser.get_userName());

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

        editBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int textSize = s.length(); // Metnin uzunluğunu al
                // Eğer metnin boyutu 13 ise Toast mesajını göster
                if ((textSize == 8 && !TextUtils.isDigitsOnly(String.valueOf(s.charAt(0)))) || textSize == 13) {
                    if (canReadBarcode) {
                        canReadBarcode = false;
                        final String barcode = editBarcode.getText().toString();
                        editBarcode.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                readPalet(barcode);
                                editBarcode.setText("");
                            }
                        }, 2000);
                    }
                    editBarcode.requestFocus();
                    hideKeyboard(editBarcode);
                }
            }
        });
        editBarcode.requestFocus();
        // EditText'e tıklanınca klavyeyi gizle
//        editBarcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideKeyboard(editBarcode);
//            }
//        });

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

    private void initDefinitions() {
        mEditor = getSharedPreferences(PREF_CREDENTIAL, MODE_PRIVATE).edit();
        mPreferences = getSharedPreferences(PREF_CREDENTIAL, MODE_PRIVATE);
        mContext = getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);
        cookieManage = new CookieManager();
        CookieHandler.setDefault(cookieManage);
        requestHandler = new RequestHandler();
        selectedItemList = new ArrayList<>();
        selectedIndex = -1;
        mPalletsInfoList = new ArrayList<>();
        loadDeliveryList();
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

    private void startDelivery(String deliveryNo, int type) throws Exception {
        if (deliveryNo != null && !deliveryNo.equals("")) {
            requestHandler.setDeliveryStatus(deliveryNo, 1, this, response.get_deliveryItem(), null, type);
            response.set_status(1);
            selectedItemList = new ArrayList<>();
            showToastMessage(getString(R.string.start_delivery), Toast.LENGTH_LONG, Gravity.TOP);

            selectedIndex = -1;
            btnUndo.setBackgroundColor(Color.GREEN);
            btnFinish.setBackgroundColor(Color.GREEN);

            btnStart.setEnabled(false);
            btnUndo.setEnabled(true);
            btnFinish.setEnabled(true);
        }
    }

    private void undoDelivery(String deliveryNo) throws Exception {
        if (deliveryNo != null && !deliveryNo.equals("")) {
            requestHandler.setDeliveryStatus(deliveryNo, 0, this, response.get_deliveryItem(), null, response.get_deliveryType());
            response.set_status(1);
            selectedItemList = new ArrayList<>();
            showToastMessage(getString(R.string.undo_delivery), Toast.LENGTH_LONG, Gravity.TOP);
        }
    }

    private void finishDelivery(String deliveryNo, List<DeliveryGroupItem> listItem, List<PalletsInfo> palletsInfoList) throws Exception {
        if (response.get_status() == 1) {
            requestHandler.setDeliveryStatus(deliveryNo, 2, this, listItem, palletsInfoList, response.get_deliveryType());
//            requestHandler.setDeliveryPalletQuantity(deliveryNo, 2, this, palletsInfoList);
        } else {
            showErrorDialog(getString(R.string.error_finish));
        }
    }

    private void initializeDetectorsAndSources() {
        barcodeString = "";
        readedPalet = new Palet();
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
//                    showToastMessage(getString(R.string.barcode_read), Toast.LENGTH_LONG, Gravity.TOP);
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
                                readPalet(barcodes.valueAt(0).displayValue);
                            }
                        }, 2000);
                    }
                }
            }
        });
        cameraSwitch.setChecked(false);
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

    public void setPaletRows(String readedBarcode) {
        readedPalet = requestHandler.getPalet(readedBarcode);
        if(readedPalet == null) return;
        selectedIndex = -1;
        int index = -1;
        boolean isExist = false;
        for (DeliveryGroupItem item: mAdapterRVDeliveryItem.listDelivery) {
            index++;
            if (item.getMateryalKodu().equals(readedPalet.getMateryalKodu())){
                selectedIndex = index;
                if(item.getPalets() != null){
                    for (Palet itemPalet : item.getPalets()) {
                        if (itemPalet != null && itemPalet.getSeriNo().equals(readedPalet.getSeriNo())){
                            isExist = true;
                        }
                    }
                }
                break;
            }
        }
        if (readedPalet == null) {
            showToastMessage(getString(R.string.couldnot_find_pallet), Toast.LENGTH_LONG, Gravity.TOP);
        }else if(selectedIndex == -1){
            showToastMessage(getString(R.string.couldnot_find_pallet_delivery), Toast.LENGTH_LONG, Gravity.TOP);
        } else {
            float remainingQuantity = mAdapterRVDeliveryItem.listDelivery.get(selectedIndex).getMiktar() - mAdapterRVDeliveryItem.listDelivery.get(selectedIndex).getMiktar2();
            if (remainingQuantity >= readedPalet.getMiktar()){
                editNumber.setText(String.valueOf(readedPalet.getMiktar()));
                addRow();
            } else {
                addPaletRow(isExist);
            }
        }
    }

    private void openPopUpWindow(View v, int docType) {
        popUpClass = new MyPopUpWindow(mRequestQueue, serviceDefinitions, cookieManage, v, DeliveryActivity.this, docType);
        popUpClass.showPopupWindow();
    }

    private void openPopUpWindowPallet(View v) {
        if (response != null && mPalletsInfoList != null){
            mPalletsInfoList = response.get_palletsInfoList();
            MyPopUpPalet popUpClassPallet = new MyPopUpPalet(mRequestQueue, serviceDefinitions, cookieManage, v, DeliveryActivity.this, mPalletsInfoList);
            popUpClassPallet.showPopupWindow();
        }
    }
    private void openPopUpWindowPallet2(View v) {
        if (response != null) {
            mReadedPallets = GetAllPallets();
            if( mReadedPallets != null){
                MyPopUpPalet2 popUpClassPallet2 = new MyPopUpPalet2(mRequestQueue, serviceDefinitions, cookieManage, v, DeliveryActivity.this, mReadedPallets);
                popUpClassPallet2.showPopupWindow();
            }
        }
    }

    private List<Palet> GetAllPallets() {
        List<Palet> palets = new ArrayList<>();
        for (DeliveryGroupItem item: mAdapterRVDeliveryItem.listDelivery) {
            if (item.getPalets() != null) {
                for (Palet itemPalet: item.getPalets()) {
                    palets.add(itemPalet);
                }
            }
        }
        return palets;
    }

    public void closePopupWindow(String emirNo, int docType) {
        popUpClass.dismissPopupWindow();
        strCountDeliveryNo = emirNo;
        fillList(null, docType);
    }

    private void showToastMessage(String message, int duration, int gravity) {
        Toast toast = Toast.makeText(getApplicationContext(), message, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    private void setDeliveryListView(List<DeliveryGroupItem> items) {
        mAdapterRVDeliveryItem = new AdapterRVDeliveryItem(items, DeliveryActivity.this);
        recViewDeliveryList.setAdapter(mAdapterRVDeliveryItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recViewDeliveryList.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onDestroy() {
        saveDeliveryList();
        super.onDestroy();
    }

    private void fillList(DeliveryGroup res, int docType) {
        try {
            if (res == null) {
                response = requestHandler.getDelivery(strCountDeliveryNo, docType);
                mPalletsInfoList = response.get_palletsInfoList();
                selectedItemList = new ArrayList<>();
                selectedIndex = -1;
                setDeliveryListView(response.get_deliveryItem());

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
            String htmlContent = setWebViewVehicleInfo();
            webViewVehicle.loadData(htmlContent, "text/html", "UTF-8");

            clearPalletRow();
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

    public void clearAll(String docNo, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.finish_delivery) + "(" + getString(R.string.documentNo) + ":" + docNo + ")";
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
        clearPalletRow();
        editBarcode.setText("");
        btnDeliveryNo.setText(getString(R.string.delivery));
        btnStart.setBackgroundColor(Color.LTGRAY);
        btnUndo.setBackgroundColor(Color.LTGRAY);
        btnFinish.setBackgroundColor(Color.LTGRAY);
//        txtVehicle.setText("");
        String htmlContent = "<html><body><h1></h1></body></html>";
        webViewVehicle.loadData(htmlContent, "text/html", "UTF-8");
        strCountDeliveryNo = "";
        selectedItemList.clear();
        if (mAdapterRVDeliveryItem != null){
            mAdapterRVDeliveryItem.listReadedBarcodeList.clear();
            int size = mAdapterRVDeliveryItem.listDelivery.size();
            mAdapterRVDeliveryItem.listDelivery.clear();
            mAdapterRVDeliveryItem.notifyItemRangeRemoved(0, size);
        }
        response = new DeliveryGroup();
        mPalletsInfoList = response.get_palletsInfoList();
        selectedIndex = -1;

        btnStart.setEnabled(false);
        btnUndo.setEnabled(false);
        btnFinish.setEnabled(false);
        canReadBarcode = true;
        txtBarcodeValue.setText("");
    }

    private String controlConditions() {
        String errMessage = "";
        boolean isValid = true;
        for (DeliveryGroupItem item: mAdapterRVDeliveryItem.listDelivery) {
            if (item.getMiktar() != item.getMiktar2()){
                isValid = false;
                break;
            }
        }
        if (!isValid){
            errMessage = "Tüm satırların yükleme miktarları doldurulmalıdır.";
        }
        return errMessage;
    }
    //endregion

    //region $SURFACE OVERRIDE METHODS
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (ActivityCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (cameraSwitch.isChecked()){
                    cameraSource.start(mSurfaceHolder);
                }
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
                if (cameraSwitch.isChecked()){
                    cameraSource.start(mSurfaceHolder);
                }
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
//        saveState(outState);
        saveDeliveryList();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
//endregion

    //region $EVENTS
    public void onClickBtnDeliveryNo(View v) {
        if (btnDeliveryNo.getId() == v.getId()) {
            showOptionsDialog();
        }
    }

    public void onClickBtnPalet(View v) {
        openPopUpWindowPallet(v);
        hideProgressDialog();
    }
    public void onClickBtnPalet2(View v) {
        openPopUpWindowPallet2(v);
        hideProgressDialog();
    }

    public void onClickBtnClear(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.clear_screen));
        builder.setCancelable(false);
        builder.setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                clearPage();
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

    public void onClickBtnStart(View v) {
        try {
            if (response == null || response.get_deliveryNo().isEmpty()) {
                showErrorDialog(getString(R.string.error_select_delivery));
            } else if (response.get_status() == 0 || response.get_status() == -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.is_delivery_start));
                builder.setCancelable(false);
                builder.setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            startDelivery(strCountDeliveryNo, response.get_deliveryType());
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
            if (response == null || response.get_deliveryNo().isEmpty()) {
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
                            finishDelivery(strCountDeliveryNo, mAdapterRVDeliveryItem.listDelivery, mPalletsInfoList);
                            response.set_status(2);
                            saveDeliveryList();
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
        addRow();
        hideKeyboard(editNumber);
        editBarcode.requestFocus();
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
        mEditor.remove(KEY_USERNAME);
        mEditor.remove(KEY_PASSWORD);
        mEditor.apply();
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
            try {
                cameraSource.stop();
                isCameraRunning = false;
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void clearPalletRow(){
        txtMalAdi.setText("");
        txtPaletMiktar.setText("");
        editNumber.setText("0");
        btnUpdate.setText(R.string.add);
        readedPalet = null;
        editBarcode.requestFocus();
    }
    private String setWebViewVehicleInfo(){
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "  <table style=\"width: 100%; border-collapse: collapse;\">\n" +
                "    <tr>\n" +
                "      <td style=\"border: 1px solid black; padding: 1px; text-align: center; font-size: 12px;\"><b>Plaka</b></td>\n" +
                "      <td style=\"border: 1px solid black; padding: 1px; text-align: center; font-size: 12px;\"><b>Şoför Ad-Soyad</b></td>\n" +
                "      <td style=\"border: 1px solid black; padding: 1px; text-align: center; font-size: 12px;\"><b>Şoför TCKN</b></td>\n" +
                "      <td style=\"border: 1px solid black; padding: 1px; text-align: center; font-size: 12px;\"><b>Şoför Tel</b></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"border: 1px solid black; padding: 1px; text-align: center; font-size: 12px;\">"+response.get_plate()+"</td>\n" +
                "      <td style=\"border: 1px solid black; padding: 1px; text-align: center; font-size: 12px;\">"+response.get_driverName() + " " + response.get_driverSurname()+"</td>\n" +
                "      <td style=\"border: 1px solid black; padding: 1px; text-align: center; font-size: 12px;\">"+response.get_driverTCKN()+"</td>\n" +
                "      <td style=\"border: 1px solid black; padding: 1px; text-align: center; font-size: 12px;\">"+response.get_driverPhone()+"</td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        return htmlContent;
    }
    //endregion
    public void ResetPalet(){
        for (DeliveryGroupItem item: mAdapterRVDeliveryItem.listDelivery) {
            item.setMiktar2(0);
            if(item.getPalets() != null){
                item.getPalets().clear();
            }
            for (Palet itemPalet: mReadedPallets) {
                if (item.getMateryalKodu().equals(itemPalet.getMateryalKodu())){
                    item.setMiktar2(item.getMiktar2() + itemPalet.getMiktar());
                    item.addPalet(itemPalet);
                }
            }
        }
        mAdapterRVDeliveryItem.notifyDataSetChanged();
        savePrefResponse();
        savePrefPallet();
        setDeliveryListView(mAdapterRVDeliveryItem.listDelivery);
    }

    private void addRow(){
        try {
            float dNumber = 0, quantity = 0, fixedQuantity = 0;
            if (!String.valueOf(editNumber.getText()).equals("")) {
                dNumber = Float.valueOf(String.valueOf(editNumber.getText()));
                if (mAdapterRVDeliveryItem.listDelivery != null && readedPalet != null) {
                    if(dNumber > readedPalet.getMiktar()){
                        showErrorDialog(getString(R.string.error_counting));
                        editNumber.setText("0");
                        return;
                    }
                    for (DeliveryGroupItem item: mAdapterRVDeliveryItem.listDelivery) {
                        if (item.getMateryalKodu().equals(readedPalet.getMateryalKodu())){
                            float prevQuantity = 0;
                            if(item.getPalets() != null){
                                for (Palet itemPalet : item.getPalets()) {
                                    if (itemPalet != null && itemPalet.getSeriNo().equals(readedPalet.getSeriNo())){
                                        prevQuantity = itemPalet.getMiktar();
                                    }
                                }
                            }

                            if (dNumber > item.getMiktar()){
                                showErrorDialog(getString(R.string.error_counting));
                                editNumber.setText("0");
                                return;
                            }
                            if (item.getMiktar() < item.getMiktar2() + dNumber - prevQuantity){
                                showErrorDialog(getString(R.string.error_counting));
                                editNumber.setText("0");
                                return;
                            }
                            Palet pal = readedPalet;
                            if(item.getPalets() != null){
                                int index = -1;
                                for(int i = 0; i <= item.getPalets().size()-1; i++){
                                    if (item.getPalets().get(i).getSeriNo().equals(pal.getSeriNo())){
                                        index = i;
                                        break;
                                    }
                                }
                                if (index == -1){
                                    pal.setMiktar(dNumber);
                                    item.addPalet(pal);
                                    mAdapterRVDeliveryItem.notifyDataSetChanged();
                                    savePrefResponse();
                                    savePrefPallet();
                                    SoundManager.playSound(this, R.raw.beep);
                                } else {
                                    fixedQuantity = - item.getPalets().get(index).getMiktar();
                                    item.getPalets().get(index).setMiktar(dNumber);
                                    mAdapterRVDeliveryItem.notifyDataSetChanged();
                                    savePrefResponse();
                                    savePrefPallet();
                                    SoundManager.playSound(this, R.raw.beep);
                                }
                            } else {
                                pal.setMiktar(dNumber);
                                item.addPalet(pal);
                                mAdapterRVDeliveryItem.notifyDataSetChanged();
                                savePrefResponse();
                                savePrefPallet();
                                SoundManager.playSound(this, R.raw.beep);
                            }
                            quantity = mAdapterRVDeliveryItem.listDelivery.get(selectedIndex).getMiktar2();
                            mAdapterRVDeliveryItem.setSayimMiktar(selectedIndex, quantity + dNumber + fixedQuantity, "");
                            mAdapterRVDeliveryItem.notifyDataSetChanged();
                            clearPalletRow();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addPaletRow(boolean isExist){
        txtMalAdi.setText(readedPalet.getMateryalAdi());
        txtPaletMiktar.setText(String.valueOf(readedPalet.getMiktar()));
        editNumber.setText("0");
        btnUpdate.setText(R.string.add);
        if(isExist){
            btnUpdate.setText(R.string.fix);
        }
        editNumber.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editNumber, InputMethodManager.SHOW_IMPLICIT);
        editNumber.selectAll();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    private void selectDrawerItem(MenuItem menuItem) {
        Button btnTemp = new Button(this);
        switch (menuItem.getItemId()) {
            case R.id.nav_pallet:
                onClickBtnPalet(btnTemp);
                break;
            case R.id.nav_pallet_delete:
                onClickBtnPalet2(btnTemp);
                break;
            case R.id.nav_clear:
                onClickBtnClear(btnTemp);
                break;
            case R.id.nav_logout:
                onClickBtnChangeUser(btnTemp);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void readPalet(String barcode){
        canReadBarcode = true;
        if (response != null && response.get_status() == 1) {
//            barcodeString = barcodes.valueAt(0).displayValue;
            barcodeString = barcode;
//                                    if(barcodeString.length() < 9) return;
            if (barcodeString.length() > 8) {
                barcodeString = barcodeString.substring(barcodeString.length() - 6);
            }
            txtBarcodeValue.setText(barcodeString);
            Log.d(TAG, "initializeDetectorsAndSources: barcode was scanned. scanned string : " + barcodeString);
            setPaletRows(barcodeString);
            showToastMessage(barcodeString, Toast.LENGTH_LONG, Gravity.TOP);
        } else if (response == null || response.get_deliveryNo() == null) {
//            barcodeString = barcodes.valueAt(0).displayValue;
            barcodeString = barcode;
            if(barcodeString.length() != 8) return;
            strCountDeliveryNo = barcodeString;
            response = requestHandler.getDelivery(strCountDeliveryNo, 500);
            if (response == null){
                response = requestHandler.getDelivery(strCountDeliveryNo, 55);
            }
            if (response == null){
                response = requestHandler.getDelivery(strCountDeliveryNo, 52);
            }
            if (response != null && (response.get_status() != 2)){
                mPalletsInfoList = response.get_palletsInfoList();
                setDeliveryListView(response.get_deliveryItem());
                mAdapterRVDeliveryItem.listDelivery = response.get_deliveryItem();
                selectedItemList = new ArrayList<>();
                mAdapterRVDeliveryItem.listReadedBarcodeList = selectedItemList;
                fillList(response, response.get_deliveryType());
                try {
                    if (response.get_status() == 0){
                        startDelivery(strCountDeliveryNo, response.get_deliveryType());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else{
                strCountDeliveryNo = "";
                response = null;
            }
        }
    }

    // Klavyeyi gizleme metodu
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void saveState(Bundle outState){
        if (response != null) {
            outState.putParcelable(KEY_DELIVERY, response);
            outState.putIntegerArrayList(KEY_DELIVERYITEMS_SELECTED, selectedItemList);
            outState.putParcelableArrayList(KEY_DELIVERYITEMS_ADAPTER, (ArrayList<? extends Parcelable>) mAdapterRVDeliveryItem.listDelivery);
            outState.putString(KEY_DELIVERYNO, strCountDeliveryNo);
        }
    }

    public void saveDeliveryList(){
        if(response == null) return;
        Gson gson = new Gson();
        String json = "";

        mEditor.putString(KEY_SAVE_DELIVERYNO, strCountDeliveryNo);

        savePrefResponse();
        savePrefPallet();

        json = gson.toJson(mPalletsInfoList);
        mEditor.putString(KEY_SAVE_PALLETS_INFO, json);

        json = gson.toJson(selectedItemList);
        mEditor.putString(KEY_SAVE_SELECTEDITEMLIST, json);

        mEditor.apply();
    }

    public void loadDeliveryList(){
        Gson gson = new Gson();
        String json = "";
        List<DeliveryGroupItem> tempList = new ArrayList<>();

        strCountDeliveryNo = mPreferences.getString(KEY_SAVE_DELIVERYNO,"");

        if (strCountDeliveryNo.equals("")) return;

        Type listType = new TypeToken<List<DeliveryGroupItem>>(){}.getType();
        json = mPreferences.getString(KEY_SAVE_ADAPTER_LIST,"");
        tempList =  gson.fromJson(json, listType);

        json = mPreferences.getString(KEY_SAVE_RESPONSE,"");
        response = new DeliveryGroup();
        response = gson.fromJson(json, DeliveryGroup.class);
        response.set_deliveryItem(tempList);
        setDeliveryListView(tempList);

        if (response.get_status() == 2){
            clearPage();
            return;
        }

        mAdapterRVDeliveryItem.listDelivery = tempList;
        mAdapterRVDeliveryItem.notifyDataSetChanged();

        json = mPreferences.getString(KEY_SAVE_SELECTEDITEMLIST,"");
        listType = new TypeToken<ArrayList<Integer>>(){}.getType();
        selectedItemList = gson.fromJson(json, listType);
        mAdapterRVDeliveryItem.listReadedBarcodeList = selectedItemList;

        mReadedPallets = GetAllPallets();

        json = mPreferences.getString(KEY_SAVE_PALLETS_INFO,"");
        listType = new TypeToken<ArrayList<PalletsInfo>>(){}.getType();
        mPalletsInfoList = gson.fromJson(json, listType);
        if (mPalletsInfoList == null){
            mPalletsInfoList = new ArrayList<>();
        }
        fillList(response, response.get_deliveryType());
    }
    public void savePrefResponse() {
        Gson gson = new Gson();
        String json = "";
        json = gson.toJson(response);
        mEditor.putString(KEY_SAVE_RESPONSE, json);
    }
    public void savePrefPallet() {
        Gson gson = new Gson();
        String json = "";
        mAdapterRVDeliveryItem.notifyDataSetChanged();
        json = gson.toJson(mAdapterRVDeliveryItem.listDelivery);
        mEditor.putString(KEY_SAVE_ADAPTER_LIST, json);
    }
    private void showOptionsDialog() {
        // Seçenekleri içeren diziye resources üzerinden erişim
        String[] options = getResources().getStringArray(R.array.dropdown_items_document_types);
        final int[] docType = {-1};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.select_document));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Her seçenek için ayrı bir işlem gerçekleştir
                switch (which) {
                    case 0:
                        docType[0] = 500;
                        break;
                    case 1:
                        docType[0] = 55;
                        break;
                    case 2:
                        docType[0] = 52;
                        break;
                    default:
                        break;
                }
                if(docType[0] != -1){
                    showProgressDialog("", getApplicationContext().getString(R.string.read));
                    openPopUpWindow(btnDeliveryNo, docType[0]);
                    hideProgressDialog();
                }
                // Dialog'u kapat
                dialog.dismiss();
            }
        });

        // Dialog'u göster
        builder.show();
    }
}