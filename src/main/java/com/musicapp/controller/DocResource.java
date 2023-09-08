package com.musicapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

@RestController
public class DocResource {

  @GetMapping("/api-docs")
  public String getApiDocs() throws IOException {
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
    String schemaHostPortUri = uri.toString().replace(uri.getPath(), "");
    try (InputStream resource = getClass().getClassLoader().getResourceAsStream("api-swagger.yaml")) {
      String oas =
          new BufferedReader(new InputStreamReader(resource)).lines()
              .map(line -> line.replace("{{_hostUrl_}}", schemaHostPortUri))
              .collect(Collectors.joining(System.lineSeparator()));

      return oas;
    }
  }
  @GetMapping("/")
  public RedirectView index() throws IOException {
    return new RedirectView("/swagger-ui/index.html");
  }


}