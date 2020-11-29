function commentTalk(type,tagetId,content,ip,token) {
    //console.log(ip+"--2--"+token);
    //var questionId = $("#question_id").val();
   // var content = $("#comment_content").val();
    if(content.length>1024)
        swal({
            title: "回复超过1024个字长!",
            text: "你的回复字数为:"+content.length+"，请精简您的发言!",
            icon: "warning",
            button: "确认",
        });
    else comment2target(questionId, 1, content,ip,token);
}