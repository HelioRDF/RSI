package br.com.rsi.bean;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.rsi.dao.complementos.RFCDAO;
import br.com.rsi.domain.complementos.RFC;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

@ManagedBean
@SessionScoped
public class RFCBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private RFC rFC;
	private RFCDAO daoRFC;
	private List<RFC> listaRFC;
	private int total;
	static String CAMINHO = "";
	String siglaAtual;

	// Salvar usuário
	// -------------------------------------------------------------------------------------
	public void salvar() {
		try {
			daoRFC.salvar(rFC);
			Messages.addGlobalInfo(siglaAtual + " - Salva | Inspeção:" + rFC.getCodInspecao());
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar a Silga:" + siglaAtual);
			System.out.println("Erro ao salvar --------------------------------------" + siglaAtual + e);
		} finally {
			fechar();
		}
	}

	// Novo
	// -------------------------------------------------------------------------------------------
	public void novo() {
		rFC = new RFC();
		daoRFC = new RFCDAO();
	}

	// Fechar
	// -------------------------------------------------------------------------------------------
	public void fechar() {
		RequestContext.getCurrentInstance().reset("formFiltro");
		rFC = new RFC();
		daoRFC = new RFCDAO();
	}

	// Excluir
	// -------------------------------------------------------------------------------------------
	public void excluir(ActionEvent evento) {
		try {
			rFC = (RFC) evento.getComponent().getAttributes().get("meuSelect");
			RFCDAO dao = new RFCDAO();
			Messages.addGlobalInfo("Removido com sucesso!!!");
			dao.excluir(rFC);
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Remover: ");
		} finally {
			fechar();
		}
	}

	// Editar
	// -------------------------------------------------------------------------------------------
	public void editar() {
		try {
			daoRFC = new RFCDAO();
			daoRFC.merge(rFC);
			Messages.addGlobalInfo(" Editado com sucesso!!!");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar ");
		} finally {
			fechar();
		}
	}

	// Instanciar
	// -------------------------------------------------------------------------------------------
	public void getinstancia(ActionEvent evento) {
		try {
			rFC = (RFC) evento.getComponent().getAttributes().get("meuSelect");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar: ");
		}
	}

	// Carrega no Banco de Dados
	// -------------------------------------------------------------------------------------------
	public void salvarPlanilha() {
		rFC = new RFC();
		daoRFC = new RFCDAO();
		String codRfc, codProj, sigla, observacao, status, codVazio, codInspecao;
		String dataCadastro = "";
		String dataInspecao = "";
		Date dateC = new Date();
		Date dateI = new Date();
		String salvarSigla;
		int setCodInspecaoInt = 0;
		int setCodInspecaoAnteriorInt = 0;

		// Carrega a planilha
		Workbook workbook = null;

		try {
			workbook = Workbook.getWorkbook(new File(CAMINHO));
		} catch (Exception e) {
			System.out.println("Catch 01 ---------------------------------");
			e.printStackTrace();
		}
		// Seleciona a aba do excel
		Sheet sheet = workbook.getSheet(0);
		// Numero de linhas com dados do xls
		int linhas = sheet.getRows();
		for (int i = 1; i < linhas; i++) {
			Cell celulaRFC = sheet.getCell(1, i); // coluna 1 -COD_RFC
			Cell celulaCodProj = sheet.getCell(2, i); // coluna 2 - COD_PROJ
			Cell celulaSigla = sheet.getCell(3, i); // coluna 3 - SIGLA
			Cell celulaCodInsp = sheet.getCell(4, i); // coluna 7 - Cod Inspeção
			Cell celulaDataC = sheet.getCell(5, i); // coluna 4 - DATA_CADASTRO
			Cell celulaDataI = sheet.getCell(6, i); // coluna 5 - DATA_INSPECAO
			Cell celulaObs = sheet.getCell(7, i); // coluna 6 - OBSERVACAO
			Cell celulaStatus = sheet.getCell(8, i); // coluna 7 - STATUS
			Cell celulaCodVazio = sheet.getCell(9, i); // coluna 7 - Cod_Vazio


			Cell celula11 = sheet.getCell(10, i); // coluna 10 - Salvar no Banco

			codRfc = celulaRFC.getContents().toString().trim().toUpperCase(); // Coluna 1:COD_RFC
			codProj = celulaCodProj.getContents().toString().trim().toUpperCase(); // Coluna 2:COD_PROJ
			sigla = celulaSigla.getContents().toString().trim().toUpperCase(); // Coluna 3:SIGLA
			dataCadastro = celulaDataC.getContents().toString().trim().toUpperCase(); // Coluna 4:DATA_CADASTRO DB
			dataInspecao = celulaDataI.getContents().toString().trim().toUpperCase(); // Coluna 5:DATA_INSPECAO
			observacao = celulaObs.getContents().toString().trim().toUpperCase(); // Coluna 6:OBSERVACAO
			status = celulaStatus.getContents().toString().trim().toUpperCase(); // Coluna 7:STATUS
			codVazio = celulaCodVazio.getContents().toString().trim().toUpperCase(); // Coluna 8:Cod_Vazio
			codInspecao = celulaCodInsp.getContents().toString().trim().toUpperCase(); // Coluna 9:Cod_Inspeção
																							// Anterior
			salvarSigla = celula11.getContents().toString().trim().toUpperCase();// Coluna 11:Salvar no Banco

			// Encerra a leitura quando encontra linha vazia
			if (sigla.isEmpty()) {
				break;
			}
			try {
				setCodInspecaoInt = Integer.parseInt(codInspecao);

			} catch (Exception e) {
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Cod Inspeção - Erro XXXXXXXXXXXXXXX");
				setCodInspecaoInt = 0;
				setCodInspecaoAnteriorInt = 0;
			}

			if (!salvarSigla.isEmpty()) {
				dateC = validadorData(dataCadastro, "Data Cadastro");
				dateI = validadorData(dataInspecao, "Data Inspeção");
				rFC.setInspecionar(validarInspecao(codVazio, status));
				rFC.setCodRfc(codRfc);
				rFC.setCodProj(codProj);
				rFC.setSigla(sigla);
				rFC.setDataCadastro(dateC);
				rFC.setDataInspecao(dateI);
				rFC.setObservacao(observacao);
				rFC.setStatus(status);
				rFC.setCodVazio(codVazio);
				rFC.setCodInspecao(setCodInspecaoInt);
				rFC.setCodInspecaoAnterior(setCodInspecaoAnteriorInt);
				siglaAtual = sigla;
				salvar();
			}
		}
	}

	// Seleciona uma RFC da table
	// -------------------------------------------------------------------------------------------
	public void selecionarRFC(ActionEvent evento) {
		try {
			rFC = (RFC) evento.getComponent().getAttributes().get("meuSelect");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar: ");
		}
	}

	// Lista RFCs
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {
			daoRFC = new RFCDAO();
			listaRFC = daoRFC.listar();
			total = listaRFC.size();
			Messages.addGlobalInfo("Lista Atualizada!");

		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		} finally {
		}
	}

	// Validador de data
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public Date validadorData(String dataInfo, String msg) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		Date dataFinal = new Date();
		if (dataInfo.isEmpty()) {
			dataFinal = null;
		} else {
			try {
				dataFinal = (Date) formatter.parse(dataInfo);
			} catch (ParseException e) {
				dataFinal = null;
				System.out.println("\n-----------------------------------------Erro em data" + msg);
				e.printStackTrace();
			}
		}
		return dataFinal;
	}

	// Valida Inspeção
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public String validarInspecao(String vazio, String status) {
		String resultado = "NÃO";
		if (vazio.equalsIgnoreCase("false") && status.equalsIgnoreCase("ativa")) {
			resultado = "SIM";
		}
		System.out.println("--------------Inspecionar? ------- " + resultado);
		return resultado;
	}
	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getCaminho() {
		return CAMINHO;
	}

	public void setCaminho(String caminho) {
		RFCBean.CAMINHO = caminho;
	}

	public List<RFC> getListaRFC() {
		return listaRFC;
	}

	public void setListaRFC(List<RFC> listaRFC) {
		this.listaRFC = listaRFC;
	}

	public RFC getrFC() {
		return rFC;
	}

	public void setrFC(RFC rFC) {
		this.rFC = rFC;
	}
}