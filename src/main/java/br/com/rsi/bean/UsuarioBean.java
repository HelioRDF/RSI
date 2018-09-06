package br.com.rsi.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Messages;

import br.com.rsi.dao.usuarios.UsuarioDAO;
import br.com.rsi.domain.usuarios.Usuario;

/**
 * -Classe BEAN UsuarioBean.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 *
 */

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable {

	Usuario usuarioLogado = LoginBean.getUsuarioLogado();
	private Usuario usuario = new Usuario();
	private UsuarioDAO dao;
	private String NomeTest;
	private Boolean statusBoolean = false;
	private Boolean telaEditar = false;
	private Boolean botaoEditar = false;
	private Boolean botaoSalvar = false;


/*
 * Editar objeto do tipo usuário
 */
	// -------------------------------------------------------------------------------------------
	public void editar() {
		try {
			UsuarioDAO dao = new UsuarioDAO();
			dao.editar(usuarioLogado);
			Messages.addGlobalInfo("Usuário(a) ' " + usuarioLogado.getNome() + "' Editado com sucesso!!!");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar Usuário(a) '" + usuarioLogado.getNome() + "'");
		}
	}

/**
 * Editar senha
 */
	// -------------------------------------------------------------------------------------------
	public void editarSenha() {
		try {
			// Cria um hash e criptografa a senha
			SimpleHash hash = new SimpleHash("md5", usuarioLogado.getSenhaSemCriptografia());
			usuarioLogado.setSenha(hash.toHex());
			dao = new UsuarioDAO();
			dao.merge(usuarioLogado);
			Messages.addGlobalInfo("Usuário Editado com sucesso: " + usuarioLogado.getNome());
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar: " + usuarioLogado.getNome());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String getNomeTest() {
		return NomeTest;
	}

	public void setNomeTest(String nomeTest) {
		NomeTest = nomeTest;
	}

	public Boolean getStatusBoolean() {
		return statusBoolean;
	}

	public void setStatusBoolean(Boolean statusBoolean) {
		this.statusBoolean = statusBoolean;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getTelaEditar() {
		return telaEditar;
	}

	public void setTelaEditar(Boolean telaEditar) {
		this.telaEditar = telaEditar;
	}

	public Boolean getBotaoEditar() {
		return botaoEditar;
	}

	public void setBotaoEditar(Boolean botaoEditar) {
		this.botaoEditar = botaoEditar;
	}

	public Boolean getBotaoSalvar() {
		return botaoSalvar;
	}

	public void setBotaoSalvar(Boolean botaoSalvar) {
		this.botaoSalvar = botaoSalvar;
	}

}