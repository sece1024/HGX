# 章节

## 7-事件总线

> 事件总线EventBus和otto，解决组件之间高耦合的同时仍能继续高效地通信

### EventBus

#### 介绍

* 发布-订阅事件总线
* 优点：
  * 开销小，代码更优雅
  * 将发送者和接收者解耦



#### 使用

##### EventBus的三要素

1. Event: 事件，可以是任意类型的对象
2. Subscribe: 事件订阅者，事件处理方法可以随便取名，但需要添加注解`@Subscribe`，并且指定线程模型（ThreadMode）
3. Publisher: 事件发布者，可以自己实例化EventBus对象，一般使用`EventBus.getDefault()`

##### 四种ThreadMode(线程模型)

1. POSTING：该事件在哪个线程发布，事件处理函数在哪个线程中运行（发送和接收是在同一个线程）。该模型的事件处理函数应尽量避免执行耗时的操作，因为它会阻塞事件的传递，甚至引起ANR(Application Not Responding)问题
2. MAIN：事件的处理会在UI线程中执行。处理事件太长会引起ANR问题
3. BACKGROUND：如果事件是在UI线程中发布，事件处理函数就会在新的线程中运行。如果在子线程中发布，那么事件处理函数直接在发送事件的线程中执行。在此事件处理函数中禁止进行UI更新操作
4. ASYNC：无论在哪里发布，该事件处理函数都会在新建的子线程中执行。此事件处理函数禁止UI更新操作

```java
private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
    switch (subscription.subscriberMethod.threadMode) {
        case POSTING:
            invokeSubscriber(subscription, event);
            break;
        case MAIN:          
            if (isMainThread) {    // 是主线程
                // 通过反射直接运行订阅的方法
                invokeSubscriber(subscription, event);
            } else {
                // 否则，需要mainThreadPoster将订阅事件添加到主线程队列
                // mainThreadPoster是HandlerPoster类型的，继承自Handler，通过Handler将订阅方法切换到主线程执行
                mainThreadPoster.enqueue(subscription, event);
            }
            break;
        case BACKGROUND:
            if (isMainThread) {
                backgroundPoster.enqueue(subscription, event);
            } else {
                invokeSubscriber(subscription, event);
            }
            break;
        case ASYNC:
            asyncPoster.enqueue(subscription, event);
            break;
        default:
            throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
    }
}
```



##### 基本用法

* 自定义事件类

```java
public class MessageEvent{
    
}
```

* 在需要订阅事件的地方注册事件

```java
EventBus.getDefault().register(this);
```

* 发送事件

```java
EventBus.getDefault().post(messageEvent);
```

* 处理事件

```java
@Subscribe (threadMode = ThreadMode.MAIN)
public void XXX(MessageEvent messageEvent){
    
}
```

* 取消事件订阅

```java
EventBus.getDefault().unregister(this);
```

```java
/** Only updates subscriptionsByEventType, not typesBySubscriber! Caller must update typesBySubscriber. */
private void unsubscribeByEventType(Object subscriber, Class<?> eventType) {
    // 获取对应的订阅对象集合
    List<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
    if (subscriptions != null) {
        int size = subscriptions.size();
        for (int i = 0; i < size; i++) {
            Subscription subscription = subscriptions.get(i);
            if (subscription.subscriber == subscriber) {	// 如果订阅对象的订阅者属性等于传进来的订阅者
                // 移除该订阅对象
                subscription.active = false;
                subscriptions.remove(i);
                i--;
                size--;
            }
        }
    }
}

/** Unregisters the given subscriber from all event classes. */
public synchronized void unregister(Object subscriber) {
    // 通过订阅者找到事件类型集合
    List<Class<?>> subscribedTypes = typesBySubscriber.get(subscriber);
    if (subscribedTypes != null) {
        for (Class<?> eventType : subscribedTypes) {
            // 将订阅者对应的事件类型从事件类型集合中移除
            unsubscribeByEventType(subscriber, eventType);
        }
        typesBySubscriber.remove(subscriber);
    } else {
        Log.w(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
    }
}
```

##### ProGuard混淆配置

```properties
-keepattributes *Annotation*
-keepclassmembers class **{
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode{*;}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent{
    <init>(java.lang.Throwable);
}
```

##### EventBus的黏性事件

> 即在发送事件之后再订阅该事件也能收到该事件，这跟黏性广播类似。

```
// 订阅者处理黏性事件
@Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
public void ononMoonStrickyEvent(MessageEvent messageEvent){
    mtvShow.setText(messageEvent.getMessage());
}
```

```
// 发送黏性事件
public void sendStickyEvent(View view) {
    EventBus.getDefault().postSticky(new MessageEvent("这是一个黏性事件"));
    finish();
}
```

### otto

> Square公司发布的一个发布-订阅模式框架，基于EventBus模块开发，针对Android平台做了优化和加强。
>
> Square已停止对otto的更新并推荐使用RxJava和RxAndroid代替它。



#### 依赖库

```gradle
implementation 'com.squareup:otto:1.3.8'
```

#### 定义消息

*这是一个bean文件*

```java
public class BusData {
    public String message;
    public BusData(String message){
        this.message = message;
    }
    public String getMessage(){return message;}

    public void setMessage(String message) {
        this.message = message;
    }


}
```

#### 单例封装Bus

*otto的Bus类相当于EventBus中的EventBus类，它封装了otto的主要功能，但它不是一个单例，为了方便使用，在这里用单例模式将它再次封装。*

```java
public class OttoBus extends Bus {
    private volatile static OttoBus bus;
    private OttoBus(){
    }

    public static OttoBus getInstance(){
        if (bus == null){
            synchronized (OttoBus.class){
                if (bus == null){
                    bus = new OttoBus();
                }
            }
        }
        return bus;
    }
}
```

#### 注册/取消注册事件

```java
public class MessageMainActivity extends AppCompatActivity {
    private TextView mTvOtto;
    private Bus bus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_main);
        mTvOtto = findViewById(R.id.tv_otto);

        bus = OttoBus.getInstance();

    }

    // 界面跳转
    public void goToSecond(View view) {
        startActivity(new Intent(this, MessageSecondActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册事件
        bus.unregister(this);
    }

    // 注册otto事件
    public void registOttoEvent(View view) {

        bus.register(this);
    }
}
```

#### 事件订阅者处理事件

    @com.squareup.otto.Subscribe
    public void setContent(BusData data){
        mTvOtto.setText(data.getMessage());
    }

#### 使用post发送事件

```java
public class MessageSecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_second);

    }

    public void sendOttoEvent(View view) {
        OttoBus.getInstance().post(new BusData("这是一个otto事件"));
        finish();
    }
}
```

#### 使用`@Produce`发送事件

```java
public class MessageSecondActivity extends AppCompatActivity {
    private OttoBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_second);

        bus = OttoBus.getInstance();
        bus.register(this);
    }
    // 这个注解用于生产发送事件
    // 它生产事件前需要进行注册，生产完事件后需要取消注册
    // 如果使用这种方法，则在跳转到发布者所在类中时会立即产生事件并出发订阅者
    @Produce
    public BusData setInitialContent(){
        return new BusData("otto bus 更新了");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    public void sendOttoEvent(View view) {
        OttoBus.getInstance().post(new BusData("这是一个otto事件"));
        finish();
    }
}
```

在跳转到`MessageSecondActivity`界面时，`MessageMainActivity`会马上收到事件，事实上，在`MessageMainActivity`注册otto事件并点击跳转按钮，界面尚未切换时`MessageMainActivity`就已经收到了事件并且改变了显示的文字。

## 8-函数式编程

> 函数式编程是面向数学的抽象，将计算描述为一种表达式求值。
>
> 函数式编程可以极大地简化项目，尤其可以处理嵌套回调的异步事件、复杂的列表过滤和变换，以及与时间相关的问题。

### RxJava

> RxJava是ReactiveX的一种Java实现。
>
> ReactiveX是Reactive Extension的缩写，它是一个函数库，让开发者可以利用可观察序列和LINQ（Language Integrated Query）风格查询操作符来编写异步和基于事件的程序，用Observables表示异步数据流，用Schedulers参数化异步数据流的并发处理。
>
> Rx = Observables + LINQ + Schedulers

