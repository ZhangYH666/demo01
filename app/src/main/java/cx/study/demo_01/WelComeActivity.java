package cx.study.demo_01;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import cx.study.demo_01.SqLite.SQLDate;
import cx.study.demo_01.SqLite.SqliteDataHelper;
import cx.study.demo_01.SqLite.User_Data;
import cx.study.demo_01.Tool.Set_TimeNow;
import cx.study.demo_01.Tool.TextChange;
import de.hdodenhof.circleimageview.CircleImageView;

import static cx.study.demo_01.Tool.UseTool.changPic;

public class WelComeActivity extends AppCompatActivity {

    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 1;
    private static final int IMAGE_REQUEST_CODE = 2;
    private static String path;
    private CircleImageView img_pic;
    private TextView tv_log_tx;
    private EditText ed_Name;
    private EditText ed_pwd;
    private EditText ed_pwd2;
    private Button btn_ok;
    private RadioGroup rg;
    private RadioButton rb_name;
    private RadioButton rb_pwd;

    private TextInputLayout til_zh;
    private TextInputLayout til_pwd1;
    private TextInputLayout til_pwd2;

    private Uri imageUri;
    private LinearLayout linear;
    private String createOrUpdata;

    //数据库
    private SqliteDataHelper sqliteDataHelper;
    private User_Data user_data;
    private SQLDate sqlDate;


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come);
        /*
         * 先判断用户以前有没有对我们的应用程序允许过读写内存卡内容的权限，
         * 用户处理的结果在 onRequestPermissionResult 中进行处理
         */
        if (ContextCompat.checkSelfPermission(WelComeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(WelComeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_SDCARD_PERMISSION_REQUEST_CODE);
        }
        initView();
        initEvent();
    }

    private void initView() {
        img_pic = findViewById(R.id.img_tx);
        tv_log_tx = findViewById(R.id.tv_log_tx);
        ed_Name = findViewById(R.id.ed_Name_change);
        ed_pwd = findViewById(R.id.ed_Pwd);
        ed_pwd2 = findViewById(R.id.ed_Pwd2);
        btn_ok = findViewById(R.id.btn_ok);
        rg = findViewById(R.id.rg_two);
        rb_name = findViewById(R.id.rb_name);
        rb_pwd = findViewById(R.id.rb_pwd);
        linear = findViewById(R.id.Linear01);
        til_pwd1 = findViewById(R.id.til_pwd1);
        til_pwd2 = findViewById(R.id.til_pwd2);
        til_zh = findViewById(R.id.til_Zh);
        sqliteDataHelper = new SqliteDataHelper(WelComeActivity.this);
        sqlDate = new SQLDate(WelComeActivity.this);
        sqliteDataHelper.getWritableDatabase();
        //创建一个对象
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        //实例化
        editor = pref.edit();
    }

    //设置提示样式
    public void setShowName(String name, String pwd1, String pwd2) {
        til_zh.setHint(name);
        til_pwd1.setHint(pwd1);
        til_pwd2.setHint(pwd2);
    }

    private void initEvent() {


        //EditText 输入后事件
        TextChange ed_name_change = new TextChange("修改", ed_Name, WelComeActivity.this);
        ed_Name.addTextChangedListener(ed_name_change);

        Intent intent = getIntent();
        createOrUpdata = intent.getStringExtra("data");
        if (createOrUpdata.equals("创建")) {
            rg.setVisibility(View.INVISIBLE);
            setShowName("请输入用户名", "输入密码", "确认密码");

        } else if (createOrUpdata.equals("更改")) {
            rg.setVisibility(View.VISIBLE);
            img_pic.setEnabled(false);
            setShowName("请输入用户名", "输入旧密码", "输入新密码");
        }


        img_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在这里跳转到手机系统相册里面
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                TS("穿越成功~~");
            }
        });
        //输入框的功能
        editLister();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_name:
                        //不修改头像
                        img_pic.setEnabled(false);

                        break;
                    case R.id.rb_pwd:
                        if (sqlDate.query_Name(ed_Name.getText().toString())) {
                            //修改头像
                            img_pic.setEnabled(true);
                            rb_pwd.isChecked();
                        } else {
//                            rb_pwd.setChecked(false);
                            rb_name.setChecked(true);

                            TS("用户不存在");

                        }
                        break;
                    default:
                        break;
                }
            }
        });
        //确定
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNull()) {
                    if (createOrUpdata.equals("创建")) {
                        //两次密码是否相同  判断输入字符是否符合规定
                        if (ed_pwd.getText().toString().equals(ed_pwd2.getText().toString()) && !til_pwd2.isErrorEnabled()) {
                            user_data = Insert_Data();
                            if (user_data != null) {
                                long l = sqlDate.Insert(isSetPic(), user_data);
                                if (l > 0) {
                                    TS("创建成功,2秒后跳转");
                                    intentTimeOf3();
                                }
                            } else {
                                TS("用户名已存在！！!");
                            }
                        } else {
                            TS("两次密码不相同");
                        }
                    } else if (createOrUpdata.equals("更改")) {
                        //先查询名字是否存在
                        if (sqlDate.query_Name(ed_Name.getText().toString())) {
                            //查询用户名和旧密码是否相同
                            user_data = new User_Data();
                            user_data.setName(ed_Name.getText().toString());
                            user_data.setPwd(ed_pwd.getText().toString());
                            if (sqlDate.query_login(user_data)) {
                                user_data.setPwd(ed_pwd2.getText().toString());
                                user_data.setPic(path);
                                if (sqlDate.updata(user_data) > 0) {
                                    TS("修改成功");
                                    intentTimeOf3();
                                }
                            } else {
                                TS("旧密码不正确");
                            }
                        } else {
                            TS("用户名不存在");
                        }
                    }
                } else {
                    TS("数据不能为空");
                }
            }
        });

    }

    private boolean isSetPic() {
        return path != null;
    }

    public User_Data Insert_Data() {
        //注册新用户 判断用户名是否存在
        if (!sqlDate.query_Name(ed_Name.getText().toString())) {
            user_data = new User_Data();
            user_data.setName(ed_Name.getText().toString());
            user_data.setPwd(ed_pwd2.getText().toString());
            user_data.setCreateTime(Set_TimeNow.gettime());
            user_data.setPic(path);
        }
        return user_data;
    }

    //延时跳转
    private void intentTimeOf3() {
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                startActivity(new Intent(WelComeActivity.this, MainActivity.class));
                finish();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 2000);
    }

    private boolean isNull() {
        //选中的是修改密码
        if (ed_Name.getText().toString().isEmpty() ||
                ed_pwd.getText().toString().isEmpty()
                || ed_pwd2.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }


    //edittext监听
    private void editLister() {
        TextChange ed_pwd_change = new TextChange("注册", ed_pwd, til_pwd1);
        ed_pwd.addTextChangedListener(ed_pwd_change);
        TextChange ed_pwd_change2 = new TextChange("注册", ed_pwd2, til_pwd2);
        ed_pwd2.addTextChangedListener(ed_pwd_change2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void changeImage() {
        path = sqlDate.query_Path(ed_Name.getText().toString());
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            //旋转图片
            bitmap = changPic(bitmap, path);
            img_pic.setImageBitmap(bitmap);
        } else {
            img_pic.setImageResource(R.drawable.beijing02);
        }
    }

    //返回从图库获取的图片信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case IMAGE_REQUEST_CODE://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultCode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        //旋转图片
                        bitmap = changPic(bitmap, path);
                        img_pic.setImageBitmap(bitmap);
                        //把图片路径存起来
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == MainActivity.RESULT_OK) {   // 回传成功
//            switch (requestCode) {                // 选择请求码
//                case IMAGE_REQUEST_CODE:
//                    try {
//                        imageUri = data.getData();
//                        if (imageUri != null) {
//                            startUCrop();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case UCrop.REQUEST_CROP: {
//                    // 裁剪照片
//                    final Uri croppedUri = UCrop.getOutput(data);
//                    try {
//                        if (croppedUri != null) {
//                            Bitmap bit =
//                                    BitmapFactory.decodeStream(getContentResolver()
//                                    .openInputStream(croppedUri));
//                            img_pic.setImageBitmap(bit);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//
//                case UCrop.RESULT_ERROR:
//                    final Throwable cropError = UCrop.getError(data);
//                    Log.i("RESULT_ERROR", "UCrop_RESULT_ERROR");
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + requestCode);
//            }
//        }
//    }
//
//    private void startUCrop() {
//        //裁剪后保存到文件中
//        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "myCroppedImage.jpg"));
//        UCrop uCrop = UCrop.of(imageUri, destinationUri);
//        UCrop.Options options = new UCrop.Options();
//        //设置裁剪图片可操作的手势
//        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE,
//                UCropActivity.ALL);
//        //设置toolbar颜色
//        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorAccent));
//        //设置状态栏颜色
//        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
//        //是否能调整裁剪框
//        // options.setFreeStyleCropEnabled(true);
//        uCrop.withOptions(options);
//        uCrop.start(this);
//    }

    public void TS(String data) {
        Toast.makeText(WelComeActivity.this, data, Toast.LENGTH_SHORT).show();
        //.show()别忘加了 这个是显示
    }

}
