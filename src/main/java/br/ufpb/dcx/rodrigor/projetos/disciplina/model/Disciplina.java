package br.ufpb.dcx.rodrigor.projetos.disciplina.model;

import org.bson.types.ObjectId;

public class Disciplina {
    private ObjectId id;
    private String nome;
    private String descricao;
    //adicionar um professor e alunos posteriormente
    private int periodo;
    private PesoDisciplina peso;

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

    public String getDescricao() {return descricao;}

    public void setDescricao(String descricao) {this.descricao = descricao;}

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
                ", descricao='" + descricao + '\'' +
                ", periodo=" + periodo +
                ", pesoDisciplina=" + peso +
                '}';
    }
}
