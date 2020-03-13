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

    //定义控件
    //这里就像java里边那样 需要int值就定义int类型的变量
    //但是这是Android 我们需要控件  所以需要定义控件
    //界面上一共放了四个空间  我们需要获取两个输入框的值和Button的点击事件
    /*
    还有选择框的选中事件  所以就定义四个控件
     */
    private Button btn_send;
    private EditText ed_name;
    private EditText ed_pwd;
    private CheckBox cbx_show;

    //这个是onCreate事件 Activity初始化的时候会运行这个
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这句代码的意思就是取消表头  看到没就是刚才那里
        //王奥写的是不是有一个表头 这句的意思就是去掉那个
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //这就是两个方法
        initView();
        initEven();
    }

    /**
     * 功能实现
     */
    private void initEven() {

        //会自动生成  这是一个抽象方法的实现
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里边就是写 点击需要出发的功能
            }
        });
        //这个也就是控件的点击事件  大部分控件都有  onClick就是点击的意思
        //按钮的点击事件
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断输入框是否为空  这个应该能看懂 JAVA差不多
                if (!ed_name.getText().toString().isEmpty() ||
                        !ed_pwd.getText().toString().isEmpty()) {
                    //判断用户名密码
                    if (ed_name.getText().toString().equals("admin") && ed_pwd.getText().toString().equals("123456")) {
                        //intent 就是用来跳转用的。 构造方法第一个写这个activity  第二个参数写跳转到哪个activity
                        Intent intent = new Intent(MainActivity.this, WelComeActivity.class);
                        startActivity(intent);
                    } else {
                        TS("用户名密码错误");
                    }
                } else {
                    //这两个就是提示
                    TS("用户名密码不能为空！！！");
                }
                //取消光标
                CancelFocus();
            }
        });
        //复选框的选中事件
        //选中事件  button 是 click  这个是checked   专业术语叫监听器
        cbx_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override                                           //这个参数就是多选框的状态 会在这里呈现
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //这个就是固定语句   英语开头能看懂就行  记住就行了
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
     * 和类的初始化一样
     * 上边只是定义出来了 我们需要把代码和界面连接起来
     * findViewByID 翻译一下通过寻找ID找控件
     * 这样就和界面连接起来了
     */
    private void initView() {
        btn_send = findViewById(R.id.btn_send);
        ed_name = findViewById(R.id.ed_Name);
        ed_pwd = findViewById(R.id.ed_Pwd);
        cbx_show = findViewById(R.id.cbx_show);
    }

    //这个是吐司。中文叫法就这样  固定写法 data就是需要显示的文字  这个选项就是显示时间的长短
    public void TS(String data) {
        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
        //.show()别忘加了 这个是现实
    }

    /**
     * 取消EditText的焦点
     */
    public void CancelFocus() {
        // 翻译一下就懂什么意思
        //还有哪么？
        //
        ed_name.clearFocus();
        ed_pwd.clearFocus();//取消焦点

    }

}
