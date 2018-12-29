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
import org.jeecqrs.common.sagas.AbstractEventSourcedSaga;
import org.jeecqrs.common.sagas.SagaCommandBus;
import org.jeecqrs.common.sagas.SagaTimeoutProvider;
import org.jeecqrs.common.util.ReflectionUtils;
import org.jeecqrs.event.EventInterestBuilder;
import org.jeecqrs.sagas.*;
import org.jeecqrs.sagas.config.SagaConfigBuilder;
import org.jeecqrs.sagas.config.autodiscover.SagaConfigProvider;
import org.jeecqrs.sagas.registry.autodiscover.RegisterSaga;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <S>  the actual saga type
 */
public abstract class AbstractSaga<S extends Saga<Event>> extends AbstractEventSourcedSaga
        implements Saga<Event>, RegisterSaga<S, Event>, SagaConfigProvider<S, Event>  {

    private String sagaId;

    @Inject // only available upon registration of the saga in the system
    private SagaCommandBus commandBus;

    @Inject // only available upon registration of the saga in the system
    private SagaTimeoutProvider timeoutProvider;

    private final List<SagaEventInterestEntry<? extends Event>> registeredEvents = new ArrayList<>();

    /**
     * Setup the saga instance.
     * This function is supposed to call {@code #listenTo()} for all events
     * that this saga shall listen to.
     */
    protected abstract void setupSaga();

    /**
     * Listen to events.
     * Can only be called with static methods when the generic type
     * is propagated to the {@code Sagaidentifier} (i.e., no dynamic generation).
     * @param <T>
     * @param idf 
     */
    protected final <T extends Event> void listenTo(SagaIdentifier<T> idf) {
        if (idf == null)
            throw new NullPointerException("SagaIdentifier must not be null");
        Class<? extends Event> eventClass = (Class) ReflectionUtils.findSuperClassParameterType(idf, SagaIdentifier.class, 0);
        if (eventClass == null) {
            throw new IllegalStateException("Event type parameter missing on "
                    + SagaIdentifier.class.getSimpleName() + " for #listenTo() in class "
                    + getClass().getName());
        }
        this.listenTo((Class) eventClass, idf);
    }

    protected final <T extends Event> void listenTo(final Class<T> eventClass, final SagaIdentifier<T> ssei) {
        if (ssei == null)
            throw new NullPointerException("SagaSingleEventInterest must not be null");
        this.registeredEvents.add(new SagaEventInterestEntry<T>() {
            @Override
            public Class<T> eventClass() {
                return eventClass;
            }
            @Override
            public String sagaIdFor(T event) {
                return ssei.sagaIdFor(event);
            }
        });
    }

    protected SagaFactory<S> sagaFactory() {
        return new DefaultSagaFactory(this.getClass(), commandBus, timeoutProvider);
    }

    protected SagaCommitIdGenerationStrategy<S, Event> commitIdStrategy() {
        return new EventIdCommitIdGenerator();
    }

    protected SagaIdentificationStrategy<S, Event> identificationStrategy() {
        return buildIdentificationStrategy();
    }

    @Override
    public final String sagaId() {
        return this.sagaId;
    }

    @Override
    public final String id() {
        return this.sagaId();
    }

    public final void sagaId(String sagaId) {
        this.sagaId = sagaId;
    }
    
    @Override
    public SagaConfig<S, Event> sagaConfig() {
        setupSaga();
        EventInterestBuilder<Event> eib = new EventInterestBuilder<>();
        for (Class<? extends Event> cls : buildIdentificationStrategy().keySet())
            eib.add(cls);
        return new SagaConfigBuilder<S, Event>()
                .setEventInterest(eib.build())
                .setSagaIdentificationStrategy(this.identificationStrategy())
                .setSagaCommitIdGenerationStrategy(this.commitIdStrategy())
                .setSagaFactory(this.sagaFactory())
                .build();
    }

    private MapSagaIdentificationStrategy<S> buildIdentificationStrategy() {
        if (registeredEvents.isEmpty())
            throw new IllegalStateException("No events / saga identifiers have been registered");
        MapSagaIdentificationStrategy<S> msids = new MapSagaIdentificationStrategy<>();
        for (SagaEventInterestEntry<? extends Event> idf: registeredEvents)
            msids.put(idf.eventClass(), idf);
        return msids;
    }

    @Override
    public Class<S> sagaClass() {
        return (Class<S>) getClass();
    }

}
