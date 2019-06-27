package workflow.capstone.capstoneproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.entities.Notification;


public class NotificationAdapter extends BaseAdapter {

    private List<Notification> listData;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public NotificationAdapter(List<Notification> listData, Context mContext) {
        this.listData = listData;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.notification_listview, null);
            holder = new ViewHolder();
            holder.tvApprover = convertView.findViewById(R.id.tv_Approver);
            holder.tvMessage = convertView.findViewById(R.id.tv_Message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Notification notification = this.listData.get(position);
        holder.tvApprover.setText(notification.getApproverName());
        holder.tvMessage.setText(notification.getMessage());
        return convertView;
    }

    private class ViewHolder {
        TextView tvApprover;
        TextView tvMessage;
    }
}
