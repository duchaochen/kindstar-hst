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
    var accountantSearchjs = {
        tableIns : null,
        nowdate: config.dateFtt("yyyy-MM-dd", new Date()),
        search: function () {
            /*************************设置进度条*****************************************/
            var dataSearch = $("#accountant-dataSearch");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(dataSearch.hasClass(config.DISABLED)) return;
            config.loading(dataSearch,$("#progress-row"));
            console.log(1)
            /*************************开始后台交互*****************************************/
                //存储已选择数据集，用普通变量存储也行
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");
            //第一个实例
            accountantSearchjs.tableIns = table.render({
                elem: '#accountant-data',
                id: 'saleid',//checkbox取值使用的
                title: "销售以提交成功数据列表",
                skin: 'row', //行边框风格
                method: 'get',
                even: true, //开启隔行背景
                height: 'full-185',//满屏
                url: '/accountant/searchAccountantData',//数据接口
                page: true, //开启分页
                limit: 13,//每页多少行
                limits: [13, 30, 90],//分页条目
                loading: true,//显示加载条
                where: {
                    areaval: areaval,
                    hospitalval: hospitalval,
                    isSubmit : 1,//查询以提交
                    isAudit : 1,
                    startdate: startdate.val() === '' ? accountantSearchjs.nowdate : startdate.val(),
                    stopdate: stopdate.val() === '' ? accountantSearchjs.nowdate : stopdate.val()
                },
                text: {
                    none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                },
                cols: accountantInfoCols,
                done: function (res, curr, count) {
                    config.startProgress = true;
                    console.log(config.startProgress);
                }
            });
        },
        //撤回审核状态
        auditData : function () {
            /*************************设置进度条*****************************************/
            var accountant_cancelAudit = $("#accountant-cancelAudit");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(accountant_cancelAudit.hasClass(config.DISABLED)) return;
            config.loading(accountant_cancelAudit,$("#progress-row"));
            /*************************开始后台交互*****************************************/
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");

            if('' === startdate){
                layer.msg("开始时间不能为空,否则没有数据可保存");
                return;
            }
            axios.get('/accountant/updateAuditStatus', {
                params: {
                    areaval: areaval,
                    hospitalval: hospitalval,
                    isAudit : 1,//这个审核状态当做条件查询时使用(撤销审核)
                    editIsAudit : 0,//将改变审核状态状态
                    isSubmit : 1,//销售数据的提交状态必须是1，才能审核
                    startdate: startdate.val() === '' ? accountantSearchjs.nowdate : startdate.val(),
                    stopdate: stopdate.val() === '' ? accountantSearchjs.nowdate : stopdate.val(),
                }
            }).then(function (response) {
                //刷新表格
                accountantSearchjs.tableIns.reload({
                    where: accountantSearchjs.tableIns.where
                    ,page: accountantSearchjs.tableIns.page
                });
                //快速消失进度条
                config.startProgress = true;
                layer.msg(response.data.msg);
            }).catch(function (error) {
                console.log(error);
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

            var isSubmit = 1;//已提交
            var isAudit = 1;//已审核
            var url = "/exportAccountantData?areaval=" + encodeURIComponent(areaval) + "&" +
                "hospitalval=" + encodeURIComponent(hospitalval) + "&" +
                "startdate=" + encodeURIComponent(startdateValue) + "&" +
                "stopdate=" + encodeURIComponent(stopdateValue) + "&" +
                "isSubmit=" + encodeURIComponent(isSubmit)  + "&" +
                "isAudit=" + encodeURIComponent(isAudit);
            window.open(url);
        }
    };


    //查询以提交数据
    $("#accountant-dataSearch").on("click",function () {
        accountantSearchjs.search();
    });
    //撤回审核数据
    $("#accountant-cancelAudit").on("click",function () {
        accountantSearchjs.auditData();
    });
    //导出以提交数据
    $("#accountant-export").on("click",function () {
        accountantSearchjs.saleExport();
    });

    //时间控件加载
    config.loadlaydate();
    config.pageLoad();
    accountantSearchjs.search();

    //初始化动态元素，一些动态生成的元素如果不设置初始化，将不会有默认的动态效果
    element.init();
});