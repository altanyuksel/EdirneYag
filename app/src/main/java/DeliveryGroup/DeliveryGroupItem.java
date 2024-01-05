package DeliveryGroup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import Models.Delivery.Palet;

public class DeliveryGroupItem implements Parcelable {
	@SerializedName("MateryalKodu")
	private String materyalKodu;

	@SerializedName("MateryalAdi")
	private String materyalAdi;

	@SerializedName("Miktar")
	private float miktar;

	public void setMiktar2(float miktar2) {
		this.miktar2 = miktar2;
	}

	@SerializedName("Miktar2")
	private float miktar2;

	@SerializedName("Palet")
	private List<Palet> palets;

	public String getMateryalKodu(){
		return materyalKodu;
	}

	public String getMateryalAdi(){
		return materyalAdi;
	}

	public float getMiktar(){
		return miktar;
	}

	public float getMiktar2(){
		return miktar2;
	}

	public List<Palet> getPalets() {
		return palets;
	}

	public void addPalet(Palet palet) {
		if (this.palets == null){
			this.palets = new ArrayList<>();
		}
		this.palets.add(palet);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(miktar);
		dest.writeFloat(miktar2);
		dest.writeString(materyalKodu);
		dest.writeString(materyalAdi);
		dest.writeList(palets);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DeliveryGroupItem> CREATOR = new Creator<DeliveryGroupItem>() {
		@Override
		public DeliveryGroupItem createFromParcel(Parcel in) {
			return new DeliveryGroupItem(in);
		}

		@Override
		public DeliveryGroupItem[] newArray(int size) {
			return new DeliveryGroupItem[size];
		}
	};

	protected DeliveryGroupItem(Parcel in) {
		miktar = in.readFloat();
		miktar2 = in.readFloat();
		materyalKodu = in.readString();
		materyalAdi = in.readString();
		palets = new ArrayList<>();
		in.readList(palets,List.class.getClassLoader());
	}
}
