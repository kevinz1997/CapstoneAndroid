package workflow.capstone.capstoneproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.adapter.CommentAdapter;
import workflow.capstone.capstoneproject.entities.Comment;
import workflow.capstone.capstoneproject.entities.Connection;
import workflow.capstone.capstoneproject.entities.HandleFormRequest;
import workflow.capstone.capstoneproject.entities.Profile;
import workflow.capstone.capstoneproject.entities.RequestValue;
import workflow.capstone.capstoneproject.repository.CapstoneRepository;
import workflow.capstone.capstoneproject.repository.CapstoneRepositoryImpl;
import workflow.capstone.capstoneproject.utils.CallBackData;
import workflow.capstone.capstoneproject.utils.ConstantDataManager;
import workflow.capstone.capstoneproject.utils.DynamicWorkflowSharedPreferences;
import workflow.capstone.capstoneproject.utils.FragmentUtils;

public class HandleRequestFragment extends Fragment {

    private ImageView imgSendComment;
    private EditText edtComment;
    private CapstoneRepository capstoneRepository;
    private String stringComment;
    private String fullName;
    private ListView listViewComment;
    private List<Comment> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private ImageView imgBack;
    private LinearLayout lnButton;
    private String token = null;
    private String initiatorFullName;
    private TextView tvFromName;
    private TextView tvReason;

    public HandleRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handle_request, container, false);

        initView(view);

        imgSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListComment();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.back(getActivity());
            }
        });

        token = DynamicWorkflowSharedPreferences.getStoreJWT(getContext(), ConstantDataManager.AUTHORIZATION_TOKEN);

        final Bundle bundle = getArguments();
        getHandleForm(bundle.getString("requestActionID"));

        return view;
    }

    private void initView(View view) {
        imgSendComment = view.findViewById(R.id.img_send_comment);
        edtComment = view.findViewById(R.id.edt_comment);
        listViewComment = view.findViewById(R.id.list_comment);
        imgBack = view.findViewById(R.id.img_Back);
        lnButton = view.findViewById(R.id.ln_button);
        tvFromName = view.findViewById(R.id.tv_from_name);
        tvReason = view.findViewById(R.id.tv_reason);
    }

    private void updateListComment() {
        stringComment = edtComment.getText().toString();

        String token = DynamicWorkflowSharedPreferences.getStoreJWT(getContext(), ConstantDataManager.AUTHORIZATION_TOKEN);
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.getProfile(token, new CallBackData<List<Profile>>() {
            @Override
            public void onSuccess(List<Profile> profiles) {
                Profile profile = profiles.get(0);
                fullName = profile.getFullName();
                Comment comment = new Comment(stringComment, fullName);
                edtComment.setText("");
                commentList.add(comment);
                commentAdapter = new CommentAdapter(commentList, getContext());
                commentAdapter.notifyDataSetChanged();
                listViewComment.setAdapter(commentAdapter);
                listViewComment.setVisibility(View.VISIBLE);
                setListViewHeightBasedOnChildren(listViewComment);
                setOnLongClick(listViewComment);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    private void setOnLongClick(ListView listView) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toasty.success(getContext(), "This is long click", Toasty.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void getHandleForm(String requestActionID) {
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.getRequestHandleForm(token, requestActionID, new CallBackData<HandleFormRequest>() {
            @Override
            public void onSuccess(HandleFormRequest handleFormRequest) {
                final List<Connection> connectionList = handleFormRequest.getConnections();
//                getAccountByUserID(handleFormRequest.getRequest().getInitiatorID());
                capstoneRepository = new CapstoneRepositoryImpl();
                capstoneRepository.getAccountByUserID(handleFormRequest.getRequest().getInitiatorID(), new CallBackData<List<Profile>>() {
                    @Override
                    public void onSuccess(List<Profile> profiles) {
                        tvFromName.setText(profiles.get(0).getEmail());
                    }

                    @Override
                    public void onFail(String message) {

                    }
                });

                List<RequestValue> requestValueList = handleFormRequest.getUserRequestAction().getRequestValues();
                for (RequestValue requestValue : requestValueList) {
                    if (requestValue.getKey().equals("text")) {
                        tvReason.setText(requestValue.getValue());
                    }
                }
                for (final Connection connection : connectionList) {
                    Button btn = new Button(getContext());
                    btn.setText(connection.getConnectionTypeName());
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toasty.success(getContext(), connection.getNextStepID(), Toasty.LENGTH_SHORT).show();
                        }
                    });
                    lnButton.addView(btn);
                }
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    private void getAccountByUserID(String ID) {
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.getAccountByUserID(ID, new CallBackData<List<Profile>>() {
            @Override
            public void onSuccess(List<Profile> profiles) {
                initiatorFullName = profiles.get(0).getFullName();
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
