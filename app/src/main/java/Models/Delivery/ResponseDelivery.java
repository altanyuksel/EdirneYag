package Models.Delivery;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseDelivery {

    @SerializedName("ErrStatus")
    private boolean errStatus;

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @SerializedName("Paging")
    private Paging paging;

    @SerializedName("ListItem")
    private List<Delivery> listItem;

    public ResponseDelivery() {
        this.errStatus = false;
        this.paging = new Paging();
        this.listItem = new ArrayList<>();
    }
    public ResponseDelivery(boolean errStatus, Paging paging, List<Delivery> listItem) {
        this.errStatus = errStatus;
        this.paging = paging;
        this.listItem = listItem;
    }

    public boolean isErrStatus(){
        return errStatus;
    }

    public Paging getPaging(){
        return paging;
    }

    public List<Delivery> getListItem(){
        return listItem;
    }
}