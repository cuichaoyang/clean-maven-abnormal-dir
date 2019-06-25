# clean-maven-abnormal-dir

清理maven仓库中出现的.lastUpdated非正常依赖目录
用法:
> `java -jar mvnCleaner.jar -Drepo=/Users/user/.m2/ -Dall -Ddel`  
> 指定maven仓库地址: `-Drepo=/Users/user/.m2/`  
> 如果包含jar包是否删除: `-Dall`  
> 是否删除,不加的话只打印会删除的文件夹: `-Ddel`
