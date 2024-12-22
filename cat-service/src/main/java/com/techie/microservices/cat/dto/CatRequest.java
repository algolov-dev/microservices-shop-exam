package com.techie.microservices.cat.dto;

public record CatRequest(String title, Integer price, String userEmail,
                           String ownerEmail) {
}


