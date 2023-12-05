package Models.Delivery;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DeliveryItem implements Parcelable {
    @SerializedName("SiraNo")
    private int siraNo;

    @SerializedName("MateryalKodu")
    private String materyalKodu;

    @SerializedName("MateryalAdi")
    private String materyalAdi;

    @SerializedName("Miktar")
    private float miktar;

    @SerializedName("Miktar2")
    private float miktar2;

    @SerializedName("Palet")
    private List<Palet> palet;

    @SerializedName("SiparisNo")
    private String siparisNo;

    @SerializedName("SiparisSiraNo")
    private int siparisSiraNo;

    @SerializedName("SiparisMiktar")
    private float siparisMiktar;

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

    public float getMiktar2(){
        return miktar2;
    }

    public List<Palet> getPalet() {
        return palet;
    }

    public void setPalet(List<Palet> palet) {
        this.palet = palet;
    }

    public String getMateryalKodu(){
        return materyalKodu;
    }

    public String getMateryalAdi(){
        return materyalAdi;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(siraNo);
        dest.writeFloat(miktar);
        dest.writeFloat(miktar2);
        dest.writeString(materyalKodu);
        dest.writeString(materyalAdi);
        dest.writeString(siparisNo);
        dest.writeInt(siparisSiraNo);
        dest.writeFloat(siparisMiktar);
        dest.writeList(palet);
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
        siraNo = in.readInt();
        miktar = in.readFloat();
        miktar2 = in.readFloat();
        materyalKodu = in.readString();
        materyalAdi = in.readString();
        siparisNo = in.readString();
        siparisSiraNo = in.readInt();
        siparisMiktar = in.readFloat();
        palet = new ArrayList<>();
        in.readList(palet,List.class.getClassLoader());
    }
}