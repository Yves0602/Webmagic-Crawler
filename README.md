# Webmagic-Crawler
### 项目描述
该项目通过对51Job相关职位的搜索网页url获取html，分析html对51Job网站的职位信息进行爬取并记录在本地数据库里面。

### 使用技术
SpringBoot+IDEA+Maven+Mysql+Webmagic

### 项目心得
该项目使用SpringBoot构建entity、dao、service并连接Msql数据库，通过解析相应的51Job页面，通过Webmagic爬虫框架对需要的职位信息进行爬取，期间使用了Xpath以及Jsoup技术获取相关的elements、nodes以及text（），虽然51Job没有反爬虫，但也通过Webmagic中的相关类进行了代理设置。
