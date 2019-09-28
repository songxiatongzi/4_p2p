
//同意实名认证协议
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

//验证姓名
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


//验证手机号码
function checkPhone(){
    //得到手机电话号码
    var phone = $.trim($("#phone").val());

    if(phone == ""){
        //查看电话号码是不是为空
        showError("phone","请输入电话号码");
        return false;

    }else if(!/^1[1-9]\d{9}$/.test(phone)){
        //验证电话号码的格式
        showError("phone","请输入正确的手机号码");
        return false;
    }else{
        showSuccess("phone");
    }
    return true;
}

//为点击获取验证码绑定事件
$(function(){

    //对点击倒计时按钮进行绑定
    $("#dateBtn1").on("click",function(){
        //点击之后，获取电话号码
        var phone = $.trim($("#phone").val());

        //alert("------");
        //如果样式中有灰色的样式，那就不能点击点击倒计时，如果没有回色的样式，才能点击按钮
        if(!$("#dateBtn1").hasClass("on")){
            //对手机号码和密码进行验证之后，在进行执行倒计时操作
            if(checkPhone() && checkRealName()){

                /*点击获取验证码发送成功后执行倒计时操作*/
                $.ajax({
                    url:"test/messageCodeText",
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
                            showError("dateBtn1","短信平台异常，请稍后重试");
                        }
                    },
                    error:function(){
                        showError("dateBtn1","短信平台异常，请稍后重试");
                    }
                });

            }

        }

    });
});

//验证电话号码是不是已经输入正确
function checkMessageCode(){
    var messageCode = $.trim($("#messageCode").val());

    if(messageCode == ""){
        showError("messageCode","请输入验证码");
        return false;
    }else{
        showSuccess("messageCode")
    }

    return true;
}

//验证身份证号
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

//验证银行卡号
function checkBankCardNo(){
    //获得银行卡号码
    var bankCardNo = $.trim($("#bankCardNo").val());

    //对身份证进行判断
    if(bankCardNo == ""){
        //身份证号不能为空
        showError("bankCardNo","银行卡号不能为为空");
        return false;
    }
    return true;
}

//点击提交按钮
function login (){

    var realName = $.trim($("#realName").val());
    var bankCardNo = $.trim($("#bankCardNo").val());
    var phone = $.trim($("#phone").val());
    var idCard = $.trim($("#idCard").val());
    var messageCode = $.trim($("#messageCode").val());

    if(checkPhone() && checkRealName() && checkMessageCode() && checkIdCard() && checkBankCardNo()){

        $.ajax({

            url:"test/login",
            data:{
                phone:phone,
                realName:realName,
                messageCode:messageCode,
                bankCardNo:bankCardNo,
                idCard:idCard
            },
            type:"post",
            dataType:"json",
            success:function(data){
                if(data.errorMessage == "ok"){
                    alert("成功")
                }else{
                    alert("失败")

                }
            },
            error:function(){
                alert("失败")

            }

        });

    }
}















