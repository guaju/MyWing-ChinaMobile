package demo.sharesdk.cn.mywing_s;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2018/4/1.
 */

public class MyDialog {
    private Context context;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    public MyDialog(Context context) {
        this.context = context;
        initDialog();
    }

    private void initDialog() {
        builder = new AlertDialog.Builder(context)
                .setTitle("提示消息")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public void showDialog(String msg) {
        if (builder == null)
            return;
        builder.setMessage(msg);
        dialog = builder.create();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            } else {
                dialog.show();
            }
        }
    }
}
