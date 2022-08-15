# Message SDK 

> message-sdk is part of the zclub app, which uses the WebSocket network protocol to interact with the server. 
> The message-sdk is used to control all kinds of requests from the zclub voice chat room, 
> allowing for good interaction between multiple users of the voice chat room.

## 一、Requirements

* Min Android SDK API : 9
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

// 3. send message to websocket server
JsonRequestMessage message = new JsonRequestMessage("zclub_room_01", "user_007",
                "user_friend_01", "apply_mic");
mMessageSDK.sendMessage(message)
                
```

