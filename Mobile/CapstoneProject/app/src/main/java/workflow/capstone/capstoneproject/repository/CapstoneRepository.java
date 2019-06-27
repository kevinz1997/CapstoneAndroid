package workflow.capstone.capstoneproject.repository;

import android.content.Context;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import workflow.capstone.capstoneproject.entities.Login;
import workflow.capstone.capstoneproject.entities.Notification;
import workflow.capstone.capstoneproject.entities.Profile;
import workflow.capstone.capstoneproject.api.Request;
import workflow.capstone.capstoneproject.entities.WorkflowTemplate;
import workflow.capstone.capstoneproject.utils.CallBackData;

public interface CapstoneRepository {
    void login(Context context, Map<String, String> fields, CallBackData<Login> callBackData);
    void getProfile(String token, CallBackData<Profile> callBackData);
    void getWorkflow(String token, CallBackData<List<WorkflowTemplate>> callBackData);
    void getNumberOfNotification(String token, CallBackData<String> callBackData);
    void getNotification(String token, CallBackData<List<Notification>> callBackData);
    void postRequest(String token, Request request, CallBackData<String> callBackData);
    void postRequestFile(String token, MultipartBody.Part file, CallBackData<String[]> callBackData);
    void postMultipleRequestFile(String token, MultipartBody.Part[] files, CallBackData<String[]> callBackData);

    void forgotPassword(String email, CallBackData<String> callBackData);
    void confirmForgotPassword(String code, String email, String newPassword, CallBackData<String> callBackData);
}
