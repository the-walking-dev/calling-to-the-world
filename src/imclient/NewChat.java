package imclient;

import java.util.List;
import java.util.UUID;

public record NewChat (String title, String description, UUID chatId, String createdBy, List<String> webhooks) {}
