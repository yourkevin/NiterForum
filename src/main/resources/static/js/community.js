function post(ip,token) {
    //console.log(ip+"--2--"+token);
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    if(content.length>1024)
        swal({
            title: "回复超过1024个字长!",
            text: "你的回复字数为:"+content.length+"，请精简您的发言!",
            icon: "warning",
            button: "确认",
        });
    else comment2target(questionId, 1, content,ip,token);
}

function comment2target(targetId, type, content,ip,token) {
    //console.log(ip+"--3--"+token);
    if (!content) {
        //alert("不能回复空内容~~~");
        sweetAlert("出错啦...", "不能回复空内容~~~", "error");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/api/comment/insert",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type,
            "ip":ip,
            "token":token
        }),
        success: function (response) {
            if (response.code == 200) {
                swal({
                    title: "回复成功!",
                    text: "点击确认后即可刷新页面!",
                    icon: "success",
                    button: "确认",
                }).then((value) => {
                    window.location.reload();
            });

            } else {
                if (response.code == 2003) {
                   /* var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=b6ecb208ce93f679a75a&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }*/
                    swal({
                        title: "错误："+response.code,
                        text: response.message,
                        icon: "warning",
                        buttons: true,
                        dangerMode: true,
                    })
                        .then((willDelete) => {
                        if (willDelete) {
                            window.open("/sso/login");
                            window.localStorage.setItem("closable", true);

                            var interval = setInterval(function(){
                                var loginState = window.localStorage.getItem("loginState");
                                if (loginState == "true") {
                                    window.localStorage.removeItem("loginState");
                                  //  console.log("0");
                                    clearInterval(interval);
                                   // location.reload();
                                   // $("#comment_content").val(content);
                                    swal({
                                        title: "登陆成功!",
                                        text: "您可以提交回复啦!",
                                        icon: "success",
                                        button: "好的",
                                    });

                                    //$("#navigation").load("#navigation");

                                    return;
                                }
                               // console.log("1");
                               // document.getElementById("comment_content").value=content;
//do whatever here..
                            }, 2000);


                        } else {
                            swal({
                                     title: "已取消登录!",
                                     text: "取消登陆后，无法成功回复!",
                                     icon: "error",
                                     button: "确认",
                                 });
                }
                });
                }
                else {
                    sweetAlert("错误："+response.code, response.message, "error");
                }
            }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            swal(textStatus, "错误："+XMLHttpRequest.status, "error");
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var btnId = e.getAttribute("id");
    var strs= new Array();
    strs=btnId.split("-");
    var inputId = strs[1];
    var commentType = e.getAttribute("data-type");
    var inputBtn = $("#input-" + inputId);
    var content = inputBtn.attr('placeholder')+inputBtn.val();
    content=filterXSS(content);
    //console.log('inputId:'+inputId+'commentId:'+commentId+'placeholder:'+inputBtn.attr('placeholder')+'c:'+content);
    if(content.length>1024)
        swal({
            title: "回复超过1024个字长!",
            text: "你的回复字数为:"+content.length+"，请精简您的发言!",
            icon: "warning",
            button: "确认",
        });
    else comment2target(commentId, commentType, content,returnCitySN["cip"],"commentType2");


}

function like_comment(e) {
    var commentId = e.getAttribute("data-id");
    var thumbicon = $("#thumbicon-" + commentId);
    if(thumbicon.attr('class').indexOf('zanok')!=-1){
        swal({
            title: "点赞失败!",
            text: "请不要重复点赞哦!",
            icon: "error",
            button: "确认",
        });
        return;
    }
    like2target(commentId, 2);
}

function like_subComment(e) {
    var commentId = e.getAttribute("data-id");
    like2target(commentId, 3);
}

function like_question(e) {
    var questionId = e.getAttribute("data-id");
    var thumbtext = $("#questionLikeText-" + questionId);
    if('已'==thumbtext.text()){
        swal({
            title: "收藏失败!",
            text: "请不要重复收藏哦!您可以在帖子管理页取消收藏",
            icon: "error",
            button: "确认",
        });
        return;
    }
    like2target(questionId, 1);
}


function like2target(targetId, type){
    $.ajax({
        type: "POST",
        url: "/api/like/insert",
        contentType: 'application/json',
        data: JSON.stringify({
            "targetId": targetId,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {//点赞成功
                swal({
                    title: ""+response.message,
                    text: "感谢您的支持，作者将会收到通知!",
                    icon: "success",
                    button: "确认",
                });
                if(type==2||type==3){//点赞评论时
                var thumbicon = $("#thumbicon-" + targetId);
                thumbicon.addClass("zanok");
                var likecount = $("#likecount-" + targetId);
                likecount.html(parseInt(likecount.text())+1);//点赞+1
                }
                if(type==1){//收藏问题时
                    var thumbicon = $("#questionLikeIcon-" + targetId);
                    thumbicon.html('&#xe658;');
                    var thumbtext = $("#questionLikeText-" + targetId);
                    thumbtext.html('已');
                   /* thumbicon.removeClass("glyphicon-heart-empty");
                    thumbicon.addClass("glyphicon-heart");*/
                    var likecount = $("#questionlikecount-" + targetId);
                    likecount.html(parseInt(likecount.text())+1);//点赞+1
                }
            } else {
                if (response.code == 2003) {

                    swal({
                        title: "错误："+response.code,
                        text: response.message,
                        icon: "warning",
                        buttons: true,
                        dangerMode: true,
                    })
                        .then((willDelete) => {
                        if (willDelete) {
                            window.open("https://github.com/login/oauth/authorize?client_id=b6ecb208ce93f679a75a&redirect_uri=" + document.location.origin + "/callback&scope=user&state=1");
                            window.localStorage.setItem("closable", true);

                            var interval = setInterval(function(){
                                var loginState = window.localStorage.getItem("loginState");
                                if (loginState == "true") {
                                    window.localStorage.removeItem("loginState");
                                    //  console.log("0");
                                    clearInterval(interval);
                                    // location.reload();
                                    // $("#comment_content").val(content);
                                    swal({
                                        title: "登陆成功!",
                                        text: "您可以点赞啦!",
                                        icon: "success",
                                        button: "好的",
                                    });

                                    //$("#navigation").load("#navigation");

                                    return;
                                }
                                // console.log("1");
                                // document.getElementById("comment_content").value=content;
//do whatever here..
                            }, 2000);


                        } else {
                            swal({
                                     title: "已取消登录!",
                                     text: "取消登陆后，无法成功回复!",
                                     icon: "error",
                                     button: "确认",
                                 });
                }
                });
                }
                else if (response.code == 2022) {
                    if(type==2){//点赞评论时
                    var thumbicon = $("#thumbicon-" + targetId);
                    thumbicon.addClass("zanok");
                    }
                    swal({
                        title: "点赞失败!",
                        text: "请不要重复点赞/收藏哦!",
                        icon: "error",
                        button: "确认",
                    });
                }
                else if (response.code == 2023) {
                  /*  var thumbicon = $("#questionlikespan-" + targetId);
                    thumbicon.removeClass("glyphicon-heart-empty");
                    thumbicon.addClass("glyphicon-heart");*/
                    swal({
                        title: "收藏失败!",
                        text: "请不要重复收藏哦!您可以在帖子管理页面取消收藏",
                        icon: "error",
                        button: "确认",
                    });
                }
                else {
                    sweetAlert("错误："+response.code, response.message, "error");
                }
            }
        },
        dataType: "json"
    });
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);  //获取二级评论元素

    var inputComments2 = $("#input-" + id);
    var btnComments2 = $("#btn-" + id);
    inputComments2.attr('placeholder','');
    //inputComments2.attr('id','input-'+id);
    //btnComments2.attr('id','btn-'+id);
    btnComments2.attr('data-id',id);
    btnComments2.attr('data-type',2);
    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("layui-show");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {//没有加载过为1，已加载过大于1
            //展开二级评论
            comments.addClass("layui-show");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/api/comment/list/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<a/>", {
                        "class": "fly-avatar niter-avatar",
                        "href":"/user/"+comment.user.id
                    }).append($("<img/>", {
                        "class": "img-rounded",
                        "src": comment.user.avatarUrl,
                        "alt": comment.user.name
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "fly-detail-user"
                    }).append($("<a/>", {
                        "class": "fly-link",
                        "href": "/user/"+comment.user.id
                    }).append($("<span/>", {
                        "class": "menu"
                    }).append($("<cite/>", {
                        "html": comment.user.name
                    }))));
                 /*   var mediaBodyElement = $("<div/>", {
                        "class": "fly-detail-user"
                    }).append($("<a/>", {
                        "class": "fly-link",
                        "href": "/user/"+comment.user.id
                    }).append($("<cite/>", {
                        "html": comment.user.name
                    }))).append($("<span/>", {
                        "class": "menu"
                    }).append($("<i/>", {
                        "class": "iconfont icon-renzheng",
                        "title": "认证信息"
                    })).append($("<i/>", {
                        "class": "layui-badge fly-badge-vip",
                        "text": "VIP3"
                    })));*/
                  var timeElement = $("<div/>", {
                        "class": "detail-hits"
                    }).append($("<span/>", {
                        "class": "",
                        "html": comment.gmtModifiedStr
                    })).append($("<span/>", {
                      "class": "rightbtn",
                      "style":"cursor: pointer;",
                      "data-id": comment.id,
                      "data-name": comment.user.name,
                      "id": "comment-"+comment.id,
                      "onclick" :"collapseSubComments("+id+","+comment.id+");"
                  }).append($("<i/>", {
                      "class": "iconfont icon-svgmoban53"
                  })));
                    var filterHtml = filterXSS(comment.content);
                    var contentElement = $("<div/>", {
                        "class": "detail-body sub-detail-body jieda-body photos",
                        "html":filterHtml
                    });

                    var infoElement = $("<div/>", {
                        "class": "detail-about detail-about-reply"
                    }).append(mediaLeftElement).append(mediaBodyElement).append(timeElement).append(contentElement);




                    var commentElement = $("<div/>", {
                        "class": "jieda-daan"
                    }).append(infoElement);


                    var media2Element = $("<div/>", {
                        "class": "fly-detail-user"
                    }).append($("<a/>", {
                        "class": "fly-link",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD  HH:mm')
                    })));
                 /*   var comment2Element = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);*/

                    subCommentContainer.prepend(commentElement);
                    console.log("00")
                });

                //展开二级评论
                comments.addClass("layui-show");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");

                $('.sub-detail-body').each(function(){
                    var othis = $(this), html = othis.html();
                    othis.html(fly.content(html));
                });
            });
        }
    }
}

function collapseSubComments(upId,subId) {
   // e1.removeClass("layui-show");
   // var subId = e2.getAttribute("data-id");
   // alert("subId:"+subId+"upId"+upId);
    var thisComment = $("#comment-" + subId);
    var upComments = $("#comment-" + upId);
    var inputComments = $("#input-" + upId);
    var btnComments = $("#btn-" + upId);
    var upName = '回复 @'+thisComment.attr('data-name')+' ：';
    //alert(upName);
    inputComments.attr('placeholder',upName);
   // inputComments.attr('id','input-'+subId);
   // upComments.attr('id','comment-'+subId);
   // btnComments.attr('id','btn-'+subId);
    btnComments.attr('data-id',subId);
    btnComments.attr('data-type',3);

    // if(inputComments==Object)

   // alert(thisComment.attr('data-name'));
    //upComments.removeClass("layui-show");
    //thisComment.removeClass("rightbtn");
    /* if(thisComment==Object)
     var upId = thisComment.getAttribute("data-upid")
     alert("upId:"+upId);
     var upComment = $("#comment-" + upid);
     upComment.removeClass("layui-show");*/

}


function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}


