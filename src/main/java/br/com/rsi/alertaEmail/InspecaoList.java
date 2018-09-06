package br.com.rsi.alertaEmail;

import java.text.SimpleDateFormat;

import br.com.rsi.dao.complementos.AnaliseCodigoHKDAO;
import br.com.rsi.domain.complementos.AnaliseCodigoHK;
import br.com.rsi.domain.complementos.RFC;
import tratamentos.TrataDados;

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
	public StringBuffer alertaInspecao(RFC obj) {
		AnaliseCodigoHK inspecaoObj = new AnaliseCodigoHK();
		AnaliseCodigoHKDAO dao = new AnaliseCodigoHKDAO();
		inspecaoObj = dao.buscarPorID(obj.getCodInspecao());

		StringBuffer linhasTabela = new StringBuffer();
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
		String planoFundoCorTxt="style='background-color:#a0a0a0;color:#1e5cdf;font-size:11px;'";

		if (inspecaoObj.getResultado().equalsIgnoreCase("Alerta")) {
			corResultado = "style='color:orange;'";
		} else {
			corResultado = "style='color:#12d812; font-size:11px;'";
		}

		linhasTabela.append(" <tr>");
		linhasTabela.append(TrataDados.incluirHtmlTd(obj.getGestorEntrega())); // Gestor Entrega lista Santander
		linhasTabela.append(TrataDados.incluirHtmlTd(obj.getLider()));// Gestor de Teste
		linhasTabela.append(TrataDados.incluirHtmlTd(obj.getCodProj()));
		linhasTabela.append(TrataDados.incluirHtmlTd(obj.getCodRfc()));
		linhasTabela.append(TrataDados.incluirHtmlTd(obj.getSigla()));
		linhasTabela.append(TrataDados.incluirHtmlTd(inspecaoObj.getPainelGestor().toUpperCase()));
		linhasTabela.append(TrataDados.incluirHtmlTd(notaAnterior));
		linhasTabela.append(TrataDados.incluirHtmlTdStyle(inspecaoObj.getNotaProjeto(), planoFundoCorTxt, "%"));
		
		//linhasTabela.append(
		//		"<td style=\"background-color:#a0a0a0;color:#1e5cdf;\" > &ensp; " + inspecaoObj.getNotaProjeto());
		linhasTabela.append(TrataDados.incluirHtmlTd(dataTxt));
		linhasTabela.append(TrataDados.incluirHtmlTd(dataCommit));
		//linhasTabela.append(TrataDados.incluirHTMLtd(inspecaoObj.getCodigoAlterado()));
		linhasTabela.append(TrataDados.incluirHtmlTd(Integer.toString(inspecaoObj.getLinhaCodigo())));
		linhasTabela.append(TrataDados.incluirHtmlTd(Integer.toString(inspecaoObj.getIssuesMuitoAlta())));
		linhasTabela.append(TrataDados.incluirHtmlTd(Integer.toString(inspecaoObj.getIssuesAlta())));
		linhasTabela.append("<td " + corResultado + "> &ensp; " + inspecaoObj.getResultado() + "&ensp; </td>");
		linhasTabela.append("</tr>");

		return linhasTabela;
	}

}
