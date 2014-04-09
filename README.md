iSCAU-Android
=============

华农宝iSCAU是一款以校园资源为核心的，由学生志愿者开发的，方便人民群众的APP。目前她集成了**华南农业大学教务系统**, **华南农业大学图书馆**, **华南农业大学校园通知** 的主要功能。过往曾经有过 **校巴实时报站**, **校园卡实时查询** 功能，但由于硬件、政策我等学生不可坑力的因素不可继续，未来我们将更基于当前，发展更多方便大家的功能。

#### 为何开源华农宝?

1. 我们希望更多同学参与进来Build华农人自己的APP
2. 主要开发者已面临实习危机，我们希望有人能成为这个项目的顶力柱
3. 同时希望发布这份源代码能帮助更多的学校能够开发出属于自己的APP
4. 希望更多高人指点，提高APP的用户体验与质量

#### 版权声明

1. 华农宝工作室保留**华农宝**及**iSCAU**软件名称的所有权利
2. 本软件采用**GPL V3协议**开源
3. 软件代码仅授权个人用于非商业性质用途的开发与学习
4. 华农宝工作室对本协议保留最终解释及更改的权力
5. 未获事前取得的书面许可，不得使用华农宝或本软件贡献者名称，来为本软件衍生物做任何表示支持、认可或推广、促销行为

# 开发环境指引

本项目建议使用以下环境进行开发:

* Android Studio 0.4.6 (不建议低于本版本，可能有BUG未修正)
* Gradle 1.10 (不建议低于本版本，可能有BUG未修正)
* Genymotion (非必要，但推荐用其代替官方的模拟器)
* Android 4.4.2 SDK API 19 (在Android Studio的SDK Manager可下载)
* Android SDK Build-tools version 19.0.1 (在Android Studio的SDK Manager可下载)

在下载了以上环境后，请用Android Studio导入本项目，**并且在Android Studio配置使用本地的Gradle而不使用Android Studio自带的Gradle**

> Preferences -> Gradle -> check "Use local gradle distribution" and fill your gradle path

在完成了以上操作后，点击工具栏的 "Sync Project with Gradle Files"，Gradle会自动为项目解决依赖并构建。

如出现不能下载可能您所在的区域网络存在不文明上网现象(自行访问Maven Central判断)，请自行使用GoAgent等文明上网工具进行上网。

# 项目架构说明

软件主要以androidannotations作为支撑框架，因此您在代码中随处可见 @xxx 的注解。关于 androidannotations 的使用方法及介绍，请自行搜索Github。另外还使用了较多的开源框架控件，可参看 libraries 文件夹及 app/build.gradle 的依赖项目。

### 文件夹结构

```
├── app                         // 代码主要文件夹
│   ├── libs                    // jar库文件夹
│   └── src                     // 源代码及资源文件
├── gradle
│   └── wrapper
└── libraries                   // 引用的Android Libary project
    ├── Android-PullToRefresh
    ├── SlideExpandableListView
    ├── caldroid
    └── wheelspinner
```

#### Java代码目录说明

```
├── adapter // ListView用到的adapters
├── api     // 连接服务器的API
├── helper  // 帮助方法
├── impl    // java接口文件
├── model
├── ui      // Activity, Fragment存放目录
├── util
└── widget  // 自定义控件目录
```

# 功能计划

| 功能简述 | 进度 | 预计上线时间 |
| :-----:  | :--: | :----------: |
| 选课评论 |  1% 功能策划书准备中 | 2014年3月中旬 |
| 桌面插件加强 | 0% | 2014年3月中旬 |
| 用户体验加强 | 0% | 2014年3月中旬 |

# 贡献代码

我们非常欢迎您Fork本项目，并提交Pull request。如果您想加入我们成为维护开发的主要人员，可Email至: specialcyci#gmail.com


```
版权声明

1. 华农宝工作室保留**华农宝**及**iSCAU**软件名称的所有权利
2. 本软件采用**GPL V3协议**开源
3. 软件代码仅授权个人用于非商业性质用途的开发与学习
4. 华农宝工作室对本协议保留最终解释及更改的权力
5. 未获事前取得的书面许可，不得使用华农宝或本软件贡献者名称，来为本软件衍生物做任何表示支持、认可或推广、促销行为

附: [GPL 协议全文](https://github.com/iSCAU/iSCAU-Android/blob/master/LICENSE)
