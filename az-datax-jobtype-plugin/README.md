# 自定义azkaban datax插件

### 使用
- 在项目路径下`gradlew build -x test`，
然后会在当前模块路径下生成
`build/distributions/az-datax-jobtype-plugin-${version}.zip`包
- 解压zip包，里面的datax文件夹放入`azkaban-exec-server/plugins/jobtypes`目录下即可

- 解压后目录结构
```
|-- azkaban-executor-server
    |-- plugins
        |-- jobtypes
            |-- datax
                |-- lib
                |-- private.properties
            |--commonprivate.properties
```

- 重启executor，即可使用，`type=datax`
- 示例当前模块中`job_examples/quickstart_datax_example`压缩为zip包上传到azkaban project即可，
- **注意**，这里示例中的datax json文件里面的writer，
`hdfspluswriter`是自己开发的datax插件，
点击[项目地址](https://github.com/codingdebugallday/DataX)去查看获取，
或者可修改此json文件，在自己的datax中可运行的即可
- **注意** job或flow文件必须写datax.home或者去配置环境变量DATAX_HOME指定datax的安装路径

#### 联系 qq: 283273332@qq.com