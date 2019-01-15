var config = {
    //是否快速结束
    startProgress : false,
    //按钮禁用样式
    DISABLED : 'layui-btn-disabled',
    /**************************************时间格式化处理************************************/
    dateFtt : function (fmt,date){ //author: meizz
        var o = {
            "M+" : date.getMonth()+1,                 //月份
            "d+" : date.getDate(),                    //日
            "h+" : date.getHours(),                   //小时
            "m+" : date.getMinutes(),                 //分
            "s+" : date.getSeconds(),                 //秒
            "q+" : Math.floor((date.getMonth()+3)/3), //季度
            "S"  : date.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    },
    //加载起止时间
    loadlaydate: function () {
        var startdate = laydate.render({
            elem: '#startdate', //指定元素
            type: 'date', //可选择：年、月、日。type默认值，一般可不填
            format: "yyyy-MM-dd",
            value: new Date(), //默认显示当天
            max: 0, //时间以今天为准，大于今天多少天
            calendar: true, //是否显示节假日
            mark: {
                '0-12-31': '跨年', //每年12月31日
                '0-0-15': '发工资', //每个月10号
            },
            done: function (value, dates, endDate) { //设置起止时间的范围
                $("#startdate").val(value);
                stopdate.config.min = {
                    year: dates.year,
                    month: dates.month - 1, //关键
                    date: dates.date,
                    hours: 0,
                    minutes: 0,
                    seconds: 0
                };

            }
        });

        var stopdate = laydate.render({
            elem: '#stopdate', //指定元素
            type: 'date', //可选择：年、月、日。type默认值，一般可不填
            format: "yyyy-MM-dd",
            value: new Date(), //默认显示当天
            max: 0, //时间以今天为准，大于今天多少天
            calendar: true,
            mark: {
                '0-12-31': '跨年' //每年12月31日
                ,
                '0-0-15': '发工资' //每个月10号
            },
            done: function (value, dates, endDate) {
                console.log(config.stopdate);
                startdate.config.max = {
                    year: dates.year,
                    month: dates.month - 1, //关键
                    date: dates.date,
                    hours: 0,
                    minutes: 0,
                    seconds: 0
                };
                $("#stopdate").val(value);
            }
        });
    },
    //加载地区
    loadArea: function () {
        return axios.get('/base/area');
    },
    //加载医院
    loadHospital: function () {
        return axios.get('/base/hospital');
    },
    //页面加载
    pageLoad: function () {
        axios.all([config.loadArea(), config.loadHospital()])
            .then(axios.spread(function (area, hospital) {
                //两个请求现已完成
                // t_this.areaList = area.data.datas.areaList;
                // t_this.cityList = area.data.datas.cityList;
                // t_this.hospitalList = hospital.data.data;
                //绑定地区
                $.each(area.data.datas.areaList, function (index, item) {
                    $("#areaval").append("<option value='" + item.areaName + "'>" + item.areaName + "</option>");
                })
                //绑定医院
                $.each(hospital.data.data, function (index, item) {
                    $("#hospitalval").append("<option value='" + item.hospitalName + "'>" + item.hospitalName + "</option>");
                })
                form.render("select");//刷新form表单
            }));
    },
    //进度条
    loading: function(othis,div){
        if(othis.hasClass(config.DISABLED)) return;
        //获取当前页面的所有的btn按钮
        var btns = $("#where-btn .layui-btn");
        //首先清空进度条
        element.progress('demo', '0%');
        //显示进度条显示层
        div.show();
        config.startProgress = false;
        var n = 1, s = 5,timer = setInterval(function(){
            // console.log(config.startProgress);
            //快速结束
            if(config.startProgress){
                n = n + 50;
                element.progress('demo', n.toFixed(2)+'%');
                // console.log("config.startProgress" + n);
            }

            if(n<30){
                n = n + 4.3;
                element.progress('demo', n.toFixed(2)+'%');
                // console.log(30+"============" + n);
            }
            else if(n<50){
                n = n + 2.2;
                element.progress('demo', n.toFixed(2)+'%');
                // console.log(50+"============" + n);
            }
            else if(n<70){
                n = n + 0.6;
                element.progress('demo', n.toFixed(2)+'%');
                // console.log(70+"============" + n);
            }
            else if(n<85){
                n = n + 0.1;
                element.progress('demo', n.toFixed(2)+'%');
                // console.log(85+"============" + n);
            }
            else if(n<90){
                n = n + 0.01;
                element.progress('demo', n.toFixed(2)+'%');
                // console.log(90+"============" + n);
            }
            else if(n<95){
                n = n + 0.001;
                element.progress('demo', n.toFixed(2)+'%');
                // console.log(95+"============" + n);
            }
            else if(n<99){
                element.progress('demo', '99%');
                // console.log(99+"============" + n);
            }
            else if(n>100){
                n = 100;
                element.progress('demo', '100%');
                //停止在加了
                config.startProgress = false;
            }
            //当n到100时直接就关闭定时器了
            else if(n === 100) {//必须到百分之百才能消失显示层
                clearInterval(timer);
                // othis.removeClass(config.DISABLED);
                //放开按钮
                config.lockBtn(btns,1);
                // console.log(100+"============" + n);
                div.hide();
            }

        }, 300+Math.random()*1000);
        // othis.addClass(config.DISABLED);
        //禁用按钮
        config.lockBtn(btns,0);
    },
    /**
     * 禁用按钮样式(锁定按钮不让点击和需要点击)
     * @param btns 需要控制的按钮集合
     * @param status 0表示禁用按钮,其它表示放开禁用
     */
    lockBtn : function (btns,status) {
        $.each(btns,function (index, item) {
            if(0 === status){
                $(this).addClass(config.DISABLED);
            }
            else{
                $(this).removeClass(config.DISABLED);
            }
        })
    },
    //判断是否是数字
    checkRate : function (str) {
        var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
        if (!re.test(str)) {
            return false;
        }
        return true;
    }
};
//销售列头
var saleInfoCols = [[ //表头
    {
        type: 'checkbox', fixed: 'left',LAY_CHECKED : false
    },
    {
        field: 'saleid',
        title: 'id',
    },
    {
        field: 'areaName',
        title: '省份',
        // sort: true//允许排序
        // ,edit: 'text'//允许编辑
    },
    // {
    //     field: 'areaName1',
    //     title: '城市',
    // },

    //到时候要打开的
    // {
    //     field: 'sellUser',
    //     title: '销售人员',
    //     sort: true
    // },
    // {
    //     field: 'realName',
    //     title: '病人姓名',
    //     sort: true
    // },





    // {
    //     field: 'age',
    //     title: '年龄',
    // },
    // {
    //     field: 'gender',
    //     title: '性别'
    // },
    // {
    //     field: 'hospitalization',
    //     title: '住院号',
    // },


    //到时候要打开的
    // {
    //     field: 'barCode',
    //     title: '总条码号',
    // },
    // {
    //     field: 'customLevelCode',
    //     title: '医院级别',
    // },
    // {
    //     field: 'hospitalizationName',
    //     title: '医院名称'
    // },
    // {
    //     field: 'departmentName',
    //     title: '科室'
    // },
    // {
    //     field: 'signing',
    //     title: '是否签约',
    // },



    // {
    //     field: 'billDoctor',
    //     title: '医生',
    // },


    //到时候要打开的
    // {
    //     field: 'combinationItemName',
    //     title: '项目名称',
    // },
    // {
    //     field: 'noteTime',
    //     title: '实际录入时间',
    // },
    // {
    //     field: 'squadName',
    //     title: '检验小组',
    // },
    // {
    //     field: 'productLine',
    //     title: '产品线',
    // },
    // {
    //     field: 'sampleType',
    //     title: '样本类型',
    // },




    // {
    //     field: 'acceptanceNO',
    //     title: '检验时机',
    // },
    // {
    //     field: 'logisticsUser',
    //     title: '物流人员',
    // },
    // {
    //     field: 'sampleStatus',
    //     title: '接检类型',
    // },
    {
        field: 'price',
        title: '项目价格',
    },
    {
        field: 'discount',
        title: '折扣',
        sort: true//允许排序
        ,edit: 'text'//允许编辑
    },
    {
        field: 'reportTime',
        title: '出报告时间',
        sort: true//允许排序
        ,edit: 'text'//允许编辑
    },
    {
        field: 'invoiceTime',
        title: '开票日期',
        sort: true//允许排序
        ,edit: 'text'//允许编辑
    },
    {
        field: 'realPrice',
        title: '实收金额',
        sort: true//允许排序
        ,edit: 'text'//允许编辑
    }
    ,{fixed: 'right', title:'操作', toolbar: '#sale-data-bar', width:80}
]];


//财务列头
var accountantInfoCols = [[ //表头
    {
        type: 'checkbox', fixed: 'left',LAY_CHECKED : false
    },
    {
        field: 'saleid',
        title: 'id',
    },
    {
        field: 'areaName',
        title: '省份',
        // sort: true//允许排序
        // ,edit: 'text'//允许编辑
    }
    // ,
    // {
    //     field: 'areaName1',
    //     title: '城市',
    // },

    //到时候要打开的
    // {
    //     field: 'sellUser',
    //     title: '销售人员',
    //     sort: true
    // },
    // {
    //     field: 'realName',
    //     title: '病人姓名',
    //     sort: true
    // },





    // {
    //     field: 'age',
    //     title: '年龄',
    // },
    // {
    //     field: 'gender',
    //     title: '性别'
    // },
    // {
    //     field: 'hospitalization',
    //     title: '住院号',
    // },


    //到时候要打开的
    // {
    //     field: 'barCode',
    //     title: '总条码号',
    // },
    // {
    //     field: 'customLevelCode',
    //     title: '医院级别',
    // },
    // {
    //     field: 'hospitalizationName',
    //     title: '医院名称'
    // },
    // {
    //     field: 'departmentName',
    //     title: '科室'
    // },
    // {
    //     field: 'signing',
    //     title: '是否签约',
    // },



    // {
    //     field: 'billDoctor',
    //     title: '医生',
    // },


    //到时候要打开的
    // {
    //     field: 'combinationItemName',
    //     title: '项目名称',
    // },
    // {
    //     field: 'noteTime',
    //     title: '实际录入时间',
    // },
    // {
    //     field: 'squadName',
    //     title: '检验小组',
    // },
    // {
    //     field: 'productLine',
    //     title: '产品线',
    // },
    // {
    //     field: 'sampleType',
    //     title: '样本类型',
    // },




    // {
    //     field: 'acceptanceNO',
    //     title: '检验时机',
    // },
    // {
    //     field: 'logisticsUser',
    //     title: '物流人员',
    // },
    // {
    //     field: 'sampleStatus',
    //     title: '接检类型',
    // },






    // {
    //     field: 'price',
    //     title: '项目价格',
    // },
    // {
    //     field: 'discount',
    //     title: '折扣',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // },
    // {
    //     field: 'reportTime',
    //     title: '出报告时间',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // },
    // {
    //     field: 'invoiceTime',
    //     title: '开票日期',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // },
    // {
    //     field: 'realPrice',
    //     title: '实收金额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'salesNet',
    //     title: '销售净额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'salesRemarks',
    //     title: '销售备注',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'returnedNumber',
    //     title: '回款编号',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'returnedTime',
    //     title: '回款日期',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'returnedPrice',
    //     title: '回款金额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'balancePrice',
    //     title: '应收余额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'countPrice',
    //     title: '回款总额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'invoice',
    //     title: '开票类型',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'invoicePrice',
    //     title: '开票金额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'invoiceNumber',
    //     title: '发票号码',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'rate1',
    //     title: '税率',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'rate2',
    //     title: '税额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'payTime',
    //     title: '补交日期',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'payPrice',
    //     title: '补交金额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'refundTime',
    //     title: '退款日期',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'refundPrice',
    //     title: '退款金额',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'receipt',
    //     title: '发票',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'idNumber',
    //     title: 'ID号',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'subbarCode',
    //     title: '子条码',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'adjustmentTime',
    //     title: '调整日期',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    // ,
    // {
    //     field: 'adjustmentDiscount',
    //     title: '调整折扣',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
    ,
    {
        field: 'adjustedNet',
        title: '调整后净额',
        sort: true//允许排序
        ,edit: 'text'//允许编辑
    }
    ,
    {
        field: 'adjustmentRemarks',
        title: '调整备注',
        sort: true//允许排序
        ,edit: 'text'//允许编辑
    }
    ,
    {
        field: 'remarks',
        title: '备注',
        sort: true//允许排序
        ,edit: 'text'//允许编辑
    }
    ,
    {
        field: 'validationInvoiceAmount',
        title: '开票金额验证',
        sort: true//允许排序
        ,edit: 'text'//允许编辑
    }


    // ,
    // {
    //     field: 'isAudit',
    //     title: '是否审核',
    //     sort: true//允许排序
    //     ,edit: 'text'//允许编辑
    // }
]];