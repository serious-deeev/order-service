package org.serious.dev.web.controller;

import lombok.RequiredArgsConstructor;
import org.serious.dev.dto.OrderRequestDto;
import org.serious.dev.dto.OrderResponseDto;
import org.serious.dev.service.OrderApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> takeOrder(@RequestBody OrderRequestDto request) {
        OrderResponseDto response = orderApplicationService.processOrderCreation(request);
        return ResponseEntity
                .status(CREATED)
                .body(response);
    }
}
