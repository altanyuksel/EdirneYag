package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.link.edirneyag.DeliveryActivity;
import com.link.edirneyag.R;

import java.util.List;

import Models.Delivery.PalletsInfo;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<PalletsInfo> dataList;
    private DeliveryActivity activity;

    // Adapter'ı başlatan constructor
    public MyAdapter(List<PalletsInfo> dataList, DeliveryActivity activity) {
        this.dataList = dataList;
        this.activity = activity;
    }

    // ... Diğer metodlar

    // Liste değişkenine erişim sağlayan bir metot
    public List<PalletsInfo> getDataList() {
        return dataList;
    }

    // ViewHolder sınıfı
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtCustomerCode);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ViewHolder'ı oluştur ve bağla
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_delivery_palet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Veriyi ViewHolder'a bağla
        holder.textView.setText(dataList.get(position).getCustomer().get_customerCode());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
