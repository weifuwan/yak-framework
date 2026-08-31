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
- `GET /page?...&startTime=1788163200000&endTime=1788249599999`
- `GET /detail?id=1`
- `POST /mark-read` with `{ "id": 1 }`
- `POST /batch-read` with `{ "ids": [1, 2] }`

`startTime` and `endTime` use Unix epoch milliseconds.

All read/detail/update operations are scoped to the authenticated user and the Projects the user can currently access.

## Message model

A message can carry:

- `type`: business category such as `TASK`, `QUALITY`, `SECURITY`, `SYSTEM`
- `level`: `INFO`, `SUCCESS`, `WARNING`, `ERROR`
- `scope`: `SYSTEM` or `PROJECT`
- `projectId`: Project owner; null means a system message
- `sourceType + sourceId`: weak link to the originating business object
- `actionPath`: optional frontend navigation target
- `summary/content`: list copy and full detail
- `readTag/readTime`: read state
- `oplogId`: optional security-operation-log link

### Project ownership invariant

`projectId` is the security boundary and the single source of truth for ownership:

- `projectId == null` -> `scope=SYSTEM`
- `projectId != null` -> `scope=PROJECT`

The service always recalculates `scope` before persistence. Callers cannot persist `SYSTEM + projectId` or `PROJECT + null projectId` combinations.

Authorization queries also use `project_id`, not `message_scope`. This keeps malformed or historical scope labels from bypassing Project access checks.

## Read visibility

When `projectId` is supplied to `/page`:

- the Project must be accessible to the current user;
- the result contains system messages plus messages for that Project.

When `projectId` is omitted:

- root users can read all messages addressed to them;
- ordinary users can read system messages plus messages for Projects they can currently access;
- messages belonging to Projects from which the user has been removed are no longer returned.

The same Project visibility rule applies to detail, read mutations, legacy list/switch endpoints, and unread counts.

## Publishing

Business modules should depend on `NotificationPublisher`, not the message DAO. The default publisher delegates to `MessageService`, so persistence and normalization remain owned by `yak-security`.

```java
notificationPublisher.publish(messageDTO);
```

Publishing normalization:

- `type`: `SECURITY` when an operation log is linked, otherwise `SYSTEM` when omitted; supplied values are normalized to upper case
- `level`: defaults to `INFO`; supported values are `INFO`, `SUCCESS`, `WARNING`, `ERROR`
- `scope`: always derived from `projectId`
- `readTag`: defaults to `false`

Business modules should publish meaningful user-facing events rather than every successful execution. Typical candidates are task failures, quality exceptions, access changes, and system announcements.
