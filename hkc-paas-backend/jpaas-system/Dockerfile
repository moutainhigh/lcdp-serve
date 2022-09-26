FROM dev.redxun.cn:20080/library/jdk:jdk8u292-b10_openj9-0.26.0-slim

# RUN命令执行创建用户和用户组（命令创建了一个用户newuser设定ID为5000，并指定了用户登录后使用的主目录和shell）
RUN groupadd --gid 5000 jpaas \
  && useradd --home-dir /home/jpaas --create-home --uid 5000 --gid 5000 --shell /bin/bash --skel /dev/null jpaas

RUN   ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone

USER jpaas

#工作目录
WORKDIR /home/jpaas

#暴露端口
EXPOSE 7009

#导入编译后的jar包
ADD --chown=jpaas:jpaas target/jpaas-system.jar /home/jpaas/jpaas-system.jar


