package ServiceSetting;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import Models.Delivery.User;

public class ServiceDefinitions {
    //region $MEMBERS
    public static final String TAG = "SERVICE_DEFINITIONS";
    public static String mainUrl;
    private static ServiceDefinitions mInstance = new ServiceDefinitions();
    public static User loginUser;
    //endregion

    //region $METHODS
    public static ServiceDefinitions getYNSHandlerInstance(Resources resources, int xmlResID, String username, String password, String userType) throws IOException, XmlPullParserException {
        XmlResourceParser parser = resources.getXml(xmlResID);
        int eventType = -1;
        // Loop through the XML data
        // Gerekli dataları xml dosyadan(endpoint) ve varsa cihaz hafızasından çekiyoruz.
        while (eventType != parser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                String parserName = parser.getName();
                if (parserName.equals("linkconfig")) {
                    mainUrl = parser.getAttributeValue(null, "main_url");
                    mInstance.mainUrl = mainUrl;
                    Log.d(TAG, "mainURL = " + mainUrl);
                    loginUser = new User();
                    loginUser.set_userName(username);
                    loginUser.set_password(password);
                    loginUser.set_userType(userType);
                    Log.d(TAG, "USERNAME = " + loginUser.get_userName());
                    break;
                }
            }
            eventType = parser.next();
        }
        return mInstance;
    }
    //endregion
}