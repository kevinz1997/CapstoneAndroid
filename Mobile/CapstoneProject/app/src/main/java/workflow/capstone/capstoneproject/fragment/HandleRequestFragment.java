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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.adapter.CommentAdapter;
import workflow.capstone.capstoneproject.adapter.ListFileNameAdapter;
import workflow.capstone.capstoneproject.api.ActionValue;
import workflow.capstone.capstoneproject.api.RequestApprove;
import workflow.capstone.capstoneproject.entities.Comment;
import workflow.capstone.capstoneproject.entities.Connection;
import workflow.capstone.capstoneproject.entities.HandleFormRequest;
import workflow.capstone.capstoneproject.entities.Profile;
import workflow.capstone.capstoneproject.entities.Request;
import workflow.capstone.capstoneproject.entities.RequestFile;
import workflow.capstone.capstoneproject.entities.RequestValue;
import workflow.capstone.capstoneproject.entities.StaffRequestAction;
import workflow.capstone.capstoneproject.repository.CapstoneRepository;
import workflow.capstone.capstoneproject.repository.CapstoneRepositoryImpl;
import workflow.capstone.capstoneproject.utils.CallBackData;
import workflow.capstone.capstoneproject.utils.ConstantDataManager;
import workflow.capstone.capstoneproject.utils.DynamicWorkflowSharedPreferences;
import workflow.capstone.capstoneproject.utils.DynamicWorkflowUtils;
import workflow.capstone.capstoneproject.utils.FragmentUtils;
import workflow.capstone.capstoneproject.utils.KProgressHUDManager;

public class HandleRequestFragment extends Fragment {

    private ImageView imgSendComment;
    private EditText edtComment;
    private CapstoneRepository capstoneRepository;
    private String stringComment;
    private String fullName;
    private ListView listViewComment;
    private List<Comment> commentList = new ArrayList<>();
    private List<String> stringCommentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private ListView listViewFileName;
    private List<String> fileNameList = new ArrayList<>();
    private ListFileNameAdapter listFileNameAdapter;
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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.back(getActivity());
            }
        });

        token = DynamicWorkflowSharedPreferences.getStoreJWT(getContext(), ConstantDataManager.AUTHORIZATION_TOKEN);

        final Bundle bundle = getArguments();
        getHandleForm(bundle.getString("requestActionID"));

        imgSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListComment();
            }
        });

        return view;
    }

    private void initView(View view) {
        imgSendComment = view.findViewById(R.id.img_send_comment);
        edtComment = view.findViewById(R.id.edt_comment);
        listViewComment = view.findViewById(R.id.list_comment);
        listViewFileName = view.findViewById(R.id.list_file_name);
        imgBack = view.findViewById(R.id.img_Back);
        lnButton = view.findViewById(R.id.ln_button);
        tvFromName = view.findViewById(R.id.tv_from_name);
        tvReason = view.findViewById(R.id.tv_reason);
    }

    private void updateListComment() {
        stringComment = edtComment.getText().toString();
        stringCommentList.add(stringComment);

        Profile profile = DynamicWorkflowSharedPreferences.getStoredData(getContext(), ConstantDataManager.PROFILE_KEY, ConstantDataManager.PROFILE_NAME);
        fullName = profile.getFullName();
        Comment comment = new Comment(stringComment, fullName);
        edtComment.setText("");
        commentList.add(comment);
        commentAdapter = new CommentAdapter(commentList, getContext());
        commentAdapter.notifyDataSetChanged();
        listViewComment.setAdapter(commentAdapter);
        listViewComment.setVisibility(View.VISIBLE);
        DynamicWorkflowUtils.setListViewHeightBasedOnChildren(listViewComment);
        setListViewCommentOnLongClick(listViewComment);
    }

    private void getListComment() {
        commentAdapter = new CommentAdapter(commentList, getContext());
        commentAdapter.notifyDataSetChanged();
        listViewComment.setAdapter(commentAdapter);
        listViewComment.setVisibility(View.VISIBLE);
        DynamicWorkflowUtils.setListViewHeightBasedOnChildren(listViewComment);
    }

    private void setListViewCommentOnLongClick(ListView listView) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toasty.success(getContext(), "This is long click", Toasty.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initFileNameListView() {
        listFileNameAdapter = new ListFileNameAdapter(getContext(), fileNameList);
        listViewFileName.setAdapter(listFileNameAdapter);
        DynamicWorkflowUtils.setListViewHeightBasedOnChildren(listViewFileName);
    }

    private void getHandleForm(String requestActionID) {
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.getRequestHandleForm(token, requestActionID, new CallBackData<HandleFormRequest>() {
            @Override
            public void onSuccess(final HandleFormRequest handleFormRequest) {
                final List<Connection> connectionList = handleFormRequest.getConnections();
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

                //get message
                List<RequestValue> requestValueUserList = handleFormRequest.getUserRequestAction().getRequestValues();
                for (RequestValue requestValue : requestValueUserList) {
                    if (requestValue.getKey().equals("text")) {
                        tvReason.setText(requestValue.getValue());
                    }
                }

                //get comment
                List<StaffRequestAction> staffRequestActionList = handleFormRequest.getStaffRequestActions();
                for (StaffRequestAction staffRequestAction : staffRequestActionList) {
                    List<RequestValue> requestValueStaffList = staffRequestAction.getRequestValues();
                    for (RequestValue requestValue : requestValueStaffList) {
                        commentList.add(new Comment(requestValue.getValue(), staffRequestAction.getName()));
                    }
                }
                getListComment();


                //get file path
                List<RequestFile> requestFileList = handleFormRequest.getUserRequestAction().getRequestFiles();
                for (RequestFile requestFile : requestFileList) {
                    fileNameList.add(requestFile.getPath());
                }

                initFileNameListView();

                //get dynamic button
                for (final Connection connection : connectionList) {
                    Button btn = new Button(getContext());
                    btn.setText(connection.getConnectionTypeName());
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleRequest(handleFormRequest.getRequest().getId(), connection.getNextStepID(), 2);
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

    private void handleRequest(String requestID, String nextStepID, int status) {
        List<ActionValue> actionValueList = new ArrayList<>();
        for (int i = 0; i < stringCommentList.size(); i++) {
            actionValueList.add(new ActionValue("comment " + i, stringCommentList.get(i)));
        }

        RequestApprove requestApprove = new RequestApprove();
        requestApprove.setRequestID(requestID);
        requestApprove.setNextStepID(nextStepID);
        requestApprove.setStatus(status);
        requestApprove.setActionValues(actionValueList);

        final KProgressHUD progressHUD = KProgressHUDManager.showProgressBar(getContext());
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.approveRequest(token, requestApprove, new CallBackData<String>() {
            @Override
            public void onSuccess(String s) {
                FragmentUtils.back(getActivity());
                progressHUD.dismiss();
                Toasty.success(getContext(), R.string.request_sent, Toasty.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String message) {
                progressHUD.dismiss();
                Toasty.error(getContext(), message, Toasty.LENGTH_SHORT).show();
            }
        });
    }

}
