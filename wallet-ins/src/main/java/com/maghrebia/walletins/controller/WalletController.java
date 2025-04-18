package com.maghrebia.walletins.controller;

import com.maghrebia.walletins.entity.Wallet;
import com.maghrebia.walletins.entity.WalletResponse;
import com.maghrebia.walletins.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/Wallet")
public class WalletController {

    private final WalletService walletService;

    @PostMapping()
    public ResponseEntity<WalletResponse> create(
            @RequestBody Wallet payload
    ) {
        WalletResponse walletResponse = walletService.create(payload);
        return ResponseEntity.ok(walletResponse);
    }

    @GetMapping()
    public List<WalletResponse> getAll() {
        return walletService.getAll();
    }

    @GetMapping("{userId}")
    public WalletResponse getOne(
            @PathVariable String userId,
            @RequestParam(required= false, defaultValue = "false") boolean includeTransactions
    ) {
        return walletService.getOne(userId,includeTransactions);
    }

    @PatchMapping("{id}")
    public ResponseEntity<WalletResponse> update(
            @RequestBody Wallet payload,
            @PathVariable String id ,
            @RequestParam double premium
    ) {
        WalletResponse walletResponse = walletService.update(id,payload,premium);
        return ResponseEntity.ok(walletResponse);
    }




}
