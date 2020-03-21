package cx.study.demo_01.Tool;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import cx.study.demo_01.MainActivity;
import cx.study.demo_01.WelComeActivity;

/**
 * @ProjectName: demo_01
 * @Package: cx.study.demo_01.Tool
 * @ClassName: TextChange
 * @Author: ZYH
 * @CreateDate: 2020/3/14 0014 18:39
 * @Version: 1.0
 * @Description: java类作用描述
 */
public class TextChange implements TextWatcher {

    private EditText editText;
    private TextInputLayout textInputLayout;
    private String changeActivity;
    private MainActivity mainActivity;
    private WelComeActivity welComeActivity;
    private static int MESSAGE_SEARCH;
    private static long INTERVAL = 300; // 输入变化时间间隔

    public TextChange(String changeActivity, EditText editText, TextInputLayout textInputLayout) {
        this.editText = editText;
        this.textInputLayout = textInputLayout;
        this.changeActivity = changeActivity;
    }

    public TextChange(String changeActivity, EditText editText, MainActivity mainActivity) {
        this.editText = editText;
        this.changeActivity = changeActivity;
        this.mainActivity = mainActivity;
        MESSAGE_SEARCH = 0x1;
    }


    public TextChange(String changeActivity, EditText editText, WelComeActivity welComeActivity) {
        this.editText = editText;
        this.changeActivity = changeActivity;
        this.welComeActivity = welComeActivity;
        MESSAGE_SEARCH = 0x2;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (changeActivity.equals("注册")) {
            if (textInputLayout.getCounterMaxLength() < editText.getText().length()) {
                textInputLayout.setError("超出字符限制");
            } else if (textInputLayout.getCounterMaxLength() > editText.getText().length()) {
                textInputLayout.setError("长度不够");
            } else {
                textInputLayout.setErrorEnabled(false);
            }
        } else if (changeActivity.equals("登录")) {
            MESSAGE_SEARCH = 0x1;
            if (mHandler.hasMessages(MESSAGE_SEARCH)) {
                mHandler.removeMessages(MESSAGE_SEARCH);
            }
            mHandler.sendEmptyMessageDelayed(MESSAGE_SEARCH, INTERVAL);
        } else if (changeActivity.equals("修改")) {
            MESSAGE_SEARCH = 0x2;
            if (mHandler.hasMessages(MESSAGE_SEARCH)) {
                mHandler.removeMessages(MESSAGE_SEARCH);
            }
            mHandler.sendEmptyMessageDelayed(MESSAGE_SEARCH, INTERVAL);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x1) {
                mainActivity.changeImage();
            } else if (msg.what == 0x2) {
                welComeActivity.changeImage();
            }
            return false;
        }
    });
}
