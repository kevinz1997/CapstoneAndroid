package workflow.capstone.capstoneproject.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.adapter.CommentAdapter;
import workflow.capstone.capstoneproject.adapter.HandleFileNameAdapter;
import workflow.capstone.capstoneproject.api.ActionValue;
import workflow.capstone.capstoneproject.api.RequestApprove;
import workflow.capstone.capstoneproject.entities.Comment;
import workflow.capstone.capstoneproject.entities.Connection;
import workflow.capstone.capstoneproject.entities.FormRequest;
import workflow.capstone.capstoneproject.entities.Profile;
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
    private HandleFileNameAdapter handleFileNameAdapter;

    private List<String> fileUrl = new ArrayList<>();
    private ImageView imgBack;
    private LinearLayout lnButton;
    private String token = null;
    private TextView tvInitiatorName;
    private TextView tvWorkFlowName;
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
        tvInitiatorName = view.findViewById(R.id.tv_initiator_name);
        tvWorkFlowName = view.findViewById(R.id.tv_name_of_workflow);
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
        handleFileNameAdapter = new HandleFileNameAdapter(getContext(), fileNameList);
        listViewFileName.setAdapter(handleFileNameAdapter);
        DynamicWorkflowUtils.setListViewHeightBasedOnChildren(listViewFileName);
        setOnClickListViewFileName(listViewFileName);
    }

    private void setOnClickListViewFileName(final ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String fileNameUrl = fileUrl.get(position);
                String imageName = fileNameList.get(position);
                String imageExtension = fileNameUrl.substring(fileNameUrl.lastIndexOf(".") + 1);
                boolean checkIsImage = DynamicWorkflowUtils.accept(imageExtension);

                if (checkIsImage) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.image_dialog, null);
                    dialog.setView(dialogLayout);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface d) {
                            ImageView image =  dialog.findViewById(R.id.imageDialog);

                            Picasso.get()
                                    .load(fileNameUrl)
                                    .into(image);
//                            image.setBackground(getResources().getDrawable(R.drawable.ic_notification_grey));

                            float imageWidthInPX = (float)image.getWidth();

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            image.setLayoutParams(layoutParams);


                        }
                    });
                    dialog.show();
                } else {
                    downloadFile(fileNameUrl, imageName);
                }
            }
        });
    }

    private void getHandleForm(String requestActionID) {
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.getRequestHandleForm(token, requestActionID, new CallBackData<FormRequest>() {
            @Override
            public void onSuccess(final FormRequest formRequest) {
                final List<Connection> connectionList = formRequest.getConnections();

                //set InitiatorName
                tvInitiatorName.setText(formRequest.getInitiatorName());

                //set Workflow Name
                tvWorkFlowName.setText(formRequest.getWorkFlowTemplateName());

                //get message
                List<RequestValue> requestValueUserList = formRequest.getUserRequestAction().getRequestValues();
                for (RequestValue requestValue : requestValueUserList) {
                    if (requestValue.getKey().equals("text")) {
                        tvReason.setText(requestValue.getValue());
                    }
                }

                //get comment
                List<StaffRequestAction> staffRequestActionList = formRequest.getStaffRequestActions();
                for (StaffRequestAction staffRequestAction : staffRequestActionList) {
                    List<RequestValue> requestValueStaffList = staffRequestAction.getRequestValues();
                    for (RequestValue requestValue : requestValueStaffList) {
                        commentList.add(new Comment(requestValue.getValue(), DynamicWorkflowUtils.mapNameWithUserName(staffRequestAction.getFullName(), staffRequestAction.getUserName())));
                    }
                }
                getListComment();


                //get file path
                List<RequestFile> requestFileList = formRequest.getUserRequestAction().getRequestFiles();
                for (RequestFile requestFile : requestFileList) {
                    String fileName = requestFile.getPath().substring(requestFile.getPath().lastIndexOf("\\") + 1);
                    fileNameList.add(fileName);
                    fileUrl.add(ConstantDataManager.IMAGE_URL + requestFile.getPath().replace("\\", "/"));
                }

                initFileNameListView();

                //get dynamic button
                for (final Connection connection : connectionList) {
                    Button btn = new Button(getContext());
                    btn.setText(connection.getConnectionTypeName());
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleRequest(formRequest.getRequest().getId(), connection.getNextStepID(), 2);
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

    private void downloadFile(String fileUrl, final String name) {
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.downloadFileWithDynamicUrlSync(fileUrl, new CallBackData<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody responseBody) {
                boolean writtenToDisk = writeResponseBodyToDisk(responseBody, name);
                if (writtenToDisk) {
                    Toasty.success(getContext(), "Download file success!", Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.error(getContext(), "Download file fail!", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String name) {
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File (root.getAbsolutePath() + "/download");
            dir.mkdirs();
            File file = new File(dir, name);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Toasty.success(getContext(), "file download: " + fileSizeDownloaded + " of " + fileSize, Toasty.LENGTH_SHORT).show();
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
