package br.com.rsi.html;

/**
 * - Classe para estruturar corpo de e-mail HTML
 * @author Helio Franca
 * @since 26/06/2018
 * @version 1.2
 */
public class CorpoHtmlInspecao {

	/**
	 * Cria o corpo HTML p/ Email
	 * @param resultado - String
	 * @return - String - String
	 */
	public static String bodyHTML(String resultado, String lider) {
		// embed the image and get the content id

		StringBuffer msgs = new StringBuffer();
		msgs.append("<!DOCTYPE html>");
		msgs.append("<html>");
		msgs.append("<head>");
		msgs.append("<meta charset=\"utf-8\">");
		msgs.append("</head>");	

		msgs.append("<body style=\"background:#fff;font-family:Courier, monospace; font-size:12px; \">");
		msgs.append("<h4> <img\r\n" + 
				"		// src='http://intranet.rsinet.com.br/sites/default/files/rsi_logo_0.png'\r\n" + 
				"		// width='213' height='59' align='BASELINE' />&ensp;  Monitor automático INSPEÇÃO/RFC. </h4> ");
		msgs.append("<div>");
		
		msgs.append(" <br></br>  ");
			msgs.append("<span>Prezados, segue abaixo o resultado da inspeção de código referente ás RFCs  que foram implantados em HK.   </span>  ");

		msgs.append("<br></br>");
		msgs.append("<table border='1'>");
		msgs.append("<tr style=\"background-color:#2f75b5; color:#fff;\">");
	
		msgs.append("<td>&ensp; Gestor de Entrega  &ensp;</td>");
		msgs.append("<td>&ensp; Sigla  &ensp;</td>");
		msgs.append("<td>&ensp; Linhas.Cód  &ensp;</td>");
		msgs.append("<td>&ensp; Blocker  &ensp;</td>");
		msgs.append("<td>&ensp; Critical  &ensp;</td>");
		msgs.append("<td>&ensp; Nota atual  &ensp;</td>");
		msgs.append("<td>&ensp; Nota Ant  &ensp;</td>");
		msgs.append("<td>&ensp; Data Captura &ensp;</td>");
		msgs.append("<td>&ensp; Dt.Alt.Cód   &ensp;</td>");
		msgs.append("<td>&ensp; Alt.Cód   &ensp;</td>");
		msgs.append("<td>&ensp; Cód.RFC   &ensp;</td>");
		msgs.append("<td>&ensp; Cód.Proj   &ensp;</td>");
		msgs.append("<td>&ensp; Líder  &ensp;</td>");
		msgs.append("<td>&ensp; Resultado   &ensp;</td></tr>");
		
		msgs.append(resultado);
		msgs.append("</table>");
	
		msgs.append("<br></br><br></br> <img\r\n" + 
				"		// src='https://firebasestorage.googleapis.com/v0/b/cccc-4ff93.appspot.com/o/Legenda.PNG?alt=media&token=d2c573f8-58bd-4662-a35d-ecb7d51a5398'\r\n" + 
				"		//width='75%' height='75%' align='BASELINE'/> ");

		msgs.append("</div>  " + "</body>");

		msgs.append("</html>");

		return msgs.toString();

	}

}
