package org.paul.springbootlearning.controller;

import org.paul.springbootlearning.entity.Account;
import org.paul.springbootlearning.entity.Message;
import org.paul.springbootlearning.service.AccountService;
import org.paul.springbootlearning.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class SocialMediaController {

        private final AccountService accountService;
        private final MessageService messageService;

        public SocialMediaController(AccountService accountService, MessageService messageService) {
            this.accountService = accountService;
            this.messageService = messageService;
        }

        // 1) Register
        @PostMapping("/register")
        public Account register(@RequestBody Account account) {
            return accountService.register(account);
        }

        // 2) Login
        @PostMapping("/login")
        public Account login(@RequestBody Account account) {
            return accountService.login(account);
        }

        // 3) Create message
        @PostMapping("/messages")
        public Message createMessage(@RequestBody Message message) {
            return messageService.createMessage(message);
        }

        // 4) Get all messages
        @GetMapping("/messages")
        public List<Message> getAllMessages() {
            return messageService.getAllMessages();
        }

        // 5) Get message by id (200 always, empty body if not found)
        @GetMapping("/messages/{messageId}")
        public Message getMessageById(@PathVariable Integer messageId) {
            return messageService.getMessageById(messageId).orElse(null);
        }

        // 6) Delete message (200 always, body=1 if existed, empty body if not)
        @DeleteMapping("/messages/{messageId}")
        public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
            Optional<Integer> deleted = messageService.deleteMessageById(messageId);
            return deleted.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.ok().build());
        }

        // 7) Patch message text (200 + body=1 if success, else 400 handled by service)
        @PatchMapping("/messages/{messageId}")
        public Integer updateMessageText(@PathVariable Integer messageId, @RequestBody Message patchBody) {
            return messageService.updateMessageText(messageId, patchBody);
        }

        // 8) Get messages by account id
        @GetMapping("/accounts/{accountId}/messages")
        public List<Message> getMessagesByAccountId(@PathVariable Integer accountId) {
            return messageService.getMessagesByAccountId(accountId);
        }
}
