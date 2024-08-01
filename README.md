<h1 align="center">Polaris API SDK</h1>
<p align="center"><strong>papi-client-sdk 是一个专门为用户和开发者提供全面API接口调用服务的功能包 🛠</strong></p>
<div align="center">
	<a target="_blank" href="https://github.com/polarisronx/Papi-backend">
    	<img alt="" src="https://github.com/qimu666/qi-api/badge/star.svg?theme=gvp"/>
	</a>
    <img alt="Maven" src="https://raster.shields.io/badge/Maven-3.8.2-red.svg"/>
    <img alt="SpringBoot" src="https://raster.shields.io/badge/SpringBoot-2.7+-green.svg"/>
    <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img alt="" src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
	</a></div>

## 为什么是 Papi

Papi 为广大的用户和开发者提供了大量实用的、新奇的接口。

- 如果您是狂热的 Java 开发者，您可以前往 <a href="https://github.com/polarisronx/Papi-backend">Polaris API </a> 的源码进行研究，我们欢迎您一起交流，一起玩点好玩的。
- 如果您是用 API 调用需求的用户，且有 Java 开发的经验，您可以实用 maven 引入 <a href="https://github.com/polarisronx/Papi-backend">papi-client-sdk</a> 依赖包在本地轻松调用接口。
- 如果您没有 Java 开发的基础或您只是好奇，您可以前往 <a href="https://api.papi.icu">Papi 在线开放平台</a> 在线尝试。

## 网站指南

<a href="https://api.papi.icu">Papi 在线开放平台</a> 

<a href="https://doc.papi.icu">Papi 开发者文档</a>

> 我们也推荐您前往我们新开发的 <a href="bi.papi.icu">Polaris BI</a> 智能分析平台体验，目前正在内测✨

## 源码指南

<a href="https://github.com/polarisronx/Papi-backend">Papi 后端项目</a>

<a href="https://github.com/polarisronx/Papi-frontend">Papi 前端项目</a>

<a href="https://github.com/polarisronx/papi-gateway">Papi 网关</a>

<a href="https://github.com/polarisronx/papi-client-sdk">Papi SDK</a>

<a href="https://github.com/polarisronx/papi-interface">Papi 接口</a>

## 接口客户端 SDK 使用教程

- 不需要springboot，不需要任何框架！甚至不需要其他依赖！
- 不需要不需要任何工具类！不需要实现Http请求！
- 不需要高深的Java开发经验和技巧！

只需要 Java基础 JDK，和寥寥几句代码，即可轻松调用 papi 丰富的接口！

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240710135353.png)

此外，papi-client-sdk 已经登陆 **Maven 中心仓库**，只需要在 pom 文件中引入即可快速启动🎉

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/maven-papi.png)

(maven 上传有延迟，1.0.3版本已经发布)

```xml
<dependency>
    <groupId>io.github.polarisronx</groupId>
    <artifactId>papi-client-sdk</artifactId>
    <version>1.0.3</version>
</dependency>
```

您可以手动实例化 papi-client

```java
// 01 配置开发密钥
Credential credential = new Credential("polaris", "abcdefgh");
// 02 配置接口和请求参数
HttpProfile httpProfile = new HttpProfile("localhost:8123", "/api/v1/roman/intToRoman", "GET");
HttpConnection httpConnection = new HttpConnection();
// 03 创建papi客户端
PapiClient papi = new PapiClient(credential, httpProfile,httpConnection);
```

也可以引入springboot框架，用配置文件注入属性自动装配 papi-client 实例

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/papi-yml.png)

然后在spring扫描路径的类下自动装配实例，就可以愉快使用papi的所有功能啦

```java
@Resource
PapiClient papiClient;
```

### 