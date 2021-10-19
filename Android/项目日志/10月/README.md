# 获取经纬度

[Android调用高德地图API实现定位 - xd_1989 - 博客园 (cnblogs.com)](https://www.cnblogs.com/XieDong/p/7724556.html)

# RxJava

[大佬们，一波RxJava 3.0来袭，请做好准备~ - 掘金 (juejin.cn)](https://juejin.cn/post/6844903885245513741)

[RxJava 的基本使用用法(一) - 简书 (jianshu.com)](https://www.jianshu.com/p/fce825833d36)

## 操作符

https://www.androidhive.info/RxJava/rxjava-operators-just-range-from-repeat/

# OpenCv

## AndroidStudio 升级为Artic Fox 2020 3.1后，无法导入opencv SDK的问题

https://stackoverflow.com/questions/68649524/opencv-android-studio-module-importing-issue

## Mat

[Android OpenCV（二）：Mat像素操作 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/232147441)

# 自定义样式

## 自定义按钮样式

[Android：自定义控件样式（Selector） - 小艾.luoaz - 博客园 (cnblogs.com)](https://www.cnblogs.com/luoaz/p/3764784.html)

# ScrollView滚动到底部

[(2条消息) Android 控制ScrollView滚动到底部_Whatever is worth doing is worth doing well.-CSDN博客](https://blog.csdn.net/t12x3456/article/details/12799825?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-0.no_search_link&spm=1001.2101.3001.4242)

# 设置浮点数精度

[(2条消息) Java 浮点数精度控制_Medlen-CSDN博客](https://blog.csdn.net/weixin_38481963/article/details/82120870)
``` java
public class test {
    public static void main(String args[])
    {
//      System.out.println(String.format("%.2f", Math.PI));
        double pi = 3.142;
        //仅取整数部分
        System.out.println(new DecimalFormat("0").format(pi));//3
        System.out.println(new DecimalFormat("#").format(pi));//3
        //取小数点后两位
        System.out.println(new DecimalFormat("0.00").format(pi));//3.14
        //取小数点后两位，整数部分取两位，不足前面补零
        System.out.println(new DecimalFormat("00.00").format(pi));//03.14
        //取小数点后4位，不足补零
        System.out.println(new DecimalFormat("0.0000").format(pi));//3.1420
        //以百分比方式计数并小数点后2位
        System.out.println(new DecimalFormat("0.00%").format(pi));//314.20%

        long l = 123456789;
        //科学计数法，取5位小数
        System.out.println(new DecimalFormat("0.00000E0").format(l));//1.23457E8
        //显示为两位整数，并保留小数点后四位的科学计数法
        System.out.println(new DecimalFormat("00.0000E0").format(l));//12.3457E7
        //每三位以逗号分隔
        System.out.println(new DecimalFormat(",000").format(l));//123,456,789
        //嵌入格式文本
        System.out.println(new DecimalFormat("这个长整数为：0").format(l));//这个长整数为：123456789
    }

}
```



# 模拟器上无法打开相机

## resolveActivity 判断返回Null

[(4条消息) Android11踩坑之路：resolveActivity 判断返回Null_yhroppo的博客-CSDN博客](https://blog.csdn.net/yhroppo/article/details/109074775?utm_medium=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~default-2.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~default-2.no_search_link)

[拍照  | Android 开发者  | Android Developers (google.cn)](https://developer.android.google.cn/training/camera/photobasics?hl=zh-cn)

官方给出的拍摄照片函数中，在跳转界面直接先判断`takePictureIntent.resolveActivity(getPackageManager()) != null`

```java
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    
```

> 请注意，`startActivityForResult()` 方法受调用 `resolveActivity()`（返回可处理 Intent 的第一个 Activity 组件）的条件保护。执行此检查非常重要，因为如果您使用任何应用都无法处理的 Intent 调用 `startActivityForResult()`，您的应用就会崩溃。所以只要结果不是 Null，就可以放心使用 Intent。

真机上可以正常运行，但是在模拟器上，这个返回值却总是为null导致相机无法打开。

为了解决这个问题，可以再加一个条件。

```java
CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
String[] cameraIds = cameraManager.getCameraIdList();
if (cameraIds.length > 0) {
    //摄像头存在
    if (cameraIds[0] != null || cameraIds[1] != null) {
        isCamera = true;
    }
}
```

检查手机是否有摄像头，当 `isCamera`为 `true`时同样跳转到拍照界面。

