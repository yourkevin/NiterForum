/**

 @Name: 求解板块

 */
 
layui.define('fly', function(exports){

  var $ = layui.jquery;
  var layer = layui.layer;
  var util = layui.util;
  var laytpl = layui.laytpl;
  var form = layui.form;
  var fly = layui.fly;
  
  var gather = {}, dom = {
    jieda: $('#jieda')
    ,content: $('#L_content')
    ,jiedaCount: $('#jiedaCount')
  };

  //监听专栏选择
  form.on('select(column)', function(obj){
    var value = obj.value
   // ,elemQuiz = $('#LAY_quiz')
    ,tips = {
      tips: 1
      ,maxWidth: 250
      ,time: 10000
    };
   // elemQuiz.addClass('layui-hide');
    if(value === '1'){
      layer.tips('为了得到更多人更加准确的回答，请务必保证问题详尽且清楚哦~', obj.othis, tips);
    //  elemQuiz.removeClass('layui-hide');
    } else if(value === '2'){
      layer.tips('欢迎分享干货，好的帖子，大家可能会为你点赞加分哦！', obj.othis, tips);
    }else if(value === '4'){
      layer.tips('讨论是一种快速学习，加深印象的方法哦~', obj.othis, tips);
    }else if(value === '3'){
      layer.tips('有好建议，放肆提，一起完善社区！采纳后会收到站长的么么哒哦(づ￣ 3￣)づ', obj.othis, tips);
    }else if(value === '5'){
      layer.tips('这是站长和管理员专用的哦~', obj.othis, tips);
    }else if(value === '6'){
      layer.tips('新闻动态，科技前沿，可以选这里哦！', obj.othis, tips);
    }
  });

  //提交回答
  fly.form['/jie/reply/'] = function(data, required){
    var tpl = '<li>\
      <div class="detail-about detail-about-reply">\
        <a class="fly-avatar" href="/u/{{ layui.cache.user.uid }}" target="_blank">\
          <img src="{{= d.user.avatar}}" alt="{{= d.user.username}}">\
        </a>\
        <div class="fly-detail-user">\
          <a href="/u/{{ layui.cache.user.uid }}" target="_blank" class="fly-link">\
            <cite>{{d.user.username}}</cite>\
          </a>\
        </div>\
        <div class="detail-hits">\
          <span>刚刚</span>\
        </div>\
      </div>\
      <div class="detail-body jieda-body photos">\
        {{ d.content}}\
      </div>\
    </li>'
    data.content = fly.content(data.content);
    laytpl(tpl).render($.extend(data, {
      user: layui.cache.user
    }), function(html){
      required[0].value = '';
      dom.jieda.find('.fly-none').remove();
      dom.jieda.append(html);
      
      var count = dom.jiedaCount.text()|0;
      dom.jiedaCount.html(++count);
    });
  };

  //求解管理
  gather.jieAdmin = {
    //删求解
    del: function(div){
      layer.confirm('确认删除该贴么？删除后将无法恢复！', function(index){
        layer.close(index);
        $.post('/p/del/id', {
          id: div.data('id')
        }, function(res){
          if(res.code==200) {swal("Good job!", ""+res.msg, "success").then((value) => {
            location.href = '/';});
          }else swal("Oh,no!", ""+res.msg, "error");
        });


      });
    }
    
    //设置置顶等状态于操作
    ,set: function(div){
      var othis = $(this);
      $.post('/p/set/id', {
        id: div.data('id')
        ,rank: othis.attr('rank')
        ,field: othis.attr('field')
      }, function(res){
        if(res.code==200) {swal("Good job!", ""+res.msg, "success").then((value) => {
          location.reload();});
        }else swal("Oh,no!", ""+res.msg, "error");
      });


    }

    //收藏
    ,collect: function(div){
      var othis = $(this), type = othis.data('type');
      fly.json('/collection/'+ type +'/', {
        cid: div.data('id')
      }, function(res){
        if(type === 'add'){
          othis.data('type', 'remove').html('取消收藏').addClass('layui-btn-danger');
        } else if(type === 'remove'){
          othis.data('type', 'add').html('收藏').removeClass('layui-btn-danger');
        }
      });
    }
  };

  $('body').on('click', '.jie-admin', function(){
    var othis = $(this), type = othis.attr('type');
    gather.jieAdmin[type] && gather.jieAdmin[type].call(this, othis.parent());
  });

  //异步渲染
  var asyncRender = function(){
    var div = $('.fly-admin-box'), jieAdmin = $('#LAY_jieAdmin');
    //查询帖子是否收藏
    if(jieAdmin[0] && layui.cache.user.uid != -1){
      fly.json('/collection/find/', {
        cid: div.data('id')
      }, function(res){
        jieAdmin.append('<span class="layui-btn layui-btn-xs jie-admin '+ (res.data.collection ? 'layui-btn-danger' : '') +'" type="collect" data-type="'+ (res.data.collection ? 'remove' : 'add') +'">'+ (res.data.collection ? '取消收藏' : '收藏') +'</span>');
      });
    }
  }();

  //解答操作
  gather.jiedaActive = {
    zan: function(li){ //赞
      var othis = $(this), ok = othis.hasClass('zanok');
      fly.json('/api/jieda-zan/', {
        ok: ok
        ,id: li.data('id')
      }, function(res){
        if(res.status === 0){
          var zans = othis.find('em').html()|0;
          othis[ok ? 'removeClass' : 'addClass']('zanok');
          othis.find('em').html(ok ? (--zans) : (++zans));
        } else {
          layer.msg(res.msg);
        }
      });
    }
    ,reply: function(li){ //回复
      var val = dom.content.val();
      var aite = '@'+ li.find('.fly-detail-user cite').text().replace(/\s/g, '');
      dom.content.focus()
      if(val.indexOf(aite) !== -1) return;
      dom.content.val(aite +' ' + val);
    }
    ,accept: function(li){ //采纳
      var othis = $(this);
      layer.confirm('是否采纳该回答为最佳答案？', function(index){
        layer.close(index);
        fly.json('/api/jieda-accept/', {
          id: li.data('id')
        }, function(res){
          if(res.status === 0){
            $('.jieda-accept').remove();
            li.addClass('jieda-daan');
            li.find('.detail-about').append('<i class="iconfont icon-caina" title="最佳答案"></i>');
          } else {
            layer.msg(res.msg);
          }
        });
      });
    }
    ,edit: function(li){ //编辑
      fly.json('/jie/getDa/', {
        id: li.data('id')
      }, function(res){
        var data = res.rows;
        layer.prompt({
          formType: 2
          ,value: data.content
          ,maxlength: 100000
          ,title: '编辑回帖'
          ,area: ['728px', '300px']
          ,success: function(layero){
            fly.layEditor({
              elem: layero.find('textarea')
            });
          }
        }, function(value, index){
          fly.json('/jie/updateDa/', {
            id: li.data('id')
            ,content: value
          }, function(res){
            layer.close(index);
            li.find('.detail-body').html(fly.content(value));
          });
        });
      });
    }
    ,del: function(li){ //删除
      layer.confirm('确认删除该回复么？删除后无法恢复！', function(index){
        layer.close(index);
        $.post('/comment/del/id', {
          id: li.data('id'),
          type:1
        }, function(res){
          if(res.code==200) {
            li.remove();
            swal("Good job!", ""+res.msg, "success");
          }else swal("Oh,no!", ""+res.msg, "error");
        });

      });    
    }
  };

  $('.jieda-reply span').on('click', function(){
    var othis = $(this), type = othis.attr('type');
    gather.jiedaActive[type].call(this, othis.parents('li'));
  });


  //定位分页
  if(/\/page\//.test(location.href) && !location.hash){
    var replyTop = $('#flyReply').offset().top - 80;
    $('html,body').scrollTop(replyTop);
  }

  $('#admin-btn').on('click', function(){
    layer.open({
      title: '请下拉选择'
      ,  type: 1
      ,area:['auto','220px']
      ,content: $("#admin-panel")
    });
  });

  //监听提交
  form.on('submit(submitAdmin)', function(data) {
    $.post('/p/set/id', {
      id: data.field.id
      ,json: JSON.stringify(data.field)
      ,field: 'admin'
    }, function(res){
      if(res.code==200) {
        location.reload();
      }else swal("Oh,no!", ""+res.msg, "error");
    });

    return false;
  });

  exports('jie', null);
});