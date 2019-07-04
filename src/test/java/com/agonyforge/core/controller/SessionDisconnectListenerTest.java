package com.agonyforge.core.controller;

import com.agonyforge.core.model.Creature;
import com.agonyforge.core.repository.CreatureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.agonyforge.core.controller.ControllerConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor.HTTP_SESSION_ID_ATTR_NAME;

public class SessionDisconnectListenerTest {
    @Mock
    private CreatureRepository creatureRepository;

    private SessionDisconnectListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Creature creature = new Creature();

        when(creatureRepository
            .findByConnectionSessionId(anyString()))
            .thenReturn(Optional.of(creature));

        listener = new SessionDisconnectListener(creatureRepository);
    }

    @Test
    public void testOnApplicationEvent() {
        Message<byte[]> message = buildMockMessage(true);
        SessionDisconnectEvent event = new SessionDisconnectEvent("source", message, "ffff", CloseStatus.NORMAL);

        listener.onApplicationEvent(event);

        verify(creatureRepository).delete(any(Creature.class));
    }

    @Test
    public void testOnApplicationEventNoSessionAttributes() {
        Message<byte[]> message = buildMockMessage(false);
        SessionDisconnectEvent event = new SessionDisconnectEvent("source", message, "ffff", CloseStatus.NORMAL);

        listener.onApplicationEvent(event);

        verify(creatureRepository, never()).delete(any());
    }

    @Test
    public void testOnApplicationEventNoCreature() {
        Message<byte[]> message = buildMockMessage(true);
        SessionDisconnectEvent event = new SessionDisconnectEvent("source", message, "ffff", CloseStatus.NORMAL);

        when(creatureRepository
            .findByConnectionSessionId(anyString()))
            .thenReturn(Optional.empty());

        listener.onApplicationEvent(event);

        verify(creatureRepository, never()).delete(any(Creature.class));
    }

    private Message<byte[]> buildMockMessage(boolean includeAttributes) {
        UUID springSessionId = UUID.randomUUID();
        UUID stompSessionId = UUID.randomUUID();
        Map<String, Object> sessionAttributes = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();

        sessionAttributes.put(HTTP_SESSION_ID_ATTR_NAME, springSessionId.toString());
        sessionAttributes.put(AGONY_REMOTE_IP_KEY, "12.34.56.78");

        headers.put(SimpMessageHeaderAccessor.SESSION_ID_HEADER, stompSessionId.toString());

        if (includeAttributes) {
            headers.put(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES, sessionAttributes);
        }

        return new GenericMessage<>(new byte[0], headers);
    }
}
