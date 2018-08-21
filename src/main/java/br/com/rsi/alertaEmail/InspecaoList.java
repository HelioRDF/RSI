package br.com.rsi.alertaEmail;

import java.text.SimpleDateFormat;

import br.com.rsi.dao.complementos.AnaliseCodigoHKDAO;
import br.com.rsi.domain.complementos.AnaliseCodigoHK;
import br.com.rsi.domain.complementos.RFC;

/**
 * Classe criada p/ gerar linhas html(td), para inclusão em uma tabela HTML.
 *
 * @author helio.franca
 * @version V2.1.3
 * @since 08-08-2018
 *
 */
public class InspecaoList {

	/**
	 * 
	 * @param obj
	 *            - recebe um objeto
	 * 
	 * @return - Retorna uma linha para tabela HTML
	 * 
	 */
	public String alertaInspecao(RFC obj) {
		AnaliseCodigoHK inspecaoObj = new AnaliseCodigoHK();
		AnaliseCodigoHKDAO dao = new AnaliseCodigoHKDAO();
		inspecaoObj = dao.buscarPorID(obj.getCodInspecao());

		String resultado;
		StringBuffer estiloH2 = new StringBuffer();
		String dataTxt = inspecaoObj.getDataCaptura().toString();
		String dataCommit = inspecaoObj.getDataCommit().toString();
		String notaAnterior = inspecaoObj.getNotaAnterior() + "%";

		if (notaAnterior.equalsIgnoreCase("null%") || notaAnterior.equalsIgnoreCase("0%")) {
			notaAnterior = "N/A";
		}

		try {
			SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy");
			dataTxt = formatar.format(inspecaoObj.getDataCaptura());
			String array[] = new String[3];
			array = dataCommit.split("-");
			dataCommit = array[2] + "-" + array[1] + "-" + array[0];

		} catch (Exception e) {
			// TODO: handle exception
		}

		estiloH2.append("margin:5px;");
		estiloH2.append("color:green;");
		estiloH2.append("font:12px;");
		estiloH2.append("padding:5px;");

		StringBuffer estiloH3 = new StringBuffer();
		estiloH3.append("padding:auto;");
		estiloH3.append("margin-left:10px;");
		estiloH3.append("color:red;");
		estiloH3.append("font:12px;");
		estiloH3.append("padding-left:10px;");
		String corResultado = "style='color:blue;'";


		if (inspecaoObj.getResultado().equalsIgnoreCase("Alerta")) {
			corResultado = "style='color:orange;'";
		} else {
			corResultado = "style='color:#12d812;'";
		}

		resultado = " <tr>"
				+ "<td> &ensp; " + obj.getGestorEntrega() + " </td>" // Gestor Entrega lista Santander
				+ "<td> &ensp; " + obj.getLider() + "&ensp; </td>" // Gestor de Teste
				+ "<td> &ensp; " + obj.getCodProj() + "&ensp; </td>" + "<td> &ensp; " + obj.getCodRfc() + "&ensp; </td>"
				+ "<td> &ensp; " + obj.getSigla() + " </td>" + "<td> &ensp; "
				+ inspecaoObj.getPainelGestor().toUpperCase() + " </td>" // Painel Gestor Sigla
				+ "<td> &ensp; " + notaAnterior + " </td>"
				+ "<td style=\"background-color:#a0a0a0;color:#1e5cdf;\" > &ensp; " + inspecaoObj.getNotaProjeto()
				+ "% </td>" + "<td> &ensp; " + dataTxt + " </td>" // Data de captura
				+ "<td> &ensp; " + dataCommit + " </td>" // DT alt Cod
				+ "<td> &ensp; " + inspecaoObj.getCodigoAlterado()+ " </td>" //
				+ "<td> &ensp; " + inspecaoObj.getLinhaCodigo() + " </td>" + "<td> &ensp; "
				+ inspecaoObj.getIssuesMuitoAlta() + " </td>" + "<td> &ensp; " + inspecaoObj.getIssuesAlta() + " </td>"
				+ "<td " + corResultado + "> &ensp; " + inspecaoObj.getResultado() + "&ensp; </td>" + "</tr>";
		return resultado;
	}

}
