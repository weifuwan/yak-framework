# Message Center Contract

`yak-security` exposes a user-scoped notification inbox under:

`/yak-security/api/v1/message`

## Compatibility

Legacy endpoints remain available:

- `GET /list`
- `GET /list/{readTag}`
- `PUT /switch`
- `GET /unread-count`

New message-center endpoints:

- `GET /page?pageNum=1&pageSize=10&status=UNREAD&type=TASK&projectId=1`
- `GET /detail?id=1`
- `POST /mark-read` with `{ "id": 1 }`
- `POST /batch-read` with `{ "ids": [1, 2] }`

All read/detail/update operations are scoped to the authenticated user.

## Message model

A message can carry:

- `type`: business category such as `TASK`, `QUALITY`, `SECURITY`, `SYSTEM`
- `level`: `INFO`, `SUCCESS`, `WARNING`, `ERROR`
- `scope`: `SYSTEM` or `PROJECT`
- `projectId`: project owner when `scope=PROJECT`
- `sourceType + sourceId`: weak link to the originating business object
- `actionPath`: optional frontend navigation target
- `summary/content`: list copy and full detail
- `readTag/readTime`: read state
- `oplogId`: optional security-operation-log link

When `projectId` is supplied to the page query, the result contains system messages plus messages for that project. Omitting `projectId` returns all messages owned by the current user.

## Publishing

Business modules should depend on `NotificationPublisher`, not the message DAO. The default publisher delegates to `MessageService`, so persistence and defaulting remain owned by `yak-security`.

```java
notificationPublisher.publish(messageDTO);
```

Missing metadata is normalized as follows:

- `type`: `SECURITY` when an operation log is linked, otherwise `SYSTEM`
- `level`: `INFO`
- `scope`: `PROJECT` when `projectId` exists, otherwise `SYSTEM`
- `readTag`: `false`

Business modules should publish meaningful user-facing events rather than every successful execution. Typical candidates are task failures, quality exceptions, access changes, and system announcements.
