package com.customer.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /*@Test
    void createAndGetNote() {
        NoteDetailDTO d1 = NoteDetailDTO.builder().key("user").value("test@example.com").sensitive(false).build();
        NoteDetailDTO d2 = NoteDetailDTO.builder().key("pass").value("secret").sensitive(true).build();
        NoteDTO dto = NoteDTO.builder().nodeId(999L).title("Credenciales de correo").details(List.of(d1, d2)).build();
        ResponseEntity<NoteDTO> post = restTemplate.postForEntity("/api/notes", dto, NoteDTO.class);
        assertThat(post.getStatusCode().is2xxSuccessful()).isTrue();
        NoteDTO created = post.getBody();
        assertThat(created).isNotNull();
        ResponseEntity<NoteDTO> get = restTemplate.getForEntity("/api/notes/" + created.getNodeId(), NoteDTO.class);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody().getTitle()).isEqualTo("Credenciales de correo");
        assertThat(get.getBody().getDetails()).hasSize(2);
    }*/
}
