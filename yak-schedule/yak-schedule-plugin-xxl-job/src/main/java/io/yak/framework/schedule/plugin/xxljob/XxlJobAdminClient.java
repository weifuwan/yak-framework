package io.yak.framework.schedule.plugin.xxljob;

import java.util.Map;
import java.util.Optional;

/** XXL-JOB 管理端适配接口，可由业务系统覆盖以适配定制认证或版本。 */
public interface XxlJobAdminClient {
    String create(Map<String, String> form);

    void update(String id, Map<String, String> form);

    void delete(String id);

    void pause(String id);

    void resume(String id);

    void trigger(String id, String executorParam);

    Optional<XxlJobAdminTask> find(String id, int jobGroupId);
}
