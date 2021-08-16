package study.core.chat;

import study.core.model.Message;

interface MessageHandler {
    void handleMessage(Client c, Message message);
}
