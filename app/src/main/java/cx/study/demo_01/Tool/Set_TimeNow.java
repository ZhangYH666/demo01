package cx.study.demo_01.Tool;

/**
 * @ProjectName: demo_01
 * @Package: cx.study.demo_01.Tool
 * @ClassName: Get_TimeNow
 * @Author: ZYH
 * @CreateDate: 2020/3/20 0020 16:30
 * @Version: 1.0
 * @Description: java类作用描述
 */
public class Set_TimeNow {
    static java.util.Date date = new java.util.Date();
    static long datetime = date.getTime();

    public static long gettime() {
        return datetime;
    }
}
