# Запуск
Наиболее удобен запуск из среды разработки IntelliJ IDEA(в том числе Community edition). Проект базируется на maven,
так что все зависимости будут автоматически подставлены в classpath и в строке запуска.
Отдельно строку запуска из командной строки не привожу, т.к. она слишком большая.

При тестировании приложения, возможно, удобно будет смотреть данные в БД
Поскольку в данном случае выбрана H2 (in-memory), то после запуска приложения можно воспользоваться
консолью:
    http://localhost:8080/h2

Для подключения использовать данные:

    Driver Class: org.h2.Driver
    JDBC URL: jdbc:h2:mem:taskManager;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE;MODE=PostgreSQL;
    User Name: admin
    Password: admin

При загрузке (для удобства тестирования функционала) в ДБ помещены 2 пользователя (user, admin) и 
несколько задач (см. файл db/data.sql)

# Тестирование функциональности
P.S. Для проверки функциональности приведем готовые curl-запросы

### Проверка доступа к контроллеру задач без предварительного логина:
    curl -L -I 'http://localhost:8080/api/tasks/2'
Ожидаемый результат:

    HTTP/1.1 403
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 0
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: SAMEORIGIN
    Date: Tue, 21 Jul 2026 08:46:35 GMT

### Получение токена

    curl -X POST 'http://localhost:8080/api/auth/login' --header 'Content-Type: application/json' --data '{
    \"username\":\"user\",
    \"password\":\"user\"
    }'

Ожидаемый результат:

    {"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MjQyNzMsImV4cCI6MTc4NDcxMDY3M30.OtdZpZKdNIIbjegxLBXxw3vO2AT6MB49jMuwLisTSJA9mAWtFGID4twaHuTzxRBY3l2Tdsh5Fuu_2kP9LWbzYw","username":"user"}
 
P.S. Для вашего запуска этот токен будет другим и именно его мы будем копировать в последующие запросы

### Получение списка имеющихся задач

    curl -L 'http://localhost:8080/api/tasks' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MjkwNzYsImV4cCI6MTc4NDcxNTQ3Nn0.0TJetr4kV7qh0SWucYTIMgNAj63mu_2ghdcy9IrXK1kp6HCmNK3JoS2bojtuyUPG7zzKYaW_v7bC_UQvOc186g'

Ожидаемый результат:

    [
        {
            "id": 1,
            "title": "Creation",
            "description": "Create controller for Rest-full TaskManager service",
            "status": "DONE",
            "priority": "HIGH",
            "author": "admin",
            "assignee": "user"
        },
        {
            "id": 2,
            "title": "Design",
            "description": "Add filters",
            "status": "TODO",
            "priority": "MEDIUM",
            "author": "admin",
            "assignee": null
        },
        {
            "id": 3,
            "title": "Publication",
            "description": "Publish to Git",
            "status": "TODO",
            "priority": "LOW",
            "author": "user",
            "assignee": "user"
        },
        {
            "id": 4,
            "title": "Testing",
            "description": "Add tests",
            "status": "TODO",
            "priority": "LOW",
            "author": "admin",
            "assignee": null
        }
    ]

P.S. Текст ответа отформатирован в данном документе. Данные можно светить через БД-консоль /h2

### Получение списка имеющихся задач с фильтром по статусу

    curl -L 'http://localhost:8080/api/tasks?status=TODO' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MjkwNzYsImV4cCI6MTc4NDcxNTQ3Nn0.0TJetr4kV7qh0SWucYTIMgNAj63mu_2ghdcy9IrXK1kp6HCmNK3JoS2bojtuyUPG7zzKYaW_v7bC_UQvOc186g' 

Ожидаемый результат:

    [
        {
            "id": 2,
            "title": "Design",
            "description": "Add filters",
            "status": "TODO",
            "priority": "MEDIUM",
            "author": "admin",
            "assignee": null
        },
        {
            "id": 3,
            "title": "Publication",
            "description": "Publish to Git",
            "status": "TODO",
            "priority": "LOW",
            "author": "user",
            "assignee": "user"
        },
        {
            "id": 4,
            "title": "Testing",
            "description": "Add tests",
            "status": "TODO",
            "priority": "LOW",
            "author": "admin",
            "assignee": null
        }
    ]

### Получение списка имеющихся задач с фильтром по НЕКОРРЕКТНОМУ статусу

    curl -L 'http://localhost:8080/api/tasks?status=PENDING' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MjkwNzYsImV4cCI6MTc4NDcxNTQ3Nn0.0TJetr4kV7qh0SWucYTIMgNAj63mu_2ghdcy9IrXK1kp6HCmNK3JoS2bojtuyUPG7zzKYaW_v7bC_UQvOc186g'

Ожидаемый результат:

    {"timestamp":"2026-07-21T10:37:49.339+00:00","status":400,"error":"Bad Request","path":"/api/tasks"}

P.S. В задании не оговаривался явно вариант реакций на ошибочные/неточные данные, поэтому использован такой подход

### Получение списка имеющихся задач с фильтром по автору

    curl -L 'http://localhost:8080/api/tasks?authorId=1' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MjkwNzYsImV4cCI6MTc4NDcxNTQ3Nn0.0TJetr4kV7qh0SWucYTIMgNAj63mu_2ghdcy9IrXK1kp6HCmNK3JoS2bojtuyUPG7zzKYaW_v7bC_UQvOc186g'

Ожидаемый результат:

    [{"id":3,"title":"Publication","description":"Publish to Git","status":"TODO","priority":"LOW","author":"user","assignee":"user"}]

### Получение списка имеющихся задач с фильтром по назначенному

    curl -L 'http://localhost:8080/api/tasks?assigneeId=1' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MjkwNzYsImV4cCI6MTc4NDcxNTQ3Nn0.0TJetr4kV7qh0SWucYTIMgNAj63mu_2ghdcy9IrXK1kp6HCmNK3JoS2bojtuyUPG7zzKYaW_v7bC_UQvOc186g'

Ожидаемый результат:

    [
        {
            "id": 1,
            "title": "Creation",
            "description": "Create controller for Rest-full TaskManager service",
            "status": "DONE",
            "priority": "HIGH",
            "author": "admin",
            "assignee": "user"
        },
        {
            "id": 3,
            "title": "Publication",
            "description": "Publish to Git",
            "status": "TODO",
            "priority": "LOW",
            "author": "user",
            "assignee": "user"
        }
    ]

### Получение списка имеющихся задач со всеми фильтрами сразу

    curl -L 'http://localhost:8080/api/tasks?status=TODO&assigneeId=1&authorId=1' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzExODEsImV4cCI6MTc4NDcxNzU4MX0.0JtuE9cO7-fvvL1g7q0mhnEUtS6ddKbIutIF6yELo6Nrtzv2CI_Hl1MVgm3SdxuCdu1hdZd7D8EGdvHkEURHRg'

Ожидаемый результат:

    [{"id":3,"title":"Publication","description":"Publish to Git","status":"TODO","priority":"LOW","author":"user","assignee":"user"}]

### Создание задачи от имени user

    curl -X POST 'http://localhost:8080/api/tasks' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzQxNjAsImV4cCI6MTc4NDcyMDU2MH0.PjoRqWb_3yEzL-ImSs5TnBmLBTJPt_kysfYYjqv46bEntZvorPCItOsrYUidn3LsSCBeRWZ4WdetVnTvWShJNw' --header 'Content-Type: application/json' --data '{
    \"title\":\"Documentation\",
    \"description\":\"Create README.md\",
    \"status\":\"IN_PROGRESS\",
    \"priority\":\"MEDIUM\",
    \"assignee\":\"admin\"
    }'

Ожидаемый результат:

    {"id":5,"title":"Documentation","description":"Create README.md","status":"IN_PROGRESS","priority":"MEDIUM","author":"user","assignee":"admin"}

P.S. Обратите внимание, что автор проставлен автоматически - им взят тот пользователь, который проводит данную операцию

### Создание задачи от имени user со значениями по умолчанию(atatus = TODO, priority = LOW)

    curl -X POST 'http://localhost:8080/api/tasks' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzUyNDYsImV4cCI6MTc4NDcyMTY0Nn0.gZSyodj9jyKmnVePeoZzXlMC-fpfg9saCFnIs3nTxw319ooRZRz9bsilcu3NXQlr4A0LeO3d491-Vwxl53dpyg' --header 'Content-Type: application/json' --data '{
    \"title\":\"JWT\",
    \"description\":\"Add JWT token system\"
    }'

Ожидаемый результат:

    {"id":6,"title":"JWT","description":"Add JWT token system","status":"TODO","priority":"LOW","author":"user","assignee":null}

### Редактирование задачи от имени user(меняем приоритет на HIGH)

    curl -X PUT 'http://localhost:8080/api/tasks/6' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzUyNDYsImV4cCI6MTc4NDcyMTY0Nn0.gZSyodj9jyKmnVePeoZzXlMC-fpfg9saCFnIs3nTxw319ooRZRz9bsilcu3NXQlr4A0LeO3d491-Vwxl53dpyg' --header 'Content-Type: application/json' --data '{
    \"title\":\"JWT\",
    \"description\":\"Add JWT token system\",
    \"status\":\"TODO\",
    \"priority\":\"HIGH\"
    }'

Ожидаемый результат:

    {"id":6,"title":"JWT","description":"Add JWT token system","status":"TODO","priority":"HIGH","author":"user","assignee":null}

## Проверка доступов по ролям
### Попытка удаления "чужой" задачи от имени user(task_id = 1 создана пользователем admin)

    curl -X DELETE 'http://localhost:8080/api/tasks/1' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzUyNDYsImV4cCI6MTc4NDcyMTY0Nn0.gZSyodj9jyKmnVePeoZzXlMC-fpfg9saCFnIs3nTxw319ooRZRz9bsilcu3NXQlr4A0LeO3d491-Vwxl53dpyg'

Ожидаемый результат:

    {"timestamp":"2026-07-21T12:11:37.552+00:00","status":403,"error":"Forbidden","path":"/api/tasks/1"}

### Попытка удаления "своей" задачи от имени user(task_id = 6)

    curl -X DELETE 'http://localhost:8080/api/tasks/6' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzUyNDYsImV4cCI6MTc4NDcyMTY0Nn0.gZSyodj9jyKmnVePeoZzXlMC-fpfg9saCFnIs3nTxw319ooRZRz9bsilcu3NXQlr4A0LeO3d491-Vwxl53dpyg'

Ожидаемый результат:

 - пустой ответ со статусом 200

 Проверка(список всех задач, ищем (и не находим) 6):

    curl -L 'http://localhost:8080/api/tasks' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzUyNDYsImV4cCI6MTc4NDcyMTY0Nn0.gZSyodj9jyKmnVePeoZzXlMC-fpfg9saCFnIs3nTxw319ooRZRz9bsilcu3NXQlr4A0LeO3d491-Vwxl53dpyg'

Ожидаемый результат:

    [
        {
            "id": 1,
            "title": "Creation",
            "description": "Create controller for Rest-full TaskManager service",
            "status": "DONE",
            "priority": "HIGH",
            "author": "admin",
            "assignee": "user"
        },
        {
            "id": 2,
            "title": "Design",
            "description": "Add filters",
            "status": "TODO",
            "priority": "MEDIUM",
            "author": "admin",
            "assignee": null
        },
        {
            "id": 3,
            "title": "Publication",
            "description": "Publish to Git",
            "status": "TODO",
            "priority": "LOW",
            "author": "user",
            "assignee": "user"
        },
        {
            "id": 4,
            "title": "Testing",
            "description": "Add tests",
            "status": "TODO",
            "priority": "LOW",
            "author": "admin",
            "assignee": null
        },
        {
            "id": 5,
            "title": "Documentation",
            "description": "Create README.md",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM",
            "author": "user",
            "assignee": "admin"
        }
    ]

### Попытка удаления "чужой" задачи от имени admin

Логинимся под админом:

    curl -X POST 'http://localhost:8080/api/auth/login' --header 'Content-Type: application/json' --data '{
    \"username\":\"admin\",
    \"password\":\"admin\"
    }'

Ожидаемый результат:

    {"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc4NDYzNjI5MiwiZXhwIjoxNzg0NzIyNjkyfQ.PE-tciIHZ_cwmD1JWCBQt6-gjJD0sFWbetXPNpIKblUYyYT0F4dAhTHE2syk7x7SRqZ2PJJrYBA-zJe18UuUFA","username":"admin"}

Удаляем задачу (task_id = 5, созданную user):

    curl -X DELETE 'http://localhost:8080/api/tasks/5' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc4NDYzNjI5MiwiZXhwIjoxNzg0NzIyNjkyfQ.PE-tciIHZ_cwmD1JWCBQt6-gjJD0sFWbetXPNpIKblUYyYT0F4dAhTHE2syk7x7SRqZ2PJJrYBA-zJe18UuUFA'

Проверяем, выводя список(ищем ( и не находим) 5):

    curl -L 'http://localhost:8080/api/tasks' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc4NDYzNjI5MiwiZXhwIjoxNzg0NzIyNjkyfQ.PE-tciIHZ_cwmD1JWCBQt6-gjJD0sFWbetXPNpIKblUYyYT0F4dAhTHE2syk7x7SRqZ2PJJrYBA-zJe18UuUFA'

Ожидаемый результат:

    [
        {
            "id": 1,
            "title": "Creation",
            "description": "Create controller for Rest-full TaskManager service",
            "status": "DONE",
            "priority": "HIGH",
            "author": "admin",
            "assignee": "user"
        },
        {
            "id": 2,
            "title": "Design",
            "description": "Add filters",
            "status": "TODO",
            "priority": "MEDIUM",
            "author": "admin",
            "assignee": null
        },
        {
            "id": 3,
            "title": "Publication",
            "description": "Publish to Git",
            "status": "TODO",
            "priority": "LOW",
            "author": "user",
            "assignee": "user"
        },
        {
            "id": 4,
            "title": "Testing",
            "description": "Add tests",
            "status": "TODO",
            "priority": "LOW",
            "author": "admin",
            "assignee": null
        }
    ]

### Регистрация пользователя

    curl -X POST 'http://localhost:8080/api/auth/register' --header 'Content-Type: application/json' --data '{
    \"username\":\"cruz\",
    \"email\":\"t.cruz@gmail.com\",
    \"password\":\"cruz\"
    }'

Ожидаемый результат:

    {"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjcnV6Iiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzY3NTksImV4cCI6MTc4NDcyMzE1OX0.g8n-JiDf4ABeDgNaAyAqr8F2EIB2NZQ4ll-fYXyLetQkXaiY9SK_6MMWuU7xNpVex4ZtTJY7zHeJ5pnx3_7bVQ","username":"cruz"}

Проверка:

    curl -L 'http://localhost:8080/api/tasks' --header 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjcnV6Iiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ2MzY3NTksImV4cCI6MTc4NDcyMzE1OX0.g8n-JiDf4ABeDgNaAyAqr8F2EIB2NZQ4ll-fYXyLetQkXaiY9SK_6MMWuU7xNpVex4ZtTJY7zHeJ5pnx3_7bVQ'

Ожидаемый результат:

    [
        {
            "id": 1,
            "title": "Creation",
            "description": "Create controller for Rest-full TaskManager service",
            "status": "DONE",
            "priority": "HIGH",
            "author": "admin",
            "assignee": "user"
        },
        {
            "id": 2,
            "title": "Design",
            "description": "Add filters",
            "status": "TODO",
            "priority": "MEDIUM",
            "author": "admin",
            "assignee": null
        },
        {
            "id": 3,
            "title": "Publication",
            "description": "Publish to Git",
            "status": "TODO",
            "priority": "LOW",
            "author": "user",
            "assignee": "user"
        },
        {
            "id": 4,
            "title": "Testing",
            "description": "Add tests",
            "status": "TODO",
            "priority": "LOW",
            "author": "admin",
            "assignee": null
        }
    ]

