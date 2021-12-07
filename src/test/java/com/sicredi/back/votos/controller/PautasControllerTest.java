package com.sicredi.back.votos.controller;

import com.sicredi.back.votos.dto.SessaoDTO;
import com.sicredi.back.votos.dto.response.PautaRespostaDTO;
import com.sicredi.back.votos.entities.Pauta;
import com.sicredi.back.votos.exception.MensagemException;
import com.sicredi.back.votos.service.PautaService;
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
public class PautasControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    PautaService pautaService;

    private static final String URL_API_PAUTAS = "/api/pautas/";

    @Before
    public void setUp() throws MensagemException {
        BDDMockito.given(this.pautaService.salvar(Mockito.any(Pauta.class))).willReturn(new Pauta());
        BDDMockito.given(this.pautaService.abrirSessao(Mockito.any(SessaoDTO.class))).willReturn(new Pauta());
    }

    @Test
    public void testSalvarPauta() throws Exception {
        JSONObject json = new JSONObject();
        json.put("nome", "Pauta n1");
        json.put("descricao", "Primeira Pauta");

        mvc.perform(MockMvcRequestBuilders.post(URL_API_PAUTAS)
                .content(json.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(pautaService, times(1)).salvar(any(Pauta.class));

    }

    @Test
    public void testAbrirSessao() throws Exception {
        JSONObject json = new JSONObject();
        json.put("abertura", true);
        json.put("idPauta", 1);
        json.put("tempoMinutos", 1);

        mvc.perform(MockMvcRequestBuilders.post(URL_API_PAUTAS)
                .content(json.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(pautaService, times(1)).salvar(any(Pauta.class));
    }

    @Test
    public void testBuscarPautas() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_API_PAUTAS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
