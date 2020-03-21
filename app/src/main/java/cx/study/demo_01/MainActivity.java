package cx.study.demo_01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cx.study.demo_01.SqLite.SQLDate;
import cx.study.demo_01.SqLite.SqliteDataHelper;
import cx.study.demo_01.SqLite.User_Data;
import cx.study.demo_01.Tool.EditTextDemo.ClearEditText;
import cx.study.demo_01.Tool.TextChange;
import de.hdodenhof.circleimageview.CircleImageView;

import static cx.study.demo_01.Tool.UseTool.changPic;

public class MainActivity extends AppCompatActivity {

    //定义控件
    //这里就像java里边那样 需要int值就定义int类型的变量
    //但是这是Android 我们需要控件  所以需要定义控件
    //界面上一共放了四个空间  我们需要获取两个输入框的值和Button的点击事件
    /*
    还有选择框的选中事件  所以就定义四个控件
     */
    private Button btn_send;
    private ClearEditText ed_name;
    private ClearEditText ed_pwd;
    private TextView tv_newUser;
    private TextView tv_lostUser;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CircleImageView circleImageView;
    private String path;
    //数据库
    private SqliteDataHelper sqliteDataHelper;
    private User_Data user_data;
    private SQLDate sqlDate;


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
        changeImage();


        //EditText 输入后事件
        TextChange ed_name_change = new TextChange("登录", ed_name, MainActivity.this);
        ed_name.addTextChangedListener(ed_name_change);


        //设置下划线
        tv_newUser.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tv_lostUser.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        tv_newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WelComeActivity.class);
                intent.putExtra("data", "创建");
                startActivity(intent);
            }
        });
        tv_lostUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WelComeActivity.class);
                intent.putExtra("data", "更改");
                startActivity(intent);
            }
        });


        //会自动生成  这是一个抽象方法的实现
        //这个也就是控件的点击事件  大部分控件都有  onClick就是点击的意思
        //按钮的点击事件
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断输入框是否为空  这个应该能看懂 JAVA差不多
                if (!ed_name.getText().toString().isEmpty() ||
                        !ed_pwd.getText().toString().isEmpty()) {

                    if (!sqlDate.query_Name(ed_name.getText().toString())) {
                        TS("没有数据,请注册");
                        ed_name.startShakeAnimation();
                        return;
                    }

                    //判断用户名密码
                    if (isCorrect(ed_name.getText().toString(), ed_pwd.getText().toString())) {
                        editor.putString("saveName", ed_name.getText().toString());
                        editor.putString("savePwd", ed_pwd.getText().toString());
                        editor.commit();
                        //intent 就是用来跳转用的。 构造方法第一个写这个activity  第二个参数写跳转到哪个activity
                        Intent intent = new Intent(MainActivity.this, RegisteredActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TS("用户名密码错误");
                        ed_pwd.startShakeAnimation();
                    }
                } else {
                    //这两个就是提示
                    TS("用户名密码不能为空！！！");
                    ed_name.startShakeAnimation();
                    ed_pwd.startShakeAnimation();
                }
                //取消光标
                CancelFocus();
            }
        });
    }


    //判断用户名和密码是否正确
    public boolean isCorrect(String name, String pwd) {
        user_data = new User_Data();
        user_data.setName(name);
        user_data.setPwd(pwd);
        return sqlDate.query_login(user_data);
    }


    /**
     * 控件实例化
     * 和类的初始化一样
     * 上边只是定义出来了 我们需要把代码和界面连接起来
     * findViewByID 翻译一下通过寻找ID找控件
     * 这样就和界面连接起来了
     */
    @SuppressLint("CommitPrefEdits")
    private void initView() {
        tv_newUser = findViewById(R.id.tv_newUser);
        tv_lostUser = findViewById(R.id.tv_lostUser);
        btn_send = findViewById(R.id.btn_send);
        ed_name = findViewById(R.id.ed_Name);
        ed_pwd = findViewById(R.id.ed_Pwd);
        circleImageView = findViewById(R.id.img_tx);
        //创建一个对象
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        //实例化
        editor = pref.edit();
        //自动补充之前登陆的账号和密码
        ed_name.setText(pref.getString("saveName", null));
        ed_pwd.setText(pref.getString("savePwd", null));

        // 数据库实例化
        sqliteDataHelper = new SqliteDataHelper(MainActivity.this);
        sqlDate = new SQLDate(MainActivity.this);


    }

    //图片旋转
    public void changeImage() {
        path = sqlDate.query_Path(ed_name.getText().toString());
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            //旋转图片
            bitmap = changPic(bitmap, path);
            circleImageView.setImageBitmap(bitmap);
        } else {
            circleImageView.setImageResource(R.drawable.beijing02);
        }
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

    //返回事件
    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
