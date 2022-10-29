# java-explore-with-me
Приложение Explore With Me представляет собой сервис, который позволяет находить интересные мероприятия вокруг себя, а также размещать свои.

## Описание:
Архитектура приложения состоит из двух модулей: 
1) Основной сервис (main-service) - содержит всё необходимое для работы продукта.
2) Сервис статистики (stat-service) - хранит количество просмотров и позволяет делать различные выборки для анализа работы приложения.

### Основной сервис:
API основного сервиса состоит из трех частей:
1) Публичная, доступна без регистрации любому пользователю сети, предоставляет возможности поиска и фильтрации событий.

   Примеры запросов в публичной части API:
   * ```GET /events``` - получение событий с возможностью фильтрации.
   * ```GET /events/{id}``` - получение подробной информации об опубликованном событии по его идентификатору.
   * ```GET /compilations``` - получение подборок событий.
2) Закрытая, доступна только авторизованным пользователям. Эта часть API призвана реализовать возможности зарегистрированных пользователей продукта.

   Примеры запросов в закрытой части API:
    * ```GET /users/{userId}/events``` - получение событий, добавленных текущим пользователем.
    * ```POST /users/{userId}/events``` - добавление нового события.
    * ```PATCH /users/{userId}/events/{eventId}``` - отмена события, добавленного текущим пользователем.
3) Административная, для администраторов сервиса. Административная часть API предоставляет возможности настройки и поддержки работы сервиса.

    Примеры запросов в административной части API:
    * ```POST /admin/users``` - добавление нового пользователя.
    * ```DELETE /admin/compilations/{compId}``` - удаление подборки.
    * ```PATCH /admin/compilations/{compId}/events/{eventId}``` - добавить событие в подборку.
### Сервис статистики:
Второй сервис, статистики, призван собирать информацию. Во-первых, о количестве обращений пользователей к спискам событий и, во-вторых, о количестве запросов к подробной информации о событии. На основе этой информации формируется статистика о работе приложения.

Примеры запросов к API сервиса статистики:
   * ```POST /hit``` - сохранение информации о том, что к эндпойнту был запрос.
   * ```GET /stats``` - получение статистики по посещениям.
## Полная спецификация:
   * [Основной сервис](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json)
   * [Сервис статистики](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json)
## ER диаграмма:
   
## Развертывание:
Развертывание приложения осуществляется через docker-compose в 4 docker контейнерах:
   * Контейнер основного сервиса.
   * Контейнер сервиса статистики.
   * Контейнер БД (postgresql) основного сервиса.
   * Контейнер БД (postgresql) сервиса статистики.





