# java-explore-with-me

https://github.com/Pavel579/java-explore-with-me/pull/1#issue-1447614220

Приложение, позволяющее находить людей для участия в каком-либо мероприятии. Состоит из 2 микросервисов:
1) Основной сервис - поделен на 3 части
  - Публичная - доступна любому пользователю без регистрации
  - Приватная - доступна только аторизованным пользователям
  - Административная - доступна только администраторам

2) Сервис статистики - предназначен для сбора и хранения статистики просмотров событий с различных ip адресов. На основе этих данных можно вести статистику по популярности различных мероприятий.

Запуск приложения осуществляется командой docker-compose up. 
