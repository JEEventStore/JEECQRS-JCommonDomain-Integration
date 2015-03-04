package org.jeecqrs.integration.jcommondomain.sagas;

import javax.ejb.EJB;
import org.jeecqrs.common.commands.Command;
import org.jeecqrs.common.sagas.SagaCommandBus;

/**
 * Saga command bus service.
 * Delegates to a JEECQRS CommandBus and ReliableCommandBus.
 * Deploy as stateless bean.
 */
public class SagaCommandBusService implements SagaCommandBus {
    
    @EJB(name="commandBusDelegate")
    private org.jeecqrs.command.CommandBus<Command<?>> commandBusDelegate;

    @EJB(name="reliableCommandBusDelegate")
    private org.jeecqrs.command.ReliableCommandBus<Command<?>> reliableDelegate;

    @Override
    public <C extends Command<?>> void send(C command) {
        reliableDelegate.send(command);
    }

    @Override
    public <C extends Command<?>> void sendAndForget(C command) {
        commandBusDelegate.send(command);
    }

}
