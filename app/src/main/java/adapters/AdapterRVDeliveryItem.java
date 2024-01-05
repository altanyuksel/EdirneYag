package adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.link.edirneyag.DeliveryActivity;
import com.link.edirneyag.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DeliveryGroup.DeliveryGroupItem;

public class AdapterRVDeliveryItem extends RecyclerView.Adapter<AdapterRVDeliveryItem.ViewHolder> {
    //region $MEMBERS
    public List<DeliveryGroupItem> listDelivery;
    private LayoutInflater inflater;
    private DeliveryActivity activity;
    private ViewHolder holder;
    public ArrayList<Integer> listReadedBarcodeList;
    private DecimalFormat dform = new DecimalFormat("#,###.####");
    AlertDialog.Builder errDialog;
    private Map<Integer, String> editTextValues = new HashMap<>(); // Satır pozisyonlarına göre EditText değerlerini saklamak için bir harita kullanıyoruz.
    //endregion

    //region $METHODS
    public AdapterRVDeliveryItem(List<DeliveryGroupItem> listCountOrder, DeliveryActivity activity) {
        this.listDelivery = listCountOrder;
        this.activity = activity;
        listReadedBarcodeList = new ArrayList<>();
        initErrorDialog();
    }
    //endregion

    //region $ADAPTER OVERRIDE METHODS
    @NonNull
    @Override
    public AdapterRVDeliveryItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_list_delivery_item, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        DeliveryGroupItem selectedModelExample = listDelivery.get(position);
        holder.setData(selectedModelExample, position);
        if (listReadedBarcodeList.contains(position)) {
//            holder.txtMalkodu.setBackgroundColor(Color.YELLOW);
            holder.txtMalAdi.setBackgroundColor(Color.YELLOW);
            holder.txtMiktar.setBackgroundColor(Color.YELLOW);
            holder.txtSayimMiktar.setBackgroundColor(Color.YELLOW);
        } else {
//            holder.txtMalkodu.setBackgroundColor(Color.WHITE);
            holder.txtMalAdi.setBackgroundColor(Color.WHITE);
            holder.txtMiktar.setBackgroundColor(Color.WHITE);
            holder.txtSayimMiktar.setBackgroundColor(Color.WHITE);
        }

        // Ardından yeşil boyama işlemi
        if (selectedModelExample.getMiktar() == selectedModelExample.getMiktar2()) {
//            holder.txtMalkodu.setBackgroundColor(Color.GREEN);
            holder.txtMalAdi.setBackgroundColor(Color.GREEN);
            holder.txtMiktar.setBackgroundColor(Color.GREEN);
            holder.txtSayimMiktar.setBackgroundColor(Color.GREEN);
        }
    }

    public void setSayimMiktar(int index, float sayimMiktar, String seriNo){
        DeliveryGroupItem selectedModelExample = listDelivery.get(index);
        if (sayimMiktar <= selectedModelExample.getMiktar()){
            selectedModelExample.setMiktar2(sayimMiktar);
            notifyItemChanged(index);
        } else {
            errDialog.setTitle(activity.getApplicationContext().getResources().getString(R.string.error));
            errDialog.setMessage(activity.getApplicationContext().getText(R.string.error_counting));
            errDialog.setNeutralButton(activity.getApplicationContext().getText(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            errDialog.show();
        }
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
        return listDelivery.size();
    }
    //endregion

    //region $VIEWHOLDER CLASS
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView txtMalAdi, txtMiktar, txtSayimMiktar;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews();
            itemView.setOnLongClickListener(this);
        }

        private void initViews() {
//            txtMalkodu = itemView.findViewById(R.id.txtMalKodu);
            txtMalAdi = itemView.findViewById(R.id.txtMalAdi);
            txtMiktar = itemView.findViewById(R.id.txtMiktar);
            txtSayimMiktar = itemView.findViewById(R.id.txtSayimMiktar);
        }

        public void setData(DeliveryGroupItem selectedModelExample, int position) {
//            txtMalkodu.setText(selectedModelExample.getMateryalKodu());
            txtMalAdi.setText(selectedModelExample.getMateryalAdi());
            txtMiktar.setText(dform.format(selectedModelExample.getMiktar()));
            txtSayimMiktar.setText(dform.format(selectedModelExample.getMiktar2()));
        }

        @Override
        public boolean onLongClick(View view) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//            String message = activity.getApplicationContext().getString(R.string.select_row);
//            builder.setMessage(message);
//            builder.setNeutralButton(activity.getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int i) {
//                    try {
//                        String touchedBarcode = listDelivery.get(getAdapterPosition()).getBarkodNo().get(0);
//                        float quantity = listDelivery.get(getAdapterPosition()).getMiktar2();
//                        activity.setDeliveryRows(touchedBarcode,quantity, listDelivery.get(getAdapterPosition()).getSeriNo());
////                        Toast.makeText(view.getContext(), "Position is " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            builder.setNegativeButton(activity.getApplicationContext().getString(R.string.no), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
            return true;
        }
    }
    //endregion
}