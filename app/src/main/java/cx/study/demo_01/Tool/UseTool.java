package cx.study.demo_01.Tool;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.exifinterface.media.ExifInterface;

import java.io.IOException;

/**
 * @ProjectName: demo_01
 * @Package: cx.study.demo_01.Use
 * @ClassName: UseTool
 * @Author: ZYH
 * @CreateDate: 2020/3/14 0014 16:48
 * @Version: 1.0
 * @Description: java类作用描述
 */
public class UseTool {
    /**
     * 旋转图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap changPic(Bitmap bitmap, String path) {
        //根据图片的filepath获取到一个ExifInterface的对象
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        int degree = 0;
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        }
        if (degree != 0) {
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
        }
        return bitmap;
    }
}
