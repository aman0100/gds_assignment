# gds_assignment

# User

@Post -> http://localhost:8080/api/v1/user/add

## Request Body

```
{
    "username": "user1"
}
```

## Response

```
{
    "id": 1,
    "username": "user1",
    "smartCard": {
        "id": 1,
        "balance": 10.0
    }
}
```

# Smart Card

@Post -> http://localhost:8080/api/v1/smart-card/swipe-in

## Request

```
{
    "smartCardId": 1
}
```

## Response

```
{
    "balance": 10.0,
    "message": "You may enter. Enjoy the ride!"
}
```

@Post -> http://localhost:8080/api/v1/smart-card/swipe-out

## Request

```
{
    "smartCardId": 1,
    "startStop": "stop_1",
    "lastStop": "stop_2"
}
```

## Response

```
{
    "balance": 0.0,
    "message": "You may leave!"
}
```