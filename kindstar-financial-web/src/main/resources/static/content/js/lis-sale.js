var t_this;
var sale_vue = new Vue({
    el: '#sale-vue',
    //预加载
    mounted: function () {
        t_this = this;
        // var nowdate = dateFtt("yyyy-MM-dd",new Date());
        // var nowdate = "2018-03-01";
        // var enddate = "2018-03-31";

        // this.startdate = nowdate;
        // this.stopdate = enddate;
        layui_pageload(["config.loadlaydate", "config.pageLoad",
            "sale_vue.search"]);
    },
    data: {
        //保存当前页数据
        cookiePageArray: [],
        tableIns : null,
        // startdate: '',
        // stopdate: '',
        nowdate: config.dateFtt("yyyy-MM-dd", new Date())
    },
    methods: {
        //lis数据拉取到cw库中
        getData: function () {
            /*************************设置进度条*****************************************/
            var sale_getdata = $("#sale-getdata");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(sale_getdata.hasClass(config.DISABLED)) return;
            config.loading(sale_getdata,$("#progress-row"));
            console.log(1)
            /*************************开始后台交互*****************************************/
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");
            // vue怎么获取select选中的值
            // 也可以通过 params 对象传递参数
            axios.get('/lis/synchronizationLisAll', {
                params: {
                    areaval: $("#areaval").val(),
                    hospitalval: $("#hospitalval").val(),
                    isSubmit : 0,//未提交
                    startdate: startdate.val() === '' ? t_this.nowdate : startdate.val(),
                    stopdate: stopdate.val() === '' ? t_this.nowdate : stopdate.val(),

                }
            }).then(function (response) {
                //快速消失进度条
                config.startProgress = true;
                layer.msg(response.data.msg);
            }).catch(function (error) {
                console.log(error);
                //快速消失进度条
                config.startProgress = true;
            });
        },
        //数据保存
        saveData: function () {
            var saleInfos = [];
            //将数据集转换成后台list可以接收的样式
            $.each(layui.data('salechecked'),function (index, item) {
                saleInfos.push(item);
            })
            if(0 === saleInfos.length) {
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
            axios.post('/sale/saveSale', JSON.stringify(saleInfos),{
                headers:{
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(function (response) {
                //刷新表格
                t_this.tableIns.reload({
                    where: t_this.tableIns.where
                    ,page: t_this.tableIns.page
                });
                //快速消失进度条
                config.startProgress = true;
                layer.msg(response.data.msg);

             }).catch(function (error) {
                console.log(error);
                layer.msg("数据格式有误，不能保存!!!");
                //快速消失进度条
                config.startProgress = true;
             });

        },
        //数据提交到财务
        submitData:function () {
            /*************************设置进度条*****************************************/
            var sale_submitData = $("#sale-submitData");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(sale_submitData.hasClass(config.DISABLED)) return;
            config.loading(sale_submitData,$("#progress-row"));
            console.log(3)
            /*************************开始后台交互*****************************************/
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");

            if('' === startdate){
                layer.msg("开始时间不能为空,否则没有数据可保存");
                return;
            }
            axios.get('/sale/updateSubmitValueWhere', {
                params: {
                    areaval: areaval,
                    hospitalval: hospitalval,
                    isSubmit : 0,//提交
                    editIsSubmit : 1,//将提交状态改为以提交
                    startdate: startdate.val() === '' ? t_this.nowdate : startdate.val(),
                    stopdate: stopdate.val() === '' ? t_this.nowdate : stopdate.val(),
                }
            }).then(function (response) {
                //刷新表格
                t_this.tableIns.reload({
                    where: t_this.tableIns.where
                    ,page: t_this.tableIns.page
                });
                //快速消失进度条
                config.startProgress = true;
                layer.msg(response.data.msg);

                // console.log(response.data)
                // console.log(t_this.tableIns.where)
                // console.log(t_this.tableIns.page)
            }).catch(function (error) {
                console.log(error);
                //快速消失进度条
                config.startProgress = true;
            });
        },
        //查询
        search: function () {
            /*************************设置进度条*****************************************/
            var sale_search = $("#sale-search");
            //如果按钮已经被点击了，将操作完成之后在点击
            if(sale_search.hasClass(config.DISABLED)) return;
            config.loading(sale_search,$("#progress-row"));
            console.log(4)
            /*************************开始后台交互*****************************************/
            //存储已选择数据集，用普通变量存储也行
            layui.data('salechecked', null);
            var areaval = $("#areaval").val();
            var hospitalval = $("#hospitalval").val();
            var startdate = $("#startdate");
            var stopdate = $("#stopdate");
            //第一个实例
            t_this.tableIns = table.render({
                elem: '#sale-data',
                id: 'saleid',//checkbox取值使用的
                title: "销售数据同步列表",
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
                    isSubmit : 0,//未提交
                    startdate: startdate.val() === '' ? t_this.nowdate : startdate.val(),
                    stopdate: stopdate.val() === '' ? t_this.nowdate : stopdate.val()
                },
                text: {
                    none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                },
                cols: saleInfoCols,
                done: function (res, curr, count) {
                    //checkbox选中的记忆功能,以及更新行数据
                    t_this.checkmemory(res.data);
                    //快速消失进度条
                    config.startProgress = true;
                }
            });
            var timeFields = ["reportTime", "invoiceTime"];
            var numberFields = ["realPrice","discount"];
            //监听单元格编辑
            table.on('edit(sale-data)', function (obj) {
                var value = obj.value //得到修改后的值
                    , item = obj.data //得到所在行所有键值
                    , field = obj.field; //得到字段
                console.log(field)
                if(timeFields.indexOf(field) > -1) {
                    //判断只能输入日期格式的提醒
                    if(isNaN(value)&&!isNaN(Date.parse(value))){
                        console.log("value是日期格式！")
                        //修改字段之后需要更新缓存
                        layui.data('salechecked', {
                            key: (item.saleid+item.jzbbxh), value: item
                        });
                    }
                    else{
                        console.log("value不是日期格式！");
                        layer.msg('您输入的不是日期格式,请重新输入正确');
                    }
                    return;
                }
                if(numberFields.indexOf(field) > -1) {
                    if(config.checkRate(value)) {
                        console.log("value是数字格式！")
                        //修改字段之后需要更新缓存
                        layui.data('salechecked', {
                            key: (item.saleid+item.jzbbxh), value: item
                        });
                    }
                    else{
                        console.log("value不是数字格式！");
                        layer.msg('您输入的不是数字格式,请重新输入正确');
                    }
                }
                layer.msg('[ID: ' + item.saleid + '] ' + field + ' 字段更改为：' + value);
            });
            //监听删除事件
            table.on('tool(sale-data)', function (obj) {
                //console.log(obj)
                if (obj.event === 'del') {
                    layer.confirm('真的删除行么', function (index) {
                        axios.get('/sale/deleteSaleinfo', {
                            params: {
                                saleid: obj.data.saleid
                            }
                        }).then(function (response) {
                            response.info = obj.data;
                            //并且要删除缓存
                            layui.data('salechecked', {
                                key: obj.data.saleid+obj.data.jzbbxh, remove: true
                            });
                            obj.del();
                            layer.close(index);
                            console.log(response);
                        }).catch(function (error) {
                            console.log(error);
                            //快速消失进度条
                            config.startProgress = true;
                        });
                    });
                }
            });
            /**
             * 复选框的点击事件
             * 主要操作为：
             * 1.将所有的勾选成功的id储存传入后台拉取数据存到本地销售数据库
             */
            table.on('checkbox(sale-data)', function (obj) {
                // console.log(obj); //当前是否选中状态
                //			  console.log(obj.data); //选中行的相关数据
                //			  console.log(obj.type); //如果触发的是全选，则为：all，如果触发的是单选，则为：one

                //全选或单选数据集不一样
                var data = 'one' === obj.type ? [obj.data] : t_this.cookiePageArray;
                //遍历数据
                $.each(data, function (index, item) {
                    //假设你数据中 id 是唯一关键字
                    if (obj.checked) {
                        //增加已选中项
                        layui.data('salechecked', {
                            key: item.saleid+item.jzbbxh, value: item
                        });
                    } else {
                        //删除
                        layui.data('salechecked', {
                            key: item.saleid+item.jzbbxh, remove: true
                        });
                    }
                });


            });
        },
        //table的checkbox选中的记忆功能
        checkmemory: function (result) {
            //记下当前页数据，Ajax 请求的数据集，对应你后端返回的数据字段
            t_this.cookiePageArray = result;
            if(t_this.cookiePageArray) {
                var len = 0;
                //遍历当前页数据，对比已选中项中的 id
                for (var index = 0; index < t_this.cookiePageArray.length; index++) {
                    var itemJson = layui.data('salechecked', t_this.cookiePageArray[index]['saleid']+t_this.cookiePageArray[index]['jzbbxh']);
                    if (itemJson) {
                        console.log(itemJson)
                        /**
                         * 逐个判断是否选中，如果已经选中就改变选中模式(注意这里不能使用缓存获取LAY_TABLE_INDEX值，
                         * 只能使用table自己传过来的对象获取该值)
                         */
                        var i = t_this.cookiePageArray[index]['LAY_TABLE_INDEX'];
                        var checkbox = $('.layui-table tr[data-index="' + i + '"] input[name="layTableCheckbox"]');
                        console.log(i)
                        checkbox.prop('checked', true);
                        checkbox.next().addClass('layui-form-checked');
                        //为了设置全选的
                        len++;
                        //将缓存中的值，更新到表格当中
                        t_this.updateRow(itemJson,i);
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
    }
});
