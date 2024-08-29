package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor;

import java.util.List;
import java.util.Optional;

import com.creativedesignproject.kumoh_board_backend.auth.presentation.HttpRequestTokenExtractor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompositeHttpRequestTokenExtractor implements HttpRequestTokenExtractor{

    private final List<HttpRequestTokenExtractor> httpRequestTokenExtractors;

    @Override
    public Optional<String> extract(HttpServletRequest request) {
        for (HttpRequestTokenExtractor httpRequestTokenExtractor : httpRequestTokenExtractors) {
            Optional<String> token = httpRequestTokenExtractor.extract(request);
            if (token.isPresent()) {
                return token;
            }
        }
        return Optional.empty();
    }
}
