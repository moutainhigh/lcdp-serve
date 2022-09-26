jpaas-windows-separate-enc体验包运行手册
###  安装加密授权码工具

#### 下载客户端工具
地址：https://lm.virbox.com/tools.html
![b519f07f655a1ca79aa837423272bfa3.png](_resources/b519f07f655a1ca79aa837423272bfa3.png)

#### 添加授权码
这个授权码可以向我司获取。
![7427a2b4c998b847166a7fc0c1ad8bbf.png](_resources/7427a2b4c998b847166a7fc0c1ad8bbf.png)

#### 执行数据库脚本
数据库配置忽略大小写，切记！！！
lower_case_table_names = 1
按照顺序执行数据库脚本
![c0e20136c9ec8ec53f7b3a4bb0c2c4e4.png](_resources/c0e20136c9ec8ec53f7b3a4bb0c2c4e4.png)

#### 启动redis
![637a820ed2253cf43a6f65c803328136.png](_resources/637a820ed2253cf43a6f65c803328136.png)
选择1或者2，这里选择1，单次启动redis，默认密码是redis
![c52712382f3b067492fbd7a0566624b7.png](_resources/c52712382f3b067492fbd7a0566624b7-1.png)

#### 启动nacos
点击nacos-start.cmd启动nacos
![5add0982e79d00c2fd4d8b7ba45fc229.png](_resources/5add0982e79d00c2fd4d8b7ba45fc229.png)
创建默认命名空间local
![7634334c6d4c01756ebf17d61314e81e.png](_resources/7634334c6d4c01756ebf17d61314e81e.png)
导入nacos配置
![aa9030ee0ea450e7e99e7dfeb5892e28.png](_resources/aa9030ee0ea450e7e99e7dfeb5892e28.png)
选择配置文件
![1f8bace0519fcae25a30e9096fbbd1cb.png](_resources/1f8bace0519fcae25a30e9096fbbd1cb.png)
查看是否需要修改配置，比如数据库连接信息，redis配置信息
nacos-config-dev.properties
j2cache-dev.properties

#### 启动seata
运行下述文件：
![21d838d7dffb5b22e8323e090c4b3cb5.png](_resources/21d838d7dffb5b22e8323e090c4b3cb5.png)，
检查seata是否注册到nacos
![65b25ca58b124e4c23d6879effd9e20b.png](_resources/65b25ca58b124e4c23d6879effd9e20b.png)

#### 启动jpaas微服务
![5136951606516ebda552a56434dd644f.png](_resources/5136951606516ebda552a56434dd644f.png)
![3bb609d338eab7bd9901be50a6b51366.png](_resources/3bb609d338eab7bd9901be50a6b51366.png)
选择单个启动某个服务，或者启动所有服务，最后检查nacos是否服务都注册成功
![3606d0b935178f942d69b6bcb1797dc0.png](_resources/3606d0b935178f942d69b6bcb1797dc0.png)

#### 前端运行
进入下图目录，执行start.cmd, 在浏览器种输入http://localhost:8000/jpaas,检查系统是否可正常登录
![85a3cc0e281f2627274d152201bb2c5d.png](_resources/85a3cc0e281f2627274d152201bb2c5d.png)
