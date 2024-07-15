package imclient;

import java.util.Optional;

import core.Event;

import java.util.Map;

import java.net.URL;

public interface IMClient {
    String tenantName();
    void   sendAuthCode(String code);

    default Optional<URL> createChat(NewChat newChat) {
        return Optional.empty();
    }

    default Map<String, String> getUserInfo(String userId) {
        return Map.of();
    }

    default Optional<String> downloadFile(String fileId) {
        return Optional.empty();
    }

    default Optional<String> getInvitation(String chatId) {
        return Optional.empty();
    }

    default void removeParticipant(String chatId, String userId) {}

    default void addParticipant(String chatId, String userId, Boolean isAdmin) {}

    default void setChatTitle(String chatId, String title) {}

    default void setChatPermissions(String chatId, Boolean isActive) {}

    default void setChatDescription(String chatId, String description) {}

    default void publish(Event event) {
        //eventBus.publish(event);
    }
}
