package io.yak.framework.workflow.engine.support;

import io.yak.framework.workflow.engine.spi.IdGenerator;
import java.util.UUID;

public final class UuidIdGenerator implements IdGenerator {

    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
