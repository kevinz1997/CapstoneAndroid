package workflow.capstone.capstoneproject.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.activity.LoginActivity;
import workflow.capstone.capstoneproject.activity.MainActivity;
import workflow.capstone.capstoneproject.entities.Profile;
import workflow.capstone.capstoneproject.repository.CapstoneRepository;
import workflow.capstone.capstoneproject.repository.CapstoneRepositoryImpl;
import workflow.capstone.capstoneproject.utils.CallBackData;
import workflow.capstone.capstoneproject.utils.ConstantDataManager;
import workflow.capstone.capstoneproject.utils.DynamicWorkflowSharedPreferences;
import workflow.capstone.capstoneproject.utils.FragmentUtils;

public class ProfileFragment extends Fragment {

    private LinearLayout lnViewProfile;
    private LinearLayout lnChangePassword;
    private LinearLayout lnSignOut;
    private CapstoneRepository capstoneRepository;
    String token;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        lnViewProfile = view.findViewById(R.id.ln_view_profile);
        lnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfile();
            }
        });

        lnChangePassword = view.findViewById(R.id.ln_change_password);
        lnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        lnSignOut = view.findViewById(R.id.ln_sign_out);
        lnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        return view;
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.sign_out, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DynamicWorkflowSharedPreferences.removeJWT(getContext());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void viewProfile() {
        FragmentUtils.changeFragment(getActivity(), R.id.main_frame, new ViewProfileFragment());
    }

    private void changePassword() {
        FragmentUtils.changeFragment(getActivity(), R.id.main_frame, new ChangePasswordFragment());
    }

}
