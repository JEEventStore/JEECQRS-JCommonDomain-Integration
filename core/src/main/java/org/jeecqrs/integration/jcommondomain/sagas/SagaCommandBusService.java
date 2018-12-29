package org.jeecqrs.integration.jcommondomain.sagas;

import org.jeecqrs.common.commands.Command;
import org.jeecqrs.common.sagas.SagaCommandBus;

import javax.ejb.EJB;

/**
 * Saga command bus service.
 * Delegates to a JEECQRS CommandBus and ReliableCommandBus.
 * Deploy as stateless bean.
 */
public class SagaCommandBusService implements SagaCommandBus {
    
    @EJB
    private org.jeecqrs.command.CommandBus<Command<?>> commandBusDelegate;

    @EJB
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
