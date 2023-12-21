package adapters;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Delivery.PalletsInfo;

public class AdapterRVPalet extends RecyclerView.Adapter<AdapterRVPalet.ViewHolder> {
    //region $MEMBERS
    public List<PalletsInfo> listPalletsInfo;
    private LayoutInflater inflater;
    private DeliveryActivity activity;
    private AdapterRVPalet.ViewHolder holder;
    public ArrayList<Integer> listReadedBarcodeList;
    private DecimalFormat dform = new DecimalFormat("#,###.####");
    private Map<Integer, String> editTextValues = new HashMap<>(); // Satır pozisyonlarına göre EditText değerlerini saklamak için bir harita kullanıyoruz.
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
        String editTextValue = editTextValues.get(position);
        if (editTextValue != null) {
            holder.editPalletQuantity.setText(editTextValue);
            if (editTextValue.equalsIgnoreCase("")){
                listPalletsInfo.get(position).setPalletQuantity(0);
            }else{
                listPalletsInfo.get(position).setPalletQuantity(Integer.parseInt(editTextValue));
            }
        } else {
            holder.editPalletQuantity.setText("0"); // EditText'e herhangi bir değer atanmamışsa, boş bir metin ayarla
        }
        // EditText değeri değiştiğinde sakla
        holder.editPalletQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // EditText'teki yeni değeri sakla
                if (holder.getAdapterPosition() == position) {
                    editTextValues.put(position, editable.toString());
                    String editTextValue = editTextValues.get(position);
                    if (editTextValue.equalsIgnoreCase("")){
                        listPalletsInfo.get(position).setPalletQuantity(0);
                    }else{
                        listPalletsInfo.get(position).setPalletQuantity(Integer.parseInt(editTextValue));
                    }
                }
            }
        });

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

