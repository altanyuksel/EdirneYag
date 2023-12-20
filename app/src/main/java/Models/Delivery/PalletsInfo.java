package Models.Delivery;

import com.google.gson.annotations.SerializedName;

public class PalletsInfo {

	@SerializedName("Hesaplar")
	private Customer customer;
	@SerializedName("PaletMiktar")
	private int palletQuantity;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getPalletQuantity() {
		return palletQuantity;
	}

	public void setPalletQuantity(int palletQuantity) {
		this.palletQuantity = palletQuantity;
	}

}