package Models.Delivery;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliveryItem implements Parcelable {

    @SerializedName("SiparisSiraNo")
    private int siparisSiraNo;

    @SerializedName("SiparisMiktar")
    private float siparisMiktar;

    @SerializedName("SiparisNo")
    private String siparisNo;

    @SerializedName("SiraNo")
    private int siraNo;

    @SerializedName("Miktar")
    private float miktar;

    @SerializedName("Miktar2")
    private float miktar2;

    @SerializedName("Miktar3")
    private float miktar3;

    @SerializedName("SeriNo")
    private String seriNo;

    @SerializedName("BarkodNo")
    private List<String> barkodNo;

    @SerializedName("MateryalKodu")
    private String materyalKodu;

    @SerializedName("MateryalAdi")
    private String materyalAdi;

    private int rowColor = Color.RED;

    public int getSiparisSiraNo(){
        return siparisSiraNo;
    }

    public float getSiparisMiktar(){
        return siparisMiktar;
    }

    public String getSiparisNo(){
        return siparisNo;
    }

    public int getSiraNo(){
        return siraNo;
    }

    public float getMiktar(){
        return miktar;
    }

    public void setMiktar2(float sayimMiktar){
        this.miktar2 = sayimMiktar;
    }
    public void setSeriNo(String seriNo){
        this.seriNo = seriNo;
    }
    public float getMiktar2(){
        return miktar2;
    }

    public float getMiktar3(){
        return miktar3;
    }

    public String getSeriNo(){
        return seriNo;
    }

    public List<String> getBarkodNo(){
        return barkodNo;
    }

    public String getMateryalKodu(){
        return materyalKodu;
    }

    public String getMateryalAdi(){
        return materyalAdi;
    }
    public int getRowColor(){
        return rowColor;
    }
    public void setRowColor(int color){
        this.rowColor = color;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(siparisSiraNo);
        dest.writeFloat(siparisMiktar);
        dest.writeString(siparisNo);
        dest.writeInt(siraNo);
        dest.writeFloat(miktar);
        dest.writeFloat(miktar2);
        dest.writeFloat(miktar3);
        dest.writeString(seriNo);
        dest.writeStringList(barkodNo);
        dest.writeString(materyalKodu);
        dest.writeString(materyalAdi);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryItem> CREATOR = new Creator<DeliveryItem>() {
        @Override
        public DeliveryItem createFromParcel(Parcel in) {
            return new DeliveryItem(in);
        }

        @Override
        public DeliveryItem[] newArray(int size) {
            return new DeliveryItem[size];
        }
    };

    protected DeliveryItem(Parcel in) {
        siparisSiraNo = in.readInt();
        siparisMiktar = in.readFloat();
        siparisNo = in.readString();
        siraNo = in.readInt();
        miktar = in.readFloat();
        miktar2 = in.readFloat();
        miktar3 = in.readFloat();
        seriNo = in.readString();
        barkodNo = in.createStringArrayList();
        materyalKodu = in.readString();
        materyalAdi = in.readString();
    }
}