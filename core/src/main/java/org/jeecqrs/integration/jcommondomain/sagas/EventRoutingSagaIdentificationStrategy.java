/*
 * Copyright (c) 2013 Red Rainbow IT Solutions GmbH, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.jeecqrs.integration.jcommondomain.sagas;

import org.jeecqrs.common.event.Event;
import org.jeecqrs.common.event.routing.EventRouter;
import org.jeecqrs.common.event.routing.convention.ConventionEventRouter;
import org.jeecqrs.common.util.Validate;
import org.jeecqrs.sagas.Saga;
import org.jeecqrs.sagas.SagaIdentificationStrategy;

/**
 *
 */
public abstract class EventRoutingSagaIdentificationStrategy<S extends Saga<Event>>
    implements SagaIdentificationStrategy<S, Event> {
    
    private static final String EVENT_HANDLER_NAME = "when";
    
    private final EventRouter<String, Event> eventRouter;

    protected EventRoutingSagaIdentificationStrategy() {
        this(new ConventionEventRouter<String, Event>(true, EVENT_HANDLER_NAME));
    }

    @SuppressWarnings("LeakingThisInConstructor")
    protected EventRoutingSagaIdentificationStrategy(
            EventRouter<String, Event> eventRouter) {
        Validate.notNull(eventRouter, "eventRouter must not be null");
        this.eventRouter = eventRouter;
        eventRouter.register(this);
    }

    @Override
    public String identifySaga(Event event) {
        return eventRouter.routeEvent(event);
    }

}
