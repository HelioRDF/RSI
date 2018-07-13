package br.com.rsi.bean;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Messages;

import br.com.rsi.dao.complementos.ControleRtcDevDAO;
import br.com.rsi.domain.complementos.ControleRtcDev;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * -Classe BEAN ControleRtcDevBean.
 * 
 * @author helio.franca
 * @version v1.8
 * @since 12-07-2018
 *
 */

@ManagedBean
@SessionScoped
public class ControleRtcDevBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private ControleRtcDev controle;
	private ControleRtcDevDAO dao;
	private List<ControleRtcDev> listaControle;

	private int total;
	static String CAMINHO = "";

	/**
	 * Salva objeto do tipo ControleRtcDev
	 */
	// -------------------------------------------------------------------------------------
	public void salvar() {
		try {
			dao.salvar(controle);
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar ");
			System.out.println(e.getCause());
		}
	}

	/**
	 * Lista os objetos do tipo ControleRtcDev
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {
			dao = new ControleRtcDevDAO();
			listaControle = dao.listar();
			total = listaControle.size();

			Messages.addGlobalInfo("Lista Atualizada!");
		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		} finally {
		}
	}

	/**
	 * Captura as informações de uma planilha xls e salva no banco de dados
	 * 
	 */
	// -------------------------------------------------------------------------------------------
	public void salvarPlanilha() {
		controle = new ControleRtcDev();
		dao = new ControleRtcDevDAO();
		String sigla, sistema, caminho;
		Date dateC = new Date();

		// Carrega a planilha
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(new File(CAMINHO));

			// Seleciona a aba do excel
			Sheet sheet = workbook.getSheet(0);

			// Numero de linhas com dados do xls
			int linhas = sheet.getRows();
			limparDB();
			for (int i = 1; i < linhas; i++) {
				Cell celula1 = sheet.getCell(0, i); // coluna 1 -Sigla
				Cell celula2 = sheet.getCell(1, i); // coluna 2 -Sistema
				Cell celula3 = sheet.getCell(2, i); // coluna 3 - caminho

				sigla = celula1.getContents().toString().trim().toUpperCase();
				sistema = celula2.getContents().toString().trim().toUpperCase();

				caminho = celula3.getContents().toString().trim().toUpperCase();

				// Encerra a leitura quando encontra linha vazia
				if (sigla.isEmpty()) {
					break;
				}

				if (!sigla.isEmpty()) {
					dateC = new Date();
					controle.setSigla(sigla);
					controle.setNomeSistema(sistema);
					controle.setCaminho(caminho);

					controle.setNomeArquivo(CAMINHO);
					controle.setDataVerificacao(dateC);
					salvar();
				}
			}
			Messages.addGlobalInfo("Planilha salva com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Não foi possível salvar ");
		}
	}

	/**
	 * Limpa as informações da tabela ControleRtcDev no banco de dados
	 */
	// -------------------------------------------------------------------------------------
	public void limparDB() {
		try {
			listarInfos();
			for (ControleRtcDev ControleRtcDev : listaControle) {
				ControleRtcDev entidade = dao.buscar(ControleRtcDev.getCodigo());
				dao.excluir(entidade);
			}
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar ");
			System.out.println(e.getCause());
		}
	}

	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public ControleRtcDev getControle() {
		return controle;
	}

	public void setControle(ControleRtcDev controle) {
		this.controle = controle;
	}

	public List<ControleRtcDev> getListaControle() {
		return listaControle;
	}

	public void setListaControle(List<ControleRtcDev> listaControle) {
		this.listaControle = listaControle;
	}



	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public static String getCAMINHO() {
		return CAMINHO;
	}

	public static void setCAMINHO(String cAMINHO) {
		CAMINHO = cAMINHO;
	}



	// ------------------------------------------------------------------------------------------------------------------------------------------------------

}