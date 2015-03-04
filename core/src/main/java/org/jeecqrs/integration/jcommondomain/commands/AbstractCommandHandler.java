package org.jeecqrs.integration.jcommondomain.commands;

import org.jeecqrs.command.registry.autodiscover.AutoDiscoveredCommandHandler;
import org.jeecqrs.common.commands.Command;
import org.jodah.typetools.TypeResolver;

/**
 *
 * @param <C> the actual command type to be handled
 */
public abstract class AbstractCommandHandler<C extends Command<C>> implements AutoDiscoveredCommandHandler<C> {

    private final Class<C> commandClass;

    public AbstractCommandHandler() {
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(AbstractCommandHandler.class, getClass());
        commandClass = (Class) typeArguments[0];
        if (TypeResolver.Unknown.class.equals(commandClass))
            throw new IllegalStateException("Command type parameter missing on " +
                    AbstractCommandHandler.class.getSimpleName() + " for class " +
                    getClass().getName());
    }

    @Override
    public Class<C> handledCommandType() {
        return commandClass;
    }
    
}
