package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

import br.ufpb.dcx.rodrigor.projetos.form.model.ResultadoValidacao;
import br.ufpb.dcx.rodrigor.projetos.form.model.ValidadorCampo;

public class ValidadorIdade implements ValidadorCampo {

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("A idade não pode ser cadastrada por estar vazia.");
        }
        if (!checarValorInt(valor)) {
            return new ResultadoValidacao("O valor providenciado não é um número inteiro.");
        }
        int valorInt = Integer.parseInt(valor);
        if (valorInt < 14 || valorInt > 130) {
            return new ResultadoValidacao("Sua idade não pode ser menor que 14 e nem maior que 130.");
        }
        return new ResultadoValidacao();
    }

    public static boolean checarValorInt(String valor) {
        try {
            Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}