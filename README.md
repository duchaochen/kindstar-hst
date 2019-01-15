### 一、springboot整合mybatis的com.github.pagehelper注意事项

    1.springboot有一个默认的分页插件，要过滤掉
        @SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
        注意：下面这个是去除数据源的自动配置
        @SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
        
    2.然后还有就是com.github.pagehelper在4.0版本以后都是默认就能匹配到使用的数据库，不用去标识是什么数据库了
        <!--控制台打印sql语句配置settings-->
        <settings>
            <setting name="logImpl" value="LOG4J"/>
        </settings>
        <!-- com.github.pagehelper为PageHelper类所在包名 -->
        <plugin interceptor="com.github.pagehelper.PageInterceptor">
            <!--<property name="dialect" value="SqlServer"/>-->
        </plugin>
    3.需要添加slf4j-log4j12这个日志jar
        <!-- 日志处理 -->
        <dependency>
             <groupId>org.slf4j</groupId>
             <artifactId>slf4j-log4j12</artifactId>
        </dependency>
        
### 二、IDEA设置启动运行内存设置
    -server -XX:PermSize=128M -XX:MaxPermSize=256m
    
    
### 三、后端以list<T>数组接受数据解决方法
    前端代码：
             //以数组形式传参，controller以list形式接收
            axios.post('/accountant/saveAccountant', JSON.stringify(saleInfos),{//并且要转成json字符串
                headers:{//必须设置这个
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(function (response) {
                layer.msg(response.data.msg);
             }).catch(function (error) {
                    console.log(error);
             });
    后端代码：必须要加上@RequestBody 
            @PostMapping("/accountant/saveAccountant")
            public KindStartResult saveWebAccountant(@RequestBody List<SaleInfo> saleInfos) {
                KindStartResult kindStartResult;
        
                try {
                    kindStartResult = restTemplate.postForObject(HTTP_CW_URL + "/cw/saveAccountant", saleInfos, KindStartResult.class);
                }
                catch (Exception e){
                    kindStartResult = KindStartResult.build(500,"保存失败！！");
                }
                return kindStartResult;
            }
            
### 四、根据实体类的字节码获取所有字段以及对应的值，但是该项目没有用到，因为循环的顺序无法控制，以及有些列不需要导出
    //获取该类的所有字段名称
    Field[] fields = FieldUtils.getAllFields(SaleInfo.class);
    //根据每个字段名称获取对应的值，将上面的字段循环就可以获取到各个字段的值了
    Object propertyValue = PropertyUtils.getProperty(saleInfo, field.getName());
    String value = String.valueOf(propertyValue);
    