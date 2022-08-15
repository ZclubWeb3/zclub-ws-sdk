# Message SDK 

## 一、requirements

* Min Android SDK : 14
* JDK: 1.8
* OkHttp: > 3.10.0


## 二、应用协议


### 2.1 客户端发送消息协议

```
"msg_id" : "客户端使用uuid随机生成"
"body" : {}
```

### 2.2 后端返回的消息协议

```json
{
	"status_code": 200, 
	"msg_id" : "后端为每条消息生成一条消息",
	"body" : { }
}
```

> status_code 说明:  200 代表成功, 其他代表错误, 错误消息在 body 中, {"err_msg": "错误消息"}


## 二、设计要点

1. 第一要点为: 状态管理
2. 协议扩展便捷, income、outcome 消息类型的设计与封装
3. log 记录
4. 与UI交互的便捷性、避免内存泄漏
5. 方便单元测试
