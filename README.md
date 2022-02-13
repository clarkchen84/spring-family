### spring 中的数据库操作
#### spring 数据源的相关配置
1. 通用
   * spring.datasource.url=jdbc:mysql://localhost/test
   * spring.datasource.username=dbuser
   * spring.datasource.password=dbpass
   * spring.datasrouce.driver-class=com.mysql.jdbc.Driver
2. 初始化内嵌数据库
   * spring.datasource.initialization-mode=embedded|always|never
   * spring.datasource.schema与spring.datasource.data确定初始化SQL文件
   * spring.datasource.platform=hsqldb|h2|oracle|mysql|postgreql