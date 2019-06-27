package workflow.capstone.capstoneproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.customdialog.SendEmailDialog;
import workflow.capstone.capstoneproject.entities.Login;
import workflow.capstone.capstoneproject.repository.CapstoneRepository;
import workflow.capstone.capstoneproject.repository.CapstoneRepositoryImpl;
import workflow.capstone.capstoneproject.utils.CallBackData;
import workflow.capstone.capstoneproject.utils.ConstantDataManager;
import workflow.capstone.capstoneproject.utils.DynamicWorkflowSharedPreferences;
import workflow.capstone.capstoneproject.utils.DynamicWorkflowUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLogin;
    private CapstoneRepository capstoneRepository;
    private Context context = this;
    private TextView tvErrorLogin;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }

    private void initView() {
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        tvErrorLogin = findViewById(R.id.tv_Error_Login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
    }

    private void login() {
        String username = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("userName", username);
        fields.put("password", password);

        boolean checkConnection = DynamicWorkflowUtils.isConnectingToInternet(context);
        if (!checkConnection) {
            setTextError("Please connect to the Internet to continue!");
        } else if (username.trim().isEmpty() && password.trim().isEmpty()) {
            setTextError("Please input email and password!");
        } else if (username.trim().isEmpty()) {
            setTextError("Please input email!");
        } else if (password.trim().isEmpty()) {
            setTextError("Please input password!");
        } else {
            capstoneRepository = new CapstoneRepositoryImpl();
            capstoneRepository.login(context, fields, new CallBackData<Login>() {
                @Override
                public void onSuccess(Login login) {
                    DynamicWorkflowSharedPreferences.storeJWT(context, ConstantDataManager.AUTHORIZATION_TOKEN, login.getToken());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFail(String message) {
                    if (message.contains("Invalid username or password!")) {
                        setTextError(message);
                    }
                }
            });
        }
    }

    private void setTextError(String message) {
        tvErrorLogin.setVisibility(View.VISIBLE);
        tvErrorLogin.setText(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void forgotPassword() {
        SendEmailDialog dialog = new SendEmailDialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        //lấy kích thước màn hình
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        layoutParams.width  = size.x - 50;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setCancelable(false);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.create();
        dialog.show();
    }
}
