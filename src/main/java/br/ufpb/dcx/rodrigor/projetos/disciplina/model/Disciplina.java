package br.ufpb.dcx.rodrigor.projetos.disciplina.model;

import org.bson.types.ObjectId;

public class Disciplina {
    private ObjectId id;
    private String nome;
    private int periodo;
    PesoDisciplina peso;

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public PesoDisciplina getPeso() {
        return peso;
    }

    public void setPeso(PesoDisciplina peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", periodo=" + periodo +
                ", pesoDisciplina=" + peso +
                '}';
    }
}
