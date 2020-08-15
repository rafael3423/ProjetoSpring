package com.example.demo2.api.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Erros {

    private String titulo;
    private Integer cod;
    private OffsetDateTime hora;
    private List<CamposDoErro> campos;

    public Erros() {
    }

    public Erros(String titulo, Integer cod, OffsetDateTime hora, List<CamposDoErro> campos) {
        this.titulo = titulo;
        this.cod = cod;
        this.hora = hora;
        this.campos = campos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public OffsetDateTime getHora() {
        return hora;
    }

    public void setHora(OffsetDateTime hora) {
        this.hora = hora;
    }

    public List<CamposDoErro> getCampos() {
        return campos;
    }

    public void setCampos(List<CamposDoErro> campos) {
        this.campos = campos;
    }

}











