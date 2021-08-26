# Nginx

- 反向代理

  *作用于服务器：客户端访问反向代理服务器，反向代理服务器再访问真实服务器，客户端不知道真实服务器的存在。*

- 负载均衡

  *将请求分散到多个服务器上。*

- 动静分离

  *专门的服务器存放静态资源。*

- 高可用

## 常用命令

```properties
# 快速关闭Nginx，可能不保存相关信息，并迅速终止web服务
nginx -s stop
# 平稳关闭Nginx，保存相关信息，有安排的结束web服务
nginx -s quit
# 因改变了Nginx相关配置，需要重新加载配置而重载
nginx -s reload
# 重新打开日志文件
nginx -s reopen
# 为 Nginx 指定一个配置文件，来代替缺省的
nginx -c filename
# 不运行，而仅仅测试配置文件。nginx 将检查配置文件的语法的正确性，并尝试打开配置文件中所引用到的文件
nginx -t
#  显示 nginx 的版本
nginx -v
# 显示 nginx 的版本，编译器版本和配置参数
nginx -V
# 格式换显示 nginx 配置参数
2>&1 nginx -V | xargs -n1
2>&1 nginx -V | xargs -n1 | grep lua
```



## 配置文件

**反向代理**

```
server {  
        listen       8080;        
        server_name  localhost;

        location / {
            root   html; # Nginx默认值
            index  index.html index.htm;
        }
        
        proxy_pass http://localhost:8000; # 反向代理配置，请求会被转发到8000端口
}

```

### 全局块

* 配置文件到events块之间的内容，主要设置影响nginx服务器整体运行的配置命令

比如：

```
#user  nobody;
worker_processes  1;
```

worker_processes 值越大，可以支持的并发处理量也越多。

### events块

* 涉及的指令主要影响Nginx服务器与用户的网络连接

如：

```
worker_connections 1024;
```

表示支持的最大连接数。

* 代理、缓存、日志定义等大多数功能和第三方模块的配置都在这里，是配置最频繁的部分。

### http块

