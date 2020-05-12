# 自定义azkaban sql插件

### 使用
- 在项目路径下`gradlew build -x test`，
然后会在当前模块路径下生成
`build/distributions/az-sql-jobtype-plugin-${version}.zip`包
- 解压zip包，里面的sql文件夹放入`azkaban-exec-server/plugins/jobtypes`目录下即可

- 解压后目录结构
```
|-- azkaban-executor-server
    |-- plugins
        |-- jobtypes
            |-- sql
                |-- lib
                |-- private.properties
            |--commonprivate.properties
```

- 重新加载插件，即可使用，`type=sql`
```shell
curl http://ip:EXEC_SERVER_PORT/executor?action=reloadJobTypePlugins
```
- 示例当前模块中`job_examples/quickstart_example`压缩为zip包上传到azkaban project即可，
- 目前支持http以及jdbc方式，默认为http，http模式是我项目上的客制化需求，
因为项目上有许多自定义的驱动，请求接口去实现数据库查询，所以平时使用只需指定sql.execute.type为jdbc即可，详情见example

#### 联系 qq: 283273332@qq.com