## <center>web管理系统</center>

***
###1.基本信息
		前端语言：html + css + angularjs + bootstrap
		后端语言：java + spring + springmvc
		数据库：sqlserver 2012
		应用服务器：tomcat 7.0
		JDK版本：JDK 1.8
###2.	数据库配置
	a. 数据库账户：sa
	b. 数据库密码:  Test123@
	c. 数据库名称:  myoa
	d. 数据库部署：打开sqlserver2012,点击文件->打开->文件->再找到压缩包的myoa.sql，点击执行，生成数据库信息。
####数据库中的路径设置
	```CONTAINMENT = NONE
		 ON  PRIMARY 
		( NAME = N'myoa', FILENAME = N'F:\sqlspace\MSSQL11.MSSQLSERVER\MSSQL\DATA\myoa.mdf' , SIZE = 5120KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
	 LOG ON 
		( NAME = N'myoa_log', FILENAME = N'F:\sqlspace\MSSQL11.MSSQLSERVER\MSSQL\DATA\myoa_log.ldf' , SIZE = 2048KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
		GO
	```
    可以修改filename的路径
###3.浏览器
	Chrome:良好运行，强烈推荐使用。
	FireFox:良好运行，推荐使用。
	IE：ie9,ie10,ie11支持。
	Ie9:少量css样式不兼容。
	Ie10:良好运行。
	Ie11：良好运行。
	如若ie存在数据不同步的问题，请查看后面的浏览器设置
	

###4.ajax通信
	a)	100:一切正常
	b)	2XX：客户端异常
		1)	201：ID主键重复
		2)	202：查询或更新的ID不存在:
		3)	203：参数错误（参数未输入，参数名称错误，输入不能为汉字，输入不能为空等）
	c)	3XX:服务端异常
		1)	301：响应超时
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
	"status" :100
	}
	2.	错误信息：
	{
		"message ": "具体的错误信息",
	"status" : 2XX/3XX/4XX

	}

###5.项目结构
		-tdata
			-javaresource    java资源包
				-src
					-com.web.common   公用类包
						BaseController.java   基本的控制器 
						CacheUtils.java    缓存工具类
						CookieUtils.java    Cookie工具类
						CustomeDateDetailSerializer.java     日期格式化工具
						DateUtils.java    日期工具类
						EncodeAndDecode.java   数据加密解密类
						Encodes.java    编码解码工具类
						Exceptions.java   异常工具类 
						Global.java    全局配置类
						Md5Util.java    MD5加密工具
						PropertiesLoader.java   Properties文件载入工具类.
						Reflections.java    反射工具类
						SpringContextHolder.java    bean获取工具类
						StringUtils.java    字符串工具类
						UserUtils.java    用户工具类
						ValidateCodeServlet.java    验证码servlet
					-com.web.controller   控制管理类包
						LoginController.java    登陆控制器
						PermissionController.java   权限控制器
						PlayerController.java    玩家控制器
						RechargeController.java    充值控制器
						RoleController.java    角色控制器
						SaleRecordController.java   充值记录控制器
						UserController   用户控制器
					-com.web.dao    dao类包
						BaseDao.java    基本的dao类
						PermissionDao.java    权限dao类
						PlayerDao.java    玩家dao类
						RoleDao.java    角色dao类
						SaleRecordDao.java    充值记录dao类
						UserDao.java    用户dao类
					-com.web.entity  实体类包
						BaseEntity.java    基本的实体类
						DataEntity.java    数据实体类
						IdEntity.java    ID实体类
						IdGen.java    ID生成策略实体类
						Permission.java   权限实体类
						Player.java   玩家实体类
						Role.java    角色实体类 
						SaleRecord.java    充值记录实体类
						User.java    用户实体类
					-com.web.mapper    json转换包
						JsonMapper.java    json封装工具类
					-com.web.persistence    持久类包
						Page.java    分页实体类
					-com.web.security    安全管理类包
						CaptchaException.java    验证码异常处理类
						CustomCredentialsMatcher.java    自定义的密码验证类
						DES.java   DES加密解密工具类
						FormAuthorizingRealm.java    登录表单验证类
						SystemAuthorizingRealm.java    系统安全认证实现类
						UsernamePasswordToken.java    用户和密码（包含验证码）令牌类
					-com.web.service    service包
						SystemService.java    系统service类
					-com.web.test    测试包
						test.java    测试类
					springmvc-servlet.xml    springmvc配置类
					ehcache-hibernate-local.xml    指定的hibernateCache缓存配置
					ehcache-local.xml    默认的缓存配置
					log4j.properties    日志配置类
					spring-context-shiro.xml    shiro安全管理配置类
					spring-context.xml    spring配置管理类
				-source  资源配置
					config.propertiese    配置
					mysql.propertiese    数据库配置
					
		-webcontent
			-assets 静态资源管理类
				-css
					angular-aside.css
					animate.min.css
					app.css
					font.css
				-js
					-controllers    angularjs的控制器
						-app.js    定义的app模块
						-modalCtrl.js    模态框控制器
						-proxyCtrl.js    代理用户控制器
						-rechargeCtrl.js    充值控制器
						-saleRecordCtrl.js    充值记录控制器
					-directive    指令控制
						ui-fullscreen.js    全屏指令
						ui-scroll.js     滚动指令
						ui-toggleclass.js    toggle指令
					-plugins    插件管理
						-----省略-----
					-services    服务管理
						ui-loda.js   加载资源文件工具
					-config.js    资源配置
					-lazyload.js    懒加载配置
					-main.js     主要信息配置
					-router.js   路由信息配置
				
			-html    静态文件
				-blocks  布局资源
					header.html    头部页面
					nav.html    导航页面
					settings.html   布局颜色设置页面
				-proxy  代理用户资源
					proxy.html   代理用户资源
				-sale    充值以及充值记录页面
				    recharge.html    充值页面
				    saleRecord.html    充值记录页面
				 -app.html    主配置布局页面
				 -errot.html   错误页面
				 -index.html   入口页
				 -login.html    html登陆页面
				 -syslogin.jsp  jsp登陆页面
				 -test.html    测试页面
			-META-INF
			-WEB-INF
				-lib   jar包
				web.xml    web配置 
			-readme.md    必读使用信息
### 6.使用详情
***
#### a.增加权限信息（com.web.controller.PermissionController.java类中，可以自定义权限的名称）
	/**
	 * 增加权限信息
	 * 权限分为四种：
	 * 1.增加 save
	 * 2.删除 delete
	 * 3.更新 update
	 * 4.查找 select 
	 */
	@RequestMapping("save")
	public String save(){
		ArrayList<String> list = new ArrayList<String>();
		//往list中添加权限的名称
		list.add("save");
		list.add("delete");
		list.add("update");
		list.add("select");
		for(int i = 0; i < list.size(); i++){
			DES des = new DES();
			Permission permission = new Permission();
			permission.setPermissionName(des.base64encoder.encode(des.encrypt((list.get(i)).getBytes(), des.PASSWORD)));
			//存储到数据库
			systemService.save(permission);
		}
		return "index.html";
	}
####b.用户角色权限 （com.web.controller.RoleController.java)
	
		/**
		 *赋予system角色所有权限 
		 */
	Role role1 = new Role();
		role1.setRoleName( des.base64encoder.encode(des.encrypt(("system").getBytes(), des.PASSWORD)));
		//查找系统角色的权限
		List<Permission> list1 = systemService.findAllList();
		role1.setRolePermission(list1);
		systemService.save(role1);
****
		/**
		 * 赋予admin角色所有权限
		 */
		//查找管理员角色权限
		Role role2 = new Role();
		role2.setRoleName( des.base64encoder.encode(des.encrypt(("admin").getBytes(), des.PASSWORD)));
		List<Permission> list2 = systemService.findAllList();
		role2.setRolePermission(list2);
		systemService.save(role2);
***
		/**
		 * 赋予代理用户select权限
		 */
		//查找代理角色权限
		Role role3 = new Role();
		role3.setRoleName( des.base64encoder.encode(des.encrypt(("proxy").getBytes(), des.PASSWORD)));
		List<Permission> list3 = new ArrayList<Permission>();
		for(int i = 0; i < list2.size(); i++){//查找所有权限中等于select的，赋予给代理角色
			if("select".equals(new String(des.decrypt(des.base64decoder.decodeBuffer(list2.get(i).getPermissionName()),des.PASSWORD)))){
				list3.add(list2.get(i));
				role3.setRolePermission(list3);
				break;
			}
		}
		systemService.save(role3);

####c.用户角色权限 的使用
		/**
	*package com.web.security.SystemAuthorizingRealm.java;
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 *当有@RequiresPermissions时才调用，是从当前用户对应的角色的权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("授权查询回调函数-------------");
		Principal principal = (Principal) getAvailablePrincipal(principals);
		User user = getSystemService().getUserByLoginId(principal.getLoginId());
		if (user != null) {
			UserUtils.putCache("user", user);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			/**
			 *  添加基于Permission的权限信息，先查找用户角色，再查找角色拥有的权限
			 */
			DES des = new DES();
			List<String> list = new ArrayList<String>();
			List<Permission> permissions = user.getRole().getRolePermission();
			for(Permission p:permissions){
				String s = null;
				try {
					s = new String(des.decrypt(des.base64decoder.decodeBuffer(p.getPermissionName()),des.PASSWORD));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				list.add(s);
			}
			info.addStringPermissions(list);
			// 更新登录IP和时间
			//getSystemService().updateUserLoginInfo(user.getId());
			return info;
		} else {
			return null;
		}
	}
	
***
	使用权限信息例子
	/**
	 * @return Map 成功或者错误的json消息
	 */
	@RequiresPermissions(value={"update","save"})//规定的角色权限才能访问
	@RequestMapping("save")
	@ResponseBody
	public Map update(SaleRecord sr){
	}
			
	

	