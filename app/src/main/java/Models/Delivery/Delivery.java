package Models.Delivery;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Delivery implements Parcelable {
    @SerializedName("SevkiyatNo")
    private String _deliveryNo;

    @SerializedName("SevkiyatTuru")
    private int _deliveryType;

    @SerializedName("SevkiyatDurumu")
    private int _status;

    @SerializedName("HesapKodu")
    private String _customerCode;

    @SerializedName("Unvan")
    private String _customerTitle;

    @SerializedName("Tarih")
    private String _date;

    @SerializedName("SevkiyatTarih")
    private String _deliveryDate;

    @SerializedName("Depo")
    private String _wareHouse;

    @SerializedName("SoforAdi")
    private String _driverName;

    @SerializedName("SoforSoyadi")
    private String _driverSurname;

    @SerializedName("SoforTelefon")
    private String _driverPhone;

    @SerializedName("SoforTCKN")
    private String _driverTCKN;

    @SerializedName("Plaka")
    private String _plate;

    @SerializedName("Notlar")
    private String _notes;

    @SerializedName("SevkiyatKalemi")
    private List<DeliveryItem> _deliveryItem;

    @SerializedName("HataDurumu")
    private ErrorMessage _errorData;

    public String get_wareHouse(){
        return _wareHouse;
    }

    public List<DeliveryItem> get_deliveryItem(){
        return _deliveryItem;
    }

    public String get_plate(){
        return _plate;
    }

    public String get_notes(){
        return _notes;
    }

    public ErrorMessage get_errorData(){
        return _errorData;
    }

    public String get_date(){
        return _date;
    }

    public int get_deliveryType(){
        return _deliveryType;
    }

    public String get_customerTitle(){
        return _customerTitle;
    }

    public String get_customerCode(){
        return _customerCode;
    }

    public String get_driverName(){
        return _driverName;
    }

    public String get_deliveryNo(){
        return _deliveryNo;
    }

    public String get_deliveryDate(){
        return _deliveryDate;
    }

    public String get_driverPhone(){
        return _driverPhone;
    }

    public String get_driverSurname(){
        return _driverSurname;
    }

    public int get_status(){
        return _status;
    }

    public void set_status(int status){
        _status = status;
    }

    public String get_driverTCKN(){ return _driverTCKN;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable arayüzünün gerektirdiği writeToParcel metodu
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_deliveryNo);
        dest.writeInt(_deliveryType);
        dest.writeInt(_status);
        dest.writeString(_customerCode);
        dest.writeString(_customerTitle);
        dest.writeString(_date);
        dest.writeString(_deliveryDate);
        dest.writeString(_wareHouse);
        dest.writeString(_driverName);
        dest.writeString(_driverSurname);
        dest.writeString(_driverPhone);
        dest.writeString(_driverTCKN);
        dest.writeString(_plate);
        dest.writeString(_notes);
        dest.writeList(_deliveryItem);
    }

    // Parcelable arayüzünün gerektirdiği Creator
    public static final Parcelable.Creator<Delivery> CREATOR = new Parcelable.Creator<Delivery>() {
        public Delivery createFromParcel(Parcel in) {
            return new Delivery(in);
        }

        public Delivery[] newArray(int size) {
            return new Delivery[size];
        }
    };

    // Parcelable arayüzünün gerektirdiği Creator ile nesne oluşturma metodu
    private Delivery(Parcel in) {
        _deliveryNo= in.readString();
        _deliveryType= in.readInt();
        _status= in.readInt();
        _customerCode= in.readString();
        _customerTitle= in.readString();
        _date= in.readString();
        _deliveryDate= in.readString();
        _wareHouse= in.readString();
        _driverName= in.readString();
        _driverSurname= in.readString();
        _driverPhone= in.readString();
        _driverTCKN= in.readString();
        _plate= in.readString();
        _notes= in.readString();
        _deliveryItem = new ArrayList<>();
        in.readList(_deliveryItem,List.class.getClassLoader());
    }
    public Delivery(){

    }
}