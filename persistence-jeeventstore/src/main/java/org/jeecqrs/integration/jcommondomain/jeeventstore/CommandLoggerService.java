package org.jeecqrs.integration.jcommondomain.jeeventstore;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import org.jeecqrs.command.log.CommandLogEnvelope;
import org.jeecqrs.command.CommandLogger;
import org.jeecqrs.common.commands.Command;
import org.jeecqrs.common.util.Validate;
import org.jeeventstore.ConcurrencyException;
import org.jeeventstore.DuplicateCommitException;
import org.jeeventstore.EventStore;
import org.jeeventstore.WritableEventStream;

public class CommandLoggerService implements CommandLogger<Command<?>> {

    private final static Logger log = Logger.getLogger(CommandLoggerService.class.getName());

    @EJB(name="eventStore")
    private EventStore eventStore;

    @Resource(name="commandBucketId")
    private String commandBucketId = DefaultBucketIds.COMMANDS;

    @Asynchronous
    public void log(Command<?> command) {
        log(command, new HashMap<String, String>());
    }

    @Override
    @Asynchronous
    public void log(Command<?> command, Map<String, String> metadata) {
        Validate.notNull(command, "command must not be null");
        Validate.notNull(metadata, "metadata must not be null");
        String commandId = command.id().toString();
        WritableEventStream stream = streamFor(command);
        CommandLogEnvelope<Command<?>> envelope = new CommandLogEnvelope<Command<?>>(command,
                new HashMap<>(metadata));
        log.log(Level.FINE, "Logging command: {0}", envelope);
        stream.append(envelope);
        try {
            stream.commit(commandId);
        } catch (DuplicateCommitException | ConcurrencyException e) {
            throw new RuntimeException(e);
        }
    }

    protected WritableEventStream streamFor(Command command) {
        return eventStore.createStream(commandBucketId, streamIdFor(command));
    }

    protected String streamIdFor(Command command) {
        return command.id().toString();
    }

}
