package org.jeecqrs.bundle.jcommondomain.commands;

import org.jeecqrs.command.registry.autodiscover.AutoDiscoveredCommandHandler;
import org.jeecqrs.common.commands.Command;
import org.jeecqrs.sagas.Saga;
import org.jeecqrs.sagas.config.autodiscover.SagaConfigProvider;
import org.jodah.typetools.TypeResolver;

/**
 *
 */
public abstract class AbstractCommandHandler<C extends Command> implements AutoDiscoveredCommandHandler<C> {

    private Class<C> commandClass;

    public AbstractCommandHandler() {
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(AbstractCommandHandler.class, getClass());
        commandClass = (Class) typeArguments[0];
        if (TypeResolver.Unknown.class.equals(commandClass))
            throw new IllegalStateException("Command type parameter missing on " +
                    AbstractCommandHandler.class.getSimpleName() + " for class " +
                    getClass().getName());
    }

    @Override
    public Class<? extends C> handledCommandType() {
        return commandClass;
    }
    
}
