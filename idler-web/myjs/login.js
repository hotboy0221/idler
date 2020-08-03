$("fieldset#field_register>div>button.register").on("click", function (e) {
    var username = $("fieldset#field_register input[name='username']").val();
    var password = $("fieldset#field_register input[name='password']").val();
    var passwordR = $("fieldset#field_register input[name='password-r']").val();
    var email = $("fieldset#field_register input[name='email']").val();
    if (!checkUsername(username) || !checkPassword(password) || !checkEmail(email) || !checkPasswordR(password, passwordR)) return;
    $.ajax({
        type: "POST",
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        url: "http://" + myHost + "/users/register",
        xhrFields: { withCredentials: true },
        data: {
            username: username,
            password: password,
            email: email
        },
        success: function (result) {
            if (result.succeed) {
                bootbox.alert({
                    title: "提示",
                    message: "已向您的邮箱发送注册链接，请及时确认",
                })
            } else {
                bootbox.alert({
                    title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                    message: result.data.errCode + "_" + result.data.errMsg,
                })
            }
        },
        error: function (result) {
            bootbox.alert({
                message: "服务器繁忙",
                backdrop: true
            })
        }
    })
    return false;
})
$("fieldset#field_login>div>button.login").on("click", function (e) {
    var username = $("fieldset#field_login input[name='username']").val();
    var password = $("fieldset#field_login input[name='password']").val();
    if(username==""||password==""){
        bootbox.alert({
            title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
            message: "请输入用户名和密码",
            backdrop: true
        })
        return ;
    }
    $.ajax({
        type: "POST",
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        url: "http://" + myHost + "/users/login",
        xhrFields: { withCredentials: true },
        data: {
            username: username,
            password: password
        },
        success: function (result) {
            if (result.succeed) {
                bootbox.alert({
                    title: "提示",
                    message: "登录成功",
                    backdrop: true
                })
                window.localStorage["token"] = result.data;
                console.log(result.data);
                setTimeout("window.location.href = 'a_watching.html';",2000);
            } else {
                bootbox.alert({
                    title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                    message: result.data.errCode + "_" + result.data.errMsg,
                })
            }
        },
        error: function (result) {
            bootbox.alert({
                title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                message: "服务器繁忙",
                backdrop: true
            })
        }
    })
    return false;
})
$("fieldset#field_forgot>div>button.forgot").on("click", function (e) {
    var email = $("fieldset#field_forgot input[name='email']").val();
    if(!checkEmail(email))return;
    $.ajax({
        type: "PATCH",
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        url: "http://" + my_host + "/users/forgot",
        xhrFields: { withCredentials: true },
        data: {
            email: email
        },
        success: function (result) {
            if (result.succeed) {
                bootbox.alert({
                    title: "提示",
                    message: "已向您的邮箱发送重置密码链接，请及时确认",
                })
            } else {
                bootbox.alert({
                    title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                    message: result.data.errCode + "_" + result.data.errMsg,
                })
            }
        },
        error: function (result) {
            bootbox.alert({
                title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                message: "服务器繁忙",
                backdrop: true
            })
        }
    })
    return false;
})
function checkUsername(username) {
    if (!/^([a-zA-Z0-9_\u4e00-\u9fa5]{4,16})$/.test(username)) {
        bootbox.alert({
            title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
            message: "用户名需为4-16位数字或字母或下划线",
            backdrop: true
        })
        return false;
    }
    return true;
}
function checkPassword(password) {
    if (!/^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,18}$/.test(password)) {
        bootbox.alert({
            title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
            message: "密码需为6-18位数字或字母",
            backdrop: true
        })
        return false;
    }
    return true;
}
function checkPasswordR(password, passwordR) {
    if (password != passwordR) {
        bootbox.alert({
            title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
            message: "两次密码不一致",
            backdrop: true
        })
        return false;
    }
    return true;
}
function checkEmail(email) {
    if (!/^([a-z0-9A-Z]+[-|\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$/.test(email)) {
        bootbox.alert({
            title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
            message: "邮箱格式不正确",
            backdrop: true
        })
        return false;
    }
    return true;
}
