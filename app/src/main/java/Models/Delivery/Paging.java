package Models.Delivery;

import com.google.gson.annotations.SerializedName;

public class Paging{
    public Paging() {
        this.page = 0;
        this.pageSize = 0;
    }

    public Paging(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    @SerializedName("PageSize")
    private int pageSize;

    @SerializedName("Page")
    private int page;

    public int getPageSize(){
        return pageSize;
    }

    public int getPage(){
        return page;
    }
}