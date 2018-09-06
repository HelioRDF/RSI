package br.com.rsi.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import br.com.rsi.dao.usuarios.UsuarioDAO;
import br.com.rsi.domain.usuarios.Usuario;

/**
 * -Classe BEAN ControleSiglasBean.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 *
 */
@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

	private int tipoLogin = 1;
	private Usuario usuario;
	private static Usuario usuarioLogado;
	private Boolean userlogadoB = false;

	// Login
	// -------------------------------------------------------------------------------------------
	@PostConstruct
	public void iniciar() {
		usuario = new Usuario();
	}

	/**
	 * Autenticar usuário na aplicação.
	 */
	public void autenticar() {
		if (tipoLogin == 1) {
			try {
				UsuarioDAO usuarioDAO = new UsuarioDAO();
				usuarioLogado = usuarioDAO.autenticar(usuario.getEmail().trim(), usuario.getSenha());

				if (usuarioLogado == null) {
					Messages.addGlobalWarn("Usuário e/ou senha, incorretos");
					return;
				} else {
					// Verifica se usuário está Ativo
					if (!usuarioLogado.getStatus()) {
						Messages.addGlobalError("Usuário Desativado.");
						usuarioLogado = null;
						return;
					}
				}
				// Usuário Ok...
				userlogadoB = true;
				Faces.redirect("./pages/administrativas/principal.xhtml");
				usuarioLogado.setUltimoLogin(new Date());
				usuarioDAO.editar(usuarioLogado);
			} catch (IOException e) {
				Messages.addGlobalError("Erro  ");
			}
		} // fim do If
	}

	// Logoff
	// -------------------------------------------------------------------------------------------
	/**
	 * Sair da aplicação
	 */
	public void sair() {
		try {
			userlogadoB = false;
			usuarioLogado = null;
			
			// Destroi as sessões após loggof do usuário.
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			session.invalidate();

			// Redireciona para a página de login
			Faces.redirect("./pages/publicas/login.xhtml");
			Messages.addGlobalInfo("Logout");
			
			return;
		} catch (IOException e) {
			Messages.addGlobalError("Erro  ");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public static Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public static void setUsuarioLogado(Usuario usuarioLogado) {
		LoginBean.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getTipoLogin() {
		return tipoLogin;
	}

	public void setTipoLogin(int tipoLogin) {
		this.tipoLogin = tipoLogin;
	}

	public Boolean getUserlogadoB() {
		return userlogadoB;
	}

	public void setUserlogadoB(Boolean userlogadoB) {
		this.userlogadoB = userlogadoB;
	}

	// ------------------------------------------------------------

}
