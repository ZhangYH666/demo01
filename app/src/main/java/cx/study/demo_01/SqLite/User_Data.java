package cx.study.demo_01.SqLite;

import cx.study.demo_01.MD5.MD5Utils;

/**
 * @ProjectName: demo_01
 * @Package: cx.study.demo_01.SqLite
 * @ClassName: User_Data
 * @Author: ZYH
 * @CreateDate: 2020/3/20 0020 15:39
 * @Version: 1.0
 * @Description: java类作用描述
 */
public class User_Data {

    //    java.util.Date writeTime = new java.util.Date();
//    long datatime = writeTime.getTime();
    private String name;
    private String pwd;
    private String pic;
    private long CreateTime;

    public User_Data() {
    }

    public User_Data(String name, String pwd, String pic, long createTime) {
        this.name = name;
        this.pwd = pwd;
        this.pic = pic;
        CreateTime = createTime;
    }

    public User_Data(String name, String pwd, String pic) {
        this.name = name;
        this.pwd = pwd;
        this.pic = pic;
    }

    public User_Data(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = MD5Utils.getMD5(pwd);
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }
}
