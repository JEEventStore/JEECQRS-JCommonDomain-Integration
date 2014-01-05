package org.jeecqrs.integration.jcommondomain.projections;

import org.jeecqrs.common.event.Event;
import org.jeecqrs.common.event.routing.EventRouter;
import org.jeecqrs.common.event.routing.convention.ConventionEventRouter;
import org.jeecqrs.event.EventBusListener;
import org.jeecqrs.event.EventInterest;
import org.jeecqrs.event.EventInterestBuilder;

public abstract class AbstractProjection implements EventBusListener<Event> {

    private static final String EVENT_HANDLER_NAME = "when";
    private final EventRouter<Void, Event> eventRouter;

    protected abstract Class<? extends Event>[] listenToEvents();

    protected AbstractProjection() {
        this(new ConventionEventRouter<Void, Event>(true, EVENT_HANDLER_NAME));
    }

    @SuppressWarnings("LeakingThisInConstructor")
    protected AbstractProjection(EventRouter<Void, Event> eventRouter) {
        this.eventRouter = eventRouter;
        eventRouter.register(this);
    }

    @Override
    public void receiveEvent(Event event) {
        this.eventRouter.routeEvent(event);
    }

    @Override
    public EventInterest<Event> interestedInEvents() {
        EventInterestBuilder<Event> builder = new EventInterestBuilder<>();
        for (Class<? extends Event> cls : listenToEvents())
            builder.add(cls);
        return builder.build();
    }
    
}
