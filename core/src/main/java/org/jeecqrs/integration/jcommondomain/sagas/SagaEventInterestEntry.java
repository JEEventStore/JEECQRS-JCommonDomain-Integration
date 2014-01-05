package org.jeecqrs.integration.jcommondomain.sagas;

import org.jeecqrs.common.event.Event;

/**
 * Specifies an interest of a a saga into a given event.
 * Extends {@link SagaIdentifier} to provide the means to identify
 * the corresponding saga.
 */
public interface SagaEventInterestEntry<E extends Event> extends SagaIdentifier<E> {

    Class<E> eventClass();
    
}
