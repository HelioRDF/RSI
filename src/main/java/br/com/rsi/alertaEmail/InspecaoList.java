package br.com.rsi.alertaEmail;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.rsi.dao.complementos.AnaliseCodigoHKDAO;
import br.com.rsi.domain.complementos.AnaliseCodigoHK;
import br.com.rsi.domain.complementos.RFC;

/**
 * Classe criada p/ gerar linhas html(td), para inclusão em uma tabela HTML.
 *
 * @author helio.franca
 * @version V2.0.5
 * @since 23-07-2018
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

		try {
			
		
			SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy");
		
			dataTxt = formatar.format(inspecaoObj.getDataCaptura());
			
			
			
			String array[] = new String[3];
			array = dataCommit.split("-");
			
			System.out.println("inicio XXXXXXXXXXXXXXXXX------------------------------\n");
			System.out.println( dataCommit);
			System.out.println("\n Fim XXXXXXXXXXXXXXXXXXX------------------------------");
			
			 dataCommit = array[2]+"-"+array[1]+"-"+array[0];
		


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
		
		

		resultado = " <tr><td> &ensp;   <img src='https://cdn.pixabay.com/photo/2016/06/01/07/41/green-1428507_960_720.png' width='20' height='20'  align='center' /> </td>"

				+ "<td> &ensp; " + inspecaoObj.getPainelGestor() + " </td>"
				+ "<td> &ensp; " + obj.getSigla() + " </td>"
				+ "<td> &ensp; " + inspecaoObj.getLinhaCodigo() + " </td>"
				+ "<td> &ensp; " + inspecaoObj.getIssuesMuitoAlta() + " </td>"
				+ "<td> &ensp; " + inspecaoObj.getIssuesAlta() + " </td>"
				+ "<td> &ensp; " + inspecaoObj.getNotaProjeto() + "% </td>"
				+ "<td> &ensp; " + inspecaoObj.getNotaAnterior()+ "% </td>"
				+ "<td> &ensp; " + dataTxt + " </td>"
							+ "<td> &ensp; " + dataCommit+ " </td>"
					+ "<td> &ensp; " + obj.getCodRfc() + "&ensp; </td>" 
				+ "<td> &ensp; " + obj.getCodProj() + "&ensp; </td>" 	
						+ "<td> &ensp; " + inspecaoObj.getResultado() + "&ensp; </td>" 
				+ "</tr>";

		return resultado;
	}

}
