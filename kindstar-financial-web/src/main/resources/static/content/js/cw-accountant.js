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
        //保存当前页的数据
        currentPageArray : [],
        tableIns : null,
        nowdate: config.dateFtt("yyyy-MM-dd", new Date()),
        //查询
        search: function ()     {
            /*************************设置进度条*****************************************/
            var dataSearch = $("#accountant-dataSearch");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(dataSearch.hasClass(config.DISABLED)) return;
            config.loading(dataSearch,$("#progress-row"));

            /*************************开始后台交互*****************************************/
                //存储已选择数据集，用普通变量存储也行
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");
            //存储已选择数据集，用普通变量存储也行
            layui.data('accountantchecked',null);
            //第一个实例
            accountantSearchjs.tableIns = table.render({
                elem: '#accountant-data',
                id: 'saleid',//checkbox取值使用的
                title: "财务编辑数据列表",
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
                    isAudit : 0,//表示未审核
                    startdate: startdate.val() === '' ? accountantSearchjs.nowdate : startdate.val(),
                    stopdate: stopdate.val() === '' ? accountantSearchjs.nowdate : stopdate.val()
                },
                text: {
                    none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                },
                cols: accountantInfoCols,
                done: function (res, curr, count) {
                    //checkbox选中的记忆功能,以及更新行数据
                    accountantSearchjs.checkmemory(res.data);
                    config.startProgress = true;
                    // console.log(config.startProgress);
                }
            });
            /**
             * 复选框的点击事件
             * 主要操作为：
             * 1.将所有的勾选成功的id储存到本地财务操作的数据库
             */
            table.on('checkbox(accountant-data)', function (obj) {
                //全选或单选数据集不一样
                var data = 'one' === obj.type ? [obj.data] : accountantSearchjs.cookiePageArray;
                //遍历数据
                $.each(data, function (index, item) {
                    //假设你数据中 id 是唯一关键字
                    if (obj.checked) {
                        item.isAudit = (null === item.isAudit) ? 0 : item.isAudit;
                        //增加已选中项
                        layui.data('accountantchecked', {
                            key: item.saleid+item.jzbbxh, value: item
                        });
                    } else {
                        //删除
                        layui.data('accountantchecked', {
                            key: item.saleid+item.jzbbxh, remove: true
                        });
                    }
                });
            });
        },
        //table的checkbox选中的记忆功能,result是当前页的数据
        checkmemory: function (result) {
            //记下当前页数据，Ajax 请求的数据集，对应你后端返回的数据字段
            accountantSearchjs.cookiePageArray = result;
            if(accountantSearchjs.cookiePageArray) {
                var len = 0;
                //遍历当前页数据，对比已选中项中的 id
                for (var index = 0; index < accountantSearchjs.cookiePageArray.length; index++) {
                    var itemJson = layui.data('accountantchecked', accountantSearchjs.cookiePageArray[index]['saleid']+accountantSearchjs.cookiePageArray[index]['jzbbxh']);
                    if (itemJson) {
                        console.log(itemJson)
                        /**
                         * 逐个判断是否选中，如果已经选中就改变选中模式(注意这里不能使用缓存获取LAY_TABLE_INDEX值，
                         * 只能使用table自己传过来的对象获取该值)
                         */
                        var i = accountantSearchjs.cookiePageArray[index]['LAY_TABLE_INDEX'];
                        var checkbox = $('.layui-table tr[data-index="' + i + '"] input[name="layTableCheckbox"]');
                        console.log(i)
                        checkbox.prop('checked', true);
                        checkbox.next().addClass('layui-form-checked');
                        //为了设置全选的
                        len++;
                        //将缓存中的值，更新到表格当中
                        accountantSearchjs.updateRow(itemJson,i);
                    }
                }

                // console.log(this.limit+"====="+len)
                //设置全选checkbox的选中状态，只有改变LAY_CHECKED的值， table.checkStatus才能抓取到选中的状态
                if (len === this.limit) {
                    var checkboxAll = $('.layui-table-header th[data-field=0] input[type="checkbox"]');
                    checkboxAll.prop('checked', true);
                    checkboxAll.next().addClass('layui-form-checked');
                }
                //暂时只能这样渲染表单
                form.render('checkbox');
            }
        },
        //更新table行
        updateRow: function (itemJson,i) {
            /**
             * 动态修改表格的值
             * 原因：将现已经编辑过的值，在翻页之后在回来看的时候值就还原了，
             * 因为是根据后台分页永远读取的都是数据库的值，所以现在需要将翻页之后然后在将缓存的值更新进去）
             */
            if(itemJson) {
                // console.log(itemJson)
                for(var key in itemJson)
                {
                    var div = $('.layui-table tr[data-index="' + i + '"] td[data-field="'+key+'"] div');
                    div.html(itemJson[key]);
                }
            }
        },
        //销售数据导出
        //数据保存
        saveData: function () {
            var accountants = [];
            //将数据集转换成后台list可以接收的样式
            $.each(layui.data('accountantchecked'),function (index, item) {
                accountants.push(item);
            })
            console.log(accountants)
            if(0 === accountants.length) {
                layer.msg("请先查询后在保存");
                //快速消失进度条
                // config.startProgress = true;
                return;
            }

            /*************************设置进度条*****************************************/
            var sale_saveData = $("#sale-saveData");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(sale_saveData.hasClass(config.DISABLED)) return;
            config.loading(sale_saveData,$("#progress-row"));
            /*************************开始后台交互*****************************************/

            //以数组形式传参，controller以list形式接收
            axios.post('/accountant/saveAccountant', JSON.stringify(accountants),{
                headers:{
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
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
                layer.msg("保存的数据格式不正确!!!");
            });
        },
        //数据审核
        auditData : function () {
            /*************************设置进度条*****************************************/
            var accountant_audit = $("#accountant-audit");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(accountant_audit.hasClass(config.DISABLED)) return;
            config.loading(accountant_audit,$("#progress-row"));
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
                    isAudit : 0,//这个审核状态当做条件查询时使用
                    editIsAudit : 1,//将改变审核状态状态
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
        //销售数据撤回
        backData : function () {
            /*************************设置进度条*****************************************/
            var dataSearch = $("#accountant-back");
            //如果按钮事件还未执行完，将操作完成之后在点击
            if(dataSearch.hasClass(config.DISABLED)) return;
            config.loading(dataSearch,$("#progress-row"));

            /*************************开始后台交互*****************************************/
                //存储已选择数据集，用普通变量存储也行
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");
            if('' === startdate){
                layer.msg("开始时间不能为空,否则没有数据可保存");
                return;
            }
            axios.get('/accountant/backDataStatus', {
                params: {
                    areaval: areaval,
                    hospitalval: hospitalval,
                    isAudit : 0,//这个审核状态当做条件查询时使用
                    isSubmit : 1,//这个提交状态当做条件查询时使用
                    editIsSubmit : 0,//这个是要修改的状态
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
        //撤回办事处数据
        backOfficeData : function () {
            /*************************设置进度条*****************************************/
            var dataSearch = $("#accountant-dataSearch");
            //如果按钮事件还未执行完，将操作完成之后在点击
            if(dataSearch.hasClass(config.DISABLED)) return;
            config.loading(dataSearch,$("#progress-row"));

            /*************************开始后台交互*****************************************/
                //存储已选择数据集，用普通变量存储也行
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");
            if('' === startdate){
                layer.msg("开始时间不能为空,否则没有数据可保存");
                return;
            }
            axios.get('/accountant/backOfficeData', {
                params: {
                    areaval: areaval,
                    hospitalval: hospitalval,
                    isAudit : 0,//这个审核状态当做条件查询时使用
                    isSubmit : 1,//这个提交状态当做条件查询时使用
                    officeSubmit : 1,//这个是要修改的状态
                    editOfficeSubmit : 0,//这个是要修改的状态
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
        }
    };

    //查询已提交数据
    $("#accountant-dataSearch").on("click",function () {
        accountantSearchjs.search();
    });
    //数据保存
    $("#accountant-save").on("click",function () {
        accountantSearchjs.saveData();
    });
    //数据撤回
    $("#accountant-back").on("click",function () {
        accountantSearchjs.backData();
    });
    //数据审核
    $("#accountant-audit").on("click",function () {
        accountantSearchjs.auditData();
    });
//数据审核
    $("#accountant-office-back").on("click",function () {
        accountantSearchjs.backOfficeData();
    });


    //时间控件加载
    config.loadlaydate();
    config.pageLoad();
    accountantSearchjs.search();

    //初始化动态元素，一些动态生成的元素如果不设置初始化，将不会有默认的动态效果
    element.init();

});