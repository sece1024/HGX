# Android

## 创建activity类

1. 直接New一个Activity类
2. 自己创建java类和布局
   * 新建一个类，继承`Activity`
   * 新建一个`Layout Resource File`
   * 在activity类中重载onCreate函数，并在期中添加`setContentView(R.layout.刚才创建的布局名);`
   * 在`AndroidManifest.xml`文件中声明` <activity android:name="包名.Activity类名"/>`





## ImageView

### 从网络中获取图片

* 设置xml布局

```xml
    <ImageView
        android:id="@+id/imgview_4"
        android:layout_below="@id/imgview_3"
        android:layout_marginTop="10dp"
        android:layout_width="250dp"
        android:layout_height="100dp"
        android:scaleType="centerCrop"
        />
```

* 在activity中声明控件

```java
private ImageView mIv4;
```

* 在onCreate()中找到控件并赋予图片

```java
mIv4 = findViewById(R.id.imgview_4);
        Glide.with(this).load("https://iknow-pic.cdn.bcebos.com/622762d0f703918fd091b3c4523d269759eec478").into(mIv4);
```

> 获取图片使用的是`glide`第三方库，详情参考[bumptech/glide: An image loading and caching library for Android focused on smooth scrolling (github.com)](https://github.com/bumptech/glide)



**使用这个库之前：**

* 在`build.gradle`中添加

```
repositories {
    google()
    mavenCentral()
}
```

* 在`build.gradle`的`dependencies`中添加

```
implementation 'com.github.bumptech.glide:glide:4.12.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
```

* 然后使用gradle包管理器自动同步glide库





# 一些问题

## 修改控件颜色

* 控件一直是蓝色，修改了也没用

解决方法： 将values/themes/themes.xml中的

`<style name="Theme.MyDemo01" parent="Theme.MaterialComponents.DayNight.DarkActionBar">`

改为：

` <style name="Theme.MyApplication" parent="Theme.MaterialComponents.DayNight.NoActionBar.Bridge">`



