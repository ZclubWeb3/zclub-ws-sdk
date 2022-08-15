# Message SDK 

## 一、Requirements

* Min Android SDK : 14
* JDK: 1.8
* OkHttp: > 3.10.0


## 二、Configuration

```
// your websocket server url
private static final String ECHO_SERVER_WS_UR = "ws://echo.websocket.org/" ;
   
// 1. config message sdk
MessageSDK mMessageSDK = new MessageSDK.Builder(ECHO_SERVER_WS_UR)
                .messageDispatcher(new ZClubMessageDispatcher())
                .messageLogger(new DefaultMessageLogger())
                .build();
// 2. connect to websocekt server                
mMessageSDK.connect();
```

