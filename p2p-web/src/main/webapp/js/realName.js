
//同意实名认证协议
//点击实名认证进行弹出窗口
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

//验证真实姓名
function checkRealName(){
	//根据id得到真实姓名
	var realName = $.trim($("#realName").val());

	//对姓名进行判断
	if(realName == ""){
		//姓名为空
		showError("realName","请输入姓名");
		return false;
	}else if(!/^[\u4e00-\u9fa5]{0,}$/.test(realName)){
		//如果输入汉字以外的其他字符
		showError("realName","真实姓名支持中文");
		return false;

	}else{
		showSuccess("realName");
	}

	return true;
}

//验证身份证号码是不是正确
function checkIdCard(){
	//获得身份证号码
	var idCard = $.trim($("#idCard").val());

	//对身份证进行判断
	if(idCard == ""){
		//身份证号不能为空
		showError("idCard","身份证号不能为为空");
		return false;
	}else if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)){
		//对身份证号码进行验证
		showError("idCard","请输入正确的身份证号码");
		return false;
	}else{
		//身份证号码正确
		showSuccess("idCard");
	}
	return true;
}

//验证图片验证码
function checkCaptcha(){

	//获取图形验证码
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

//对姓名 身份证 验证码进行验证
function verifyRealName(){

	//获取身份证 和 真实性名
	var realName = $.trim($("#realName").val());
	var idCard = $.trim($("#idCard").val());

	//对身份证号码和真实姓名进行验证
	if(checkRealName() && checkIdCard() && checkCaptcha()){
		$.ajax({
			url:"loan/verifyRealName",
			data:{
				realName:realName,
				idCard:idCard
			},
			type:"post",
			dataType:"json",
			success:function(data){
				if(data.errorMessage == "ok"){
					//验证成功
					//响应到前端，已经登录的页面
					//注意这里写是需要过一次后台页面
					window.location.href = "index";
				}else{
					//验证失败
					showError("captcha","请输入正确的姓名和身份证号码")
				}
			},
			error:function(){
				//验证失败
				showError("captcha","系统异常，请稍后重试")
			}
		});
	}
}
