


//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});

//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//验证手机号码
function checkPhone(){
	//获取电话号码
	var phone = $.trim($("#phone").val());
	var flag = true;

	//对电话号码进行验证，根据不同的输入情况，输出不同的验证结果
	if(phone == ""){
		//电话号码输入的为空值
		showError("phone","请输入手机号码");
		return false;
	}else if(!/^1[1-9]\d{9}$/.test(phone)){
		//已经输入了电话号码，对电话号码进行验证
		showError("phone","请您输入正确的手机号码");
		return false;
	}else{
		//对正确的电话发送ajax进行验证
		$.ajax({
			url:"loan/checkPhone",
			data:{
				"phone":phone
			},
			type:"get",
			async:false,
			//默认返回的是json格式，在这里可以不写
			dataType:"json",
			success:function(data){
				//手机号码可以注册，
				//当错误信息为ok的时候，表示可以注册
				if(data.errorMessage == "ok"){
					showSuccess("phone");
					flag = true;
				}else{
					showError("phone",data.errorMessage);
					flag = false;
				}
			},
			//对返回的错误信息进行处理
			error:function(){
				showError("phone","系统繁忙，请稍后重试");
				flag = false;
			}

		});
	}
	if(!flag){
		return false;
	}

	return flag;
}

//验证登陆密码
function checkLoginPassword(){

	var loginPassword = $.trim($("#loginPassword").val());

	if(loginPassword == null){
		//密码不能为空
		showError("loginPassword","密码不能为空");
		return false;
	}else if(!/^[0-9a-zA-Z]+$/.test(loginPassword)){
		//密码必须是数字和字母
		showError("loginPassword","密码只能使用数字和大小写字母");
		return false;

	}else if (loginPassword.length < 6 || loginPassword.length > 20) {
        showError("loginPassword", "密码长度应为6-20位");
        return false;
    } else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
		//密码必须同时包含数字和字母
		showError("loginPassword","密码必须同时包含数字和字母");
		return false;
	}else{
		//密码验证成功
		showSuccess("loginPassword");
	}

	return true;

}

//验证图片验证码
function checkCaptcha(){


	var captcha = $.trim($("#captcha").val());
	var flag = true;
	if(captcha == null){
		//查询图片是不是为空
		showError("captcha","验证码不能为空");
		return false;
	}else{
		//已经输入了验证码，需要对验证码进行判断
		$.ajax({
			url:"loan/checkCaptcha",
			data:{
				"captcha":captcha
			},
			type:"post",
			async:false,
			dataType:"json",
			success:function(data){
				//对反回的信息进行判断
				if(data.errorMessage == "ok"){
					//验证码验证成功

					showSuccess("captcha");
					flag = true;

				}else{
					//验证码验证失败，请重新输入 ,后台将查询写入到集合中
					showError("captcha",data.errorMessage);
					flag = false;
				}
			},
			error:function(){
				showError("captcha","系统繁忙，请稍后重试");
				flag = false;
			}

		});

	}
	if(!flag){
		return false;
	}

	return true;

}

//当点击注册的时候，将三个已经验证好的值
function register(){

	var phone = $.trim($("#phone").val());
	var loginPassword = $.trim($("#loginPassword").val());

	//当点击注册的时候，将获取好的手机号码和密码传递到后端
	if(checkPhone() &&checkLoginPassword()&&checkCaptcha()){
		//将密码框填充成为密文
		$("#loginPassword").val($.md5(loginPassword));

		$.ajax({

			url:"loan/register",
			data:{
				"phone":phone,
				"loginPassword":$.md5(loginPassword)
			},
			type:"post",
			success:function(data){
				if(data.errorMessage == "ok"){
					//跳转到实名认证页面
					window.location.href = "realName.jsp";
				}else{
					showError("captcha","注册异常");
				}
			},
			error:function(){
				showError("captcha","注册异常");
			}

		});
	}
}






