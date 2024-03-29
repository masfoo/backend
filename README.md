### Цель проекта 

Разработка микросервиса, который предоставит системным администраторам функционал для оптимизации и упрощения массовых действий над пользователями в midPoint с помощью различных фильтров.


### Предметная область проекта

В обязанности системного администратора входит множество рутинных задач, время на выполнение которых может быть как небольшим, так и существенным. Процесс выполнения некоторых задач возможно оптимизировать, что поможет сконцентрировать внимание системного администратора на решении более важных для компании задач.


### Существующая проблема

Часто случается, что системному администратору необходимо совершать какие либо действия над пользователями внутри проекта. Приходится вручную выполнять большое количество однотипных действий для добавления/изменений прав доступа группы пользователей, выдача ролей и назначение ресурсов. 

 
### Предполагаемое решение

Микросервис, который будет автоматизировать работу с пользователями, а именно: удаление пользователей, назначение ролей.

 
### Характеристика продукта

Микросервис, разрабатываемый в рамках этого проекта, предоставляет системным администраторам  функционал для массовых операций над пользователями с различными параметрами в midPoint. Массовые операции - это некоторый фильтр, который отбирает пользователей по заданным параметрам.

Системные администраторы могут осуществлять следующие массовые операции:

удалить пользователей 

выдать роли пользователям

назначать ресурсы пользователям


### Описание работы продукта

Системный администратор открывает тикет в Jira. В нем создает подзадачу, ее название - это операция, которую нужно совершить, а описание - определенный фильтр в формате XML – параметры, по которым будут отобраны пользователи. Например, имя пользователя начинается на определенную букву. После создания подзадача получает статус - “новая”.

Микросервис каждую минуту сканирует Jira на наличие подзадачи со статусом “новый“. Получает название операции и описание фильтра. Меняется статус подзадачи в Jira на “в процессе“. После этого отправляет запрос в midPoint.

В Docker запущен  midPoint. Происходит выполнение операции: через query - это rest API запрос с фильтром в формате XML - получаем OID (Object identifier) пользователей с указанными параметрами. К  выбранным пользователям по OID с помощью PATCH запроса применяются изменения.

После завершения этих действий происходит смена статуса статус подзадачи в Jira на “готово“.

### Стек
Код написан на Java с использованием фреймворка spring. В проекте используется sonar для проверки кода и приведения его вида к общему стандарту для всей команды. Также присутствуют unit тесты.

 
### Требования к программному решению

Роли:

Системный администратор - пользователь микросервиса, который создает запрос на операцию над пользователями системы с определенным фильтром в Jira.

Функциональные требования для роли системный администратор:

Создание запроса на операцию с определенным фильтром в Jira

В тикете Jira создать подзадачу с нужной операцией и фильтром в формате XML, составленным системным администратором по шаблону.


### Архитектура

Основной единицей представления задач в приложении являются объекты класса Ticket (далее тикет). Они хранятся в базе данных, а создаются классом JiraService . JiraService каждую минуту получает информацию о новых подзадачах некоторой родительской задачи Jira, ключ которой указан в конфигурации приложения. Каждая подзадача задаёт конкретную массовую операцию и её конкретные аргументы. После сохранения новых тикетов в базу данных, JiraService отправляет event с новыми тикетами на исполнение классу MidpointService .

Другие функции класса JiraService - изменение информации о завершённых тикетах в Jira. При получении от MidpointService event о завершённых тикетах, он сохраняет новую информацию в базу данных и добавляет комментарий к соответствующей подзадаче в Jira. Если Jira недоступна, то каждые две минуты JiraService пытается повторить отправку результатов для всех завершённых тикетов.

Все взаимодействия JiraService с Jira происходят через класс JiraClient, который описывает используемые запросы к Jira API:

Get-запросы:

получение задачи по ключу

получение списка подзадач задачи

получение доступных статусов для переход

Post-запросы:

изменение статуса задачи

добавление комментария к задаче

MidpointService ожидает event о новых тикетах в приложении. У него есть map, где ключ — это операция, значение — это класс, её исполняющий. MidpointService исполняет соответствующую тикету операцию из map, либо задаёт тикету состояние FAILED в случае указания неизвестной операции в тикете. Результат работы операции сохраняется в тикете и создаётся event о завершении выполнения.

Также MidpointService каждые две минуты запрашивает у базы данных список тикетов со статусом MIDPOINT_DOESNT_RESPONSE и пробует заново их исполнить.

MidpointService взаимодействует с MidpointOperation, от которого наследуются SetRoleOperation, SetResourceOperation и DeleteOperation. MidpointOperation взаимодействует с  MidpointClient.

Все взаимодействия с Midpoint происходят через класс MidpointClient, который описывает используемые запросы к Midpoint API:

Поиск пользователей по фильтру

Поиск роли (для получения oid)

Поиск ресурса (для получения oid)

Удаление пользователя по oid

Назначение роли по oid пользователя и oid роли

Назначение ресурса по oid пользователя и oid ресурса

Классы MidpointService и JiraService не зависят друг от друга и взаимодействуют только через events.

Возможные статусы тикетов:

COMPLETED - завершённый тикет, результат отправлен в Jira

JIRA_DOESNT_RESPONSE - завершённый тикет, происходит попытка отправки результата в Jira через shceduled-метод JiraService

MIDPOINT_DOESNT_RESPONSE - новый тикет, происходит попытка выполенния через shceduled-метод MidpointService

TO_MIDPOINT - новый тикет, происходит выполение через event-listener-метод MidpointService

TO_JIRA - завершённый тикет (успешный), результат будет отправляться в Jira через event-listener-метод JiraService

FAILED - завершённый тикет (неуспешный), результат будет отправляться в Jira через event-listener-метод JiraService
