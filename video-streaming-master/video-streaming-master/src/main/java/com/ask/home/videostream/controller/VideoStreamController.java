package com.ask.home.videostream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ask.home.videostream.service.VideoStreamService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/video")
public class VideoStreamController {

	@Autowired
    private  VideoStreamService videoStreamService;

   
    @GetMapping("/stream")
    public Mono<ResponseEntity<byte[]>> streamVideo() {
    	
        return Mono.just(videoStreamService.prepareContent());
    }
}
