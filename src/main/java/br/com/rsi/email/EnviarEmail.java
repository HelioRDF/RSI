package br.com.rsi.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import br.com.rsi.html.CorpoHtml;

//http://www.devmedia.com.br/utilizando-a-api-commons-email-para-enviar-e-mails/3306
///https://www.lifewire.com/what-are-the-gmail-smtp-settings-1170854
//https://support.google.com/a/answer/176600?hl=pt-BR

/**
 * -Classe de configuração p/ envio de e-mail.
 * 
 * @author Helio Franca
 * @since 26/06/2018
 * @version 1.2
 */

public class EnviarEmail {

	private String smtp_365 = "smtp.office365.com";
	private String office_365 = "helio.franca@rsinet.com.br";
	private String senha365 = "Rsi#123!";

	private String apelido = "Inspeção de Código HK-RSI";

	private String destino = "helio.franca@rsinet.com.br";
	private String apelidoDestino = "Inspeção";
	String[] teste = { "heliordf@hotmail.com", "helio.franca@rsinet.com.br" };

	// Usado no ProgressBarView
	private static boolean enviado = false;
	private static boolean concluido = false;

	/**
	 * Seta as propriedade de configuração de email
	 * @param resultado - String
	 * @param titulo - String
	 */
	public void emailHtml(String resultado, String titulo) {

		try {

			destino = "heliordf@hotmail.com";
			String destino2 = "helio.franca@rsinet.com.br";

			// resultado = "<html>The apache logo Office- </html>";

			// Create the email message
			HtmlEmail email = new HtmlEmail();

			email.setDebug(true);
			email.setHostName(smtp_365);
			email.setAuthenticator(new DefaultAuthenticator(office_365, senha365));
			// email.addTo(destino, apelidoDestino);
			email.addTo(destino2, apelidoDestino);

			email.setFrom(office_365, apelido);
			email.setSubject(titulo);
			email.setStartTLSEnabled(true);

			String textoHtml = CorpoHtml.bodyHTML(resultado);

			// set the html message
			email.setHtmlMsg(textoHtml);

			// set the alternative message
			email.setTextMsg("Seu E-mail não suporta mensagem no formato HTML.");

			// send the email
			email.send();
			enviado = true;
			concluido = true;

		} catch (Exception e) {
			System.out.println("Email office-365 não foi enviado----------------------------");
			enviado = false;
			concluido = false;
			emailHtmlGmail(resultado, titulo);

		}

	}

	public void emailHtmlGmail(String resultado, String titulo) {

		try {

			destino = "heliordf@hotmail.com;";
			resultado = "<html>The apache logo Gmail - </html>";
			// Create the email message
			HtmlEmail email = new HtmlEmail();

			email.setDebug(true);
			email.setHostName("smtp.gmail.com");
			email.setSslSmtpPort("465");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("heliordf@gmail.com", "senha"));
			email.addTo(destino, "Helio Franca");
			email.setFrom("heliordf@gmail.com", apelido);
			email.setSubject(titulo);
			email.setStartTLSEnabled(true);

			String textoHtml = CorpoHtml.bodyHTML(resultado);

			// set the html message
			email.setHtmlMsg(textoHtml);

			// set the alternative message
			email.setTextMsg("Seu E-mail não suporta mensagem no formato HTML.");

			// send the email
			email.send();
			enviado = true;
			concluido = true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			enviado = false;
			concluido = true;
			System.out.println("Email Gmail não foi enviado----------------------------");
		}

		// Cria o e-mail

	}

	public static boolean isEnviado() {
		return enviado;
	}

	public static void setEnviado(boolean enviado) {
		EnviarEmail.enviado = enviado;
	}

	public static boolean isConcluido() {
		return concluido;
	}

	public static void setConcluido(boolean concluido) {
		EnviarEmail.concluido = concluido;
	}

}
