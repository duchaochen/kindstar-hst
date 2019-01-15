var menuAll = [
    {
        id: 1, title: '销售模块', parentid: 0,
        iocn : 'fa fa-briefcase',active:true,
        list: [
            {
                id: 10,
                title: '销售数据获取',
                parentid: 1,
                iocn : 'fa fa-bar-chart',
                url:"lis/lis-sale.html"
            },
            {
                id: 11,
                title: '销售数据查询',
                parentid: 1,
                iocn : 'fa fa-list',
                url:"lis/lis-sale-search.html"
            }

        ]
    },
    {
        id: 2, title: '外地办事处', parentid: 0,
        iocn : 'fa fa-automobile',active:false,
        list: [
            {
                id: 14,
                title: '办事处数据编辑',
                parentid: 2,
                iocn : 'fa fa-area-chart',
                url:"/lis/cw-office.html"
            }]
    },
    {
        id: 3, title: '财务模块', parentid: 0, iocn : 'fa fa-credit-card',active:false,
        list: [{
                    id: 13,
                    title: '财务数据编辑',
                    parentid: 3,
                    iocn : 'fa fa-area-chart',
                    url:"/lis/cw-accountant.html"
                },
                {
                    id: 1,
                    title: '财务数据查询',
                    parentid: 3,
                    iocn : 'fa fa-area-chart',
                    url:"/lis/cw-accountant-search.html"
                }
            ]
    }
];

var home_div = new Vue({
    el:'#home_div',
    data : {
        items : menuAll
    },
    //预加载
    mounted : function(){
        //加载layui
        layui_pageload();
    },
    methods:{
        /**
         * @param childitem:子集对象
         * @param menuname: 选项卡名称
         */
        showMenu : function (childitem,menuname) {
            var exist=$("li[lay-id='"+childitem.id+"']").length; //判断是否存在tab
            if(0===exist){
                element.tabAdd(menuname, {
                    title: childitem.title
                    ,content: "<iframe src=\""+childitem.url+"\" style=\"width: 100%; height: 780px;\" scrolling=\"no\"></iframe>" //支持传入html
                    ,id: childitem.id
                });
            }
            element.tabChange(menuname, childitem.id);//切换选项卡
        }
    }
});




