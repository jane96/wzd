app.controller('rechargeCtrl',["$http","$scope",'toaster',function($http,$scope,toaster){
	//获得输入焦点
	document.getElementById("inputId").focus();
	
	//表单验证
	$('#rechargeForm').bootstrapValidator({
      //      live: 'disabled',
      message: 'This value is not valid',
      feedbackIcons: {
          valid: 'glyphicon glyphicon-ok',
          invalid: 'glyphicon glyphicon-remove',
          validating: 'glyphicon glyphicon-refresh'
      },
      fields: {
          rechargeId: {
              message: '充值ID不合法',
              validators: {
                  notEmpty: {
                      message: '充值ID不能为空'
                  },
                  stringLength: {
                      min: 1,
                      max: 16,
                      message: '充值ID为1-16位'
                  },
                  regexp: {
                      regexp: /^[0-9]+$/,
                      message: '充值ID只能为数字'
                  }
              }
          },
          rechargeMark: {
        	  validators: {
        		  stringLength: {
        			  min: 0,
        			  max: 30,
        			  message: '备注最多为30个字符'
        		  }
        	  }
          },
      rechargeNumber: {
    	  message: '充值数量不合法',
    	  validators: {
    		  notEmpty: {
    			  message: '充值数量不能为空'
    		  },
    		  stringLength: {
    			  min: 1,
    			  max: 8,
    			  message: '充值数量最大值为99999999'
    		  },
    		  regexp: {
    			  regexp: /^[0-9]+$/,
    			  message: '充值数量只能为正整数'
    		  }
    	  }
      	}
      }
	})
	
	//页面参数初始化,参数要与后台一致
	var pageNo = 1;//第一页
	var pageSize = 30;//每页显示的数据
	var orderBy ="desc-playerRechargeDate";//排序规则如："desc-name","asc-name";前面是排序递增或递减，后面是排序的对象;默认为时间递减
	var pageCount ="10"//总的页面数量
	var pageMin = 1;
	var pageMax = pageCount;
	var pageCur = 1;
	var searchDate = "";//查询的时间段
	var searchId = "";//查询的Id
	
	//时间格式化
	Date.prototype.Format = function (fmt) { 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
	
	//确认充值按钮
	$('#confirmSubmit').click(function() {
		var flag = true;//标志是否退出
		var path = "/tdata/checkLogout";
		$http.get(path).then(function(response){
			
			if(response.data.status == undefined){//如果退出了就结束操作
				flag = false;
				toaster.pop({type:"warning",title:"充值信息提示",body:"登录失效！请重新登录",timeout:2000});
				return ;
			}else{
				if(isNaN($scope.playerNumber) || $scope.playerId.length >16 || $scope.playerNumber.length > 8 || ($scope.rechargeMark != undefined && $scope.rechargeMark.length > 30)){
					toaster.pop({type:"warning",title:"充值信息提示",body:"输入不合法",timeout:2000});	
					return;
				}
				if($scope.playerId==undefined || $scope.playerNumber==undefined){
					toaster.pop({type:"warning",title:"充值信息提示",body:"输入不合法",timeout:2000});
					return ;	
				}
				if($scope.playerNumber[0] == 0){
					toaster.pop({type:"warning",title:"充值信息提示",body:"输入第一位不能为0",timeout:2000});
					return;
				}
				if($scope.playerNumber[0] == "-" || $scope.playerNumber[0] == "+"){
					toaster.pop({type:"warning",title:"充值信息提示",body:"输入中不能包含符号",timeout:2000});
					return ;
				}
				if($scope.rechargeMark == undefined){
					$scope.rechargeMark = ""
				}
				if($scope.playerId != undefined && $scope.playerNumber != undefined && flag){
					$('#rechargeConfirm').modal('show');
				}
				
			}
		})
		
	});
	
	//完成充值按钮
	$scope.submit = function(){
		var s  = encodeURI(encodeURI($scope.rechargeMark)); //ie浏览器进行UTF-8编码
		//var s = $scope.rechargeMark;
		var path1 ="/tdata/recharge/save?playerId=" + $scope.playerId + "&&playerNumber=" + $scope.playerNumber +"&&playerRemark=" + s;
		$http.post(path1).then(function(response){//post默认使用utf-8
			var time = new Date().Format("yyyy-MM-dd hh:mm:ss"); 
			if(response.data.status == "true"){//充值成功
				$("#result").prepend(	
						"<div class='alert alert-success alert-dismissable text-center'><button type='button' class='close' data-dismiss='alert'aria-hidden='true'>&times;</button>" +
						"你于:"+time
						+"给 " + $scope.playerId + " 充值：" + $scope.playerNumber + "次" + ";" + "<br>"
						+response.data.message+"</div>");
			}else if(response.data.status == "false"){
					$("#result").prepend(//充值失败
						"<div class='alert alert-danger alert-dismissable text-center'><button type='button' class='close' data-dismiss='alert'aria-hidden='true'>&times;</button>" +"你于:"+time+
							"给 " + $scope.playerId + " 充值：" + $scope.playerNumber + "次" + ";" +  "<br>"
						+ "充值失败：" + response.data.message+"</div>");
					return false;
			}else{$("#result").prepend(//权限异常等未知错误
					"<div class='alert alert-danger alert-dismissable text-center'><button type='button' class='close' data-dismiss='alert'aria-hidden='true'>&times;</button>" + "ID:"+
					$scope.playerId + "；充值次数：" + $scope.playerNumber + "<br>"
					+"充值失败：权限异常或未知的错误！"+"</div>");
				return false;
			}
			
			//参数重置
			$scope.playerId = undefined;
			$scope.playerNumber = undefined;
			$scope.rechargeMark = undefined;
			
			//更新显示的房卡数量
			var timeRandom = Date.parse(new Date());
		    var path = "/tdata/findLoginUser/?random" + timeRandom;
		    $http.get(path).then(function (response) {
		    	$scope.app.userNumber = response.data.currentNumber;
		    });
		      
			//重新获取充值列表
			var path = "/tdata/saleRecord/find?pageNo="+ pageNo + "&&pageSize=" + pageSize+"&&orderBy=" + orderBy +"";
			$http.get(path).then(function(response){
				$scope.sales = response.data.list;
				pageCount=response.data.pageCount;
				pageMax = pageCount;
				$scope.pageCount = pageCount;
			});
			
		});
	}
	
	//重置按钮
	$('#resetBtn').click(function() {
        $('#rechargeForm').data('bootstrapValidator').resetForm(true);
        $scope.playerId = undefined;
        $scope.playerNumber = undefined;
    });
	
	//日历组件
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	
	//初始化日期
	$scope.searchDate = year+"-"+month+"-"+day + " " + "-" + " " + year+"-"+month+"-"+day;
	$('#reservation').daterangepicker(null, function(start, end, label) {
	});
	
	//提示框组件启用
	 $("[data-toggle='popover']").popover(); 
    
	//初始化载入数据
	var path = "/tdata/saleRecord/find?pageNo="+ pageNo + "&&pageSize=" + pageSize+"&&orderBy=" + orderBy +"";
	$http.get(path).then(function(response){
		$scope.sales = response.data.list;
		pageCount=response.data.pageCount;
		pageMax = pageCount;
		$scope.pageCount = pageCount;
	});
	
	//载入页面数据函数
	function loadPage(no,size,order){
		var path = "/tdata/saleRecord/find?pageNo="+ no + "&&pageSize=" + size+"&&orderBy=" + order
		if(searchId)
			path += "&&playerId=" + searchId 
		if(searchDate)
			path += "&&searchDate=" + searchDate;
		$http.get(path).then(function(response){
			$scope.sales = response.data.list;
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
		});
	}
	
	//搜索按钮
	$scope.search = function(){
		
		pgaeCur = 1;
		$scope.pageCur = pageCur;//初始化分页
		searchDate =document.getElementById("reservation").value;
		searchId = $scope.searchId;
		var path = "/tdata/saleRecord/find?pageNo="+ pageNo + "&&pageSize=" + pageSize+"&&orderBy=" + orderBy;
		if(searchId)
			path += "&&playerId=" + searchId 
		if(searchDate)
			path += "&&searchDate=" + searchDate;
		$http.get(path).then(function(response){
			$scope.sales = response.data.list;
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
		});
		
	}
	
	//------------分页初始化---------
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
	
	//------------click page start------------
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
		
		//重新载入页面
		loadPage(pageCur,pageSize,orderBy);
	}
	//------------end click page------------
	
}])