package br.com.rsi.alertaEmail;

import java.text.SimpleDateFormat;

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
		String corResultado = "style='color:blue;'";
		String imgCommit = "https://image.freepik.com/icones-gratis/ponto-de-interrogacao-em-um-esboco-do-circulo_318-53407.jpg";

		if (inspecaoObj.getResultado().equalsIgnoreCase("Alerta")) {
			corResultado = "style='color:orange;'";
		} else {
			corResultado = "style='color:#12d812;'";
		}
		
		if(inspecaoObj.getTipo().equalsIgnoreCase("Legado")) {
			imgCommit = "https://firebasestorage.googleapis.com/v0/b/cccc-4ff93.appspot.com/o/vermelho.png?alt=media&token=ec40fab6-97da-4d83-aa83-3be4ed080d9e";
		}else {	
			imgCommit = "https://firebasestorage.googleapis.com/v0/b/cccc-4ff93.appspot.com/o/verde.png?alt=media&token=bbcbc111-b495-400a-9c66-12b6f2f2b20b";
		}	
		
		
	
		resultado = " <tr><td> &ensp;   <img src='"+imgCommit+"' width='20' height='20'  align='center' /> </td>"
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
				+ "<td> &ensp; " + obj.getLider() + "&ensp; </td>" 
				+ "<td "+corResultado+"> &ensp; " + inspecaoObj.getResultado() + "&ensp; </td>" 
				+ "</tr>";
		return resultado;
	}

}
