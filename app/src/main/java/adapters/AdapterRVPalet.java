package adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.link.edirneyag.DeliveryActivity;
import com.link.edirneyag.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Models.Delivery.PalletsInfo;

public class AdapterRVPalet extends RecyclerView.Adapter<AdapterRVPalet.ViewHolder> {
    //region $MEMBERS
    public List<PalletsInfo> listPalletsInfo;
    private LayoutInflater inflater;
    private DeliveryActivity activity;
    private AdapterRVPalet.ViewHolder holder;
    public ArrayList<Integer> listReadedBarcodeList;
    private DecimalFormat dform = new DecimalFormat("#,###.####");
    AlertDialog.Builder errDialog;
    //endregion

    //region $METHODS
    public AdapterRVPalet(List<PalletsInfo> listCountOrder, DeliveryActivity activity) {
        this.listPalletsInfo = listCountOrder;
        this.activity = activity;
        listReadedBarcodeList = new ArrayList<>();
        initErrorDialog();
    }
    //endregion

    //region $ADAPTER OVERRIDE METHODS
    @NonNull
    @Override
    public AdapterRVPalet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_list_palet, parent, false);
        holder = new AdapterRVPalet.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(AdapterRVPalet.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setData(listPalletsInfo.get(position));
    }
    private void initErrorDialog() {
        errDialog = new AlertDialog.Builder(activity);
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    @Override
    public int getItemCount() {
        return listPalletsInfo.size();
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

        public void setData(PalletsInfo selectedModelExample) {
            txtCustomerCode.setText(selectedModelExample.getCustomer().get_customerCode());
            txtCustomerTitle.setText(selectedModelExample.getCustomer().get_customerTitle());
            editPalletQuantity.setText("0");
        }
    }
    //endregion
}

