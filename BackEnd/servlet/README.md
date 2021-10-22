# 搭建`servlet`服务器

编译器: IntelliJ IDEA 2021.2.3(Ultimate Edition)

服务器: Tomcat10.0.10

## 配置tomcat

[TomCat服务器搭建及初识servlet_林夕_影的博客-CSDN博客](https://blog.csdn.net/qq_30347133/article/details/83893513?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0.opensearchhbase&spm=1001.2101.3001.4242.0)

[Java Servlet搭建和实现_mengtao0609的专栏-CSDN博客_搭建servlet](https://blog.csdn.net/mengtao0609/article/details/80443810)

[(1条消息) IntelliJ IDEA创建Servlet最新方法 Idea版本2020.2.2以及IntelliJ IDEA创建Servlet 404问题（超详细）_gaoqingliang521的专栏-CSDN博客_idea新建servlet](https://blog.csdn.net/gaoqingliang521/article/details/108677301)

https://help.aliyun.com/document_detail/72732.html

[Tomcat中的乱码问题 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/279185777#:~:text=Tomcat中默,设置为UTF-8)

- 创建项目，在Add Frameworks Support 中添加web 框架
- 在web-WEB-INF下创建classes和lib文件夹，在Project Structure-Modules-Paths-Compiler Output中设置输出路径为刚才创建的文件夹
- 在Run/Debug Configurations - Tomcat Server配置自己的tomcat服务器
- 运行`index.jsp`

**搭建简单页面**

- 添加servlet依赖：在Project Structure-Modules-Dependencies中添加Tomcat安装目录下的`\lib\servlet-api.jar`
- 在`src`下创建一个继承`HttpServlet`的类

```java
public class HelloWorld extends HttpServlet {
    private String message;
    private String message2 = "";

    @Override
    public void init() throws ServletException {
        message = "Hello world, this message is from servlet";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 相应内容
        resp.setContentType("text/html");

        // 实现逻辑
        PrintWriter out = resp.getWriter();
        out.println("<h1>" + message + "</h1>");
        out.println("<h2>"+"This is message2 "+message2+"</h2>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        message2 = req.getQueryString();

        // 实现逻辑
        PrintWriter out = resp.getWriter();
        out.println("<h2>"+"This is message2 "+message2+"</h2>");
        this.doGet(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}

```

- 在web - WEB-INF - web.xml中添加配置

```xml
<servlet>
    <servlet-name>HelloWorld</servlet-name>
    <servlet-class>HelloWorld</servlet-class>        
</servlet>
<servlet-mapping>
    <servlet-name>HelloWorld</servlet-name>
    <url-pattern>/HelloWorld</url-pattern>
</servlet-mapping>
```

- 在浏览器输入`http://localhost:8080/Demo211021_war_exploded/HelloWorld?value=12df`

  后，会得到页面：

  ```html
  <html>
      <head></head>
      <body>
          <h1>Hello world, this message is from servlet</h1>
  		<h2>This is message2 value=1fdsf</h2>
  	</body>
  </html>
  ```

  

- 使用Postman发送post请求可以改变页面显示内容，在浏览器修改"?xx=xxx"似乎不行？

## 配置MySql

[IntelliJ IDEA 连接数据库 详细过程 - 东聃 - 博客园 (cnblogs.com)](https://www.cnblogs.com/Ran-Chen/p/9646187.html)

- 安装mysql驱动后将其添加到模块依赖当中`C:\Users\dell\AppData\Roaming\JetBrains\IntelliJIdea2021.2\jdbc-drivers\MySQL ConnectorJ\8.0.25`
- 创建测试用的数据表

```bash
mysql> create table account(
    -> username VARCHAR(40) NOT NULL,
    -> age INT NOT NULL,
    -> id INT NOT NULL AUTO_INCREMENT,
    -> PRIMARY KEY(id)
    -> )ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.06 sec)
```

- 在项目下创建mysql连接类
  - `com.mysql.jdbc.Driver`需要改为`com.mysql.cj.jdbc.Driver`

```java
public class ConnMySql {
    public static void main(String[] args) throws Exception{
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        try{
            // 加载驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            long start = System.currentTimeMillis();
            // 建立连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc",
                    "root", "root");
            long end = System.currentTimeMillis();
            System.out.println(conn);
            System.out.println("建立连接耗时: " + (end - start) + "ms");
            stmt = conn.createStatement();
            // 执行SQL语句
            rs = stmt.executeQuery("select * from account");
            System.out.println("");
            while(rs.next()){
                System.out.println(rs.getString(1) + "\t" + rs.getInt(2));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (null != rs){
                    rs.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        try {
            if (null != stmt){
                stmt.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        try {
            if (null != conn){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
```

## 无法通过http请求控制服务器查询数据的原因

- 连接`mysql`的驱动`mysql-connector-java`没有放入tomcat的lib文件夹，tomcat找不到驱动。