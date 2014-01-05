package org.jeecqrs.bundle.jcommondomain.sagas;

import org.jeecqrs.common.event.Event;

/**
 * Provides the ability to identify the saga for a given event.
 * 
 * @param <E>  the actual event type
 */
public interface SagaIdentifier<E extends Event> {

    public String sagaIdFor(E event);
    
}
