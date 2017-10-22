# Maven
## Maven概述
Maven 是apache 下的开源项目，项目管理工具，管理java 项目。

1. 项目对象模型(Project Object Model)
POM 对象模型，每个maven 工程中都有一个pom.xml 文件，定义工程所依赖的jar 包、本工程的坐标、打包运行方式。

2. 依赖管理系统（基础核心）
maven 通过坐标对项目工程所依赖的jar 包统一规范管理。

3. maven 定义一套项目生命周期
清理、初始化、编译、测试、报告、打包、部署、站点生成

4. 一组标准集合
强调：maven 工程有自己标准的工程目录结构、定义坐标有标准。

5. maven 管理项目生命周期过程都是基于插件完成的

>以上五点，每一点是出于什么目的设计的？

###Maven仓库

1. **中央仓库**
就是远程仓库，仓库中jar 由专业团队（maven 团队）统一维护。
中央仓库的地址：http://repo1.maven.org/maven2/
2. **本地仓库**
相当于缓存，工程第一次会从远程仓库（互联网）去下载jar 包，将jar 保存
在本地仓库（在程序员的电脑上）。第二次不需要从远程仓库去下载。先从本地
仓库找，如果找不到才会去远程仓库找。
3. **私服**
在公司内部架设一台私服，其它公司架设一台仓库，对外公开。

>本地仓库是会把所有jar包都缓存下来吗？还是缓存需要的？如果是缓存需要的，那么又是根据什么判断的呢？

##环境搭建
可以到maven 的官网下载
http://maven.apache.org/download.cgi

1. 下载
2. 本地仓库配置
	1. 拷贝本地仓库
	2. 配置本地仓库
		1. 打开maven 的安装目录中conf/ settings.xml 文件，在这里配置本地仓库：
		
		<localRepository>D:\repository_ssh</localRepository>
		
		这里的意思是配置本地仓库的目录为D:\repository_ssh
3. eclipse 配置Maven
4. 重建本地仓库索引---如果不进行这一步创建索引，那么无法在eclipse的showview下的local repository中看到jar包

工程目录结构说明：

project

/src/main/java 主体程序java 源文件（不要放配置文件）

/src/main/resources 主体程序所需要的配置文件（不要放java 文件）

/src/test/java 单元测试程序java 源文件

/src/test/resources 单元测试程序所用的配置文件

/target 编译输出目录

pom.xml Maven 进行工作的主要配置文件。

###依赖管理
