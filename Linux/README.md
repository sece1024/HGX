# Ubuntu18.04
## 权限设置
https://www.cnblogs.com/zknublx/p/8422166.html
ubuntu下查看权限的命令为：

ls -l filename

ls -ld folder

ubuntu下设置权限的命令为：

一共有10位数

其中： 最前面那个 - 代表的是类型

中间那三个 rw- 代表的是所有者（user）

然后那三个 rw- 代表的是组群（group）

最后那三个 r-- 代表的是其他人（other）

 

然后我再解释一下后面那9位数：

r 表示文件可以被读（read）

w 表示文件可以被写（write）

x 表示文件可以被执行（如果它是程序的话）

- 表示相应的权限还没有被授予

 

现在该说说修改文件权限了

 

在终端输入：

chmod o+w xxx.xxx

表示给其他人授予写xxx.xxx这个文件的权限

 

chmod go-rw xxx.xxx

表示删除xxx.xxx中组群和其他人的读和写的权限

 

其中：

u 代表所有者（user）

g 代表所有者所在的组群（group）

o 代表其他人，但不是u和g （other）

a 代表全部的人，也就是包括u，g和o

 

r 表示文件可以被读（read）

w 表示文件可以被写（write）

x 表示文件可以被执行（如果它是程序的话）

 

其中：rwx也可以用数字来代替

r ------------4

w -----------2

x ------------1

- ------------0

 

行动：

+ 表示添加权限

- 表示删除权限

= 表示使之成为唯一的权限

 

当大家都明白了上面的东西之后，那么我们常见的以下的一些权限就很容易都明白了：

-rw------- (600) 只有所有者才有读和写的权限

-rw-r--r-- (644) 只有所有者才有读和写的权限，组群和其他人只有读的权限

-rwx------ (700) 只有所有者才有读，写，执行的权限

-rwxr-xr-x (755) 只有所有者才有读，写，执行的权限，组群和其他人只有读和执行的权限

-rwx--x--x (711) 只有所有者才有读，写，执行的权限，组群和其他人只有执行的权限

-rw-rw-rw- (666) 每个人都有读写的权限

-rwxrwxrwx (777) 每个人都有读写和执行的权限 


sudo chmod 600 ××× （只有所有者有读和写的权限） 

sudo chmod 644 ××× （所有者有读和写的权限，组用户只有读的权限） 

sudo chmod 700 ××× （只有所有者有读和写以及执行的权限） 

sudo chmod 666 ××× （每个人都有读和写的权限） 

sudo chmod 777 ××× （每个人都有读和写以及执行的权限） 

 

 

 

若分配给某个文件所有权限，则利用下面的命令：

sudo chmod -R 777 文件或文件夹的名字（其中sudo是管理员权限）


## 防火墙

```bash
sudo ufw status # 查看防火墙状态
sudo ufw enable # 开启
sudo ufw allow 80 # 开发端口
sudo ufw allow 8001/tcp # 指定开放8001的协议
sudo ufw delete allow 8001/tcp # 关闭指定协议端口
sudo ufw reload # 重新加载防火墙
sudo ufw disable # 关闭
sudo ufw allow from 192.xxx.xxx.x # 指定ip为192...的计算机所有端口
sudo ufw delete allow from 192... # 关闭1902...

```
## 终端美化
https://blog.csdn.net/zmx2473162621/article/details/108828584
https://blog.csdn.net/air_knight/article/details/109911039
https://www.jianshu.com/p/d194d29e488c?open_source=weibo_search
## nginx

### 管理nginx进程

```bash
sudo systemctl stop nginx
sudo systemctl start nginx
sudo systemctl restart nginx
sudo systemctl reload nginx
sudo systemctl disable nginx
```

### 服务器模块设置

```bash
sudo nano /etc/hosts
# 添加
#127.0.0.1 test.com www.test.com # 设置域名
sudo /etc/init.d/networking restart	# 重启network
```

## 搭建nginx后端服务器

[Linux搭建服务器Node+Nginx+Tomcat+Redis Ubuntu篇_fw19940314的博客-CSDN博客](https://blog.csdn.net/fw19940314/article/details/80136824)

## 搭建flask后端

[Ubuntu-20.04 Flask Nginx uwsgi supervisor环境搭建_hapyandluck的博客-CSDN博客](https://blog.csdn.net/hapyandluck/article/details/108414754)
https://blog.csdn.net/jspython/article/details/106019068?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-1.control&spm=1001.2101.3001.4242
### Python+flask+mysql

```bash
 sudo apt-get install mysql-server
 #sudo apt-get install python-mysqldb
```

### 修改mysql密码

```bash
# 查看初始账号
sudo vim /etc/mysql/debian.cnf
# 使用初始账号登录
mysql -u xxx -p
# 使用mysql数据库
use mysql

```
### 重装mysql
https://blog.csdn.net/fanrongwoaini/article/details/107518693

## Git
https://blog.csdn.net/weixin_43970743/article/details/117450780

## 词典

https://blog.csdn.net/www_helloworld_com/article/details/85019862?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0.control&spm=1001.2101.3001.4242
