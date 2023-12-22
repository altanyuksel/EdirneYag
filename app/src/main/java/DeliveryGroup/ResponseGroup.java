package DeliveryGroup;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import Models.Delivery.Paging;

public class ResponseGroup{
	@SerializedName("ErrStatus")
	private boolean errStatus;
	@SerializedName("Paging")
	private Paging paging;
	@SerializedName("ListItem")
	private List<DeliveryGroup> listItem;

	public boolean isErrStatus(){
		return errStatus;
	}

	public Paging getPaging(){
		return paging;
	}

	public List<DeliveryGroup> getListItem(){
		return listItem;
	}
}