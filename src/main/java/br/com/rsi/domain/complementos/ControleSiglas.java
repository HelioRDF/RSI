package br.com.rsi.domain.complementos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * -Classe POJO p/ controle de Siglas, e Entity do banco de dados via Hibernate.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 * 
 */

@Entity
public class ControleSiglas extends GenericDomain {

	private static final long serialVersionUID = 9077030507376334373L;

	@Column(nullable = true)
	private String sigla;

	@Column(nullable = true)
	private String nomeSistema;

	@Column(nullable = true)
	private String linguagem;

	@Column(nullable = true)
	private String mapa;

	@Column(nullable = true)
	private String gestoEntrega;

	@Column
	@Lob
	private String responsavel;

	@Column
	@Lob
	private String descricao;

	@Column
	@Lob
	private String instrucoes;

	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	@Column(nullable = true)
	private String nomeArquivo;

	@Column(nullable = true)
	private String andamento;

	@Column(nullable = true)
	private String repositorio;

	// --------------------------------------------------

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNomeSistema() {
		return nomeSistema;
	}

	public void setNomeSistema(String nomeSistema) {
		this.nomeSistema = nomeSistema;
	}

	public String getLinguagem() {
		return linguagem;
	}

	public void setLinguagem(String linguagem) {
		this.linguagem = linguagem;
	}

	public String getMapa() {
		return mapa;
	}

	public void setMapa(String mapa) {
		this.mapa = mapa;
	}

	public String getGestoEntrega() {
		return gestoEntrega;
	}

	public void setGestoEntrega(String gestoEntrega) {
		this.gestoEntrega = gestoEntrega;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getAndamento() {
		return andamento;
	}

	public void setAndamento(String andamento) {
		this.andamento = andamento;
	}

	public String getInstrucoes() {
		return instrucoes;
	}

	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
	}

	public String getRepositorio() {
		return repositorio;
	}

	public void setRepositorio(String repositorio) {
		this.repositorio = repositorio;
	}

	// --------------------------------------------------

}
