package workflow.capstone.capstoneproject.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import es.dmoral.toasty.Toasty;
import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.adapter.TabAdapter;
import workflow.capstone.capstoneproject.fragment.NotificationFragment;
import workflow.capstone.capstoneproject.fragment.ProfileFragment;
import workflow.capstone.capstoneproject.fragment.RequestHistoryFragment;
import workflow.capstone.capstoneproject.fragment.WorkflowFragment;
import workflow.capstone.capstoneproject.repository.CapstoneRepository;
import workflow.capstone.capstoneproject.repository.CapstoneRepositoryImpl;
import workflow.capstone.capstoneproject.utils.CallBackData;
import workflow.capstone.capstoneproject.utils.ConstantDataManager;
import workflow.capstone.capstoneproject.utils.DynamicWorkflowSharedPreferences;
import workflow.capstone.capstoneproject.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private CapstoneRepository capstoneRepository;

    private TabLayout tabLayout;
    private TextView badge;
    private ImageView imageView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private WorkflowFragment workflowFragment;
    private RequestHistoryFragment requestHistoryFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabLayout();
        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        tabLayout.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                    }
                });
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tab_Layout);
        workflowFragment = new WorkflowFragment();
        requestHistoryFragment = new RequestHistoryFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();
        for (int i = 0; i < 4; i++) {
            tabLayout.addTab(tabLayout.newTab());
        }

        setupTabIcons();
        setOnChangeTab();
        setCurrentTabFragment(0);
    }

    private void setupTabIcons() {
        //workflow tab
        tabLayout.getTabAt(0).setIcon(tabIcons[1]);

        //history tab
        tabLayout.getTabAt(1).setIcon(tabIcons[0]);

        //notification tab
        tabLayout.getTabAt(2).setCustomView(R.layout.notification_icon_grey);
        View view = tabLayout.getTabAt(2).getCustomView();
        imageView = view.findViewById(R.id.notification_icon);
        imageView.setImageResource(R.drawable.ic_notification_grey);
        badge = view.findViewById(R.id.notification_badge);
        String tokenAuthorize = DynamicWorkflowSharedPreferences.getStoreJWT(context, ConstantDataManager.AUTHORIZATION_TOKEN);
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.getNumberOfNotification(tokenAuthorize, new CallBackData<String>() {
            @Override
            public void onSuccess(String s) {
                if (Integer.parseInt(s) > 0) {
                    badge.setText(s);
                    badge.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(String message) {

            }
        });

        //profile tab
        tabLayout.getTabAt(3).setIcon(tabIcons[4]);
    }

    private int[] tabIcons = {
            R.drawable.ic_history_grey,
            R.drawable.ic_history_blue,
            R.drawable.ic_notification_grey,
            R.drawable.ic_notification_blue,
            R.drawable.ic_menu_grey,
            R.drawable.ic_menu_blue
    };

    private void setOnChangeTab() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIndex = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(tabIndex).setIcon(tabIcons[0]);
                        break;
                    case 1:
                        tabLayout.getTabAt(tabIndex).setIcon(tabIcons[0]);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.ic_notification_grey);
                        break;
                    case 3:
                        tabLayout.getTabAt(tabIndex).setIcon(tabIcons[4]);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition) {
            case 0:
                tabLayout.getTabAt(tabPosition).setIcon(tabIcons[1]);
                replaceFragment(workflowFragment);
                break;
            case 1:
                tabLayout.getTabAt(tabPosition).setIcon(tabIcons[1]);
                replaceFragment(requestHistoryFragment);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_notification_blue);
                badge.setVisibility(View.INVISIBLE);
                replaceFragment(notificationFragment);
                break;
            case 3:
                tabLayout.getTabAt(tabPosition).setIcon(tabIcons[5]);
                replaceFragment(profileFragment);
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager = MainActivity.this.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
