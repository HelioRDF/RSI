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
				"		// width='213' height='59' align='BASELINE' />&ensp;  Monitor automático! </h4> ");
		msgs.append("<div>");
		
		msgs.append(" <br></br> <span> Prezado Gestor(a) ");
		msgs.append(lider);
		msgs.append(",</span> <br></br>");
		msgs.append("<span>Segue abaixo o resultado da inspeção de código referente a RFC  com status em “Testing”, retornado do Remedy.   </span>  ");

		msgs.append("<br></br>");
		msgs.append("<table border='1'>");
		msgs.append("<tr style=\"background-color:#2f75b5; color:#fff;\"><td> Commit  </td>");
	
		msgs.append("<td>&ensp; Gestor de Entrega  &ensp;</td>");
		msgs.append("<td>&ensp; Sigla  &ensp;</td>");
		msgs.append("<td>&ensp; Linhas.Cód  &ensp;</td>");
		msgs.append("<td>&ensp; Blocker  &ensp;</td>");
		msgs.append("<td>&ensp; Critical  &ensp;</td>");
		msgs.append("<td>&ensp; Nota atual  &ensp;</td>");
		msgs.append("<td>&ensp; Nota Ant  &ensp;</td>");
		msgs.append("<td>&ensp; Data Captura &ensp;</td>");
		msgs.append("<td>&ensp; Data  Commit   &ensp;</td>");
		msgs.append("<td>&ensp; Cod.RFC   &ensp;</td>");
		msgs.append("<td>&ensp; Cod.Proj   &ensp;</td>");
		msgs.append("<td>&ensp; Resultado   &ensp;</td></tr>");
		
		msgs.append(resultado);
		msgs.append("</table>");
		
		msgs.append("<br></br><br></br> <img\r\n" + 
				"		// src='https://firebasestorage.googleapis.com/v0/b/cccc-4ff93.appspot.com/o/Legenda%20Insp.PNG?alt=media&token=28aeff51-0c75-47fe-92d9-8a38dd98300b'\r\n" + 
				"		//width='75%' height='75%' align='BASELINE' /> ");

		msgs.append("</div>  " + "</body>");

		msgs.append("</html>");

		return msgs.toString();

	}

}
