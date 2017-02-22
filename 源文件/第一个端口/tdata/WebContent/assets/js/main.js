'use strict';

/* Controllers */


var app = angular.module('app')
  .controller('AppCtrl', ['$scope','$window','$http',function($scope,$window,$http){
	  
	  var $win = $($window);
      // add 'ie' classes to html
      var isIE = !!navigator.userAgent.match(/MSIE/i);
      isIE && angular.element($window.document.body).addClass('ie');
      isSmartDevice($window) && angular.element($window.document.body).addClass('smart');

      // config
      $scope.app = {
        name: '首页',
        version: '1.6.0',
        user:"",
        role:"",
        userName:"",
        userNumber:"",
        // for chart colors
        color: {
          primary: '#7266ba',
          info: '#23b7e5',
          success: '#27c24c',
          warning: '#fad733',
          danger: '#f05050',
          light: '#e8eff0',
          dark: '#3a3f51',
          black: '#1c2b36'
        },
        settings: {
          themeID: 1,
          navbarHeaderColor: 'bg-black',
          navbarCollapseColor: 'bg-white-only',
          asideColor: 'bg-black',
          headerFixed: true,
          asideFixed: false,
          asideFolded: false,
          asideDock: false,
          container: false
        },
        isMobile: (function() { // true if the browser is a mobile device
          var check = false;
          if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
            check = true;
          };
          return check;
        })()
      }
      
      //返回登录者信息
      var timeRandom = Date.parse(new Date());//返回时间毫秒数
      var path = "/tdata/findLoginUser/?random" + timeRandom;
      $http.get(path).then(function (response) {
    	$scope.app.user = response.data.loginId;
    	$scope.app.userName = response.data.name;
    	$scope.app.userNumber = response.data.currentNumber;
      });
      
      $scope.$watch('app.settings', function() {
        if ($scope.app.settings.asideDock && $scope.app.settings.asideFixed) {
          // aside dock and fixed must set the header fixed.
          $scope.app.settings.headerFixed = true;
        }
       
      }, true);
      //返回登录用户的角色
      var rolePath = "/tdata/role";
      $http.get(rolePath).then(function (response) {
      	$scope.userRole = response.data.role;
      	
        });
      function isSmartDevice($window) {
        // Adapted from http://www.detectmobilebrowsers.com
        var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
        // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
        return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
      }

      var viewport = function() {
        var e = window,
          a = 'inner';
        if (!('innerWidth' in window)) {
          a = 'client';
          e = document.documentElement || document.body;
        }
        return {
          width: e[a + 'Width'],
          height: e[a + 'Height']
        };
      };

      // function that adds information in a scope of the height and width of the page
      $scope.getWindowDimensions = function() {
        return {
          'h': viewport().height,
          'w': viewport().width
        };
      };
      // Detect when window is resized and set some variables
      $scope.$watch($scope.getWindowDimensions, function(newValue, oldValue) {
        $scope.windowHeight = newValue.h;
        $scope.windowWidth = newValue.w;
        if (newValue.w >= 992) {
          $scope.isLargeDevice = true;
        } else {
          $scope.isLargeDevice = false;
        }
        if (newValue.w < 992) {
          $scope.isSmallDevice = true;
        } else {
          $scope.isSmallDevice = false;
        }
        if (newValue.w <= 768) {
          $scope.isMobileDevice = true;
        } else {
          $scope.isMobileDevice = false;
        }
      }, true);
      // Apply on resize
      $win.on('resize', function() {
        $scope.$apply();
      });
      
    }
  ]);