package br.com.rsi.alertaGit;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe criada p/ gerar linhas html(td), para inclusão em uma tabela HTML.
 *
 * @author helio.franca
 * @version v1.7
 * @since  N/A
 *
 */
public class GitList {

	/**
	 * 
	 * @param sigla - Recebe o nome da Sigla
	 * @param sistema - Recebe o nome do Sistema
	 * @param dataCommit - Recebe a data de commit
	 * @param dataAnt - Recebe a data anterior de commit
	 * @param alteracao - Recebe se teve alteração de commit (True/False)
	 * @return - Retorna uma linha para tabela HTML
	 * 
	 */
	public String alertaGit(String sigla, String sistema,String pacote, Date dataCommit, Date dataAnt, boolean alteracao) {
		String resultado;
		StringBuffer estiloH2 = new StringBuffer();
		String dataTxt = "---";
		String dataTxtAnt = "---";

		try {
			java.util.Date dataTemp = dataCommit;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			dataTxt = format.format(dataTemp);

			java.util.Date dataTempAnt = dataCommit;
			SimpleDateFormat formatAnt = new SimpleDateFormat("dd/MM/yyyy");
			dataTxtAnt = formatAnt.format(dataTempAnt);

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

		// Caso esteja Ok
		if (alteracao) {
			resultado = " <tr><td> &ensp;  &ensp; &ensp; <img src='https://cdn.pixabay.com/photo/2016/06/01/07/41/green-1428507_960_720.png' width='20' height='20' align='center' /> </td>"
					+ "<td> &ensp; &ensp;" + sigla + " </td>" + "<td> &ensp; &ensp;" + sistema + " </td>"
						+ "<td> &ensp; &ensp;" + pacote + " </td>" 
					+ "<td> &ensp; &ensp;" + dataTxt + " </td>" + "<td> &ensp; &ensp;" + dataTxtAnt + " </td>"
					+ "</tr>";

			// Caso de erro
		} else {
			resultado = " <tr><td> &ensp;  &ensp; &ensp; <img src='https://cdn.icon-icons.com/icons2/1380/PNG/512/vcsconflicting_93497.png' width='20' height='20' align='center' /> </td>"
					+ "<td> &ensp; &ensp;" + sigla + " </td>" + "<td> &ensp; &ensp;" + sistema + " </td>"
						+ "<td> &ensp; &ensp;" + pacote + " </td>" 
					+ "<td> &ensp; &ensp;" + dataTxt + " </td>" + "<td> &ensp; &ensp;" + dataTxtAnt + " </td>"
					+ "</tr>";
		}

		return resultado;
	}

}
