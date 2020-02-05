var userDetail = false;
var showDetail = function () {
    if (userDetail == false) {
        $("#ProfileHeader-info").hide();
        $("#ProfileHeader-detail").fadeIn();
        document.getElementById("show-ProfileHeader-detail-btn").innerText = "收起详细资料";
    } else {
        $("#ProfileHeader-detail").fadeOut();
        $("#ProfileHeader-info").show();
        document.getElementById("show-ProfileHeader-detail-btn").innerText = "显示详细资料";
    }
    userDetail = !userDetail;
}

var showAlterHead = function () {
    if (document.getElementById("alter-head") == null) {
        return;
    }
    document.getElementById("alter-head").className = "Mask UserAvatarEditor-mask";
}

var closeAlterHead = function () {
    if (document.getElementById("alter-head") == null) {
        return;
    }
    document.getElementById("alter-head").className = "Mask UserAvatarEditor-mask Mask-hidden";
}

var checkEmailUseService = function (email) {
    $.ajax({
        type: 'get',
        url: '/api/checkEmail',
        data: {
            "email": email
        },
        success: function (resultdata) {
            if(resultdata.success == false) {
                document.getElementById("emailHelp").innerText = "该账号已被注册，请尝试登陆！";
                return false;
            }  else {
                return true;
            }
        }
    });
};

var checkUserIdUseService = function (userId) {
    $.ajax({
        type: 'get',
        url: '/api/checkUserId',
        data: {
            "userId": userId
        },
        success: function (resultdata) {
            console.log(resultdata);
            if(resultdata.success == false) {
                document.getElementById("userIdHelp").innerText = resultdata.msg;
                return false;
            }  else {
                document.getElementById("userIdHelp").innerText = resultdata.msg;
                return true;
            }
        }
    });
};



function alterUserData() {
    var userName = $("#userName").val();
    var sex = getRadioButtonCheckedValue('sex');
    var school = $("#userSchool").val();
    var major = $("#userMajor").val();
    var simpleSelfIntroduction = $("#userSimpleSelfIntroduction").val();
    var selfIntroduction = $("#userSelfIntroduction").val();
    var like = $("#userLike").val();

    $.ajax({
        type: 'POST',
        url: '/api/update/data',
        data: {
            "userName":userName,
            "sex": sex,
            "school": school,
            "major": major,
            "simpleSelfIntroduction": simpleSelfIntroduction,
            "selfIntroduction": selfIntroduction,
            "like": like
        },
        success: function (resultdata) {
            console.log(resultdata);
            if(resultdata.success == false) {
                //alert(resultdata.msg);
                document.getElementById("error-message").innerText = resultdata.msg;
                $("#error-message").show();
            }  else {
                alert(resultdata.msg);
                window.location.reload();
            }
        }
    });
}


function alterPassword() {
    var newPassword = $("#newPassword").val();
    var newPasswordAgain = $("#newPasswordAgain").val();
    var oldPassword = $("#oldPassword").val();
    var CAPTCHA = $("#CAPTCHA").val();
    if(newPassword == "" || newPasswordAgain == "" || oldPassword == "" || CAPTCHA == "") {
        document.getElementById("password-message").innerText = "输入数据不完整";
        $("#password-message").show();
        return;
    }
    if(newPassword != newPasswordAgain) {
        document.getElementById("password-message").innerText = "两次输入密码不一";
        $("#password-message").show();
        return;
    }

    $.ajax({
        type: 'POST',
        url: '/api/update/password',
        data: {
            "oldPassword":oldPassword,
            "newPassword": newPassword,
            "CAPTCHA": CAPTCHA
        },
        success: function (resultdata) {
            // console.log(resultdata);
            if(resultdata.success == false) {
                document.getElementById("image-captcha").src = '/captche/images?'+Math.random();
                document.getElementById("password-message").innerText = resultdata.msg;
                $("#password-message").show();
            }  else {
                alert(resultdata.msg);
                window.location.replace("/");
            }
        }
    });
}

function closePasswordtips() {
    $("#password-message").hide();
}



function removeReadOnly(e, btn) {
    if(btn.innerText == "修改") {
        e.removeAttribute("readOnly");
        btn.innerText = '完成';
    } else {
        e.setAttribute("readOnly", true);
        btn.innerText = "修改";
    }
}


function getRadioButtonCheckedValue(tagNameAttr){
    var radio_tag = document.getElementsByName(tagNameAttr);
    for(var i=0;i<radio_tag.length;i++){
        if(radio_tag[i].checked){
            var checkvalue = radio_tag[i].value;
            return checkvalue;
        }
    }
}