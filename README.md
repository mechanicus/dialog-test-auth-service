## Auth-сервис

Auth-сервис предоставляет клиентам REST-API для управления пользователями и сессиями.

## Примеры запросов:
___

создаем пользователя

запрос
```
curl -X POST  --data 'nickname=grey' 'http://localhost:8002/auth/users'
```
ответ
```
{
    "result" : {
        "userId" : "533faa10d0f4645527c3193db27a683f11c202f72c8366210bca6bd001167e34"
    },
    "status" : "OK",
    "code" : 200
}
```

получаем информацию о пользователе

запрос
```
curl -X GET 'http://localhost:8002/auth/user/533faa10d0f4645527c3193db27a683f11c202f72c8366210bca6bd001167e34'
```
ответ
```
{
    "result" : {
        "userId" : "533faa10d0f4645527c3193db27a683f11c202f72c8366210bca6bd001167e34",
        "nickname" : "grey"
    },
    "status" : "OK",
    "code" : 200
}
```

ищем пользователя по имени

запрос
```
curl -X GET 'http://localhost:8002/auth/user/search?nickname=sergei'
```
ответ
```
{
    "result" : {
        "userId" : "116a7daa17488b72232c970bc78b9188592a098a905ada9fac62a72d4ce33a3e",
        "nickname" : "sergei"
    },
    "status" : "OK",
    "code" : 200
}
```

начинаем новую сессию

запрос
```
curl -X POST  --data 'nickname=grey' 'http://localhost:8002/auth/sessions'                                    
```
ответ
```
{
    "result" : {
        "sessionId" : "d4eeb0c7302f3e15daecb89406d3bb8cb46817af8e550c7ca63e9b0ec7c64359"
    },
    "status" : "OK",
    "code" : 200
}
```

завершаем сессию

запрос
```
curl -X DELETE 'http://localhost:8002/auth/session/d4eeb0c7302f3e15daecb89406d3bb8cb46817af8e550c7ca63e9b0ec7c64359'
```
ответ
```
{
    "result" : {
        "stoppedSessions" : 1
    },
    "status" : "OK",
    "code" : 200
}
```
