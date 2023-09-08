package com.recruit.assignment.common.util;

import io.netty.channel.ChannelOption;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * File Util
 */
@Slf4j
public class FileUtil {

    private static final List<HttpStatus> ACCEPT_STATUS = Arrays.asList(HttpStatus.OK, HttpStatus.FOUND, HttpStatus.NOT_MODIFIED); // 허용 status code
    private static final WebClient webClient;

    static {
        TcpClient tcpClient = TcpClient.newConnection()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
        HttpClient httpClient = HttpClient.from(tcpClient).followRedirect(false).keepAlive(false);

        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    /**
     * url을 head method로 호출
     * @param url 확인 url
     * @return response entity
     */
    public static Mono<ResponseEntity<Void>> checkUrl(final String url) {
        return webClient.head().uri(getUri(url))
                .retrieve()
                .onStatus(httpStatus -> !ACCEPT_STATUS.contains(httpStatus),
                        clientResponse -> Mono.error(new IllegalArgumentException("존재하지 않는 파일 입니다. 파일 : " + url)))
                .toBodilessEntity()
                .onErrorMap(throwable -> !(throwable instanceof IllegalArgumentException), throwable -> {
                    if ((throwable instanceof TimeoutException) || (throwable instanceof ConnectTimeoutException)) {
                        return new IllegalArgumentException("이미지 다운로드 시 서버 응답 시간이 지연되었습니다. 확인해 주세요. (응답시간 제한 5초)");
                    } else {
                        log.warn("FileUtil checkUrl: {}\ntrace: {}", url, ExceptionUtils.getStackTrace(throwable));
                        return new IllegalArgumentException("존재하지 않는 파일 입니다. 파일 : " + url);
                    }
                });
    }

    private static URI getUri(String fileUrl) {
        try {
            return URI.create(fileUrl);
        } catch (IllegalArgumentException e) {
            return UriComponentsBuilder.fromUriString(fileUrl).build().toUri();
        }
    }
}
