package org.jeecqrs.integration.jcommondomain.commands;

import org.jeecqrs.command.registry.autodiscover.AutoDiscoveredCommandHandler;
import org.jeecqrs.common.commands.Command;
import org.jeecqrs.common.util.ReflectionUtils;

/**
 *
 * @param <C> the actual command type to be handled
 */
public abstract class AbstractCommandHandler<C extends Command<C>> implements AutoDiscoveredCommandHandler<C> {

    private final Class<C> commandClass;

    public AbstractCommandHandler() {
        commandClass = (Class) ReflectionUtils.findSuperClassParameterType(this, AbstractCommandHandler.class, 0);
        if (commandClass == null)
            throw new IllegalStateException("Command type parameter missing on " +
                    AbstractCommandHandler.class.getSimpleName() + " for class " +
                    getClass().getName());
    }

    @Override
    public Class<C> handledCommandType() {
        return commandClass;
    }
    
}
