
//登录后返回页面的url ,将要转的地址赋值给这个变量
//在js中空值为
var referrer = "";

//获取跳转当前页面的之前页面
referrer = document.referrer;

if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});

$(function(){
	loadstart();
});

//点击登陆页面，将查询到的信息展示到登陆页面上
function loadstart(){
	$.ajax({

		url:"loan/loadStart",
		type:"get",
		dataType:"json",
		success:function(data){
			//将后端查询的历史年化收益率 平台总人数，累计成交金额装载到前端
			$(".historyAverageRate").html(data.historyAverageRate);
			$("#allUserCount").html(data.allUserCount);
			$("#allBidMoney").html(data.allBidMoney);
		}
	});

}

//验证电话号码  这组验证实在输入框的下面
function checkPhone(){
	//查询电话号码
	var phone = $.trim($("#phone").val());

	if(phone == ""){
		//查看电话号码是不是为空
		$("#showId").html("请输入电话号码");
		return false;

	}else if(!/^1[1-9]\d{9}$/.test(phone)){
		//验证电话号码的格式
		$("#showId").html("请输入正确的手机号码");
		return false;
	}else{
		$("#showId").html("");
	}
	return true;
}

//验证密码
function checkLoginPassword(){
	//获取密码
	var loginPassword = $.trim($("#loginPassword").val());

	//对密码进行判断
	if(loginPassword ==""){
		$("#showId").html("请输入登陆密码");
		return false;
	}else {
		$("#showId").html("");
	}

	return true;

}

//图形验证码
function checkCaptcha(){

	//获取验证码
	var captcha = $.trim($("#captcha").val());
	var flag = true;

	//对验证码进行判断
	if(captcha == null){
		$("#showId").html("验证码不能为空");
		return false;
	}else{
		$.ajax({
			url:"loan/checkCaptcha",
			data:{
				captcha:captcha
			},
			type:"post",
			async:false,
			dataType:"json",
			success:function (data) {
				if(data.errorMessage == "ok"){
					$("#showId").html("");
					flag = true;
				}else{
					$("#showId").html(data.errorMessage);
					flag = false;
				}
			},
			error:function(){
				$("#showId").html("系统异常，请稍后重试");
				flag = false;
			}

		});

	}
	if(!flag){
		return false;
	}

	return true;

}

//倒计时操作
/*-------------------在倒计时的时候，按钮是不能点击的---------------------------*/
//如果背景是灰色的时候，那就不能点击
$(function(){

	//对点击倒计时按钮进行绑定
	$("#dateBtn1").on("click",function(){
		//点击之后，获取电话号码
		var phone = $.trim($("#phone").val());

		//alert("------");
		//如果样式中有灰色的样式，那就不能点击点击倒计时，如果没有回色的样式，才能点击按钮
		if(!$("#dateBtn1").hasClass("on")){
			//对手机号码和密码进行验证之后，在进行执行倒计时操作
			if(checkPhone() && checkLoginPassword()){

				/*点击获取验证码发送成功后执行倒计时操作*/
				$.ajax({
					url:"loan/messageCode",
					data:{
						phone:phone
					},
					type:"get",
					dataType:"json",
					success:function(data){
						if(data.errorMessage == "ok"){
						    //用户测试
                            alert("短信验证码："+ data.messageCode);
							//执行倒计时操作(如果返回值为true,就进行倒计时)
							$.leftTime(60,function(d){
								//d.status 为true的时候正在倒计时
								if(d.status){
									//对按钮添加样式
									$("#dateBtn1").addClass("on");
									$("#dateBtn1").html((d.s =="00"?"60":d.s)+"秒后重新获取");
								}else{
									//结束之后，将页面之前的样式清理掉
									$("#dateBtn1").removeClass("on");
									$("#dateBtn1").html("获取验证码");
								}
							})
						}else{
							$("#showId").html("短信平台异常，请稍后重试");
						}
					},
					error:function(){
						$("#showId").html("短信平台异常，请稍后重试");
					}
				});

			}

		}

	});
});

//用来检验验证码是不是已经输入进去
function checkMessageCode(){
    var messageCode = $.trim($("#messageCode").val());

    if(messageCode == ""){
        $("#showId").html("请输入验证码");
        return false;
    }else{
        $("#showId").html("");
    }

    return true;
}

//执行登录操作
function login(){
	//账号 phone
	// 		        密码 loginPassword
	//获取用户名和密码
	var phone = $.trim($("#phone").val());
	var loginPassword = $.trim($("#loginPassword").val());
	//短信验证码
    var messageCode = $.trim($("#messageCode").val());


	//执行登陆操作
	/*getCaptcha()点击获取验证码 */
	if(checkPhone() && checkLoginPassword() && checkMessageCode()){
		//将密码域中的编程md5
		$("#loginPassword").val($.md5(loginPassword));

		$.ajax({

			url:"loan/login",
			data:{
				phone:phone,
				loginPassword:$.md5(loginPassword),
                messageCode:messageCode
			},
			type:"post",
			dataType:"json",
			success:function(data){
				if(data.errorMessage == "ok"){
					//如果登陆成功，就从那里来，就到那里去
					window.location.href = referrer;
				}else{
					$("#showId").html(data.errorMessage);

				}
			},
			error:function(){
				$("#showId").html("系统繁忙，请稍后");

			}

		});

	}
}



