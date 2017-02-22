/**
 * Config for the router
 */
angular.module('app')
 .run(
    ['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
            
        }
    ]
 	)
.config(
	    ['$stateProvider', '$urlRouterProvider', '$httpProvider','JQ_CONFIG', '$ocLazyLoadProvider',
	        function ($stateProvider, $urlRouterProvider,$httpProvider, JQ_CONFIG,$ocLazyLoadProvider) {
	    	//返回登录用户的角色
	        
          $ocLazyLoadProvider.config({
                debug: true,
                events: true,
                modules: JQ_CONFIG.modules
            });
          var date = new Date().getTime();
	           $urlRouterProvider
	                .otherwise('/app/recharge');
	            $stateProvider
	                .state('app', {
	                    abstract: true,// abstract: true 表明此状态不能被显性激活，只能被子状态隐性激活
	                    url: '/app'  ,
	                    templateUrl: '/tdata/html/app.html' 
	               })
	               .state('app.index',{
	            	   url: '/index' ,
	                   templateUrl: '/tdata/html/login.html' 
	               })
	               .state('app.player',{//玩家信息
	            	   url:'/player',	
	            	   templateUrl:'/tdata/html/player/player.html',
	            	   resolve:{
	            		   loadMyCtrl:['$ocLazyLoad',function($ocLazyLoad){
		            			  return $ocLazyLoad.load(['assets/js/controllers/playerCtrl.js']);
		            		   }]
	            	   }
		            	
	               })
	               .state('app.recharge',{//充值页面
	            	   url:"/recharge",
	            	   templateUrl:'/tdata/html/sale/recharge.html',
	            	   resolve:{
	            		   loadMyCtrl:['$ocLazyLoad',function($ocLazyLoad){
		            			  return $ocLazyLoad.load(['assets/js/controllers/rechargeCtrl.js','assets/js/controllers/saleRecordCtrl.js','assets/js/plugins/datepicker/daterangepicker-bs3.css',
		            			                           		                	             'assets/js/plugins/datepicker/daterangepicker.js']);
		            		   }]
	            	   }
	               })
	               .state('app.proxy',{//代理商页面
	            	   url:"/proxy",
	            	   templateUrl:'/tdata/html/proxy/proxy.html',
	            	   resolve:{
	            		   loadMyCtrl:['$ocLazyLoad',function($ocLazyLoad){
	            			   return $ocLazyLoad.load(['assets/js/controllers/proxyCtrl.js']);
	            		   }]
	            	   }
	               })
	                }]
	                );