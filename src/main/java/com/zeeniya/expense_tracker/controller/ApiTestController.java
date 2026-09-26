package com.zeeniya.expense_tracker.controller;

import com.zeeniya.expense_tracker.service.TestApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ApiTestController {
        private final TestApiService apiTestService;

        public ApiTestController(TestApiService apiTestService) {
            this.apiTestService = apiTestService;
        }

        @GetMapping
        public String testApi() {
            return apiTestService.getApiResponse();
        }
    }

