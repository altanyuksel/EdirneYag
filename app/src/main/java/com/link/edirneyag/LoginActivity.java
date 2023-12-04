package com.link.edirneyag;

import static Utils.UtilsCommon.KEY_PASSWORD;
import static Utils.UtilsCommon.KEY_TYPE;
import static Utils.UtilsCommon.KEY_USERNAME;
import static Utils.UtilsCommon.PREF_CREDENTIAL;
import static Utils.UtilsCommon.requestHandler;
import static Utils.UtilsCommon.serviceDefinitions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import RestApi.RequestHandler;
import ServiceSetting.ServiceDefinitions;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private Button btnLogin, btnCancel;
    private EditText editUsername, editPassword;
    private AlertDialog.Builder errDialog;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    //endregion

    //region $METHODS
    private void initViews() {
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);
        btnLogin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        initErrorDialog();
    }

    private void initDefinitions() {
        mContext = getApplicationContext();
        mSharedPreferences = getSharedPreferences(PREF_CREDENTIAL, MODE_PRIVATE);
        requestHandler = new RequestHandler();
    }

    private void initServices() throws IOException, XmlPullParserException {
        String userName = mSharedPreferences.getString(KEY_USERNAME, "");
        String password = mSharedPreferences.getString(KEY_PASSWORD, "");
        String userType = mSharedPreferences.getString(KEY_TYPE, "");
        serviceDefinitions = GetServiceDefination(userName, password, userType);
    }

    private ServiceDefinitions GetServiceDefination(String userName, String password, String userType) throws XmlPullParserException, IOException {
//        return ServiceDefinitions.getYNSHandlerInstance(getResources(), R.xml.test_config_link, userName, password, userType);
        return ServiceDefinitions.getYNSHandlerInstance(getResources(), R.xml.config_link, userName, password, userType);
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
            } else {
                SharedPreferences.Editor editor = getSharedPreferences(PREF_CREDENTIAL, MODE_PRIVATE).edit();
                editor.putString(KEY_USERNAME, ServiceDefinitions.loginUser.get_userName());
                editor.putString(KEY_PASSWORD, ServiceDefinitions.loginUser.get_password());
                editor.putString(KEY_TYPE, ServiceDefinitions.loginUser.get_userType());
                editor.apply();
                startActivity(new Intent(LoginActivity.this, DeliveryActivity.class));
            }
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
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

    private void initErrorDialog() {
        errDialog = new AlertDialog.Builder(LoginActivity.this);
    }
    //endregion

    //region $ACTIVITY OVERRIDE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDefinitions();
        try {
            initServices();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        checkService();
        Log.d(TAG, "onCreate: MainActivity is start.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String userName = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                try {
                    serviceDefinitions = GetServiceDefination(userName, password, "");
                    checkService();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnCancel:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                break;
        }
    }
    //endregion
}
