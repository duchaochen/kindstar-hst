var element,$,layer,laydate,laypage,table,form;
layui.use(['element','jquery','layer','laydate','laypage','table'], function () {
    element = layui.element,
    $ = layui.$,
    layer = layui.layer,
    laydate = layui.laydate,
    laypage = layui.laypage,
    table = layui.table,
    form = layui.form;
    //阻止from表单默认行为
    form.on('submit(*)', function (data) {
        return false;
    });
    var searchjs = {
        tableIns : null,
        nowdate: config.dateFtt("yyyy-MM-dd", new Date()),
        search: function () {
            /*************************设置进度条*****************************************/
            var dataSearch = $("#dataSearch");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(dataSearch.hasClass(config.DISABLED)) return;
            config.loading(dataSearch,$("#progress-row"));

            /*************************开始后台交互*****************************************/
            //存储已选择数据集，用普通变量存储也行
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");
                //第一个实例
            table.render({
                    elem: '#sale-data',
                    id: 'saleid',//checkbox取值使用的
                    title: "销售以提交成功数据列表",
                    skin: 'row', //行边框风格
                    method: 'get',
                    even: true, //开启隔行背景
                    height: 'full-185',//满屏
                    url: '/sale/searchSaleinfoData',//数据接口
                    page: true, //开启分页
                    limit: 13,//每页多少行
                    limits: [13, 30, 90],//分页条目
                    loading: true,//显示加载条
                    where: {
                        areaval: areaval,
                        hospitalval: hospitalval,
                        isSubmit : 1,//未提交
                        startdate: startdate.val() === '' ? t_this.nowdate : startdate.val(),
                        stopdate: stopdate.val() === '' ? t_this.nowdate : stopdate.val()
                    },
                    text: {
                        none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                    },
                    cols: saleInfoCols,
                    done: function (res, curr, count) {
                        config.startProgress = true;
                        console.log(config.startProgress);
                    }
                });

            },
        //销售数据导出
        saleExport : function () {
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var  startdateValue = startdate.val() === '' ? t_this.nowdate : startdate.val();
            var stopdate = $("#stopdate");
            var stopdateValue = stopdate.val() === '' ? t_this.nowdate : stopdate.val();

            var isSubmit = 1;//未提交
            var url = "/exportSaleData?areaval=" + encodeURIComponent(areaval) + "&" +
                                      "hospitalval=" + encodeURIComponent(hospitalval) + "&" +
                                      "startdate=" + encodeURIComponent(startdateValue) + "&" +
                                      "stopdate=" + encodeURIComponent(stopdateValue) + "&" +
                                      "isSubmit=" + encodeURIComponent(isSubmit);
            window.open(url);
        }
    };

    //查询以提交数据
    $("#dataSearch").on("click",function () {
        searchjs.search();
    });
    //导出以提交数据
    $("#sale-export").on("click",function () {
        searchjs.saleExport();
    });

    //时间控件加载
    config.loadlaydate();
    config.pageLoad();
    searchjs.search();

    //初始化动态元素，一些动态生成的元素如果不设置初始化，将不会有默认的动态效果
    element.init();
});