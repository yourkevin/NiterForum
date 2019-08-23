function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    if(content.length>1024)
        swal({
            title: "回复超过1024个字长!",
            text: "你的回复字数为:"+content.length+"，请精简您的发言!",
            icon: "warning",
            button: "确认",
        });
    else comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        //alert("不能回复空内容~~~");
        sweetAlert("出错啦...", "不能回复空内容~~~", "error");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
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
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    if(content.length>1024)
        swal({
            title: "回复超过1024个字长!",
            text: "你的回复字数为:"+content.length+"，请精简您的发言!",
            icon: "warning",
            button: "确认",
        });
    else comment2target(commentId, 2, content);
}

function like_comment(e) {
    var commentId = e.getAttribute("data-id");
    like2target(commentId, 2);
}

function like_question(e) {
    var questionId = e.getAttribute("data-id");
    //alert(questionId);
    like2target(questionId, 1);
}


function like2target(targetId, type){
    $.ajax({
        type: "POST",
        url: "/like",
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
                if(type==2){//点赞评论时
                var thumbicon = $("#thumbicon-" + targetId);
                thumbicon.addClass("new");
                var likecount = $("#likecount-" + targetId);
                likecount.html(parseInt(likecount.text())+1);//点赞+1
                }
                if(type==1){//收藏问题时
                    var thumbicon = $("#questionlikespan-" + targetId);
                    thumbicon.removeClass("glyphicon-heart-empty");
                    thumbicon.addClass("glyphicon-heart");
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
                if (response.code == 2022) {
                    if(type==2){//点赞评论时
                    var thumbicon = $("#thumbicon-" + targetId);
                    thumbicon.addClass("new");
                    }
                    swal({
                        title: "点赞失败!",
                        text: "请不要重复点赞哦!",
                        icon: "error",
                        button: "确认",
                    });
                }
                if (response.code == 2023) {
                    var thumbicon = $("#questionlikespan-" + targetId);
                    thumbicon.removeClass("glyphicon-heart-empty");
                    thumbicon.addClass("glyphicon-heart");
                    swal({
                        title: "收藏失败!",
                        text: "请不要重复收藏哦!",
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
    var comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD  HH:mm')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
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


