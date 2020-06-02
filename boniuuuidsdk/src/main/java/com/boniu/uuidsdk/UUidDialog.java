package com.boniu.uuidsdk;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;



public class UUidDialog extends Dialog {

    private String codeStr;
    private TextView tvXieyi;
    private DialogClick dialogClick;

    public UUidDialog(@NonNull Context context, String codeStr,DialogClick dialogClick) {
        super(context, R.style.CustomProgressDialog);
        this.dialogClick = dialogClick;
        this.codeStr = codeStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_uuid);

        tvXieyi = ((TextView) findViewById(R.id.tv_xieyi));

        tvXieyi.setText(codeStr);

        findViewById(R.id.tv_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceId = UuidCreator.getInstance(getContext()).getDeviceId();
                if (TextUtils.isEmpty(deviceId)){
                    Toast.makeText(getContext(), "获取失败，请重试", Toast.LENGTH_SHORT).show();
                }else{
                    dialogClick.dialogClick(true);
                    dismiss();
                }


            }
        });
        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClick.dialogClick(false);


            }
        });
    }

    public interface DialogClick{
        void dialogClick(boolean click);
    }


}
