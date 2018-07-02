package br.com.rsi.alertaGit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GitList {

	public String alertaGit(String sigla, String sistema,Date dataCommit, Date dataAnt, boolean alteracao) {
		String resultado;
		StringBuffer estiloH2 = new StringBuffer();
		String dataTxt="---";
		String dataTxtAnt="---";
		
		try {
			java.util.Date dataTemp = dataCommit;
	        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	        dataTxt= format.format(dataTemp);
	        
			java.util.Date dataTempAnt = dataCommit;
	        SimpleDateFormat formatAnt = new SimpleDateFormat("dd/MM/yyyy");
	        dataTxtAnt= formatAnt.format(dataTempAnt);
			
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
		resultado = " <tr><td> &ensp;  &ensp; &ensp; <img src='http://icons.iconarchive.com/icons/custom-icon-design/pretty-office-8/128/Accept-icon.png' width='20' height='20' align='BASELINE' /> </td>" 
		+ "<td> &ensp; &ensp;"+  sigla + " </td>" 
				+ "<td> &ensp; &ensp;"+  sistema + " </td>" 
		+ "<td> &ensp; &ensp;"+  dataTxt + " </td>"
			+ "<td> &ensp; &ensp;"+  dataTxtAnt + " </td>"
				+ "</tr>" ;

		 //Caso de erro
		 } else {
				resultado = " <tr><td> &ensp;  &ensp; &ensp; <img src='https://cdn.icon-icons.com/icons2/1380/PNG/512/vcsconflicting_93497.png' width='20' height='20' align='BASELINE' /> </td>" 
						+ "<td> &ensp; &ensp;"+  sigla + " </td>" 
								+ "<td> &ensp; &ensp;"+  sistema + " </td>" 
						+ "<td> &ensp; &ensp;"+  dataTxt + " </td>"
							+ "<td> &ensp; &ensp;"+  dataTxtAnt + " </td>"
								+ "</tr>" ;
		 }

		return resultado;
	}

}
