# 自定义azkaban rest插件

### 使用
- 在项目路径下`gradlew build -x test`，
然后会在当前模块路径下生成
`build/distributions/az-rest-api-jobtype-plugin-${version}.zip`包
- 解压zip包，里面的datax文件夹放入`azkaban-exec-server/plugins/jobtypes`目录下即可

- 解压后目录结构
```
|-- azkaban-executor-server
    |-- plugins
        |-- jobtypes
            |-- rest
                |-- lib
                |-- private.properties
            |--commonprivate.properties
```

- 重新加载插件，即可使用，`type=rest`
```shell
curl http://ip:EXEC_SERVER_PORT/executor?action=reloadJobTypePlugins
```
- 示例当前模块中`job_examples/quickstart_example`压缩为zip包上传到azkaban project即可

#### 联系 qq: 283273332@qq.com