package br.com.rsi.bean;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Messages;

import br.com.rsi.dao.complementos.ControleSiglasDAO;
import br.com.rsi.domain.complementos.ControleSiglas;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * -Classe BEAN ControleSiglasBean.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 *
 */

@ManagedBean
@SessionScoped
public class ControleSiglasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private ControleSiglas controle;
	private ControleSiglasDAO dao;
	private List<ControleSiglas> listaControle;
	static String CAMINHO = "";
	String path;
	private int total;

	/**
	 * Salva um objeto do tipo ControleSiglas
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
	 * Limpa a tabela do banco de dados
	 */
	// -------------------------------------------------------------------------------------
	public void limparDB() {
		try {
			listarInfos();
			for (ControleSiglas controleSiglas : listaControle) {
				ControleSiglas entidade = dao.buscar(controleSiglas.getCodigo());
				dao.excluir(entidade);
			}
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar ");
			System.out.println(e.getCause());
		}
	}

	/**
	 * Captura as informações de uma planilha xls e salva no banco de dados
	 */
	// -------------------------------------------------------------------------------------------
	public void salvarPlanilha() {
		controle = new ControleSiglas();
		dao = new ControleSiglasDAO();
		String sigla, sistema, linguagem, mapa, gestorEntrega, gestor, descricao, instrucao, andamento, repositorio;
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
				Cell celula3 = sheet.getCell(2, i); // coluna 3 - Linguagem
				Cell celula4 = sheet.getCell(3, i); // coluna 4 - Mapa
				Cell celula5 = sheet.getCell(4, i); // coluna 5 - Gestor de Entrega
				Cell celula6 = sheet.getCell(5, i); // coluna 6 - GestorSigla
				Cell celula7 = sheet.getCell(6, i); // coluna 7 - Descrição
				Cell celula8 = sheet.getCell(7, i); // coluna 8 - Instrução
				Cell celula10 = sheet.getCell(10, i); // coluna 11 - Andamento
				Cell celula13 = sheet.getCell(12, i); // coluna 13 - Repositório

				sigla = celula1.getContents().toString().trim().toUpperCase();
				sistema = celula2.getContents().toString().trim().toUpperCase();
				linguagem = celula3.getContents().toString().trim().toUpperCase();
				mapa = celula4.getContents().toString().trim().toUpperCase();
				gestorEntrega = celula5.getContents().toString().trim().toUpperCase();
				gestor = celula6.getContents().toString().trim().toUpperCase();
				descricao = celula7.getContents().toString().trim().toUpperCase();
				instrucao = celula8.getContents().toString().trim().toUpperCase();
				andamento = celula10.getContents().toString().trim().toUpperCase();
				repositorio = celula13.getContents().toString().trim().toUpperCase();

				// Encerra a leitura quando encontra linha vazia
				if (sigla.isEmpty()) {
					break;
				}

				if (!sigla.isEmpty()) {
					dateC = new Date();
					controle.setSigla(sigla);
					controle.setNomeSistema(sistema);
					controle.setLinguagem(linguagem);
					controle.setMapa(mapa);
					controle.setGestoEntrega(gestorEntrega);
					controle.setResponsavel(gestor);
					controle.setDescricao(descricao);
					controle.setInstrucoes(instrucao);
					controle.setAndamento(andamento);
					controle.setDataCadastro(dateC);
					controle.setNomeArquivo(CAMINHO);
					controle.setRepositorio(repositorio);
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
	 * Lista os objetos do tipo ControleSiglas
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {
			dao = new ControleSiglasDAO();
			listaControle = dao.listar();
			total = listaControle.size();
			Messages.addGlobalInfo("Lista Atualizada!");

		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		} finally {
		}
	}

	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	public ControleSiglas getControle() {
		return controle;
	}

	public void setControle(ControleSiglas controle) {
		this.controle = controle;
	}

	public static String getCAMINHO() {
		return CAMINHO;
	}

	public static void setCAMINHO(String cAMINHO) {
		CAMINHO = cAMINHO;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		CAMINHO = path;
	}

	public List<ControleSiglas> getListaControle() {
		return listaControle;
	}

	public void setListaControle(List<ControleSiglas> listaControle) {
		this.listaControle = listaControle;
	}

}
