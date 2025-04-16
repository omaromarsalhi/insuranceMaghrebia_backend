package com.maghrebia.blockchain.controller;

import com.maghrebia.blockchain.dto.Blockchain;
import com.maghrebia.blockchain.dto.PaymentBlockRequestDto;
import com.maghrebia.blockchain.dto.PaymentBlockResponseDto;
import com.maghrebia.blockchain.service.BlockchainService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/blockchain")
public class BlockChainController {

    private final BlockchainService blockchainService;

    @GetMapping(value = "/{index}", produces = "application/json")
    public Blockchain getPayment(@PathVariable String index) {

        return blockchainService.getPayment(index);
    }

    @PostMapping()
    public PaymentBlockResponseDto addPayment(@RequestBody PaymentBlockRequestDto blockchain) {
        return blockchainService.addPayment(blockchain);
    }
}
