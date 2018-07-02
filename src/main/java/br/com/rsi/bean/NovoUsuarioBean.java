package br.com.rsi.bean;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Messages;

import br.com.rsi.dao.usuarios.UsuarioDAO;
import br.com.rsi.domain.usuarios.Usuario;

/**
 * -Classe BEAN NovoUsuarioBean.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 *
 */

@ManagedBean
@ViewScoped
public class NovoUsuarioBean implements Serializable {

	private static final long serialVersionUID = -125586851442420009L;
	private Usuario usuario;
	UsuarioDAO dao;
	private Boolean statusBoolean = false;

	@PostConstruct
	public void inicia() {
		usuario = new Usuario();
	}

	public void submit() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", "Correct");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 *  Salvar novo usuário
	 */
	// -------------------------------------------------------------------------------------
	public void salvar() {
		dao = new UsuarioDAO();
		Boolean permitir = dao.validarEmail(usuario.getEmail());
		if (!permitir) {
			Messages.addGlobalWarn("O Endereço de e-mail já existe");
			return;
		}
		try {
			// Cria um hash e criptografa a senha
			SimpleHash hash = new SimpleHash("md5", usuario.getSenhaSemCriptografia());
			usuario.setSenha(hash.toHex());
			usuario.setDataCadastro(new Date());
			usuario.setAdmin(false);
			usuario.setStatus(true);
			dao = new UsuarioDAO();
			dao.salvar(usuario);
			Messages.addGlobalInfo("Usuário(a) " + usuario.getNome() + ", salvo com sucesso.");
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar o usuário, tente novamente mais tarde ... ");
			System.out.println("Erro" + e.getMessage());
		}
	}
	// -------------------------------------------------------------------------------------

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

}