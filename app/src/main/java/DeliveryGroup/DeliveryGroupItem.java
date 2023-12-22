package DeliveryGroup;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import Models.Delivery.Palet;

public class DeliveryGroupItem {
	@SerializedName("MateryalKodu")
	private String materyalKodu;

	@SerializedName("MateryalAdi")
	private String materyalAdi;

	@SerializedName("Miktar")
	private float miktar;

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

	public List<Palet> getPalets() {
		return palets;
	}
}
