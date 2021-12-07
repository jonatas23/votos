package com.sicredi.back.votos.controller;

import com.sicredi.back.votos.dto.VotoDTO;
import com.sicredi.back.votos.dto.response.VotoRespostaDTO;
import com.sicredi.back.votos.entities.Voto;
import com.sicredi.back.votos.exception.MensagemException;
import com.sicredi.back.votos.service.VotoService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VotosControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final String URL_API_VOTACAO = "/api/votacao/";

    @MockBean
    VotoService votoService;

    @Before
    public void setUp() throws MensagemException {
        BDDMockito.given(this.votoService.salvar(Mockito.any(Voto.class))).willReturn(new Voto());
    }

    @Test
    public void testSalvarAssociado() throws Exception {
        JSONObject json = new JSONObject();
        json.put("cpfAssociado", "03162110017");
        json.put("idPauta", 1);
        json.put("voto", "SIM");

        mvc.perform(MockMvcRequestBuilders.post(URL_API_VOTACAO)
                .content(json.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(votoService, times(1)).salvar(any(Voto.class));
    }

}
