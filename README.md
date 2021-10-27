# job4j_chat

[![Build Status](https://app.travis-ci.com/elvolt/job4j_chat.svg?branch=master)](https://app.travis-ci.com/elvolt/job4j_chat)

## О проекте
Проект представляет собой REST API чата с комнатами.

### Используемые технологии
- Spring (Boot, Data JPA, Web, Security (JWT))
- PostgreSQL
- Maven
- Jackson
- ModelMapper

## REST API

### для всех пользователей:

<table>
    <thead>
        <th>Команда</th>
        <th>Запрос</th>
        <th>Ответ</th>
    </thead>
    <tbody>
        <tr>
            <td>Регистрация</td>
            <td>POST https://sitename/person/sign-up<br> 
                Body: {name: "Pavel", login: "pavel_123", password: "123456"}</td>
            <td>Код состояния: 200</td>
        </tr>
        <tr>
            <td>Авторизация</td>
            <td>POST https://sitename/login<br> 
                Body: {login: "pavel_123" , password: "123456"}</td>
            <td>Код состояния: 200 OR 403<br> Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q..."</td>
        </tr>
    </tbody>
</table>

### для авторизованного пользователя

#### messages
<table>
    <thead>
        <th>Команда</th>
        <th>Запрос</th>
        <th>Ответ</th>
    </thead>
    <tbody>
        <tr>
            <td>Получить все сообщения</td>
            <td>GET https://sitename/message/<br> 
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q..."</td>
            <td>Код состояния:200<br> 
                Header: "Content-Type: application/json"<br> 
                Body: [{"id":5,"text":"Сообщение 1","created":"2021-10-19T09:50:58.350542","personId":1,"roomId":1},...]</td>
        </tr>
        <tr>
            <td>Получить сообщение по id</td>
            <td>GET https://sitename/message/5<br> 
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q..."</td>
            <td>Код состояния:200<br>
                Header: "Content-Type: application/json"<br> 
                Body: {"id":5,"text":"Сообщение 1","created":"2021-10-19T09:50:58.350542","personId":1,"roomId":1}</td>
        </tr>
        <tr>
            <td>Создать новое сообщение</td>
            <td>POST https://sitename/message/<br> 
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q...,<br>
                "Content-Type: application/json",<br>
                Body: {"text":"Новое сообщение","personId":1,"roomId":2"</td>
            <td>Код состояния:201<br> 
                Header: "Content-Type: application/json"<br> 
                Body: {"id":12,"text":"Новое сообщение","created":"2021-10-19T17:21:44.8859788","personId":1,"roomId":2}</td>
        </tr>
        <tr>
            <td>Обновить сообщение (вариант 1)</td>
            <td>PUT https://sitename/message/<br>
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q...,<br>
                "Content-Type: application/json",<br> 
                Body: {"id":12, "text":"Новое сообщение","personId":1,"roomId":2"</td>
            <td>Код состояния: 200</td>
        </tr>
        <tr>
            <td>Обновить сообщение (вариант 2)</td>
            <td>PATCH https://sitename/message/<br>
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q...,<br>
                "Content-Type: application/json",<br> 
                Body: {"id":12, "text":"Новое сообщение"}</td>
            <td>Код состояния: 200</td>
        </tr>
        <tr>
            <td>Удалить сообщение по id</td>
            <td>DELETE https://sitename/message/5<br> 
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q...</td>
            <td>Код состояния: 200</td>
        </tr>
    </tbody>
</table>

#### rooms
<table>
    <thead>
        <th>Команда</th>
        <th>Запрос</th>
        <th>Ответ</th>
    </thead>
    <tbody>
        <tr>
            <td>Получить список всех комнат</td>
            <td>GET https://sitename/room/<br> 
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q..."</td>
            <td>Код состояния:200<br>
                Header: "Content-Type: application/json"<br>
                Body: [{"id":1,"name":"Room1"},...]</td>
        </tr>
        <tr>
            <td>Получить комнату по id (со списком сообщений)</td>
            <td>GET https://sitename/room/1<br> 
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q..."</td>
            <td>Код состояния:200<br>
                Header: "Content-Type: application/json"<br>
                Body: {"id":1,"name":"Room1",
                "messages":[{"id":1,"text":"Сообщение 1","created":"2021-10-19T10:05:11.019532",
                    "person":{"id":2,"name":"user 2","login":"user2","role":{"id":1,"authority":"ROLE_USER"}},"room":1},...]}
            </td>
        </tr>
        <tr>
            <td>Создать новую комнату</td>
            <td>POST https://sitename/room/<br>
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q...,<br>
                "Content-Type: application/json",<br> 
                Body: {"name":"Новая комната"}</td>
            <td>Код состояния:201<br>
                Header: "Content-Type: application/json"<br> 
                Body: {"id":4,"name":"Новая комната","messages":[]}</td>
        </tr>
        <tr>
            <td>Обновить комнату (вариант 1)</td>
            <td>PUT https://sitename/room/<br>
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q...,<br>
                "Content-Type: application/json", <br>
                Body: {"id":4, "name":"Обновленная комната"}</td>
            <td>Код состояния: 200</td>
        </tr>
        <tr>
            <td>Обновить комнату (вариант 2)</td>
            <td>PATCH https://sitename/room/<br> 
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q...,<br>
                "Content-Type: application/json",<br> 
                Body: {"id":4, "name":"Обновленная комната"}</td>
            <td>Код состояния: 200</td>
        </tr>
        <tr>
            <td>Удалить комнату по id</td>
            <td>DELETE https://sitename/room/4<br>
                Header: "Authorization: Bearer eyJ0eXAiOiJKV1Q...</td>
            <td>Код состояния: 200</td>
        </tr>
    </tbody>
</table>