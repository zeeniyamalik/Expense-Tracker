package com.zeeniya.expense_tracker.dto;

public class UserResponse {
        private Long id;
        private String email;
        private String username;
        private String name;

        public UserResponse(Long id, String email, String username, String name) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }
}
