package cx.study.demo_01;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private Button btn_send;
    private EditText ed_name;
    private EditText ed_pwd;
    private CheckBox cbx_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initEven();
    }

    /**
     * 功能实现
     */
    private void initEven() {
        //按钮的点击事件
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断输入框是否为空
                if (!ed_name.getText().toString().isEmpty() ||
                        !ed_pwd.getText().toString().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, WelComeActivity.class);
                    startActivity(intent);
                } else {
                    TS("用户名密码不能为空！！！");
                }
                //取消光标
                CancelFocus();
            }
        });
        //复选框的选中事件
        cbx_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ed_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CancelFocus();
            }
        });

    }

    /**
     * 控件实例化
     */
    private void initView() {
        btn_send = findViewById(R.id.btn_send);
        ed_name = findViewById(R.id.ed_Name);
        ed_pwd = findViewById(R.id.ed_Pwd);
        cbx_show = findViewById(R.id.cbx_show);
    }

    public void TS(String data) {
        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
    }

    /**
     * 取消EditText的焦点
     */
    public void CancelFocus() {
        ed_name.clearFocus();
        ed_pwd.clearFocus();//取消焦点

    }

}
