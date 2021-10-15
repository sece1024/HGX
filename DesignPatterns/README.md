# design pattern

## 面向对象设计原则

| 名称 |    定义  | 使用频率     |
| :--: | :--: | :--: |
| 单一职责原则(Single Responsibility Principle, SRP) | 一个类只负责一个功能领域中的相应职责 | 4 |
| 开闭原则(Open-Closed Principle, OCP) | 软件实体对应扩展开放，而对修改关闭 | 5 |
| 里氏代换原则(Liskov SubStitution Principle, LSP) | 所有引用基类对象的地方能够透明地使用其子类的对象 | 5 |
| 依赖倒转原则(Dependence Inversion Principle,DIP) | 抽象不应该依赖于细节，细节应该依赖于抽象 | 5 |
| 接口隔离原则(Interface Segregation Principle, ISP) | 使用多个专门的接口，而不使用单一总接口 | 2 |
| 合成复用原则(Composite Reuse Principle,CRP) | 尽量使用对象组合，而不是继承来达到复用的目的 | 4 |
| 迪米特法则(Law of Demeter, LoD) | 一个软件实体应该尽可能少地与其他实体发生相互作用 | 3 |

## 代理

[JavaGuide/代理模式详解.md at master · Snailclimb/JavaGuide (github.com)](https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/代理模式详解.md)

> 使用代理对象，代替对真实对象的访问，在不修改目标对象的前提下，提供额外的功能操作，扩展目标对象的功能

### 静态代理

*假设有打印一段字符串的接口`IPrint`，`ClassA`实现了这个接口，如果想要在打印字符串之前或者之后做一些额外操作，可以使用代理类`ProxyA`实现与`ClassA`相同的接口。*

*在代理类内部声明`IPrint`接口，实际使用代理类时，通过构造函数将`ClassA`注入代理类`ProxyA`*

*在代理类重载的对应方法中，调用目标类的对应方法，在调用前后可以执行额外操作。*

```java
// 接口
public interface IPrint{
    String print(String str);
}
```

```java
// ClassA
public class ClassA implements IPrint{
    public String print(String str){
        System.out.println("print message: " + str);
        return str;
    }
}
```

```java
// ProxyA
public class ProxyA implements IPrint{
    private final IPrint iPrint;
    public ProxyA(IPrint iPrint){
        this.iPrint = iPrint;
    }
    @Override
    public String print(String str){
        System.out.println("发送信息之前");
        iPrint.print(str);
        System.out.println("发送信息之后");
        
        return null;
    }
}
```

```java
// 使用
public class Main{
    public static void main(Stringp[] args){
        IPrint iprint = new ClassA();
        ProxyA proxyA = new ProxyA(iPrint);
        
        proxyA.print("这是一条消息");
    }
}
```

*这样就屏蔽了对`ClassA`对象的直接访问，而是通过代理`ProxyA`对象间接访问，在间接调用`ClassA`的对应方法前后，ProxyA执行新增的操作*

#### 特点

* 需要针对每个目标类都单独创建代理类
* 必须在目标类和代理类中都实现接口
* 一旦新增方法，目标对象和代理对象都要进行修改。非常麻烦。



### 动态代理

> 相比于静态代理，动态代理更加灵活，不需要针对每个目标类都创建一个代理类，可以直接代理实现类
>
> 静态代理在编译时就将接口、实现类、代理类这些都变成了一个个实际的 class 文件。而动态代理是在运行时动态生成类字节码，并加载到 JVM 中的。

#### JDK动态代理机制

> **从 JVM 角度来说，动态代理是在运行时动态生成类字节码，并加载到 JVM 中的。**

---

*实现`InvocationHandler`来自定义处理逻辑*

>  当动态代理对象调用一个方法时，这个方法的调用就会被转发到实现`InvocationHandler` 接口类的 `invoke` 方法来调用。

```java
/**
 * 动态代理
 */
public class DebugInvocationHandler implements InvocationHandler {
    // 代理类中的真实对象
    private final Object target;
    public DebugInvocationHandler(Object o){
        this.target = o;
    }
    /**
     *
     * @param proxy 动态生成的代理类
     * @param method 代理类对象调用的方法相对应
     * @param args  当前 method 方法的参数
     * @return
     * @throws Throwable
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, InvocationTargetException, IllegalAccessException {
        // 调用前执行
        System.out.println("调用前执行");
        Object result = method.invoke(target, args);
        // 调用后执行
        System.out.println("调用后执行");

        return result;
    }
}
```

```java
/**
 * 获取代理的工厂类
 */
public class JdkProxyFactory{
    public static Object getProxy(Object target){
        // newProxyInstance() 主要用来生成一个代理对象
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new DebugInvocationHandler(target)
        );
    }
}
```

```java
// 使用
public class Main{
    public static void main(Stringp[] args){
        // 通过工厂类创建
        IPrint iprint = (ClassA) JdkProxyFactory.getProxy(new ClassA());
        // 也可以直接创建，每次都要写一长串，比较麻烦
        /*
        IPrint iprint = (ClassA) Proxy.newProxyInstance(
                ClassA.class.getClassLoader(),
                ClassA.class.getInterfaces(),
                new DebugInvocationHandler(new ClassA()));
         */
        iPrint.print("动态代理：This is a message");
    }
}
```



> `Proxy` 类中使用频率最高的方法是：`newProxyInstance()`

*通过代理类的`newProxyInstance()`创建的代理对象在调用方法时，会调用`InvocationHandler` 接口类的`invoke()`方法。*

*可以在 `invoke()`方法中自定义方法执行前后的处理逻辑。*



---

**步骤**

* 定义接口及其实现类
* 自定义 `InvocationHandler`并重写 `invoke()`方法，在这个方法中调用原生方法
* 通过 `Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)`方法创建代理对象

