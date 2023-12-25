package adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.link.edirneyag.DeliveryActivity;
import com.link.edirneyag.R;

import java.text.DecimalFormat;
import java.util.List;

import Models.Delivery.Palet;

public class AdapterRVPalet2 extends RecyclerView.Adapter<AdapterRVPalet2.ViewHolder> {
    //region $MEMBERS
    public List<Palet> palets;;
    private LayoutInflater inflater;
    private DeliveryActivity activity;
    private AdapterRVPalet2.ViewHolder holder;
    private DecimalFormat dform = new DecimalFormat("#,###.####");
    AlertDialog.Builder errDialog;
    //endregion

    //region $METHODS
    public AdapterRVPalet2(List<Palet> listCountOrder, DeliveryActivity activity) {
        this.palets = listCountOrder;
        this.activity = activity;
        initErrorDialog();
    }
    //endregion

    //region $ADAPTER OVERRIDE METHODS
    @NonNull
    @Override
    public AdapterRVPalet2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_list_palet2, parent, false);
        holder = new AdapterRVPalet2.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(AdapterRVPalet2.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setData(palets.get(position));

        holder.btnPaletSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tıklama durumunda satırı sil
                palets.remove(position);
                notifyDataSetChanged(); // Değişikliği adapter'a bildirerek liste yeniden yüklensin
            }
        });
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
        return palets.size();
    }
    //endregion

    //region $VIEWHOLDER CLASS
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtPaletNo, txtPaletMiktar;
        private Button btnPaletSil;
        public ViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            txtPaletNo = itemView.findViewById(R.id.txtPaletNo);
            txtPaletMiktar = itemView.findViewById(R.id.txtPaletMiktarPlt);
            btnPaletSil = itemView.findViewById(R.id.btnPaletSil);
        }

        public void setData(Palet selectedModelExample) {
            txtPaletNo.setText(selectedModelExample.getSeriNo());
            txtPaletMiktar.setText(String.valueOf(selectedModelExample.getMiktar()));
        }
    }
    //endregion
}