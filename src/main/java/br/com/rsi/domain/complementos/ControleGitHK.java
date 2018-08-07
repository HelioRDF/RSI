package br.com.rsi.domain.complementos;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * -Classe POJO ControleGitHK, e entity do DB via Hibernate.
 * 
 * @author helio.franca
 * @version v2.1.1
 * @since  07-08-2018
 * 
 */

@Entity
public class ControleGitHK extends GenericDomain {

	private static final long serialVersionUID = -9203193152118322163L;
	public  static final String CONTA_PAULA = "XB201520";
	public  static final String CONTA_LUIS = "XI324337";

	@Column(nullable = false)
	private String sigla;

	@Column(nullable = true)
	private String nomeSistema;

	@Column(nullable = true)
	private String caminho;

	@Column(nullable = true)
	private String pacote;

	@Column(nullable = true)
	private String author;

	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCommit;
	
	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCommitAnt;

	@Column
	@Lob
	private String descricaoLog;

	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVerificacao;

	@Column(nullable = true)
	private String nomeArquivo;
	
	@Column(nullable = true)
	private boolean alteracao;
	
	@Column(nullable = true)
	private boolean enviarEmail;
	
	@Column
	private String usuarioGit;

	// --------------------------------------------------
	
	public boolean isAlteracao() {
		return alteracao;
	}

	public String getUsuarioGit() {
		return usuarioGit;
	}

	public void setUsuarioGit(String usuarioGit) {
		this.usuarioGit = usuarioGit;
	}

	public boolean isEnviarEmail() {
		return enviarEmail;
	}

	public void setEnviarEmail(boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}

	public void setAlteracao(boolean alteracao) {
		this.alteracao = alteracao;	
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

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

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public String getPacote() {
		return pacote;
	}

	public void setPacote(String pacote) {
		this.pacote = pacote;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDataCommit() {
		return dataCommit;
	}

	public void setDataCommit(Date dataCommit) {
		this.dataCommit = dataCommit;
	}

	public String getDescricaoLog() {
		return descricaoLog;
	}

	public void setDescricaoLog(String descricaoLog) {
		this.descricaoLog = descricaoLog;
	}

	public Date getDataVerificacao() {
		return dataVerificacao;
	}

	public void setDataVerificacao(Date dataVerificacao) {
		this.dataVerificacao = dataVerificacao;
	}

	public Date getDataCommitAnt() {
		return dataCommitAnt;
	}

	public void setDataCommitAnt(Date dataCommitAnt) {
		this.dataCommitAnt = dataCommitAnt;
	}
	
	@Override
	public String toString() {
		return "[Pacote: "+ pacote + " Usuario Git: "+usuarioGit+"]";
	}

	// --------------------------------------------------

}
