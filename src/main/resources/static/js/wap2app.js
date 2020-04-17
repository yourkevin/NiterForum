function checkUserAgent(){
    //console.log(navigator.userAgent.toString());
    if(navigator.userAgent.indexOf("Html5Plus") > -1) {
        document.body.classList.add("html5plus");
        document.body.style.overflow = 'hidden';
        function _preventDefault(e) { e.preventDefault(); }
        window.addEventListener('touchmove', _preventDefault);
        //document.body.style.marginTop("0px");
        var list = document.getElementsByClassName("html5plus-display");
        var i;
        for (i = 0; i < list.length; i++) {
            list[i].style.display="";
        }
    }else {//判断页面是在手机端，平板端还是PC端打开
        var os = function (){
            var ua = navigator.userAgent,
                isWindowsPhone = /(?:Windows Phone)/.test(ua),
                isSymbian = /(?:SymbianOS)/.test(ua) || isWindowsPhone,
                isAndroid = /(?:Android)/.test(ua),
                isFireFox = /(?:Firefox)/.test(ua),
                isChrome = /(?:Chrome|CriOS)/.test(ua),
                isTablet = /(?:iPad|PlayBook)/.test(ua) || (isAndroid && !/(?:Mobile)/.test(ua)) || (isFireFox && /(?:Tablet)/.test(ua)),
                isPhone = /(?:iPhone)/.test(ua) && !isTablet,
                isPc = !isPhone && !isAndroid && !isSymbian;
            return {
                isTablet: isTablet,
                isPhone: isPhone,
                isAndroid: isAndroid,
                isPc: isPc
            };
        }();
        if (os.isAndroid || os.isPhone) {//手机
            pathname = window.location.pathname;
           if('/p/'==pathname.substring(0,3)||'/news/'==pathname.substring(0,6)){
               //window.location.href=window.location.href.replace("https","nitercn");
               setTimeout(function(){
                   window.location.href=window.location.href.replace("https","nitercn");
                 },500);
           }
        } else if (os.isTablet) {
            console.log("平板")
        } else if(os.isPc) {
            console.log("电脑")
        }

    }
}

function downloadApp(){
    window.location.href=window.location.href.replace("https","nitercn");
    var ua = navigator.userAgent,
        isWindowsPhone = /(?:Windows Phone)/.test(ua),
        isSymbian = /(?:SymbianOS)/.test(ua) || isWindowsPhone,
        isAndroid = /(?:Android)/.test(ua),
        isFireFox = /(?:Firefox)/.test(ua),
        isChrome = /(?:Chrome|CriOS)/.test(ua),
        isTablet = /(?:iPad|PlayBook)/.test(ua) || (isAndroid && !/(?:Mobile)/.test(ua)) || (isFireFox && /(?:Tablet)/.test(ua)),
        isIPhone = /(?:iPhone)/.test(ua) && !isTablet,
        isPc = !isIPhone && !isAndroid && !isSymbian;
    setTimeout(function(){//设置1S的延迟
        if(isAndroid){
            window.location.href="https://qcdn.niter.cn/static/app/NiterForum_latest.apk";
        }else if(isIPhone||isTablet){
            swal({
                icon: "info",
                title: "NiterForum for IOS is coming！",
            });
        }else {
            swal("您未处于移动访问状态，请选择适合您移动设备的版本",{
                buttons: {
                    cancel: "不了，谢谢！",
                    android: {
                        text: "安卓",
                        value: "android",
                    },
                    ios: {
                        text: "IOS（越狱）",
                        value: "ios",
                    }
                },
            }).then((value) => {
                switch (value) {
                case "android":
                    window.location.href="https://qcdn.niter.cn/static/app/NiterForum_latest.apk";
                    break;
                case "ios":
                    window.location.href="https://qcdn.niter.cn/static/app/NiterForum_latest.ipa";
                    break;
                  /*  swal({
                        icon: "info",
                        title: "NiterForum for IOS is coming！",
                    });*/
                    break;
                default:
                    swal("请随时关注后续版本更新哦!");
                }
            });
        }
    },1000);
        }

function toUCenter(){
    window.location.href="/user/set/info";
}