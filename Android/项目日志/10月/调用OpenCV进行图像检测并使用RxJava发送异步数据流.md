[TOC]

# opencv + rxjava

## 依赖

### Glide

```groovy
implementation 'com.github.bumptech.glide:glide:4.12.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
```

### RxJava

```groovy
implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
implementation 'io.reactivex.rxjava3:rxjava:3.0.0'
```

### OpenCV

*参考官方文档*

## 思路

1. 首先在界面中显示图像（从资源加载、相机拍照或者从相册中选择）

2. 点击灰度化按钮能将当前显示的图像转换为灰度图

3. 点击检测按钮能对图像的多种参数进行检测并显示在屏幕上（耗时操作，需要异步执行，计算得到数据后立即显示出来）
4. 点击模糊化按钮能使得图像变模糊，并显示改变后的模糊度

## 实现

### 主要代码

#### 相机&相册

将打开相机或者相册的代码抽离成单独的类，只需在界面的`onActivityResult()`方法中实现图像显示逻辑。

```java
/**
 * 相机帮助类
 */
public class CameraUtil {
    private static final String TAG = "CameraUtil";
    // 从相册中选择照片
    public static final int INT_SELECT_FROM_ALBUM = 1001;
    // 打开摄像头
    public static final int INT_OPEN_CAMERA = 1002;
    // 检查是否有摄像头
    private static boolean isCamera = false;

    /**
     * 打开相机
     * @param from
     */
    @SuppressLint("QueryPermissionsNeeded")
    public static void openCamera(Activity from){
        if (!isCamera){
            try {
                CameraManager cameraManager = (CameraManager) from.getSystemService(Context.CAMERA_SERVICE);
                String[] cameraIds = cameraManager.getCameraIdList();
                if (cameraIds.length > 0) {
                    //摄像头存在
                    if (cameraIds[0] != null || cameraIds[1] != null) {
                        isCamera = true;
                    }
                }
            } catch (IllegalStateException | CameraAccessException e) {
                e.printStackTrace();
            }
        }

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentCamera.resolveActivity(from.getPackageManager()) != null || isCamera) {
            from.startActivityForResult(intentCamera, INT_OPEN_CAMERA);
        } else {
            Log.d(TAG, "onClick: 打开失败");
        }

    }

    /**
     * 打开相册
     * @param from
     */
    public static void openAlbum(Activity from){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        from.startActivityForResult(intent, INT_SELECT_FROM_ALBUM);

    }
}
```

`loadBitmap()`调用Glide将图像加载到`ImageView`，`judgeBlur()`获取当前显示的图像，计算其模糊度并更新`TextView`。

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "onActivityResult: ");
    if (data != null) {
        switch (requestCode) {
            case CameraUtil.INT_SELECT_FROM_ALBUM:
                Uri uri = data.getData();
                loadBitmap(uri);
                break;
            case CameraUtil.INT_OPEN_CAMERA:
                Bundle extras = data.getExtras();
                Bitmap imageBmp = (Bitmap) extras.get("data");
                loadBitmap(imageBmp);
                judgeBlur();
                break;
            default:
                break;
        }
    } else {
        Log.d(TAG, "onActivityResult: 没有从相册获取数据");
    }
}
```





#### 观察者

*观察者决定事件触发时的行为，我们所需的行为是将接收到的字符串数据显示在TextView控件中。*

*由于是逐条接收数据，所以也要将数据逐条显示，这需要在TextView外面包裹一个ScrollView控件，并让其[自动滚动到底部。](https://blog.csdn.net/t12x3456/article/details/12799825?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-0.no_search_link&spm=1001.2101.3001.4242)*

```java
private final String CLEAR = "clear";
// 观察者
strObserver = new Observer<String>() {
    final StringBuilder sb = new StringBuilder("\n");
    @Override
    public void onSubscribe(@NonNull Disposable d) {
    }

    @Override
    public void onNext(@NonNull String s) {
        // 将接收到的数据显示在文本框
        if (CLEAR.equals(s)) {
            // 清空
            clearSb();
        } else {
            sb.append(s).append("\n");
            binding.tvImageInfo.setText(sb);
            // 滚动到底部
            binding.scrollTv.post(() -> binding.scrollTv.fullScroll(View.FOCUS_DOWN));
            Log.d(TAG, "onNext: " + s);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
    }

    @Override
    public void onComplete() {
        // 清空数据
        clearSb();
    }

    private void clearSb() {
        sb.delete(0, sb.length() - 1);
    }
};
```

#### 被观察者

*模糊度检测类`BlurDetect`参考之前写的《模糊度检测实现文档》。主要流程是传入待检测图像后，调用opencv计算图像方差并保存在内部匿名对象`detectResult`中，检测结束后，在匿名函数中执行后续操作，即判断模糊度属于哪个范围，使用emitter将描述信息发送出去。*

```java
// 被观察者 - 只检测模糊度
observableBlur = Observable.create(emitter -> {
    emitter.onNext("-----模糊度检测-----");
    // 用于模糊度检测的类
    BlurDetect blurDetect = new BlurDetect(blurImage);
    // 在接口中定义检测结束后的行为
    blurDetect.detecting(detectResult -> {
        OpenCvUtil.judgeBlurBySquDev(detectResult.resultNumber, binding.tvPictureBlur, getApplicationContext());
        emitter.onNext(detectResult.describe);
    });
});	


```
*进行图像处理和运算，每得到一个结果就用emitter发送出去，每一种检测都可以像上面的模糊度检测一样构造单独的类继承同一个模板，使用时只需要1. 声明检测对象并传入待检测图像；2. 调用主逻辑函数`detecting()`, 在其匿名函数中实现自定义功能（通过`emitter`发送`detectResult`中保存的数据）。*

*由于这部分代码暂时还没进行重构所以比较臃肿。*

```java
        // 被观察者 - 图像检测
        observableDetect = Observable.create(emitter -> {
            // 当前显示图片
            Bitmap nowBmp = BmpUtil.getBitmapFromImageView(binding.ivImage);
            Mat srcImage = OpenCvUtil.bitmapToMat(nowBmp);
            Mat dstImage = new Mat();
            // 清除缓存的字符串
            emitter.onNext(CLEAR);

            // region 色偏检测
            //  将RGB图像转变到CIE L*a*b*

            emitter.onNext("-----色偏检测-----");
            cvtColor(srcImage, dstImage, Imgproc.COLOR_BGR2Lab);
            float a = 0, b = 0;
            int[] HistA = new int[256], HistB = new int[256];
            for (int i = 0; i < 256; i++) {
                HistA[i] = 0;
                HistB[i] = 0;
            }
            int size = (int) dstImage.total() * dstImage.channels();
            for (int i = 0; i < dstImage.rows(); i++) {
                for (int j = 0; j < dstImage.cols(); j++) {
                    //在计算过程中，要考虑将CIEL*a*b*空间还原后同
                    a += (float) (dstImage.get(i, j)[1] - 128);
                    b += (float) (dstImage.get(i, j)[2] - 128);
                    int x = (int) dstImage.get(i, j)[1];
                    int y = (int) dstImage.get(i, j)[2];
                    HistA[x]++;
                    HistB[y]++;
                }
            }
            float da = a / (float) (dstImage.rows() * dstImage.cols());
            float db = b / (float) (dstImage.rows() * dstImage.cols());
            float D = (float) Math.sqrt(da * da + db * db);
            float Ma = 0, Mb = 0;
            for (int i = 0; i < 256; i++) {
                //计算范围-128～127
                Ma += Math.abs(i - 128 - da) * HistA[i];
                Mb += Math.abs(i - 128 - db) * HistB[i];
            }
            Ma /= (float) (dstImage.rows() * dstImage.cols());
            Mb /= (float) (dstImage.rows() * dstImage.cols());
            float M = (float) Math.sqrt(Ma * Ma + Mb * Mb);
            float K = D / M;
            float cast = K;
            System.out.printf("色偏指数： %f\n", cast);
            // 发送数据
            if (cast > 1.1) {
                System.out.print("存在色偏\n");
                emitter.onNext("色偏指数: " + cast + " -> 存在色偏");
            } else {
                System.out.print("不存在色偏\n");
                emitter.onNext("色偏指数: " + cast + " -> 不存在色偏");
            }
            // endregion

            // region 亮度检测

            luminanceDetection(nowBmp, emitter);
            blurDetection(nowBmp, emitter);

            // endregion

            // region 颜色检测
            emitter.onNext("-----颜色检测-----");
            cvtColor(srcImage, dstImage, Imgproc.COLOR_BGR2HSV);
            int i = 0, j = 0;
            loop:
            for (i = 0; i < dstImage.rows(); i++) {
                for (j = 0; j < dstImage.cols(); j++) {
                    //在计算过程中，考虑128为亮度均值点
                    double[] colorVec = dstImage.get(i, j);
                    int x = (int) dstImage.get(i, j)[0];
                    if ((colorVec[0] >= 0 && colorVec[0] <= 180)
                            && (colorVec[1] >= 0 && colorVec[1] <= 255)
                            && (colorVec[2] >= 0 && colorVec[2] <= 46)) {
                        continue;
                    } else if ((colorVec[0] >= 0 && colorVec[0] <= 180)
                            && (colorVec[1] >= 0 && colorVec[1] <= 43)
                            && (colorVec[2] >= 46 && colorVec[2] <= 220)) {
                        continue;
                    } else if ((colorVec[0] >= 0 && colorVec[0] <= 180)
                            && (colorVec[1] >= 0 && colorVec[1] <= 30)
                            && (colorVec[2] >= 221 && colorVec[2] <= 255)) {
                        continue;
                    } else {
                        System.out.println("彩色图像");
                        // 发送数据
                        emitter.onNext("彩色图像");
                        break loop;
                    }
                }
            }
            if (i == dstImage.rows() && j == dstImage.cols()) {
                System.out.println("黑白图像");
                // 发送数据
                emitter.onNext("黑白图像");
            }

            // endregion

            // region 清晰度检测
            emitter.onNext("-----清晰度检测-----");
            //转化为灰度图
            cvtColor(srcImage, dstImage, COLOR_BGR2GRAY);
            Mat laplacianDstImage = new Mat();
            //阈值太低会导致正常图片被误断为模糊图片，阈值太高会导致模糊图片被误判为正常图片
            Laplacian(dstImage, laplacianDstImage, CV_64F);
            //矩阵标准差
            MatOfDouble stddev = new MatOfDouble();
            //求矩阵的均值与标准差
            meanStdDev(laplacianDstImage, new MatOfDouble(), stddev);
            // ((全部元素的平方)的和)的平方根
            double norm = Core.norm(laplacianDstImage);
            emitter.onNext("平方根: " + norm);
            double mean = Core.mean(laplacianDstImage).val[0];
            emitter.onNext("矩阵均值: " + mean);

            // region Tenengrad梯度
            emitter.onNext("-----Tenengrad梯度-Sobel算子-----");
            cvtColor(srcImage, dstImage, COLOR_BGR2GRAY);
            Mat imageSobel = new Mat();
            Sobel(srcImage, imageSobel, CV_16U, 1, 1);

            //图像的平均灰度
            double meanValue = 0.0;
            meanValue = Core.mean(imageSobel).val[0];
            emitter.onNext("平均灰度: " + meanValue);

            // endregion

            // region Laplacian梯度
            emitter.onNext("-----Laplacian梯度-----");
            cvtColor(srcImage, dstImage, COLOR_BGR2GRAY);
            Mat image = new Mat();
            Laplacian(dstImage, image, CV_16U);
            // 平均灰度
            meanValue = Core.mean(image).val[0];
            emitter.onNext("平均灰度: " + meanValue);

            // endregion

            // region 方差检测
            emitter.onNext("-----方差法检测-----");
            MatOfDouble meanValueImage = new MatOfDouble();
            MatOfDouble meanStdValueImage = new MatOfDouble();

            // 求灰度图像标准差
            meanStdDev(dstImage, meanValueImage, meanStdValueImage);
            meanValue = meanStdValueImage.get(0, 0)[0];

            emitter.onNext("标准差: " + meanValue);
            // endregion

            // 检测结束
            emitter.onComplete();

        });
    /**
     * 亮度检测
     *
     * @param srcBitmap 原始图像
     * @param emitter   发射器
     */
    public void luminanceDetection(Bitmap srcBitmap, final Emitter<String> emitter) {
        if (srcBitmap == null || emitter == null) return;
        Mat srcImage = OpenCvUtil.bitmapToMat(srcBitmap);
        Mat dstImage = new Mat();
        // 将RGB图转为灰度图
        emitter.onNext("-----亮度检测-----");
        cvtColor(srcImage, dstImage, COLOR_BGR2GRAY);
        int a = 0;
        int Hist[] = new int[256];
        for (int i = 0; i < 256; i++) {
            Hist[i] = 0;
        }
        for (int i = 0; i < dstImage.rows(); i++) {
            for (int j = 0; j < dstImage.cols(); j++) {
                //在计算过程中，考虑128为亮度均值点
                a += (float) (dstImage.get(i, j)[0] - 128);
                int x = (int) dstImage.get(i, j)[0];
                Hist[x]++;
            }
        }
        float da = a / (float) (dstImage.rows() * dstImage.cols());
        System.out.println(da);
        float D = Math.abs(da);
        float Ma = 0;
        for (int i = 0; i < 256; i++) {
            Ma += Math.abs(i - 128 - da) * Hist[i];
        }
        Ma /= (float) ((dstImage.rows() * dstImage.cols()));
        float M = Math.abs(Ma);
        float K = D / M;
        float cast = K;
        System.out.printf("亮度指数： %f\n", cast);
        // 发送数据
        emitter.onNext("亮度指数: " + cast);
        if (cast >= 1) {
            System.out.printf("亮度：" + da);
            // 发送数据
            emitter.onNext("亮度: " + da);
            if (da > 0) {
                System.out.printf("过亮\n");
                // 发送数据
                emitter.onNext("过亮");
            } else {
                System.out.printf("过暗\n");
                // 发送数据
                emitter.onNext("过暗");
            }
        } else {
            System.out.printf("亮度：正常\n");
            // 发送数据
            emitter.onNext("亮度: 正常");
        }
    }

    /**
     * 模糊检测
     *
     * @param srcBitmap
     * @param emitter
     */
    private void blurDetection(Bitmap srcBitmap, final Emitter<String> emitter) {
        Mat srcImage = OpenCvUtil.bitmapToMat(srcBitmap);
        Mat dstImage = new Mat();
        Mat lap = new Mat();
        cvtColor(srcImage, dstImage, COLOR_BGR2GRAY);
        Laplacian(dstImage, lap, CV_64F);
        MatOfDouble m = new MatOfDouble();
        MatOfDouble s = new MatOfDouble();
        meanStdDev(lap, m, s);
        double st = s.get(0, 0)[0];
        emitter.onNext("-----模糊度检测-----");
        // 设置精度
        emitter.onNext("模糊度: " + new DecimalFormat("0.0000").format(st * st));

    }

```

### 布局

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        >
        <LinearLayout
            android:id="@+id/ll_containner"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toTopOf="@id/ll_btn_container"
            android:padding="15dp"
            android:orientation="vertical">
            <ImageView
                android:id="@+id/iv_image"
                android:layout_width="200dp"
                android:layout_height="150dp"
                android:layout_gravity="center"
                android:src="@drawable/drill_image_01"
                android:scaleType="centerCrop"
                android:background="#00FFFFFF"
                />
            <ScrollView
                android:id="@+id/scroll_tv"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                >
                <TextView
                    android:id="@+id/tv_image_info"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:textSize="@dimen/text_common_content"/>
            </ScrollView>
        </LinearLayout>

        <LinearLayout
            android:id="@+id/ll_btn_container"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            android:orientation="vertical"
            android:layout_gravity="bottom|end"
            android:padding="15dp">
            <TextView
                android:id="@+id/tv_picture_blur"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="@dimen/text_common_content"
                android:gravity="center"
                android:textColor="@color/colorGreen"/>
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                >
                <Button
                    android:id="@+id/btn_reload"
                    android:layout_width="0dp"
                    android:layout_weight="1"
                    android:layout_height="wrap_content"
                    android:background="@drawable/btn_selector"
                    android:textSize="@dimen/text_common_btn"
                    android:text="还原"/>
                <Button
                    android:id="@+id/btn_change"
                    android:layout_width="0dp"
                    android:layout_weight="1"
                    android:layout_height="wrap_content"
                    android:background="@drawable/btn_selector"
                    android:textSize="@dimen/text_common_btn"
                    android:text="切换"
                    android:visibility="gone"/>
                <Button
                    android:id="@+id/btn_take_photo"
                    android:layout_width="0dp"
                    android:layout_weight="1"
                    android:layout_height="wrap_content"
                    android:background="@drawable/btn_selector"
                    android:textSize="@dimen/text_common_btn"
                    android:text="拍照"/>
                <Button
                    android:id="@+id/btn_album"
                    android:layout_width="0dp"
                    android:layout_weight="1"
                    android:layout_height="wrap_content"
                    android:background="@drawable/btn_selector"
                    android:textSize="@dimen/text_common_btn"
                    android:text="相册"/>
                <Button
                    android:id="@+id/btn_detection"
                    android:layout_width="0dp"
                    android:layout_weight="1"
                    android:layout_height="wrap_content"
                    android:background="@drawable/btn_selector"
                    android:textSize="@dimen/text_common_btn"
                    android:text="检测"/>
            </LinearLayout>
            <Button
                android:id="@+id/btn_to_grey"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:background="@drawable/btn_selector"
                android:textSize="@dimen/text_common_btn"
                android:text="灰度化"
                />
            <Button
                android:id="@+id/btn_blur"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:background="@drawable/btn_selector"
                android:textSize="@dimen/text_common_btn"
                android:text="模糊化"/>
        </LinearLayout>

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
```

