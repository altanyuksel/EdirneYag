package Models.Delivery;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Palet implements Parcelable {
    @SerializedName("PaletNo")
    private String paletNo;

    @SerializedName("SiraNo")
    private int siraNo;

    @SerializedName("MateryalKodu")
    private String materyalKodu;

    @SerializedName("MateryalAdi")
    private String materyalAdi;

    @SerializedName("SeriNo")
    private String seriNo;

    @SerializedName("Miktar")
    private float miktar;

    public String getPaletNo() {
        return paletNo;
    }

    public void setPaletNo(String paletNo) {
        this.paletNo = paletNo;
    }

    public int getSiraNo() {
        return siraNo;
    }

    public void setSiraNo(int siraNo) {
        this.siraNo = siraNo;
    }

    public String getMateryalKodu() {
        return materyalKodu;
    }

    public void setMateryalKodu(String materyalKodu) {
        this.materyalKodu = materyalKodu;
    }

    public String getMateryalAdi() {
        return materyalAdi;
    }

    public void setMateryalAdi(String materyalAdi) {
        this.materyalAdi = materyalAdi;
    }

    public String getSeriNo() {
        return seriNo;
    }

    public void setSeriNo(String seriNo) {
        this.seriNo = seriNo;
    }

    public float getMiktar() {
        return miktar;
    }

    public void setMiktar(float miktar) {
        this.miktar = miktar;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paletNo);
        dest.writeInt(siraNo);
        dest.writeString(materyalKodu);
        dest.writeString(materyalAdi);
        dest.writeString(seriNo);
        dest.writeFloat(miktar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Palet> CREATOR = new Creator<Palet>() {
        @Override
        public Palet createFromParcel(Parcel in) {
            return new Palet(in);
        }

        @Override
        public Palet[] newArray(int size) {
            return new Palet[size];
        }
    };

    protected Palet(Parcel in) {
        paletNo = in.readString();
        siraNo = in.readInt();
        materyalKodu = in.readString();
        materyalAdi = in.readString();
        seriNo = in.readString();
        miktar = in.readFloat();
    }
    public Palet() {
        // İhtiyaç durumuna göre default değerler atanabilir.
    }
}
