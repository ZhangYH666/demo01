package cx.study.demo_01.Tool;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView textView;

    public TextChange(EditText editText, TextView textView) {
        this.editText = editText;
        this.textView = textView;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editText.getText().length() > 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
