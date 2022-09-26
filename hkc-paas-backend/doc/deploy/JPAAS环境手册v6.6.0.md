JPAAS-6.6.0版本环境手册
## 6.6.0
### 开发环境
#### Windows
推荐目前主流的windows10专业版。
##### 工具软件准备
具体的安装步骤及说明可以参考这里的文章：
http://doc.redxun.cn/docs/jpaas/jpEnvPre
###### Idea
推荐2021.1版本
###### JDK
安装1.8以上版本，推荐jdk1.8.0_202 64位版本。

###### MSQL
安装8以上版本，推荐8.0.25
###### NodeJS
推荐版本为v14.X系列，自带的npm版本默认为6系列，从官网下载安装，地址为：
http://nodejs.cn/download/
###### MAVEN(可选)
6.6.0版本集成了maven仓库配置，如果没有发布版本到私服的需求，可以直接用idea编辑器默认的maven。参考下图
![3a5915e315490ca7c3bda9147fb42794.png](_resources/3a5915e315490ca7c3bda9147fb42794.png)
###### Redis(可选)
6.6.0版本集成了redis启动脚本及可运行文件，目前版本为3.0.504，可参考下图
![4a4ef4155cd55386507c70ff81118927.png](_resources/4a4ef4155cd55386507c70ff81118927.png)
启动脚本为：
![deb5566006c62022513218ac8440ca00.png](_resources/deb5566006c62022513218ac8440ca00.png)
###### Rocket MQ(可选)
6.6.0版本集成了redis启动脚本及可运行文件，目前版本为4.9.3
###### Nginx(可选)
6.6.0版本集成了nginx启动脚本及可运行文件,以及进行nginx.conf默认配置，可参考下图
![d6140686b1e65d73a0e05b4aa9e1a6b8.png](_resources/d6140686b1e65d73a0e05b4aa9e1a6b8.png)
开发的配置文件已经集成配置好
![4e0d852f53cd2f7ad3fd7654b8a33216.png](_resources/4e0d852f53cd2f7ad3fd7654b8a33216.png)
###### NACOS(可选)
6.6.0版本集成了nacos启动脚本及可运行文件,版本为自编译2.1.0, 默认的命名空间为local, 需要自己创建，具体的启动位置为：
![8fccbc04de83df3e1788a01b1cf08978.png](_resources/8fccbc04de83df3e1788a01b1cf08978.png)
命名空间创建示例如下：
![c8bc5b5ac61ece6494d7e27ac8deb363.png](_resources/c8bc5b5ac61ece6494d7e27ac8deb363.png)
###### SEATA(可选)
6.6.0版本集成了seata启动脚本及可运行文件,版本为自编译1.5.2.1,具体的启动位置为：
![a100dd4f04e0af2d6d6831be96932f81.png](_resources/a100dd4f04e0af2d6d6831be96932f81.png)

###### HBuilder(可选)
如需要进行移动端开发及编译，打包，发布，需要安装。从官网安装即可，地址为：
https://www.dcloud.io/hbuilderx.html

##### jpaas-backend后端启动
###### 代码获取
http://dev.redxun.cn:18080/jpaas_application/jpaas-backend.git
导入idea编辑器
将前后端工程分别导入到idea编辑器，最终的结构如下：
![aed6242139f37f44b49ed2086e501c5f.png](_resources/aed6242139f37f44b49ed2086e501c5f.png)

###### 安装MYSQL8
安装MYSQL8系列版本，执行初始化脚本，注意MYSQL需要配置忽略大小写，如下图：
![aed6242139f37f44b49ed20834534567.png](_resources/aed6242139f37f44b49ed20834534567.png)

###### Redis启动
	进入项目的relase工程下的bin目录，直接在idea的Terminal下打开，执行redis-start.cmd
如下图所示：
![f34c2482ee6e2272f7a370dc9124e504.png](_resources/f34c2482ee6e2272f7a370dc9124e504.png)
可以输入数字1或者2，选择单次启动redis，或者以服务形式启动redis，如下图：
![bec74f54d1327f3b7be0777ab5b72806.png](_resources/bec74f54d1327f3b7be0777ab5b72806.png)
这里以单次启动，选择1，可以看到加载的配置文件，默认密码是redis，如下图：
![c52712382f3b067492fbd7a0566624b7.png](_resources/c52712382f3b067492fbd7a0566624b7.png)
###### Nacos启动
同redis一样进入release工程的bin目录，启动nacos-start.cmd, 如下图所示:
![7002bbdd05b1f14071fd3571672036fd.png](_resources/7002bbdd05b1f14071fd3571672036fd.png)
默认命名空间为local,导入nacos默认配置，配置文件位置如下：
![98e7d4800f47a406a88d7fc1096ee8a5.png](_resources/98e7d4800f47a406a88d7fc1096ee8a5.png)
启动效果如下：
![ec81a48599d582b0f9bc2c2fb1427e12.png](_resources/ec81a48599d582b0f9bc2c2fb1427e12.png)

###### Seata启动
同上进入release工程的bin目录，执行seata-start.bat命令启动，如下图：
![c7afd67dfc8cd83199e88ff8eb3637eb.png](_resources/c7afd67dfc8cd83199e88ff8eb3637eb.png)
启动成功可以看到会注册到nacos里面。
![fb2e976aa3328ec3ab02ee063b733ec7.png](_resources/fb2e976aa3328ec3ab02ee063b733ec7.png)
###### Rocketmq启动(可选)
Rokcetmq为系统的消息服务器，可以选择启动，也可以不启动，涉及功能有消息的发送及系统通知，日志相关。
同样进入release工程的bin目录, 执行rocketmq-start.cmd命令，如下图：
![c61f8e2864d2febd6fc6a7f4d34702ac.png](_resources/c61f8e2864d2febd6fc6a7f4d34702ac.png)
启动效果如下图：
![33f954ff3cc0627502edb531653051ba.png](_resources/33f954ff3cc0627502edb531653051ba.png)
分别为nameserver, broker, 以及监控界面。可以输入localhost:7777监控本机的rocketmq启动情况
![ebcb0d05496ac08b2411b08bd4f155b6.png](_resources/ebcb0d05496ac08b2411b08bd4f155b6.png)
###### Form启动
进入release工程的bin目录，执行jpaas-start.cmd,选择对应的数字1启动form
![1a82b72fa409fe92d0c160cca2dd847d.png](_resources/1a82b72fa409fe92d0c160cca2dd847d.png)
启动效果：
![fbe7b41e85d5b3c55ec97146ab7bee2e.png](_resources/fbe7b41e85d5b3c55ec97146ab7bee2e.png)
![f5e08cb1ace60adb34189068e71967fa.png](_resources/f5e08cb1ace60adb34189068e71967fa.png)

可以看到正常注册到nacos。
###### Bpm启动
进入release工程的bin目录，执行jpaas-start.cmd,如下图：
![be03c3ac155f0b926bfc7e8d3b6458be.png](_resources/be03c3ac155f0b926bfc7e8d3b6458be.png)
启动效果如下图：
![96b920a4df8401ac89aa113c2ca2b88a.png](_resources/96b920a4df8401ac89aa113c2ca2b88a.png)
![4c77db70cbd4e0461c01fb3cc4498f7c.png](_resources/4c77db70cbd4e0461c01fb3cc4498f7c.png)
可以看到bpm正常注册到nacos
###### 各个微服务启动
进入idea的View配置，配置Services面板，如下图：
![878bf16187b62c9bc62fd3630f03cb8c.png](_resources/878bf16187b62c9bc62fd3630f03cb8c.png)
依次启动各个微服务即可。

##### jpaas-front前端启动
###### 代码获取
http://dev.redxun.cn:18080/jpaas_application/jpaas-front.git

###### 启动nginx
进入前端项目的releae工程目录下，执行start.cmd启动nginx
![adf47d12dbb9312ca9ef01a6a7084602.png](_resources/adf47d12dbb9312ca9ef01a6a7084602.png)
这里会有2个选项，1为本地开发环境，2为生产环境，本地开发环境需要启动相关工程，生产环境则启动打包后的文件。
启动效果如下：
![0929e64a88c460aae98e16ea5260f8c2.png](_resources/0929e64a88c460aae98e16ea5260f8c2.png)
浏览器输入：http://localhost:8000,显示如下页面，则nginx启动成功
![157bb6cf3ccecc828a1441edbb5f848f.png](_resources/157bb6cf3ccecc828a1441edbb5f848f.png)
Nginx的配置文件在如下目录，需要修改的可以自行修改，完成后可以执行reload.cmd命令，重启nginx
![bca39599d983ea04b2a0c86719745902.png](_resources/bca39599d983ea04b2a0c86719745902.png)

###### jpaas-admin-front启动
进入jpaas-admin-front工程目录下，
执行npm install –registry=http://nexus.redxun.cn:18081/repository/npm-redxun-group安装
完成之后执行 npm run serve启动主应用工程。
启动效果
![d9f4d048b4dd4d5162cd8460bc1fee15.png](_resources/d9f4d048b4dd4d5162cd8460bc1fee15.png)
访问localhost:8000/jpaas,出现如下界面代表启动成功
![8094d682f7e0145b8fec41bcc87ce592.png](_resources/8094d682f7e0145b8fec41bcc87ce592.png)

#### Linux
待补充
#### MacOS
待补充

### 测试环境
#### Jenkins可持续集成
平台默认编写了jenkins自动化测试环境脚本，可以修改使用
1.首先安装带有ocean插件的jenkins，如下图：
![6e73b3d382b83df0bc7b7f8f1f2ef007.png](_resources/6e73b3d382b83df0bc7b7f8f1f2ef007.png)
2.增加服务器登录用户名，密码配置，如下图
![168a44800a7928240bcd7675b595d9a2.png](_resources/168a44800a7928240bcd7675b595d9a2.png)
3.修改系统的Jenkinsfile文件，使用对应的配置ID，如下图：
![04dc5377dfc8a19806c7d153f9e534bb.png](_resources/04dc5377dfc8a19806c7d153f9e534bb.png)
4.进入jenkins系统，创建流水线：如下图：
![f5f0cb179dfeb5b42a38ae3843b99958.png](_resources/f5f0cb179dfeb5b42a38ae3843b99958.png)
输入用户名，密码，或者沿用创建过的，
![2972b3396214c24fe542df21daab96a8.png](_resources/2972b3396214c24fe542df21daab96a8.png)
5.进入jenkins系统，执行扫描多分支流水线，则会将项目中的多分支扫描出来，并挨个进行配置执行。如下图：
![a2efa853faaba017e541456c070a3a83.png](_resources/a2efa853faaba017e541456c070a3a83.png)
6.然后执行流水线，则会安装Jenkinsfile定义的步骤进行自动化执行，如下图：
![5e1f2fdd9ed11fd69b7a7846837db23c.png](_resources/5e1f2fdd9ed11fd69b7a7846837db23c.png)

### 生产环境
6.6.0版本支持多种编译打包方式，如：jar包，docker离线，docker在线，windows服务，各种方式的编译打包步骤是一样的。
#### Linux
##### Jar包
1.	勾选模板release-jpaas-separate-linux,如下图所示：
      ![bac4be4b0bc41e4a41ff01b28be60dbf.png](_resources/bac4be4b0bc41e4a41ff01b28be60dbf.png)
      2.执行编译打包双击jpaas-cloud项目的install步骤，如下图所示：
      ![94b4871692367448c344648b3f6bc0b6.png](_resources/94b4871692367448c344648b3f6bc0b6.png)
      3.上述操作执行完成后，在release的target目录下会生成3个格式的文件，分别为zip格式，tar.gz格式，以及原始文件目录，如下图：
      ![68d03ac842cbcdd98b9646d0b7ca9a31.png](_resources/68d03ac842cbcdd98b9646d0b7ca9a31.png)
      4.可以将该压缩包直接复制到对应的生成服务器上。然后解压即可。
      5.进入解压目录，可以看到如下图所示文件，执行chmod a+x *.sh,给这些脚本相应的执行权限。
      ![3f997e7f64bfb57c4e4a40c2c93e5891.png](_resources/3f997e7f64bfb57c4e4a40c2c93e5891.png)

./nacos-start.sh  启动nacos
./seata-start.sh  启动seata
./redis-all.sh start  启动redis
./jpaas-all.sh start 启动jpaas各个微服务

#### Windows
##### 服务
1.勾选模板release-jpaas-separate-windows,如下图所示：
![b2b87262294582c2e43543b1a275eec2.png](_resources/b2b87262294582c2e43543b1a275eec2.png)
2.执行编译打包双击jpaas-cloud项目的install步骤，如下图所示：
![e0e4f2b4e2d4cafa2a1075b2df64f0db.png](_resources/e0e4f2b4e2d4cafa2a1075b2df64f0db.png)
3.上述操作执行完成后，在release的target目录下会生成2个格式的文件，分别为zip格式，原始文件目录，如下图：
![df83cb01b3aece508d361a8e91f44331.png](_resources/df83cb01b3aece508d361a8e91f44331.png)
4.可以将该压缩包直接复制到对应的windows产服务器上。解压目录
5.进入解压目录，可以看到如下图所示文件。
![9124bd34aafb02c7186c50167593fab6.png](_resources/9124bd34aafb02c7186c50167593fab6.png)
6.进入目录依次启动nacos，seata，rocketmq等服务
./nacos-start.cmd  启动nacos
./seata-start.bat  启动seata
./redis-start.cmd 启动redis
./rocketmq-start.cmd 启动rocketmq服务
7.进入winsw目录，执行如下安装windows服务操作，如下图：
![bf632c7147e6d59f57401d8bb9fb5eb5.png](_resources/bf632c7147e6d59f57401d8bb9fb5eb5.png)
此时可以看到任务管理器里面已经有安装的服务，如下图：
![08c0cc927a18b9c1bac9ed174f14232c.png](_resources/08c0cc927a18b9c1bac9ed174f14232c.png)
右键启动该服务即可，其他的服务比如：jpaas-system,jpaas-user, jpaas-portal, jpaas-ureport,jpaas-bpm,jpaas-form都可以按照该方式进行。可以在当前目录下查看相关日志信息，也可以设置开机自启动等操作，如下图所示：
![8178c9f69f3038f5c42ac5a1ea4baa42.png](_resources/8178c9f69f3038f5c42ac5a1ea4baa42.png)

#### Docker-compose
##### 离线
1.勾选模板release-jpaas-docker-offline,如下图所示：
![d3f4737a3b334b9ce0f4db557f13687a.png](_resources/d3f4737a3b334b9ce0f4db557f13687a.png)
2.执行编译打包双击jpaas-cloud项目的install步骤，如下图所示：
![d1948ef573f87c758820ee7074836769.png](_resources/d1948ef573f87c758820ee7074836769.png)
3.上述操作执行完成后，在release的target目录下会生成3个格式的文件，分别为zip格式，tar.gz格式，以及原始文件目录，如下图：
![18cb3784252ca8a5a976cafdd74b9957.png](_resources/18cb3784252ca8a5a976cafdd74b9957.png)
4.可以将该压缩包直接复制到对应的生成服务器上。然后解压即可。
5.进入解压目录，可以看到如下图所示文件，。
![2770a9cad0c7fac73fd5c491f23f366f.png](_resources/2770a9cad0c7fac73fd5c491f23f366f.png)
6.执行chmod a+x *.sh,给这些脚本相应的执行权限.
7. 执行./build.sh,检查本机docker环境，如果没有按照，则build.sh脚本文件会自动安装docker以及docker-compose。
   8.启动数据库
#docker-compose up -d mysql
查看日志
#docker-compose logs -f mysql
验证：使用客户端工具测试连接即可。
端口默认3306
账号/密码：root/root
提示：优先保证数据库正常启动，如果有脚本问题，请执行下图中对应目录的mysql脚本。
![b66d790112121411dc9b131bfcda3551.png](_resources/b66d790112121411dc9b131bfcda3551.png)

###### Redis启动
#docker-compose up -d redis
备注：redis密码为:redxun
###### Nacos启动
#docker-compose up -d nacos
验证：ip:8848/nacos访问
账号/密码：nacos/nacos
默认命名空间为local
###### Seata启动
#docker-compose up -d seata
验证：访问nacos后在服务列表可查看seata是否成功注册。
###### 应用批量启动
批量启动其他应用
#docker-compose up -d
###### 应用单独启动
单独启动应用
#docker-compose up -d gateway
#docker-compose up -d auth
#docker-compose up -d user
#docker-compose up -d system
#docker-compose up -d bpm
#docker-compose up -d form
#docker-compose up -d portal
#docker-compose up -d job
#docker-compose up -d ureport
###### 访问平台
服务器IP:8000/jpaas
账号/密码：admin/admin
##### 在线
1.勾选模板release-jpaas-docker-online,如下图所示：
![6355555631f5a30680305d7618db206f.png](_resources/6355555631f5a30680305d7618db206f.png)
2.执行编译打包双击jpaas-cloud项目的install步骤，如下图所示：
![fb7f667634d2ce1ce68c59c468023626.png](_resources/fb7f667634d2ce1ce68c59c468023626.png)
3.上述操作执行完成后，在release的target目录下会生成3个格式的文件，分别为zip格式，tar.gz格式，以及原始文件目录，如下图：
![deed1d9aacf848ba7106eda33ac3d48c.png](_resources/deed1d9aacf848ba7106eda33ac3d48c.png)
4.可以将该压缩包直接复制到对应的生成服务器上。然后解压即可。
5.进入解压目录，可以看到如下图所示文件，。
![3c31897bad68d343b2ea2fea82c6ce52.png](_resources/3c31897bad68d343b2ea2fea82c6ce52.png)
6.执行chmod a+x *.sh,给这些脚本相应的执行权限.
7. 执行./build.sh,检查本机docker环境，如果没有按照，则build.sh脚本文件会自动按照docker以及docker-compose。
   8.启动数据库
#docker-compose up -d mysql
查看日志
#docker-compose logs -f mysql
验证：使用客户端工具测试连接即可。
端口默认3306
账号/密码：root/root
提示：优先保证数据库正常启动，如果有脚本问题，请执行下图中对应目录的mysql脚本。
![2438c43d5685a522b6b2597393a1f5c5.png](_resources/2438c43d5685a522b6b2597393a1f5c5.png)

###### Redis启动
#docker-compose up -d redis
备注：redis密码为:redxun
###### Nacos启动
#docker-compose up -d nacos
验证：ip:8848/nacos访问
账号/密码：nacos/nacos
默认命名空间为local
###### Seata启动
#docker-compose up -d seata
验证：访问nacos后在服务列表可查看seata是否成功注册。
###### 应用批量启动
批量启动其他应用
#docker-compose up -d
###### 应用单独启动
单独启动应用
#docker-compose up -d gateway
#docker-compose up -d auth
#docker-compose up -d user
#docker-compose up -d system
#docker-compose up -d bpm
#docker-compose up -d form
#docker-compose up -d portal
#docker-compose up -d job
#docker-compose up -d ureport
###### 访问平台
服务器IP:8000/jpaas
账号/密码：admin/admin
