package com.cenfotec.backendcodesprint.api.AIBookingController;

import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.AIRecommendationRequestDto;
import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.AIRecommendationResponseDto;
import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.ChatConversationRequestDto;
import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.ChatConversationResponseDto;
import com.cenfotec.backendcodesprint.logic.BookingAI.Service.ConversationService;
import com.cenfotec.backendcodesprint.logic.BookingAI.Service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatIAController {

    private final RecommendationService recommendationService;
    private final ConversationService   conversationService;

    @PostMapping("/recommend")
    public ResponseEntity<AIRecommendationResponseDto> recommend(
            @Valid @RequestBody AIRecommendationRequestDto request) {
        return ResponseEntity.ok(recommendationService.getRecommendation(request));
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatConversationResponseDto> chat(
            @RequestBody ChatConversationRequestDto request) {
        return ResponseEntity.ok(conversationService.chat(request));
    }
}