房卡充值
1.	环境配置
a.	java版本：JDK1.8
b.	Tomcat服务器：tomcat7.0
c.	数据库：sqlserver2012
d.	运行编码环境设置：
1.在tomcat中用startup启动：
在catalina.bat第一行下面加入：
	set JAVA_OPTS=%JAVA_OPTS% %LOGGING_CONFIG% -Dfile.encoding=UTF8
2.在tomcat中用tomcat7启动：
	 
如果cmd中出现乱码 ，设置cmd的编码为UTF-8即可。

		
2.	数据库配置
	a. 数据库账户：sa
b. 数据库密码:  Test123@
c. 数据库名称:  myoa
d. 数据库部署：打开sqlserver2012,点击文件->打开->文件->再找到压缩包的myoa.sql，点击执行，生成数据库信息。

3.	浏览器
Chrome:良好运行，强烈推荐使用。
FireFox:良好运行，推荐使用。
IE：ie9,ie10,ie11支持。
Ie9:少量css样式不兼容。
Ie10:良好运行。
Ie11：良好运行。
如若ie存在数据不同步的问题，请查看后面的浏览器设置。


4.	登陆设置
a.	目前的登陆账户名：admin
b.	目前的登陆密码：1
5.	数据的加密解密
 
找到该类，里面的示例
6.	ajax通信
a)	100:一切正常
b)	2XX：客户端异常
i.	201：ID主键重复
ii.	202：查询或更新的ID不存在:
iii.	203：参数错误（参数未输入，参数名称错误，输入不能为汉字，输入不能为空等）
c)	3XX:服务端异常
i.	301：响应超时
d)	4XX：其他错误

参数说明：pId:玩家ID；pWechat:玩家昵称；pNumber:房卡数量;dNumber:减少的房卡数量;date:注册时间；
URL说明:
localhost:本地；9090:第二个端口的端口号；tdata:项目名；
a.新增玩家信息
发送形如：http://localhost:9090/tdata/player/save?pId=12345&&pWechat=hz&&pNumber=122
返回的信息Json信息：
1.	成功信息：
	{
  "message" : "success",
  "status" : 100
}
2.	错误信息：
{
		"message ": "具体的错误信息",
		"status" : 2XX/3XX/4XX

}
	

b．更新玩家信息
发送形如：
		http://localhost:9090/tdata/player/update?pId=12345&&pWechat=hz&&pNumber=122
其中pId一定要有，如果只改pWechat就：
		http://localhost:9090/tdata/player/save?pId=12345&&pWechat=ja
如果只改pNumber就：
		http://localhost:9090/tdata/player/save?pId=12345&&pNumber=133
返回的信息Json信息：
1.	成功信息：
	{
  "message" : "success",
  "status" : 100
}
2.	错误信息：
{
		"message ": "具体的错误信息",
		"status" : 2XX/3XX/4XX

}

c.	删除玩家信息
发送形如：
	http://localhost:9090/tdata/player/delete?pId=12345
只需要pId;

返回的信息Json信息：
1.	成功信息：
	{
  "message" : "success",
  "status" :100
}
2.	错误信息：
{
		"message ": "具体的错误信息",
		"status" : 2XX/3XX/4XX

}

d.	查找玩家信息：
发送形如：http://localhost:9090/tdata/player/find?pId=12345
只需要pId
返回的信息Json信息：
1.成功信息：
{"pId":"12345","pWechat":"97776767","pNumber":"30","date":"2017-02-09"}
2.错误信息：
{
		"message ": "查询ID不存在或者其他异常！",
		"status" : 2XX/3XX/4XX

}
	

e.减少玩家放卡数量
发送形如：http://localhost:9090/tdata/player/decrease?pId=12345&&dNumber=5
返回的信息Json信息：

1.	成功信息：
	{
  "message" : "success",
  "status" :1
}
2.	错误信息：
{
		"message ": "具体的错误信息",
		"status" : 2XX/3XX/4XX

}


6.sqlserver2012的备份
1.	打开sqlserver2012，连接数据库
2.	找到管理下的维护计划
 
3.	右键维护计划向导->下一步
 
4.	点击更改，进入作业计划：更改备份的频率以及时间
 
5.	点击选择备份的类型：ps(第一次备份一定是完整备份，之后可以是差异备份)
 
6.	点击下一步，选择备份的数据库以及备份的目录
 
7.	之后一直next直到完成

8.关于IE浏览器数据不同步的问题

由于IE浏览器缓存了页面数据，不会发送请求。所以修改浏览器设置。
1.打开Internet选项，点击设置
 
检查存储的页面较新版本选择：每次访问网页时。



 
9.关于项目的部署
1.在tomcat下新建一个目录，存放第二个端口的内容：
Webapp:存放第一个端口的内容。
Webapps2:存放第二个端口的内容。
 
3.	打开conf，新建Catalina2
 
4.将项目包中的server.xml替换tomcat中的server.xml
 
	
			
		
