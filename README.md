# clean-maven-abnormal-dir

清理maven仓库中出现的.lastUpdated非正常依赖目录


用法:
1. 打包  
   ```bash
   mvn package && cp target/mvnCleaner*.jar ./mvnCleaner.jar
   ```
2. 执行
    > `-Drepo=/Users/user/.m2/` 指定maven仓库地址  
    > `-Dall` 默认不会删除含有jar包的目录  
    > `-Ddel` 执行删除,默认只打印将要删除的文件
   ```sh
   java -Drepo=/Users/user/.m2/ -Dall -Ddel -jar mvnCleaner.jar
   ```
