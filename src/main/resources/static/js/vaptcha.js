vaptcha({
    //配置参数
    vid: '5d807776fc650fd878051c24', // 验证单元id
    type: 'click', // 展现类型 点击式
    container: '#getCodeBtn', // 按钮容器，可为Element 或者 selector
    scene:'01'                //场景值01，即邮箱注册
}).then(function (vaptchaObj) {
    // obj2=vaptchaObj;
    vaptchaObj.listen('pass', function() {
        // 验证成功， 进行登录操作
        // console.log(vaptchaObj.getToken());
        //getCode();
        $.ajax({
            type: "post",
            url: "/validate",
            ContentType: "application/json",
            CacheControl: "no-cache",
            data:{
                scene:'01',
                token:vaptchaObj.getToken()
            },
            //  dataType: "json",
            success: function(data) {
                if(data.success==1){
                    //成功
                    getCode();
                }
                else if(data.success==0){
                    swal({
                        title: ""+data.msg,
                        text: "您未能通过我们的第二次智能检测，请稍后再试或联系管理员",
                        icon: "error",
                        button: "确认",
                    });
                    //vaptchaObj.reset();
                }
            },
            error: function(msg) {
                console.log(msg)
            }
        })
    })
    vaptchaObj.listen('close', function() {
        //验证弹窗关闭触发
        swal({
            title: "哎哟...",
            text: "您关闭了验证哦",
            icon: "error",
            button: "确认",
        });
    })
    vaptchaObj.render()// 调用验证实例 vaptchaObj 的 render 方法加载验证按钮
})