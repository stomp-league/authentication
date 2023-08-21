package com.stompleague.stompengine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Main {
  public static void main(String[] args) {

    log.info("""
       \n
       _____ _____ _____ _____ _____
      |   __|_   _|     |     |  _  |
      |__   | | | |  |  | | | |   __|
      |_____| |_| |_____|_|_|_|__|
      \n
      """);

    SpringApplication.run(Main.class, args);
  }
}