package workflow.capstone.capstoneproject.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.adapter.NotificationAdapter;
import workflow.capstone.capstoneproject.entities.Notification;
import workflow.capstone.capstoneproject.repository.CapstoneRepository;
import workflow.capstone.capstoneproject.repository.CapstoneRepositoryImpl;
import workflow.capstone.capstoneproject.utils.CallBackData;
import workflow.capstone.capstoneproject.utils.ConstantDataManager;
import workflow.capstone.capstoneproject.utils.DynamicWorkflowSharedPreferences;

public class NotificationFragment extends Fragment {

    private CapstoneRepository capstoneRepository;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private ListView listView;
    private boolean areLoadNotification = false;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
//        loadNotifications(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !areLoadNotification) {
            //loadNotifications();
            areLoadNotification = true;
        }
    }

    private void loadNotifications() {
        String token = DynamicWorkflowSharedPreferences.getStoreJWT(getActivity(), ConstantDataManager.AUTHORIZATION_TOKEN);
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.getNotification(token, new CallBackData<List<Notification>>() {
            @Override
            public void onSuccess(List<Notification> notifications) {
                listView = getView().findViewById(R.id.list);
                notificationList = notifications;
                notificationAdapter = new NotificationAdapter(notificationList, getContext());
                listView.setAdapter(notificationAdapter);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

}
