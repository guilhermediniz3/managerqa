package com.tester.enuns;

public enum Status {

	EM_PROGRESSO("em progresso"), CONCLUIDA("Concluída"), IMPEDIMENTO("Impedimento"), RETORNO("Retorno");

	private final String descricao;

	// Construtor para atribuir a descrição ao enum
	Status(String descricao) {
		this.descricao = descricao;
	}

	// Método para acessar a descrição
	public String getDescricao() {
		return descricao;
	}

}
