# Ubuntu18.04

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