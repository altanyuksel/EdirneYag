package Models.Delivery;

import com.google.gson.annotations.SerializedName;

public class Customer {

	@SerializedName("Unvan")
	private String _customerTitle;

	@SerializedName("HesapKodu")
	private String _customerCode;

	public String get_customerTitle(){
		return _customerTitle;
	}

	public String get_customerCode(){
		return _customerCode;
	}
}