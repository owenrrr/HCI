# Backend-SEIIIAssignment

#### 0. 前言

- **dev分支用于本地测试、前端接口调用**
- **master分支用于Jenkins集成测试** 



- 自动构建: 更改master分支开启自动构建
- http://47.111.73.158:8090/demo
- 数据自动加载解决



#### 1. Jenkins重启流程

1. **远程连接服务器**

2. **输入用户名和密码，并重启jenkins服务**

3. **点击后端项目seiii，点击立即构建，等待并在流水线显示全部绿色即可访问接口**

<img src="https://s3.ax1x.com/2021/03/16/6ryKuq.png" alt="image-20210316005439472" style="zoom:50%;" />



#### **2. IDEA热更新**

1. **添加依赖**

   ```xml
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-devtools</artifactId>
               <optional>true</optional>
           </dependency>
   
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
                   <!--        add part         -->
                   <configuration>
                       <fork>true</fork>
                   </configuration>
               </plugin>
   ```

2. **修改Settings**

<img src="https://z3.ax1x.com/2021/04/16/cfCmmF.png" style="zoom: 67%;" />

3. **修改Registry（ctrl + shift + alt + /）并勾选compiler.automake.allow.when.app.running**