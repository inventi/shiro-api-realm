package io.inventi.shiro.api.audit.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditEvent {
    public UUID eventId;
    public LocalDateTime dateTime;
    public User user;
    public Action action;

    public AuditEvent(User user, Action action) {
        this.eventId = UUID.randomUUID();
        this.dateTime = LocalDateTime.now();
        this.user = user;
        this.action = action;
    }

    public static class User {
        public String userName;
        public String agent;
        public String location;

        public User(String userName, String agent, String location) {
            this.userName = userName;
            this.agent = agent;
            this.location = location;
        }
    }

    public static class Action {
        public String server;
        public String uri;
        public String query;
        public String method;
        public int status;

        public Action(String server, String uri, String query, String method, int status) {
            this.server = server;
            this.uri = uri;
            this.query = query;
            this.method = method;
            this.status = status;
        }
    }
}

