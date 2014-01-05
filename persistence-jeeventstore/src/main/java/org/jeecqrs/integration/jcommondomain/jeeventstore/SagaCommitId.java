package org.jeecqrs.integration.jcommondomain.jeeventstore;

import org.jeecqrs.common.AbstractId;

/**
 *
 */
public class SagaCommitId extends AbstractId<SagaCommitId> {

    public SagaCommitId(String idString) {
        super(idString);
    }
}
