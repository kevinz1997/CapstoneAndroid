package workflow.capstone.capstoneproject.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import es.dmoral.toasty.Toasty;
import workflow.capstone.capstoneproject.R;
import workflow.capstone.capstoneproject.repository.CapstoneRepository;
import workflow.capstone.capstoneproject.repository.CapstoneRepositoryImpl;
import workflow.capstone.capstoneproject.utils.CallBackData;

public class SendEmailDialog extends Dialog {

    private TextView tvSend;
    private TextView tvCancel;
    private EditText inputNewPassword;
    private Context mContext;
    private CapstoneRepository capstoneRepository;

    public SendEmailDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_email_dialog);
        tvSend = findViewById(R.id.tv_send);
        tvCancel = findViewById(R.id.tv_cancel);
        inputNewPassword = findViewById(R.id.input_new_password);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCodeToEmail();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void sendCodeToEmail() {
        capstoneRepository = new CapstoneRepositoryImpl();
        capstoneRepository.forgotPassword(inputNewPassword.getText().toString(), new CallBackData<String>() {
            @Override
            public void onSuccess(String s) {
                Toasty.warning(mContext, R.string.send_code_success, Toasty.LENGTH_LONG).show();
                dismiss();
            }

            @Override
            public void onFail(String message) {
                Toasty.error(mContext, R.string.send_code_fail, Toasty.LENGTH_LONG).show();
            }
        });
    }
}
