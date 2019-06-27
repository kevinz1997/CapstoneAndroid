package workflow.capstone.capstoneproject.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import workflow.capstone.capstoneproject.entities.Login;
import workflow.capstone.capstoneproject.entities.Notification;
import workflow.capstone.capstoneproject.entities.Profile;
import workflow.capstone.capstoneproject.api.Request;
import workflow.capstone.capstoneproject.entities.WorkflowTemplate;
import workflow.capstone.capstoneproject.retrofit.ClientApi;
import workflow.capstone.capstoneproject.utils.CallBackData;
import workflow.capstone.capstoneproject.utils.KProgressHUDManager;

public class CapstoneRepositoryImpl implements CapstoneRepository {
    ClientApi clientApi = new ClientApi();

    @Override
    public void login(final Context context, Map<String, String> fields, final CallBackData<Login> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDynamicWorkflowServices().login(fields);
        Log.e("URL=", clientApi.getDynamicWorkflowServices().login(fields).request().url().toString());
        //show progress bar
        final KProgressHUD khub = KProgressHUDManager.showProgressBar(context);

        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //close progress bar
                KProgressHUDManager.dismiss(context, khub);
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<Login>() {}.getType();
                            Login responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            callBackData.onFail(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if(response.code() == 400) {
                    try {
                        callBackData.onFail(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(t.getMessage());
            }
        });
    }

    @Override
    public void getProfile(String token, final CallBackData<Profile> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDWServices(token).getProfile();
        Log.e("URL=", clientApi.getDWServices(token).getProfile().request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<Profile>() {
                            }.getType();
                            Profile responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }

    @Override
    public void getWorkflow(String token, final CallBackData<List<WorkflowTemplate>> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDWServices(token).getWorkflow();
        Log.e("URL=", clientApi.getDWServices(token).getWorkflow().request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<List<WorkflowTemplate>>() {
                            }.getType();
                            List<WorkflowTemplate> responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }

    @Override
    public void getNumberOfNotification(String token, final CallBackData<String> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDWServices(token).getNumberNotification();
        Log.e("URL=", clientApi.getDWServices(token).getNumberNotification().request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<String>() {
                            }.getType();
                            String responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }

    @Override
    public void getNotification(String token, final CallBackData<List<Notification>> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDWServices(token).getNotification();
        Log.e("URL=", clientApi.getDWServices(token).getNotification().request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<List<Notification>>() {
                            }.getType();
                            List<Notification> responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }

    @Override
    public void postRequest(String token, Request request, final CallBackData<String> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDWServices(token).postRequest(request);
        Log.e("URL=", clientApi.getDWServices(token).postRequest(request).request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 201) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<String>() {
                            }.getType();
                            String responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }

    @Override
    public void postRequestFile(String token, MultipartBody.Part file, final CallBackData<String[]> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDWServices(token).upFile(file);
        Log.e("URL=", clientApi.getDWServices(token).upFile(file).request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<String[]>() {
                            }.getType();
                            String[] responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }

    @Override
    public void postMultipleRequestFile(String token, MultipartBody.Part[] files, final CallBackData<String[]> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDWServices(token).uploadMultipleFile(files);
        Log.e("URL=", clientApi.getDWServices(token).uploadMultipleFile(files).request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<String[]>() {
                            }.getType();
                            String[] responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }

    @Override
    public void forgotPassword(String email, final CallBackData<String> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDynamicWorkflowServices().forgotPassword(email);
        Log.e("URL=", clientApi.getDynamicWorkflowServices().forgotPassword(email).request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<String>() {
                            }.getType();
                            String responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }

    @Override
    public void confirmForgotPassword(String code, String email, String newPassword, final CallBackData<String> callBackData) {
        Call<ResponseBody> serviceCall = clientApi.getDynamicWorkflowServices().confirmForgotPassword(code, email, newPassword);
        Log.e("URL=", clientApi.getDynamicWorkflowServices().confirmForgotPassword(code, email, newPassword).request().url().toString());
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            String result = response.body().string();
                            Type type = new TypeToken<String>() {
                            }.getType();
                            String responseResult = new Gson().fromJson(result, type);
                            if (responseResult == null) {
                                callBackData.onFail(response.message());
                            }
                            callBackData.onSuccess(responseResult);
                        } catch (JsonSyntaxException jsonError) {
                            jsonError.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        callBackData.onFail(response.message());
                    }
                } else if(response.code() == 400) {
                    callBackData.onFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBackData.onFail(call.toString());
            }
        });
    }
}
