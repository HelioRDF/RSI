package tratamentos;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author helio.franca
 * @version v2.1.9
 * @since 23-08-2018
 * 
 */
public class TrataDados {

	public static void main(String[] args) {
		System.out.println(dateParaTexto(dataTxtToDate("2018/08/23")));
	}

	// ------------------------------------------------------------------------
	/**
	 * Converte uma data String (yyyy/mm/dd) para Date.
	 * 
	 * @param data
	 *            - String de texto data.
	 * @return Retorna um Objeto Data
	 */
	public static Date dataTxtToDate(String data) {
		Date dataResultado = null;
		String array[] = new String[3];
		array = data.split("/");

		try {
			data = array[0].trim() + "/" + array[1].trim() + "/" + array[2].trim();
			@SuppressWarnings("deprecation")
			Date dataCommit = new Date(data);
			dataResultado = dataCommit;

		} catch (Exception e) {
			System.out.println(dataTxtToDate("Erro" + dataResultado));
		}
		return dataResultado;
	}

	// ------------------------------------------------------------------------
	/**
	 * Converte um date para String (dd/mm/yyyy).
	 * 
	 * @param data
	 *            - String de texto data.
	 * @return Retorna um Objeto Data
	 */
	public static String dateParaTexto(Date data) {

		Date data2 = data;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data2);
		System.out.println(dataFormatada);
		return dataFormatada;
	}

	// ------------------------------------------------------------------------
	/**
	 * Retorna uma linha TD para tabela HTML
	 * 
	 * @param informacao
	 *            - String de texto com os dados da Coluna.
	 * @return Retorna um Objeto String
	 */
	public static String incluirHtmlTd(String informacao) {
		String linha = "<td style='font-size:10px;' > &ensp;" + informacao + " &ensp; </td>";
		return linha;
	}

	// ------------------------------------------------------------------------
	/**
	 * Retorna uma linha TD para tabela HTML com CSS
	 * 
	 * @param informacao
	 *            - String de texto com os dados da Coluna.
	 * @param txtStyleCss
	 *            - String com argumento css
	 * @param simbolo
	 *            - String com simbolos que representa os dados, exemplo '%', '$'
	 * @return Retorna um Objeto String
	 */
	public static String incluirHtmlTdStyle(String informacao, String txtStyleCss, String simbolo) {
		String linha = "<td " + txtStyleCss + " > &ensp;" + informacao + simbolo+" &ensp; </td>";
		return linha;
	}

}