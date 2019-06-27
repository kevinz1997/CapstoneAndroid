package workflow.capstone.capstoneproject.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import workflow.capstone.capstoneproject.api.Request;

public interface DynamicWorkflowServices {
    @Headers({"Content-Type:application/json"})
    @POST(ConfigApi.Api.LOGIN)
    Call<ResponseBody> login(@Body Map<String, String> fields);

    @GET(ConfigApi.Api.GET_PROFILE)
    Call<ResponseBody> getProfile();

    @GET(ConfigApi.Api.GET_WORKFLOW)
    Call<ResponseBody> getWorkflow();

    @GET(ConfigApi.Api.GET_NUMBER_NOTIFICATION)
    Call<ResponseBody> getNumberNotification();

    @GET(ConfigApi.Api.GET_NOTIFICATION)
    Call<ResponseBody> getNotification();

    @Headers({"Content-Type:application/json"})
    @POST(ConfigApi.Api.POST_REQUEST)
    Call<ResponseBody> postRequest(@Body Request request);

    @Multipart
    @POST(ConfigApi.Api.POST_REQUEST_FILE)
    Call<ResponseBody> upFile(
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST(ConfigApi.Api.POST_REQUEST_FILE)
    Call<ResponseBody> uploadMultipleFile(
            @Part MultipartBody.Part[] files
    );

    @POST(ConfigApi.Api.FORGOT_PASSWORD)
    Call<ResponseBody> forgotPassword(@Field("email") String email);

    @PUT(ConfigApi.Api.CONFIRM_FORGOT_PASSWORD)
    Call<ResponseBody> confirmForgotPassword(@Field("code") String code,
                                             @Field("email") String email,
                                             @Field("password") String password);
}
