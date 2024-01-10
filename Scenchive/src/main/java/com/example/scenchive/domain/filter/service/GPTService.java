//package com.example.scenchive.domain.filter.service;
//
//import com.example.scenchive.domain.filter.config.ChatGptConfig;
//import com.example.scenchive.domain.filter.dto.ChatGptMessage;
//import com.example.scenchive.domain.filter.dto.ChatGptRequestDto;
//import com.example.scenchive.domain.filter.dto.ChatGptResponseDto;
//import com.example.scenchive.domain.filter.dto.QuestionRequestDto;
//import com.example.scenchive.domain.filter.repository.Perfume;
//import com.example.scenchive.domain.filter.repository.PerfumeRepository;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;

//@Slf4j
//@Transactional(readOnly = true)
//@Service
//public class GPTService {
//    private final RestTemplate restTemplate;
//    private final PerfumeRepository perfumeRepository;
//
//    @Autowired
//    public GPTService(RestTemplate restTemplate, PerfumeRepository perfumeRepository) {
//        this.restTemplate = restTemplate;
//        this.perfumeRepository = perfumeRepository;
//    }
//
//    @Value("${api-key.chat-gpt}")
//    private String apiKey;
//
//    private final ObjectMapper objectMapper = new ObjectMapper()
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//
//    //패러프레이징 기능
//    @Transactional
//    public void engToKr() throws JsonProcessingException {
//        WebClient client = WebClient.builder()
//                .baseUrl(ChatGptConfig.CHAT_URL)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) //defaultHeader: 모든 요청에 사용할 헤더
//                .defaultHeader(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey)
//                .build();
//
//        for(int i=15001;i<=16022;i++){
//            Perfume perfume=perfumeRepository.findById((long) i).get();
//            String prompt = perfume.getPerfumeName() + "\n"
//                    + "윗줄에 주어진 영어로 된 향수 이름을 한국어 발음대로 한글로 바꿔서 반환해줄 수 있어? 예를 들어, banana는 바나나로 바꾸는거야. 다른 부가적인 설명은 하지 말고 딱 한글로 변환한 이름만 반환해줘.";
//
//            List<ChatGptMessage> messages = new ArrayList<>();
//            messages.add(ChatGptMessage.builder()
//                    .role(ChatGptConfig.ROLE)
//                    .content(prompt)
//                    .build());
//            ChatGptRequestDto chatGptRequest = new ChatGptRequestDto(
//                    ChatGptConfig.CHAT_MODEL,
//                    ChatGptConfig.MAX_TOKEN,
//                    ChatGptConfig.TEMPERATURE,
//                    ChatGptConfig.STREAM,
//                    messages
//            );
//            String requestValue = objectMapper.writeValueAsString(chatGptRequest);
//
//            Mono<ChatGptResponseDto> responseMono = client.post() //HTTP POST 요청 생성
//                    .bodyValue(requestValue) //POST 요청의 본문(body) 설정, ChatGpt 서비스로 전송할 데이터
//                    .accept(MediaType.APPLICATION_JSON)
//                    .retrieve()
//                    .bodyToMono(ChatGptResponseDto.class); // ChatGptResponseDto로 받기
//
//            ChatGptResponseDto chatGptResponseDto = responseMono.block();
//            String content = getContentFromResponse(chatGptResponseDto);
//
//            System.out.println("content = " + i + content);
//
//            perfume.update(content);
//        }
//    }
//
//    //챗gpt 응답에서 문자열 응답 부분만 추출
//    public String getContentFromResponse(ChatGptResponseDto chatGptResponseDto) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(chatGptResponseDto));
//        return jsonNode.at("/choices/0/message/content").asText();
//    }
//}
