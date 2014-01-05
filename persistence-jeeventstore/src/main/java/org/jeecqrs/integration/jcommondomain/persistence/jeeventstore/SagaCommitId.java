package org.jeecqrs.integration.jcommondomain.persistence.jeeventstore;

import org.jeecqrs.common.AbstractId;

/**
 *
 */
public class SagaCommitId extends AbstractId<SagaCommitId> {

    public SagaCommitId(String idString) {
        super(idString);
    }
}
