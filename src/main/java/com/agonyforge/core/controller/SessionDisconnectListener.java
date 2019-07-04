package com.agonyforge.core.controller;

import com.agonyforge.core.repository.CreatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.inject.Inject;
import java.util.Map;

import static com.agonyforge.core.controller.ControllerConstants.*;

@Component
public class SessionDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDisconnectListener.class);

    private CreatureRepository creatureRepository;

    @Inject
    public SessionDisconnectListener(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attributes = headerAccessor.getSessionAttributes();

        if (attributes != null) {
            LOGGER.info("Lost connection from {}", attributes.get(AGONY_REMOTE_IP_KEY));

            creatureRepository
                .findByConnectionSessionId(event.getSessionId())
                .ifPresent(creature -> creatureRepository.delete(creature));

            return;
        }

        LOGGER.error("Unable to fetch session attributes from message!");
    }
}
