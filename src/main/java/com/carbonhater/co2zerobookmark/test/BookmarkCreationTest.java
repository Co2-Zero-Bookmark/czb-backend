/*
package com.carbonhater.co2zerobookmark.test;

import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkCreateDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BookmarkCreationTest {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        BookmarkCreateDTO dto = new BookmarkCreateDTO();
        dto.setBookmarkName("네이버");
        dto.setBookmarkUrl("www.naver.com");
        dto.setFolderId(1L);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BookmarkCreateDTO> requestEntity = new HttpEntity<>(dto, headers);

        String url = "http://localhost:8080/api/v1/bookmarks";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            System.out.println("Response Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
