package br.ufpb.dcx.rodrigor.projetos.disciplina.model;

public enum PesoDisciplina {
    LEVE("Leve"),
    PESADO("Pesada");

    private final String peso;
    PesoDisciplina(String peso){ this.peso = peso;}

    @Override
    public String toString() { return this.peso; }
}
