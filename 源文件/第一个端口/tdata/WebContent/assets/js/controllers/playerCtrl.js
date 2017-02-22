app.controller('playerCtrl',['$scope','$http',function($scope,$http){

	//页面参数初始化,参数要与后台一致
	var pageNo = 1;//第一页
	var pageSize = 30;//每页显示的数据
	var orderBy ="desc-date";//排序规则如："desc-name","asc-name";前面是排序递增或递减，后面是排序的对象;默认为时间递减
	var pageCount =0//总的页面初始化
	$scope.pageCount = 0;
	var pageMin = 1;
	var pageMax = pageCount;
	var pageCur = 1;
	var searchDate = "";//查询的时间段
	var searchId = "";//查询的Id
	
	//初始化页面
	var path = "/tdata/player/findPlayer?pageNo=" + pageNo + "&&pageSize=" + pageSize + "&&orderBy=" + orderBy;
	$http.get(path).then(function(response){
		$scope.players = response.data.list;
		pageCount=response.data.pageCount;
		pageMax = pageCount;
		$scope.pageCount = pageCount;
	})
	
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
	
	//----------click page start----------
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
	//----------end click page----------
	
	//载入页面数据函数
	function loadPage(no,size,order){
		var path = "/tdata/player/findPlayer?pageNo="+ no + "&&pageSize=" + size+"&&orderBy=" + order
		$http.get(path).then(function(response){
			$scope.players = response.data.list;
			pageCount=response.data.pageCount;
			pageMax = pageCount;
			$scope.pageCount = pageCount;
		});
	}
	
	//搜索按钮
	$scope.search = function(){
		var path = "/tdata/checkLogout";
		$http.get(path).then(function(response){
			if(response.data.status == undefined){//如果退出了就结束操作
				toaster.pop({type:"warning",title:"充值信息提示",body:"登录失效！请重新登录",timeout:2000});
				return ;
			}else{
				var wechat = $scope.searchWechat;
				var id = $scope.searchId;
				if(wechat == undefined){
					wechat = "";
				}
				if(id == undefined){
					id = "";
				}
				var path = "/tdata/player/findPlayer?pageNo="+ pageCur + "&&pageSize=" +pageSize+"&&orderBy=" + orderBy +"&&pId=" + id + "&&pWechat=" + wechat;
				$http.get(path).then(function(response){
					$scope.players = response.data.list;
					pageCount=response.data.pageCount;
					pageMax = pageCount;
					$scope.pageCount = pageCount;
				});
			}
		})
	}
}]);