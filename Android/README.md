# 原型设计

[六款优秀的App原型设计工具 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/415083329)

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

# 布局

## ConstantLayout

[约束布局ConstraintLayout看这一篇就够了 - 简书 (jianshu.com)](https://www.jianshu.com/p/17ec9bd6ca8a)

# Kotlin

## `LearnKotlin.kt`

```kotlin
package com.example.kotlindemo

import java.util.*

class LearnKotlin {

}

fun main() {
    val s: String = "Hello, world!"
    val a = 3
    val b = 9
    println(s)

    val test = if (a > b) {
        a
    } else {
        b
    }
    println(test)
    println(
        if (a > b) {
            a
        } else {
            b
        }
    )

    println("Tom's score = " + getScore("Jack"))

    // 循环
    // ".." 是关键字，表示闭区间

    for (i in 0..4){ // 打印0到4
        print(" "+i)
    }
    println("-----")

    // "until": 左闭右开
    for (i in 0 until 4){
        print(" "+i)
    }
    println("-----")

    // "step": 步长
    for (i in 0 until 8 step 2){
        print(" "+i)
    }
    println("-----")

    // "downTo": 降序区间
    for(i in 10 downTo 1 step 2){
        print(" "+i)
    }
    println("-----")

    // 测试数据类
    println("测试数据类")
    val phone1 = Cellphone("Samsung", 1299.88)
    val phone2= Cellphone("Samsung", 1299.88)
    println(phone1)
    println("phone1 equals phone2: " + (phone1 == phone2))

    // 测试单例类
    println("测试单例类")
    Singleton.singletonTest()

    // lambda表达式
    println("Lambda表达式")
    // 不可变列表，可变列表用"mutableListOf"声明
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
    // Lambda表达式语法： {参数名: 参数类型 -> 函数体}
    val lambda = {fruit: String -> fruit.length}
    // maxBy: 根据传入的条件遍历集合，找到该条件下最大的值(这里定义的条件为字符串长度)
    val maxLengthFruit = list.maxByOrNull(lambda)
    println("最长的字符串："+maxLengthFruit)

    // 简化Lambda表达式
    // 1. lambda表达式直接传入maxBy函数
    val maxLengthFruit2 = list.maxByOrNull({ fruit: String -> fruit.length})

    // 2. 当Lambda参数是最后一个参数时，可以将Lambda表达式移到函数括号外面
    val maxLengthFruit3 = list.maxByOrNull(){ fruit: String -> fruit.length}

    // 3. 参数如果唯一，还可以将函数的括号省略
    val maxLengthFruit4 = list.maxByOrNull{ fruit: String -> fruit.length}

    // 4. 由于Kotlin可以自动推导类型，还可以省略参数类型声明
    val maxLengthFruit5 = list.maxByOrNull{ fruit -> fruit.length }

    // 5. 当lambda参数列表中只有一个参数时，没必要声明参数，而是用it关键字代替
    val maxLengthFruit6 = list.maxByOrNull{it.length}
    println("lambda表达式经过优化后，最长字符串："+maxLengthFruit6)

    // map函数,映射为大写字母
    val newList = list.map { it.uppercase(Locale.getDefault()) }
    println("使用map函数,映射为大写字母")
    for (fruit in newList){
        print(" "+fruit)
    }
    println()

    // filter函数，过滤数据
    println("使用filter函数，过滤长度小于等于5的数据")
    val list_filter = list.filter { it.length<=5 }
        .map { it.uppercase() }
    for (fruit in list_filter){
        print(" "+fruit)
    }
    println()

    // 函数式API，any和all
    println("函数式API，any和all")
    val anyResult = list.any { it.length<5 }    // 是否至少存在一个元素满足条件
    val allResult = list.all { it.length<5 }    // 是否所有元素满足条件
    println("antResult is "+anyResult+", allResult is "+allResult)

    // Java函数式 API
    /*
    * // Java 的子线程创建方式：
    * new Thread(new Runnable(){
    *   @override
    *   public void run(){
    *       System.out.println("Thread is running");
    *   }
    * })
    * */
    // Kotlin 版本
    // 1. 舍弃new关键字，改用object
    println("Java函数式 API")
    Thread(object: Runnable{
        override fun run() {
            println("\n优化1. 直接用Kotlin重写")
            println("Thread_1 is running")
        }
    }).start()

    // 2. Runnable只有一个待实现的方法: "run()", 故可省略声明
    Thread(Runnable {
        println("\n优化2. 省略类型声明和方法声明")
        println("Thread_2 is running")
    }).start()

    // 3. 如果Java方法的参数列表中不存在一个以上的Java单抽象方法接口参数，可以将接口名省略
    Thread{
        println("\n优化3. 省略接口名")
        println("Thread_3 is running")
    }.start()

    // 仿照上述使用单抽象方法，安卓中使用按钮监听也可以写为
    /*
    * button.setOnClickListener{
    * }
    * */

}

fun getScore(name: String) = when (name){
    "Tom" -> 86
    "Jim" -> 77
    "Jack" -> 95
    "Lily" -> 100
    else -> 0
}
// 不带参数
fun getScore2(name: String) = when{
    // Tom开头的名字
    name.startsWith("Tom") -> 86
    name == "Jim" -> 77
    name == "Jack" -> 95
    name == "Lily" -> 100
    else -> 0
}

// 数据类
data class Cellphone(val band: String, val price: Double)   // 类中没有任何代码时可以省略花括号

// 单例类
// 使用object关键字
object Singleton{
    fun singletonTest(){
        println("singletonTest is called.")
    }
}

```

## 绑定控件

* 在gradle文件中添加

```
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
```

- 在activity文件导入

```kotlin
import kotlinx.android.synthetic.main.activity_main.*
```

- 如果布局中存在id为`tv_text`的TextView

```kotlin
tv_text.text = "你好, kotlin!"
```

## 自定义Kotlin生成的Java类名

- 在kt文件第一行加上

``` kotlin
@file:JvmName("KotlinTestEntity") 
```

- 之后就可以在Java中直接调用

## 参考链接

[Learning materials overview | Kotlin (kotlinlang.org)](https://kotlinlang.org/docs/learning-materials-overview.html)

[在 Android 开发中开始使用 Kotlin  | Android 开发者  | Android Developers](https://developer.android.com/kotlin/get-started)

[(15 封私信 / 32 条消息) Kotlin - 知乎 (zhihu.com)](https://www.zhihu.com/topic/20008824/hot)



# 控件

## 第三方框架

[很值得收藏的安卓开源控件库 - 终端研发部 - 博客园 (cnblogs.com)](https://www.cnblogs.com/codeGoogler/p/9232448.html)



## 步骤条

https://github.com/VictorAlbertos/BreadcrumbsView

[anton46/Android-StepsView: Android-StepsView (github.com)](https://github.com/anton46/Android-StepsView/)

# 一些问题

## 修改控件颜色

* 控件一直是蓝色，修改了也没用

解决方法： 将values/themes/themes.xml中的

`<style name="Theme.MyDemo01" parent="Theme.MaterialComponents.DayNight.DarkActionBar">`

改为：

` <style name="Theme.MyApplication" parent="Theme.MaterialComponents.DayNight.NoActionBar.Bridge">`



## 添加第三方库后运行失败

### `android.enableJetifier=true`

[(6条消息) [Android\][踩坑]gradle中配置android.useAndroidX与android.enableJetifier使应用对support库的依赖自动转换为androidx的依赖_Ryan ZHENG的专栏-CSDN博客_enablejetifier](https://blog.csdn.net/u014175785/article/details/115295136)

android.useAndroidX=true 表示“Android插件会使用对应的AndroidX库，而非Support库”；未设置时默认为false；
android.enableJetifier=true 表示Android插件会通过重写其二进制文件来自动迁移现有的第三方库，以使用AndroidX依赖项；未设置时默认为false；

## Android升级Gradle7后

https://github.com/gradle/gradle/issues/18006

Gradle 7 blocks insecure URIs, so the following no longer works:

```
maven { url "http://mycompany.com/mvn" }
apply from: "http://mycompany.com/buildscript.gradle"
```

The workaround is to opt-in for each and every URL.

```
maven {
    url "http://mycompany.com/mvn"
    allowInsecureProtocol = true
}
apply from: resources.text.fromInsecureUri("http://mycompany.com/buildscript.gradle")
```

But this is not practical for anything more than a handful of projects.

### Expected Behavior

Provide `org.gradle.allow-insecure-protocol=true` to be set in the gradle.properties or elsewhere for a project wide opt-in.

### Current Behavior

Opt-in required for each and every unsecure URL.

### Context

Build fails.

## Android Artic Fox 2020 3.1 新特性

https://blog.csdn.net/zwluoyuxi/article/details/112912410
