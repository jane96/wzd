var rootUserId2;

app.controller('proxyCtrl',["$http","$scope","toaster",function($http,$scope,toaster){
	//页面参数初始化,参数要与后台一致
	var pageNo = 1;//第一页
	var pageSize = 10;//每页显示的数据
	var orderBy ="desc-date";//排序规则如："desc-name","asc-name";前面是排序递增或递减，后面是排序的对象;默认为时间递减
	var pageCount =0//总的页面初始化
	$scope.pageCount = 0;
	var pageMin = 1;
	var pageMax = pageCount;
	var pageCur = 1;
	var searchDate = "";//查询的时间段
	var searchId = "";//查询的Id
	
	//初始化页面
	var path = "/tdata/proxy/find?pageNo=" + pageNo + "&&pageSize=" + pageSize + "&&orderBy=" + orderBy;
	$http.get(path).then(function(response){
		$scope.proxys = response.data.list;
		pageCount=response.data.pageCount;
		pageMax = pageCount;
		$scope.pageCount = pageCount;
	})
	
	//分页初始化
	var pageArr = [];
	var count = 10;//显示多少页
	$scope.count = count;
	for(var i = 1; i <= count; i++){
		pageArr.push(i);
	}
	$scope.pages = pageArr;
	var curMin = 1;//当前最小页码值
	var curMax = count;//当前最大页码值
	$scope.curMin = curMin;
	$scope.curMax = curMax;
	$scope.pageCur = pageCur;//当前页码
	
	//---------click page start---------
	$scope.clickPage = function(n){
		if(pageCount > count){//总页数大于count数
			if(n==pageCur){//当前页码点击不响应
				return;
			}else if(n==-1 && pageCur != 1){//首页
				pageCur = 1;
				pageArr.splice(0,pageArr.length);//清空数组
				for(var i = 1; i <= count; i++){
					pageArr.push(i);
				}
			}else if(n == -2 && pageCur > pageMin){//前一页
				pageCur = pageCur - 1;
				if(pageCur < pageArr[0]){//如果小于数组最小页则重新定义数组
					for(var i = 0; i < count; i++){
						pageArr.push(pageArr[i] - 1);
					}
					pageArr.splice(0,count);//清空数组
				}
			}else if(n==-3 && pageCur < pageMax ){//后一页
				pageCur = pageCur + 1;
				if(pageCur > pageArr[pageArr.length - 1]){//如果大于数组最大页则重新定义数组
					for(var i = 0; i < count; i++){
						pageArr.push(pageArr[i] + 1);
					}
					pageArr.splice(0,count);//清空数组
				}
			}else if(n == -4 && pageCur != pageCount){//末页
				pageArr.splice(0,pageArr.length);//清空数组
				for(var i =pageCount - count + 1; i <= pageCount; i++){
					pageArr.push(i);
				}
				pageCur = pageCount;
			}else if(n > 0 ){
				pageCur = n;
			}else{//本页
				return;
			}
		}
		else{//总页数小于count数量
			if(n==pageCur){//点击当前页
				return;
			}
			else if(n == -1 && pageCur != 1){//首页
				pageCur = 1;
			}else if(n == -2 && pageCur > 1){//前一页
				pageCur = pageCur - 1
			}else if(n == -3 && pageCur < pageMax){//后一页
				pageCur = pageCur + 1;
			}else if(n == -4 && pageCur != pageCount){//末页
				pageCur = pageCount;
			}else if(n > 0){//点击的页码
				pageCur = n;
			}else{//本页
				return;
			}
		}
	
		//返回当前页码数以及页码数组
		$scope.pages = pageArr;
		$scope.pageCur = pageCur;
		
		//返回当前的最大、最小页码
		curMin = pageArr[0];
		curMax = pageArr[pageArr.length - 1];
		$scope.curMin = curMin;
		$scope.curMax = curMax;
		loadPage(pageCur,pageSize,orderBy);
	}
	//---------end click page---------
	
	//生成代理账号
	$scope.createProxy = function(){
		var path = "/tdata/proxy/save?oderBy=" + orderBy;
		$http.get(path).then(function(response){
			$scope.proxys = response.data.list;//获取代理账户列表
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
			//父类向子类发送请求
			$scope.$broadcast("createProxy", "child");
		})
	}
	
	//载入页面数据函数
	function loadPage(no,size,order){
		var path = "/tdata/proxy/find?pageNo="+ no + "&&pageSize=" + size+"&&orderBy=" + order
		$http.get(path).then(function(response){
			$scope.proxys = response.data.list;
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
		});
	}
	
	//表单验证
	$('#modalForm3').bootstrapValidator({
//      live: 'disabled',
      message: 'This value is not valid',
      feedbackIcons: {
          valid: 'glyphicon glyphicon-ok',
          invalid: 'glyphicon glyphicon-remove',
          validating: 'glyphicon glyphicon-refresh'
      },
      fields: {
    	  proxyWechat: {
              message: '微信不合法',
              validators: {
                  stringLength: {
                      min: 0,
                      max: 16,
                      message: '微信为0-16位'
                  },
                  regexp: {
                      regexp: /^[a-zA-Z0-9_\.]+$/,
                      message: '微信只能输入数字和字母'
                  }
              }
          },
          proxyPhone: {
              message: '手机不合法',
              validators: {
                  stringLength: {
                      min: 11,
                      max: 11,
                      message: '手机号为11位'
                  },
                  regexp: {
                      regexp: /^[0-9_\.]+$/,
                      message: '手机号只能为数字'
                  }
              }
          },
          proxyPassword: {
              message: '密码不合法',
              validators: {
                  stringLength: {
                      min: 7,
                      max: 16,
                      message: '密码为7-16位'
                  },
                  regexp: {
                      regexp: /^[a-zA-Z0-9_\.]+$/,
                      message: '密码只能为数字和字母'
                  }
              }
          }
      	
      }
	})
	
	//修改信息
	$scope.changeInfo = function(id,password,phone,weChat){
		$scope.proxyId = id;
		$scope.proxyWechat = weChat;
		$scope.proxyPhone = phone;
		$scope.proxyPassword = password;
	}
	 
	//提交修改
	$scope.modalSubmit = function(){
		
		var path = "/tdata/checkLogout";
		$http.get(path).then(function(response){
			if(response.data.status == undefined){//如果退出了就结束操作
				toaster.pop({type:"warning",title:"充值信息提示",body:"登录失效！请重新登录",timeout:2000});
				return ;
			}
		})
		var weChat = $scope.proxyWechat;
		var phone = $scope.proxyPhone;
		var password = $scope.proxyPassword;
		//密码验证
		var chReg = new RegExp("[\\u4E00-\\u9FFF]+","g");
		if(chReg.test(password)){//是否包含汉字
			toaster.pop({type:"warning",title:"修改密码提示",body:"密码不能为汉字！",timeout:2000}); 
			return;
		}
		var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\￥)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\——)(\)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\；)(\:)(\：)(\')(\‘)(\’)(\`)(\·)(\、)(\")(\,)(\.)(\/)(\<)(\《)(\》)(\>)(\?)(\)(\（)(\）)(\【)(\】)]+/);      
		if(containSpecial.test(password)){//是否包含特殊符号
			toaster.pop({type:"warning",title:"修改密码提示",body:"密码不能为特殊符号！",timeout:2000}); 
			return;
		}
		
		if(password == "" || password == undefined || /^[\u4e00-\u9fa5]+$/.test(password) || password.length < 7){
			toaster.pop({type:"warning",title:"修改密码提示",body:"密码输入不合法！",timeout:2000});
			return;
		}
		
		if(weChat == undefined ){
			weChat = "";
		}
		if(phone == undefined){
			phone = "";
		}
		
		weChat = encodeURI(encodeURI( weChat)); //ie浏览器进行UTF-8编码
		
		var path = "/tdata/proxy/update?id=" + $scope.proxyId + "&&proxyWeChat=" + weChat + "&&proxyPhone=" + phone +"&&password=" + $scope.proxyPassword; 
		$http.post(path).then(function(response){
			if(response.data.status == 1){
				toaster.pop({type:"success",title:"更新代理商信息提示",body:response.data.message,timeout:2000});
				//重新获取数据
				var path = "/tdata/proxy/find?pageNo=" + $scope.pageCur + "&&pageSize=" + pageSize + "&&orderBy=" + orderBy;
				$http.post(path).then(function(response){
					$scope.proxys = response.data.list;
					pageCount=response.data.pageCount;
					pageMax = pageCount;
					$scope.pageCount = pageCount;
					//父类向子类发送请求
					$scope.$broadcast("parentChange", $scope.pageCur);//发送当前页码给子控制器
				})
			}else{
				toaster.pop({type:"error",title:"更新代理商信息提示",body:response.data.message,timeout:2000});
			}
			
		});
	}
}]);


app.controller('proxyListCtrl',['$http','$scope','toaster',function($http,$scope,toaster){
	//页面参数初始化,参数要与后台一致
	var pageNo = 1;//第一页
	var pageSize = 10;//每页显示的数据
	var orderBy ="desc-date";//排序规则如："desc-name","asc-name";前面是排序递增或递减，后面是排序的对象;默认为时间递减
	var pageCount =0//总的页面初始化
	$scope.pageCount = 0;
	var pageMin = 1;
	var pageMax = pageCount;
	var pageCur = 1;
	var searchDate = "";//查询的时间段
	var searchId = "";//查询的Id
	
	//初始化页面
	var path = "/tdata/proxy/find?pageNo=" + pageNo + "&&pageSize=" + pageSize + "&&orderBy=" + orderBy;
	$http.get(path).then(function(response){
		$scope.proxyList = response.data.list;
		pageCount=response.data.pageCount;
		pageMax = pageCount;
		$scope.pageCount = pageCount;
	})
	
	//分页初始化
	var pageArr = [];
	var count = 10;//显示多少页
	$scope.count = count;
	for(var i = 1; i <= count; i++){
		pageArr.push(i);
	}
	$scope.pages = pageArr;
	var curMin = 1;//当前最小页码值
	var curMax = count;//当前最大页码值
	$scope.curMin = curMin;
	$scope.curMax = curMax;
	$scope.pageCur = pageCur;//当前页码
	
	//---------click page start---------
	$scope.clickPage = function(n){
		if(pageCount > count){//总页数大于count数
			if(n==pageCur){//当前页码点击不响应
				return;
			}else if(n==-1 && pageCur != 1){//首页
				pageCur = 1;
				pageArr.splice(0,pageArr.length);//清空数组
				for(var i = 1; i <= count; i++){
					pageArr.push(i);
				}
			}else if(n == -2 && pageCur > pageMin){//前一页
				pageCur = pageCur - 1;
				if(pageCur < pageArr[0]){//如果小于数组最小页则重新定义数组
					for(var i = 0; i < count; i++){
						pageArr.push(pageArr[i] - 1);
					}
					pageArr.splice(0,count);//清空数组
				}
			}else if(n==-3 && pageCur < pageMax ){//后一页
				pageCur = pageCur + 1;
				if(pageCur > pageArr[pageArr.length - 1]){//如果大于数组最大页则重新定义数组
					for(var i = 0; i < count; i++){
						pageArr.push(pageArr[i] + 1);
					}
					pageArr.splice(0,count);//清空数组
				}
			}else if(n == -4 && pageCur != pageCount){//末页
				pageArr.splice(0,pageArr.length);//清空数组
				for(var i =pageCount - count + 1; i <= pageCount; i++){
					pageArr.push(i);
				}
				pageCur = pageCount;
			}else if(n > 0 ){
				pageCur = n;
			}else{//本页
				return;
			}
		}
		else{//总页数小于count数量
			if(n==pageCur){//点击当前页
				return;
			}
			else if(n == -1 && pageCur != 1){//首页
				pageCur = 1;
			}else if(n == -2 && pageCur > 1){//前一页
				pageCur = pageCur - 1
			}else if(n == -3 && pageCur < pageMax){//后一页
				pageCur = pageCur + 1;
			}else if(n == -4 && pageCur != pageCount){//末页
				pageCur = pageCount;
			}else if(n > 0){//点击的页码
				pageCur = n;
			}else{//本页
				return;
			}
		}
	
		//返回当前页码数以及页码数组
		$scope.pages = pageArr;
		$scope.pageCur = pageCur;
		
		//返回当前的最大、最小页码
		curMin = pageArr[0];
		curMax = pageArr[pageArr.length - 1];
		$scope.curMin = curMin;
		$scope.curMax = curMax;
		loadPage(pageCur,pageSize,orderBy);
	}
	//---------end click page---------
	
	//载入页面数据函数
	function loadPage(no,size,order){
		var id = $scope.searchId;
		var phone = $scope.searchPhone;
		if(phone == undefined){
			phone = "";
		}
		if(id == undefined){
			id = "";
		}
		var path = "/tdata/proxy/find?pageNo="+ no + "&&pageSize=" + size+"&&orderBy=" + order + "&&phone=" + phone+"&&loginId=" + id;
		$http.get(path).then(function(response){
			$scope.proxyList = response.data.list;
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
		});
	}
	
	//子类响应父类
	$scope.$on('parentChange',function(even,data){
		var path = "/tdata/proxy/find?pageNo=" + data + "&&pageSize=" + pageSize + "&&orderBy=" + orderBy;
		$http.get(path).then(function(response){
			$scope.pageCur = data;
			$scope.proxyList = response.data.list;
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
		});
	})
	
	//生成代理账号进行更新
	$scope.$on('createProxy',function(){
		var path1 = "/tdata/checkLogout";
		$http.get(path1).then(function(response){
			
			if(response.data.status == undefined){//如果退出了就结束操作
				flag = false;
				toaster.pop({type:"warning",title:"充值信息提示",body:"登录失效！请重新登录",timeout:2000});
				return ;
			}
		});
		$http.get(path).then(function(response){
			$scope.pageCur = 1;//返回到第1页
			$scope.proxyList = response.data.list;
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
		});
	})
	
	//搜索按钮
	$scope.search = function(){
		var id = $scope.searchId;
		var phone = $scope.searchPhone;
		
		if((id != undefined && id != "") && (phone != undefined && phone != "")){
			toaster.pop({type:"warning",title:"搜索提示信息",body:"搜索失败：只能输入一项搜索内容！",timeout:3000});
			return false;
		}
		if(phone == undefined){
			phone = "";
		}
		if(id == undefined){
			id = "";
		}
		var path = "/tdata/proxy/find?phone=" + phone + "&&loginId=" + id;
		$http.get(path).then(function(response){
			$scope.proxyList = response.data.list;
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
		});
	}
	
	//充值模态框
	$scope.modalClick2 = function(id,loginId){
		$scope.proxyId = id;//proxy的id
		$scope.proxyUser = loginId;//user的id
		
	}
	
	//提交充值
	$scope.modalSubmit3 = function(){
		var path1 = "/tdata/checkLogout";
		$http.get(path1).then(function(response){
			
			if(response.data.status == undefined){//如果退出了就结束操作
				toaster.pop({type:"warning",title:"充值信息提示",body:"登录失效！请重新登录",timeout:2000});
				return ;
			}
		});
		$('#myModal5').modal('show');//确认充值
	 }
	
	//完成充值
	$scope.submitRecharge = function(){
		var path = "/tdata/proxy/update?id=" + $scope.proxyId + "&&rechargeNumber=" + $scope.proxyNumber;
		$http.get(path).then(function(response){
			if(response.data.status == 1){
				var path = "/tdata/proxy/find?pageNo="+ $scope.pageCur + "&&pageSize=" + 10  +"&&orderBy=" + "desc-date" ;
				$http.get(path).then(function(response){
					$scope.proxyList = response.data.list;
					pageCount=response.data.pageCount;
					pageMax = pageCount;
					$scope.pageCount = pageCount;
				});
				toaster.pop({type:"success",title:"充值信息提示",body:"充值信息成功！",timeout:2000});
			}else{
				toaster.pop({type:"error",title:"充值信息提示",body:"充值信息失败:" + response.data.message,timeout:2000});
			}
		})
	}
	
	//表单验证
	$('#modalForm4').bootstrapValidator({
//      live: 'disabled',
      message: 'This value is not valid',
      feedbackIcons: {
          valid: 'glyphicon glyphicon-ok',
          invalid: 'glyphicon glyphicon-remove',
          validating: 'glyphicon glyphicon-refresh'
      },
      fields: {
    	  proxyNumber: {
              message: '充值数量不合法',
              validators: {
            	  notEmpty: {
                      message: '充值数量不能为空'
                  },
                  stringLength: {
                      min: 1,
                      max: 8,
                      message: '位数为1-8位'
                  },
                  regexp: {
                      regexp: /^[0-9]+$/,
                      message: '充值数量只能为数字'
                  }
              }
          }
      }
	});
}]);
