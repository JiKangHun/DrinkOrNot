package com.ssafy.cadang.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

//    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            System.out.println("연결성공");
            System.out.println(accessor.getMessage());
            
            // todo: 토큰 검증하는 로직 필요
//            if(!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization")))
//                throw new AccessDeniedException("");
        }
        return message;
    }

//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        System.out.println("message:" + message);
//        System.out.println("헤더 : " + message.getHeaders());
//        System.out.println("토큰" + accessor.getNativeHeader("Authorization"));
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            jwtTokenProvider.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7));
//        }
//        return message;
//    }


}
