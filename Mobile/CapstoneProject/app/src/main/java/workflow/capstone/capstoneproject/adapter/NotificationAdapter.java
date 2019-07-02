package workflow.capstone.capstoneproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.entities.UserNotification;


public class NotificationAdapter extends BaseAdapter {

    private List<UserNotification> listData;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public NotificationAdapter(List<UserNotification> listData, Context mContext) {
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
            holder.tvMessage = convertView.findViewById(R.id.tv_message);
            holder.imgIsHandled = convertView.findViewById(R.id.img_is_handled);
            holder.lineView = convertView.findViewById(R.id.line_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserNotification userNotification = this.listData.get(position);
        holder.tvMessage.setText(userNotification.getMessage() + " from " + userNotification.getActorName());
        if (userNotification.getIsHandled()) {
            holder.imgIsHandled.setVisibility(View.VISIBLE);
        } else {
            holder.imgIsHandled.setVisibility(View.GONE);
        }
        if (position == getCount() - 1) {
            holder.lineView.setVisibility(View.GONE);
        } else {
            holder.lineView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvMessage;
        ImageView imgIsHandled;
        View lineView;
    }
}
