package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.link.edirneyag.DeliveryActivity;
import com.link.edirneyag.R;

import Models.Delivery.Delivery;
import Models.Delivery.PalletsInfo;

public class AdapterRVPalet extends RecyclerView.Adapter<AdapterRVPalet.ViewHolder> {
    //region $MEMBERS
    private Delivery delivery;
    private LayoutInflater inflater;
    private Context mContext;
    private DeliveryActivity activity;
    //endregion

    //region $METHODS
    public AdapterRVPalet(Context context, Delivery delivery, DeliveryActivity activity) {
        inflater = LayoutInflater.from(context);
        this.delivery = delivery;
        this.mContext = context;
        this.activity = activity;
    }
    //endregion

    //region $ADAPTER OVERRIDE METHODS
    @NonNull
    @Override
    public AdapterRVPalet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_list_palet, parent, false);
        AdapterRVPalet.ViewHolder holder = new AdapterRVPalet.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(AdapterRVPalet.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setData(delivery.get_palletsInfoList().get(position));
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    @Override
    public int getItemCount() {
        return delivery.get_palletsInfoList().size();
    }
    //endregion

    //region $VIEWHOLDER CLASS
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCustomerCode, txtCustomerTitle;
        private EditText editPalletQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            txtCustomerCode = itemView.findViewById(R.id.txtCustomerCode);
            txtCustomerTitle = itemView.findViewById(R.id.txtCustomerTitle);
            editPalletQuantity = itemView.findViewById(R.id.editPalletQuantity);
        }

        public void setData(PalletsInfo item) {
            txtCustomerCode.setText(item.getCustomer().get_customerCode());
            txtCustomerTitle.setText(item.getCustomer().get_customerTitle());
            editPalletQuantity.setText("0");
        }
    }
    //endregion
}