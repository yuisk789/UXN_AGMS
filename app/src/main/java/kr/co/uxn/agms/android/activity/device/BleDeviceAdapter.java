package kr.co.uxn.agms.android.activity.device;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import kr.co.uxn.agms.android.R;

public class BleDeviceAdapter extends RecyclerView.Adapter<BleDeviceAdapter.BleDeviceViewHolder> {

    List<BleAdapterItem> mList = new ArrayList<>();

    private OnItemClickListener mListener;
    private Context ctx;

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public BleDeviceAdapter(final Context context){
        ctx = context;
    }
    public void addDeviceItem(BleAdapterItem item){
        if(item==null){
            return;
        }
        if(mList!=null && !mList.isEmpty()){
            boolean isDuplicate = false;
            for(BleAdapterItem tmp : mList){
                if(tmp.getUuid()!=null && item.getUuid()!=null && tmp.getUuid().equalsIgnoreCase(item.getUuid())){
                    isDuplicate = true;
                    tmp.setRssi(item.getRssi());
                }
            }
            if(!isDuplicate){
                mList.add(item);
            }
        } else {
            mList.add(item);
        }
        notifyDataSetChanged();
    }

    public void setData(List<BleAdapterItem> list){
        if(list!=null && !list.isEmpty()){
            mList.addAll(list);
            mList.sort(new Comparator<BleAdapterItem>() {
                @Override
                public int compare(BleAdapterItem o1, BleAdapterItem o2) {
                    if(o1.getUuid()!=null && o2.getUuid()!=null){
                        return o1.getUuid().toLowerCase().compareTo(o2.getUuid().toLowerCase());
                    } else if(o1.getUuid()!=null){
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        } else {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public BleDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        // set the view's size, margins, paddings and layout parameters
        BleDeviceViewHolder vh = new BleDeviceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BleDeviceViewHolder holder, int position) {
        final BleAdapterItem item = mList.get(position);
        final int finalPosition = position;
        if(!TextUtils.isEmpty(item.getName())){
            holder.name.setText(item.getName());
        } else {
            holder.name.setText(ctx.getResources().getString(R.string.ble_device_name_none));
        }
        holder.address.setText(item.getUuid());
        if(item.getRssi()<0){
            holder.rssi.setText("rssi:"+ String.valueOf(item.getRssi()));
        } else {
            holder.rssi.setText("rssi:-");
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                },200);
                if(mListener!=null){
                    mListener.onItemClick(v,item,finalPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class BleDeviceViewHolder extends RecyclerView.ViewHolder {

        public TextView address;
        public TextView name;
        public TextView rssi;
        public View parent;

        public BleDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;
            address = itemView.findViewById(R.id.address);
            name = itemView.findViewById(R.id.name);
            rssi = itemView.findViewById(R.id.rssi);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, BleAdapterItem obj, int position);
    }
}
