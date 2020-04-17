# SSR Subscribe
![](https://github.com/ywxt/ssr-subscribe/workflows/CI%20with%20Gradle/badge.svg)

## 介绍

这是一个SSR(非SS)订阅工具

## 用法

- 添加订阅源
  
  ```shell script
  ssr-sub subscribe add SOURCE [--file|-f FILE]
  ```
  
  - FILE: 配置文件位置，默认为`$HOME/.ssr-sub/setting.json`

- 移除订阅源
  
  ```shell script
  ssr-sub remove SOURCE_INDEX [--file|-f FILE] 
  ```
  
  - FILE: 配置文件位置
  - SOURCE_INDEX: 源序号，使用`source`命令查看

- 查看所有的订阅源
  
  ```shell script
  ssr-sub source [--file|-f FILE] 
  ```
  
  - FILE: 配置文件位置
  
- 更新订阅源
  
  ```shell script
  ssr-sub update [SOURCE_INDEX]
  ```
  
  - SOURCE_INDEX: 源序号，默认更新所有源
  
  
- 查看服务器配置
  
  ```shell script
  ssr-sub server [SERVER_INDEX] [--group|-g] [--file|-f FILE]
  ```
  
  - SERVER_INDEX: 服务器(组)序号，默认显示所有服务器
  - FILE: 配置文件位置
  - \-\-group: `SERVER_INDEX`是否为组序号，默认为`false`
  
- 切换服务器
  
  ```shell script
  ssr-sub switch SERVER_INDEX [--file|-f FILE] [--path|-p PATH]
  ```
  
  - SERVER_INDEX: 服务器序号，使用`source`命令查看
  - FILE:　配置文件位置
  - PATH: SSR 软件服务器配置文件位置，默认为`./config.json`
  
- 显示 SSR 软件使用的服务器配置
  
  ```shell script
  ssr-sub show [--path|p PATH]
  ```
  - PATH: SSR 软件服务器配置文件位置
 
## 开源协议
[MIT](LICENSE)
