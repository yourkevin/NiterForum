

var E = window.wangEditor
var editor = new E('#editor')
editor.customConfig.colors = [
    '#000000',
    '#eeece0',
    '#1c487f',
    '#4d80bf',
    '#ff0000',
    '#00FF00',
    '#7b5ba1',
    '#46acc8',
    '#f9963b',
    '#ffffff'
]
editor.customConfig.pasteFilterStyle = false
editor.customConfig.uploadImgServer = '/file/upload';  // 上传图片到服务器
editor.customConfig.uploadFileName = 'myFile';//设置文件上传的参数名称
editor.customConfig.uploadImgMaxSize = 1 * 1024 * 1024;//最大尺寸1M
editor.customConfig.uploadImgMaxLength = 5;//最多同时上传5张
editor.customConfig.customAlert = function (info) {
    // info 是需要提示的内容
    swal("上传失败!", info, "error");
}
editor.customConfig.emotions =[
    {
        // tab 的标题
        title: '贴吧',
        // type -> 'emoji' / 'image'
        type: 'image',
        // content -> 数组
        content: [
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/1.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/2.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/3.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/4.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/5.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/6.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/7.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/8.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/9.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/10.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/11.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/12.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/13.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/14.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/15.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/16.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/17.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/18.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/19.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/20.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/21.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/22.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/23.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/24.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/25.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/26.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/27.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/28.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/29.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/30.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/31.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/32.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/33.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/34.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/35.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/36.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/37.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/38.jpg'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/39.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/40.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/41.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/42.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/43.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/44.png'
            },
            {
                'src': 'https://qcdn.niter.cn/static/img/smile/tieba/45.png'
            }
        ]
    },
    {
        'title': '抖音（大表情）',
        'type': 'image',
        'content': [
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bld.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/ble.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blf.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blg.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blh.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bli.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blj.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blk.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bll.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blm.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bln.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blo.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blp.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blq.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blr.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bls.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blt.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blu.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blv.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blw.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blx.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bly.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/blz.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm_.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm0.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm1.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm2.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm3.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm4.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm5.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm6.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm7.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm8.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bm9.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bma.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmb.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmc.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmd.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bme.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmf.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmg.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmh.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmi.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmj.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmk.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bml.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmm.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmn.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmo.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmp.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmq.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmr.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bms.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmt.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmu.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmv.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmw.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmx.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmy.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bmz.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn_.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn0.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn1.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn2.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn3.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn4.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn5.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn6.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn7.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn8.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bn9.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bna.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnb.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnc.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnd.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bne.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnf.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bng.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnh.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bni.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnj.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnk.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnl.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnm.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnn.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bno.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnp.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnq.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnr.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bns.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnt.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnu.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnv.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnw.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnx.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bny.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bnz.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo_.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo0.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo1.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo2.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo3.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo4.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo5.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo6.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo7.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo8.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bo9.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boa.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bob.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boc.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bod.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boe.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bof.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bog.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boh.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boi.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boj.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bok.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bol.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bom.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bon.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boo.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bop.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boq.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bor.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bos.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bot.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bou.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bov.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bow.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/box.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boy.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/boz.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bp0.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bp1.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bp2.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bp3.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bp4.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bp5.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bp6.png'
            },
            {
                'src': 'https://community-1251590924.cos.ap-hongkong.myqcloud.com/images/smile/douyin/bp7.png'
            }
        ]
    }
]
var $description = $('#description')
editor.customConfig.onchange = function (html) {
    // 监控变化，同步更新到 textarea
    $description.val(html)
}
editor.create();
// 初始化 textarea 的值
$description.val(editor.txt.html());
//var objeditor = document.getElementById("editor");
var obj = document.getElementById("descriptionP");
//alert(obj.innerText);//这样就自动解析了
obj.innerHTML = obj.innerText;//这样重新设置html代码为解析后的格式