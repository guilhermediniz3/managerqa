package com.tester.enuns;

public enum TaskStatus {

	in_progress("Em progresso"), expiring("expirando"), expired("expirado");

	private final String descricao;

	// Construtor para atribuir a descrição ao enum
	TaskStatus(String descricao) {
		this.descricao = descricao;
	}

	// Método para acessar a descrição
	public String getDescricao() {
		return descricao;
	}

}
