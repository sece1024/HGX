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

    // 空指针检查
    // doStudy(null);   // 会报错
    doStudy2(null); // 使用可空类型和空指针检查






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
// "open" 表示可被继承
open class Person{
    var age = 0
    var name = ""
    fun eat(){
        println(name + " is eating. He's age is "+ age + " years old.")
    }

}
// 接口
interface Study{
    fun readBooks()
    fun doHomework()
}
// 学生类
class Student(): Person(), Study{
    override fun readBooks() {
        println(name + "is reading.")
    }

    override fun doHomework() {
        println(name + "is doing homework.")
    }

}
fun doStudy(study: Study){
    study.doHomework()
    study.readBooks()
}
// 使用可空类型和空指针检查
fun doStudy2(study: Study?){
    if (study != null) {
        study.doHomework()
        study.readBooks()
    }
}

// "?."操作符
// 对象为空时什么都不做，否则正常调用相应的方法
fun doStudy3(study: Study?){
    study?.doHomework()
    study?.readBooks()
}
