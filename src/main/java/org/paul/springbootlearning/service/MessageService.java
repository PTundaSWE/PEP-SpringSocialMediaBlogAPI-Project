package org.paul.springbootlearning.service;

import org.paul.springbootlearning.entity.Message;
import org.paul.springbootlearning.repository.AccountRepository;
import org.paul.springbootlearning.repository.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * User Story 3: Create message
     * - messageText not blank
     * - messageText <= 255
     * - postedBy refers to an existing Account
     * Success: return saved Message (with messageId)
     * Failure: 400
     */
    public Message createMessage(Message message) {
        if (message == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String text = message.getMessageText();
        Integer postedBy = message.getPostedBy();

        if (text == null || text.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (text.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (postedBy == null || !accountRepository.existsById(postedBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return messageRepository.save(message);
    }

    /**
     * User Story 4: Get all messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * User Story 5: Get message by id
     * Expected: controller returns 200 always; empty body if not found.
     */
    public Optional<Message> getMessageById(Integer messageId) {
        if (messageId == null) return Optional.empty();
        return messageRepository.findById(messageId);
    }

    /**
     * User Story 6: Delete by id
     * - if existed: delete and return 1
     * - if not existed: return empty behavior handled by controller (Optional.empty())
     *
     * We'll return Optional.of(1) if deleted, Optional.empty() if nothing deleted.
     */
    public Optional<Integer> deleteMessageById(Integer messageId) {
        if (messageId == null) return Optional.empty();

        if (!messageRepository.existsById(messageId)) {
            return Optional.empty();
        }

        messageRepository.deleteById(messageId);
        return Optional.of(1);
    }

    /**
     * User Story 7: Patch message text
     * - message must exist
     * - new text not blank and <= 255
     * Success: return 1
     * Failure: 400
     *
     * Note: request body "contains a new messageText value"
     * We'll accept a Message object and read messageText from it.
     */
    public Integer updateMessageText(Integer messageId, Message patchBody) {
        if (messageId == null || patchBody == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String newText = patchBody.getMessageText();
        if (newText == null || newText.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (newText.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Message existing = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        existing.setMessageText(newText);
        messageRepository.save(existing);

        return 1;
    }

    /**
     * User Story 8: Get all messages by accountId
     */
    public List<Message> getMessagesByAccountId(Integer accountId) {
        if (accountId == null) {
            return List.of();
        }
        return messageRepository.findByPostedBy(accountId);
    }
}
