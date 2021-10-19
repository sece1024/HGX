[TOC]



# Java 基础

## 反射

[JavaGuide/反射机制.md at master · Snailclimb/JavaGuide (github.com)](https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/反射机制.md)

> 反射之所以被称为框架的灵魂，主要是因为它赋予了我们在运行时分析类以及执行类中方法的能力。
>
> 通过反射你可以获取任意一个类的所有属性和方法，你还可以调用这些方法和属性。

### 应用场景

#### [动态代理](../DesignPatterns/README.md#dynamic-proxy)
``` java
public class DebugInvocationHandler implements InvocationHandler {
    /**
     * 代理类中的真实对象
     */
    private final Object target;

    public DebugInvocationHandler(Object target) {
        this.target = target;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        System.out.println("before method " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("after method " + method.getName());
        return result;
    }
}
```

#### 注解



### 优缺点

**优点**

- 代码更灵活。

**缺点**

- 运行时分析操作类增加了安全问题：泛型参数的安全检查发生在编译时，而反射能够无视泛型参数的安全检查；
- 反射的性能稍差。

### 使用示例

**首先定义一个用于测试的类**

```java
/**
 * 反射测试：
 * 目标类
 */
public class PersonObject {
    private String name;
    private int age;
    public PersonObject(){
        name = "lina";
        age = 21;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * print "person say str"
     * @param str
     */
    public void publicMethod(String str){
        System.out.println(name + " say " + str + " to you.");
    }
    
    /**
     * print person's age
     */
    public void privateMethod(){
        System.out.println(name + " is " + age + " years old.");
    }
}
```
---

**具体使用**

```java
    // 反射测试
    private void reflectTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        /**
         * 获取TargetObject类的Class对象并且创建TargetObject类实例
         */
        // 通过类获取
//        Class<?> targetClass = PersonObject.class;

        // 通过包名获取
//        Class<?> targetClass = Class.forName("com.sece.imagequalitydetection.test.PersonObject");

        // 通过实例对象获取
//        PersonObject personObject = new PersonObject();
//        Class<?> targetClass = personObject.getClass();

        // 通过类加载器传入类路径获取
        Class<?> targetClass = getClassLoader().loadClass("com.sece.imagequalitydetection.test.PersonObject");


        PersonObject targetObject = (PersonObject) targetClass.newInstance();

        /**
         * 获取所有类中所有定义的方法
         */
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method m :
                methods) {
            System.out.println(m.getName());
        }
        /**
         * 获取指定方法并调用
         */
        // public 方法
        Method publicMethod = targetClass.getDeclaredMethod("publicMethod",
                String.class);
        // private 方法
        Method privateMethod = targetClass.getDeclaredMethod("privateMethod");
        System.out.println("-------------------");
        publicMethod.invoke(targetObject, "你好");
        privateMethod.invoke(targetObject);

        /**
         * 获取指定参数并对参数进行修改
         */
        Field field = targetClass.getDeclaredField("age");
        //为了对类中的参数进行修改我们取消安全检查
        field.setAccessible(true);
        field.set(targetObject, 63);

        Field field2 = targetClass.getDeclaredField("name");
        field2.setAccessible(true);
        field2.set(targetObject, "小王");

        // 取消安全检查
//        privateMethod.setAccessible(true);
//        privateMethod.invoke(targetObject);

        // 第二次调用
        System.out.println("-------------------");
        publicMethod.invoke(targetObject, "hello");
        privateMethod.invoke(targetObject);
    }

```

**输出**

```bash
I/System.out: getAge
    getName
    privateMethod
    publicMethod
    setAge
    setName
    -------------------
    lina say 你好 to you.
    lina is 21 years old.
    -------------------
I/System.out: 小王 say hello to you.
    小王 is 63 years old.
```

## 枚举

### 链接

[A Guide to Java Enums | Baeldung](https://www.baeldung.com/a-guide-to-java-enums)

[JavaGuide/用好Java中的枚举真的没有那么简单.md at master · Snailclimb/JavaGuide (github.com)](https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/用好Java中的枚举真的没有那么简单.md)

### 优缺点

**优点**

- 使用枚举定义常量会让代码更有可读性
- 允许编译时检查，预先记录可接收值的列表

**缺点**

- 

### 自定义枚举方法

*由于目前做的项目有用到图像质量检测，因此学习枚举的同时也用这个来举例。*

```java
/**
 * 枚举：
 * 图像质量检测
 */
public class Detection {
    public enum DetectionState{
        COLOR,          // 颜色
        COLOR_CAST,     // 色偏
        CLARITY,        // 清晰度
        LUMINANCE,      // 亮度
        BLUR            // 模糊度
    }
    private DetectionState status;

    public boolean isColorDetect(){
    /*
     * 枚举类确保JVM中仅存在一个常量实例，因此可以安全地使用 “==” 运算符
     * 如果两个值为空，使用“return getStatus().equals(DetectionState.COLOR);”
     * 反倒会抛出空指针异常。
     */
        return getStatus() == DetectionState.COLOR;
    }
    // region getter and setter

    public DetectionState getStatus() {
        return status;
    }
    

    public void setStatus(DetectionState status) {
        this.status = status;
    }

    // endregion
}
```

