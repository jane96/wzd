app.controller('modalCtrl',["$http","$scope",'toaster',function($http,$scope,toaster){
	//----------表单验证 start----------
	$('#modalForm2').bootstrapValidator({
	  //      live: 'disabled',
      message: 'This value is not valid',
      feedbackIcons: {
          valid: 'glyphicon glyphicon-ok',
          invalid: 'glyphicon glyphicon-remove',
          validating: 'glyphicon glyphicon-refresh'
      },
      fields: {
          modalPassword2: {
              message: '密码不合法',
              validators: {
                  notEmpty: {
                      message: '密码不能为空'
                  },
                  stringLength: {
                      min: 7,
                      max: 16,
                      message: '密码为7-16位'
                  },
                  regexp: {
                      regexp: /^[a-zA-Z0-9]+$/,
                      message: '密码不能为汉字以及符号'
                  }
              }
          }
      	
      }
	});
	//----------end 表单验证----------
	$scope.modalSubmit2 = function(id){
		var password = $scope.modalPassword2;
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
		var path1 ="/tdata/proxy/updatePassword?loginId=" + id + "&&password=" + password;
		$http.get(path1).then(function(response){
			if(response.data.status == 1){
				toaster.pop({type:"success",title:"修改密码信息提示",body:"修改密码信息成功",timeout:2000});
			}else{
				taoster.pop({type:"error",title:"修改密码信息提示",body:response.data.message,timeout:2000});

			}
		})
		 
	}	
	}])
