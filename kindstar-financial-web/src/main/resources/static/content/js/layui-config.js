var element,$,layer,laydate,laypage,table,form;
var layui_pageload = function(array){
    layui.use(['element','jquery','layer','laydate','laypage','table'], function () {
        element = layui.element;
        $ = layui.$;
        layer = layui.layer;
        laydate = layui.laydate;
        laypage = layui.laypage;
        table = layui.table;
        form = layui.form;
        // form.render();//刷新form表单
        //阻止from表单默认行为
        form.on('submit(*)', function (data) {
            return false;
        });

        if(array){
            for (var index = 0;index<array.length;index++) {
                eval(array[index]+"()");
            }
        }
        //初始化动态元素，一些动态生成的元素如果不设置初始化，将不会有默认的动态效果
        element.init();
    });
}
