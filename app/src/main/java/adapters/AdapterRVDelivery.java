package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.link.edirneyag.DeliveryActivity;
import com.link.edirneyag.R;

import DeliveryGroup.DeliveryGroup;
import DeliveryGroup.ResponseGroup;

public class AdapterRVDelivery extends RecyclerView.Adapter<AdapterRVDelivery.ViewHolder> {
    //region $MEMBERS
    private ResponseGroup listCountOrder;
    private LayoutInflater inflater;
    private Context mContext;
    private DeliveryActivity activity;
    //endregion

    //region $METHODS
    public AdapterRVDelivery(Context context, ResponseGroup listCountOrder, DeliveryActivity activity) {
        inflater = LayoutInflater.from(context);
        this.listCountOrder = listCountOrder;
        this.mContext = context;
        this.activity = activity;
    }
    //endregion

    //region $ADAPTER OVERRIDE METHODS
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_list_delivery, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        DeliveryGroup selectedModelExample = listCountOrder.getListItem().get(position);
        holder.setData(selectedModelExample, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.closePopupWindow(listCountOrder.getListItem().get(position).get_deliveryNo(), listCountOrder.getListItem().get(position).get_deliveryType());
            }
        });
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    @Override
    public int getItemCount() {
        return listCountOrder.getListItem().size();
    }
    //endregion

    //region $VIEWHOLDER CLASS
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDeliveryNo, txtStatus, txtDate, txtDeliveryDate;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            txtDeliveryNo = itemView.findViewById(R.id.txtDeliveryNo);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtDeliveryDate = itemView.findViewById(R.id.txtDeliveryDate);
        }

        public void setData(DeliveryGroup selectedModelExample, int position) {
            txtDeliveryNo.setText(selectedModelExample.get_deliveryNo());
            String iptalDurumuAck = "";
            switch (selectedModelExample.get_status()) {
                case -1:
                    iptalDurumuAck = mContext.getResources().getString(R.string.status_ready);
                    break;
                case 0:
                    iptalDurumuAck = mContext.getResources().getString(R.string.status_ready);
                    break;
                case 1:
                    iptalDurumuAck = mContext.getResources().getString(R.string.status_process);
                    break;
                case 2:
                    iptalDurumuAck = mContext.getResources().getString(R.string.status_finish);
                    break;
                default:
                    iptalDurumuAck = " ";
                    break;
            }
            txtStatus.setText(iptalDurumuAck);
            txtDate.setText(selectedModelExample.get_date());
            txtDeliveryDate.setText(selectedModelExample.get_deliveryDate());
        }
    }
    //endregion
}
