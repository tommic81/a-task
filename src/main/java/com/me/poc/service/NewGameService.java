package com.me.poc.service;

import com.me.poc.controller.TransferObject;

import java.util.Map;

public class NewGameService implements ApplicationService {
    @Override
    public TransferObject handle(Map<String, String> requestParams) {
        System.out.println("Opened New Game Service");
        return null;
    }
}
