# 在Android上通过OkHttp向本地服务器的8080端口发起网络请求

## 主要步骤

- 首先需要搭建一个本地服务器，并且连接数据库

  之前准备的服务器地质为：`http://localhost:8080/DBAccess`

  基本操作有1. 查询数据库的`account`表；2.向`account`表插入数据（`姓名`和`年龄`）

- 在Android Studio项目中添加OkHttp依赖

  `    implementation("com.squareup.okhttp3:okhttp:4.9.2")`

- 搭建界面布局，写点击不同按钮进行查询或插入的代码

  - 查询

    ```java
    // 查询
    binding.btnSearch.setOnClickListener(v -> {
        String url = binding.etIp.getText().toString();
        Log.d(TAG, "setBtnListener: url = "+url);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = client.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
    
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String s = Objects.requireNonNull(response.body()).string();
                Log.d(TAG, "onResponse: s = \t"+s);
                handlerFlushTV.post(() -> {
                    binding.tvResultInfo.setText(s);
                    binding.wvWeb.loadUrl(url);
                });
            }
        });
    });
    ```

    

  - 插入

    ```java
    // 插入
    binding.btnAdd.setOnClickListener(v->{
        String name = binding.etUsername.getText().toString();
        String age = binding.etAge.getText().toString();
        if (name.equals("") || age.equals("")){
            String s = "用户名和年龄不能为空";
            Log.d(TAG, "setBtnListener: " + s);
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            handlerFlushTV.post(()->binding.tvResultInfo.setText(s));
    
        }else {
            String url = IP + "/DBAccess";
            Log.d(TAG, "setBtnListener: url = " + url);
            RequestBody fromBody = new FormBody.Builder()
                .add("username", name)
                .add("age", age)
                .build();
            Request request = new Request.Builder()
                .url(url)
                .post(fromBody)
                .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }
    
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String str = response.body().string();
                    Log.d(TAG, "onResponse: str = \n" + str);
                    handlerFlushTV.post(()->{
                        binding.tvResultInfo.setText(str);
                        binding.wvWeb.loadUrl(url);
                    });
                }
            });
        }
    });
    
    ```

    

## 重点

- 模拟器中访问本地ip应该输入`http://10.0.2.2:8080`（参考[Android 模拟器的本地ip_mazaiting的博客-CSDN博客](https://blog.csdn.net/mazaiting/article/details/72822948)）

- 发起`http`请求需要在`AndroidManifest.xml`中申请网络权限`android.permission.INTERNET`

  并且还要在`application`中设置`android:usesCleartextTraffic="true"`

## 代码

### 服务器页面

`account`表只有三个字段`username`, `age`, `id`.

```java
public class DBAccess extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // JDBC
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // 数据库URL
    static final String DB_URL = "jdbc:mysql://localhost:3306/testjdbc";
    // 用户名与密码
    static final String USER = "root";
    static final String PSW = "root";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DBAccess(){
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;
        Statement stmt = null;
        // 响应内容
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String title = "Servlet Mysql 测试";

        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n");
        try{
            // 注册 JDBC 驱动器  web/WEB-INF/lib/mysql-connector-java-8.0.25.jar/com/mysql/jdbc/Driver.class
            Class.forName(JDBC_DRIVER);

            // 打开一个连接
            conn = DriverManager.getConnection(DB_URL,USER,PSW);

            // 执行 SQL 查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, username, age FROM account";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                int id  = rs.getInt("id");
                String name = rs.getString("username");
                int age = rs.getInt("age");

                // 输出数据
                out.println("ID: " + id);
                out.println(", 姓名: " + name);
                out.println(", 年龄: " + age);
                out.println("<br />");
            }
            out.println("</body></html>");

            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch(Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 最后是用于关闭资源的块
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user_name = req.getParameter("username");
        int user_age = Integer.parseInt(req.getParameter("age"));

        Connection conn = null;
        Statement stmt = null;
        // 响应内容
        resp.setContentType("text/html;charset=UTF-8");

        try{
            // 注册 JDBC 驱动器  web/WEB-INF/lib/mysql-connector-java-8.0.25.jar/com/mysql/jdbc/Driver.class
            Class.forName(JDBC_DRIVER);

            // 打开一个连接
            conn = DriverManager.getConnection(DB_URL,USER,PSW);

            // 执行 SQL 查询
            stmt = conn.createStatement();
            String sql;
            sql = "insert into account (username, age) values ('"+user_name+"', "+user_age+")";
            System.out.println("sql = " + sql);
            boolean b = stmt.execute(sql);
            if (b){
                System.out.println("插入失败");
            }else{
                System.out.println("数据插入成功\tusername = "+user_name+"\tage = "+user_age);
            }
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch(Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 最后是用于关闭资源的块
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        doGet(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}

```

### Android界面布局

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="15dp">
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal">
                <TextView
                    android:layout_width="85dp"
                    android:layout_height="match_parent"
                    android:text="IP"
                    android:textSize="@dimen/text_common_content"
                    android:gravity="center"/>
                <EditText
                    android:id="@+id/et_ip"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:textSize="@dimen/text_common_content"
                    android:textColor="@color/colorAccent"
                    android:gravity="center"/>
            </LinearLayout>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal">
                <TextView
                    android:layout_width="85dp"
                    android:layout_height="match_parent"
                    android:text="用户名"
                    android:textSize="@dimen/text_common_content"
                    android:gravity="center"/>
                <EditText
                    android:id="@+id/et_username"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:textSize="@dimen/text_common_content"
                    android:gravity="center"/>
            </LinearLayout>
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal">
                <TextView
                    android:layout_width="85dp"
                    android:layout_height="match_parent"
                    android:text="年龄"
                    android:textSize="@dimen/text_common_content"
                    android:gravity="center"/>
                <EditText
                    android:id="@+id/et_age"
                    android:inputType="number"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:textSize="@dimen/text_common_content"
                    android:gravity="center"/>
            </LinearLayout>
            <Button
                android:id="@+id/btn_add"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="@dimen/text_common_btn"
                android:text="添加数据"/>
            <Button
                android:id="@+id/btn_search"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="@dimen/text_common_btn"
                android:text="查询"/>
            <ScrollView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                >
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    tools:ignore="WebViewLayout">
                    <TextView
                        android:id="@+id/tv_result_info"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"/>
                    <WebView
                        android:id="@+id/wv_web"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content">
                    </WebView>
                </LinearLayout>

            </ScrollView>
        </LinearLayout>

    </androidx.coordinatorlayout.widget.CoordinatorLayout>
</layout>
```

### Activity

```java
public class NetTestActivity extends AppCompatActivity {
    private static final String TAG = "NetTestActivity";
    private ActivityNetTestBinding binding;
    private static final String IP = "http://10.0.2.2:8080";
    final OkHttpClient client = new OkHttpClient();
    private final Handler handlerFlushTV = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_net_test);
        binding.etIp.setText(IP);
        setBtnListener();
    }
    private void setBtnListener(){
        // 查询
        binding.btnSearch.setOnClickListener(v -> {
            String url = binding.etIp.getText().toString();
            Log.d(TAG, "setBtnListener: url = "+url);
            Request.Builder requestBuilder = new Request.Builder().url(url);
            requestBuilder.method("GET", null);
            Request request = requestBuilder.build();
            Call mcall = client.newCall(request);
            mcall.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String s = Objects.requireNonNull(response.body()).string();
                    Log.d(TAG, "onResponse: s = \t"+s);
                    handlerFlushTV.post(() -> {
                        binding.tvResultInfo.setText(s);
                        binding.wvWeb.loadUrl(url);
                    });
                }
            });
        });
        // 插入
        binding.btnAdd.setOnClickListener(v->{
            String name = binding.etUsername.getText().toString();
            String age = binding.etAge.getText().toString();
            if (name.equals("") || age.equals("")){
                String s = "用户名和年龄不能为空";
                Log.d(TAG, "setBtnListener: " + s);
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                handlerFlushTV.post(()->binding.tvResultInfo.setText(s));
            }else {
                String url = IP + "/DBAccess";
                Log.d(TAG, "setBtnListener: url = " + url);
                RequestBody fromBody = new FormBody.Builder()
                        .add("username", name)
                        .add("age", age)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(fromBody)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String str = response.body().string();
                        Log.d(TAG, "onResponse: str = \n" + str);
                        handlerFlushTV.post(()->{
                            binding.tvResultInfo.setText(str);
                            binding.wvWeb.loadUrl(url);
                        });
                    }
                });
            }
        });
    }

}

```

