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

import net.jodah.typetools.TypeResolver;
import org.jeecqrs.common.event.Event;
import org.jeecqrs.sagas.Saga;

/**
 * A saga that receives exactly one event.
 * @param <S>  the actual saga type
 */
public abstract class AbstractSingleEventSaga<S extends Saga<Event>, E extends Event> extends AbstractSaga<S> {

    protected abstract void when(E event);

    @Override
    protected void setupSaga() {
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(AbstractSingleEventSaga.class, getClass());
        Class<E> eventClass = (Class) typeArguments[1];
        if (TypeResolver.Unknown.class.equals(eventClass))
            throw new IllegalStateException("Event type parameter missing on " + getClass().getSimpleName());
        listenTo(eventClass, new SagaIdentifier<E>() {
            @Override
            public String sagaIdFor(E event) {
                return event.id().toString();
            }
        });
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
