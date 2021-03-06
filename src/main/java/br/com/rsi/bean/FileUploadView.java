package br.com.rsi.bean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.faces.bean.ManagedBean;

import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * -Classe de captura de eventos do FileUpload PrimeFaces
 * 
 * @author helio.franca
 * @version v2.2.5
 * @since 04-09-2018
 *
 */

@ManagedBean
public class FileUploadView {

	/**
	 * handleFileUpload p/ RFC
	 * 
	 * @param event
	 *            - Evento
	 */
	// -----------------------------------------------------------------------------------
	public void handleFileUpload(FileUploadEvent event) {

		// Cria Pasta tempCarga caso não exista
		new File("C:\\TempCargaRFC").mkdir();
		UploadedFile arq = event.getFile();

		// Cria um arquivo UploadFile, para receber o arquivo do evento
		try (InputStream in = new BufferedInputStream(arq.getInputstream())) {

			// copiar para pasta do projeto
			File file = new File("C:/TempCargaRFC/" + arq.getFileName());
			// O método file.getAbsolutePath() fornece o caminho do arquivo criado
			// Pode ser usado para ligar algum objeto do banco ao arquivo enviado
			RFCBean.CAMINHO = file.getAbsolutePath();

			try (FileOutputStream fout = new FileOutputStream(file)) {
				while (in.available() != 0) {
					fout.write(in.read());
				}
			}
		} catch (Exception ex) {
			Messages.addGlobalError("Falha ao carregar arquivo:");
		}
		RFCBean bean = new RFCBean();
		bean.salvarPlanilha();
	}

	/**
	 * handleFileUpload p/ Controle de Siglas
	 * 
	 * @param event
	 *            - Evento
	 */
	// ---------------------------------------------------------------------
	public void handleFileUploadControle(FileUploadEvent event) {
		new File("C:\\TempCargaRFC").mkdir();
		UploadedFile arq = event.getFile();
		try (InputStream in = new BufferedInputStream(arq.getInputstream())) {
			File file = new File("C:/TempCargaRFC/" + arq.getFileName());
			ControleSiglasBean.CAMINHO = file.getAbsolutePath();
			try (FileOutputStream fout = new FileOutputStream(file)) {
				while (in.available() != 0) {
					fout.write(in.read());
				}
			}
		} catch (Exception ex) {
			Messages.addGlobalError("Falha ao carregar arquivo:");
		}
		ControleSiglasBean bean = new ControleSiglasBean();
		bean.salvarPlanilha();
	}

	/**
	 * handleFileUpload p/ controle git hk
	 * 
	 * @param event
	 *            - Evento
	 */
	// -----------------------------------------------------------------------------
	public void handleFileUploadGitHK(FileUploadEvent event) {
		new File("C:\\TempCargaRFC").mkdir();
		UploadedFile arq = event.getFile();
		try (InputStream in = new BufferedInputStream(arq.getInputstream())) {

			File file = new File("C:/TempCargaRFC/" + arq.getFileName());
			ControleGitHKBean.CAMINHO = file.getAbsolutePath();
			try (FileOutputStream fout = new FileOutputStream(file)) {
				while (in.available() != 0) {
					fout.write(in.read());
				}
			}
		} catch (Exception ex) {
			Messages.addGlobalError("Falha ao carregar arquivo:");
		}
		ControleGitHKBean bean = new ControleGitHKBean();
		bean.salvarPlanilha();
	}

	/**
	 * handleFileUpload p/ Controle Git Dev
	 * 
	 * @param event
	 *            - Evento
	 */
	// -----------------------------------------------------------------------------
	public void handleFileUploadGitDev(FileUploadEvent event) {
		new File("C:\\TempCargaRFC").mkdir();
		UploadedFile arq = event.getFile();
		try (InputStream in = new BufferedInputStream(arq.getInputstream())) {

			File file = new File("C:/TempCargaRFC/" + arq.getFileName());
			ControleGitDevBean.CAMINHO = file.getAbsolutePath();
			try (FileOutputStream fout = new FileOutputStream(file)) {
				while (in.available() != 0) {
					fout.write(in.read());
				}
			}
		} catch (Exception ex) {
			Messages.addGlobalError("Falha ao carregar arquivo:");
		}
		ControleGitDevBean bean = new ControleGitDevBean();
		bean.salvarPlanilha();
	}

	/**
	 * handleFileUpload p/ Controle RTC Dev
	 * 
	 * @param event
	 *            - Evento
	 */
	// -----------------------------------------------------------------------------
	public void handleFileUploadRtcDev(FileUploadEvent event) {
		new File("C:\\TempCargaRFC").mkdir();
		UploadedFile arq = event.getFile();
		try (InputStream in = new BufferedInputStream(arq.getInputstream())) {

			File file = new File("C:/TempCargaRFC/" + arq.getFileName());
			ControleRtcDevBean.CAMINHO = file.getAbsolutePath();
			try (FileOutputStream fout = new FileOutputStream(file)) {
				while (in.available() != 0) {
					fout.write(in.read());
				}
			}
		} catch (Exception ex) {
			Messages.addGlobalError("Falha ao carregar arquivo:");
		}
		ControleRtcDevBean bean = new ControleRtcDevBean();
		bean.salvarPlanilha();
	}

	/**
	 * handleFileUpload p/ Controle RTC HK
	 * 
	 * @param event
	 *            - Evento
	 */
	// -----------------------------------------------------------------------------
	public void handleFileUploadRtcHK(FileUploadEvent event) {
		new File("C:\\TempCargaRFC").mkdir();
		UploadedFile arq = event.getFile();
		try (InputStream in = new BufferedInputStream(arq.getInputstream())) {

			File file = new File("C:/TempCargaRFC/" + arq.getFileName());
			ControleRtcHKBean.CAMINHO = file.getAbsolutePath();
			try (FileOutputStream fout = new FileOutputStream(file)) {
				while (in.available() != 0) {
					fout.write(in.read());
				}
			}
		} catch (Exception ex) {
			Messages.addGlobalError("Falha ao carregar arquivo:");
		}
		ControleRtcHKBean bean = new ControleRtcHKBean();
		bean.salvarPlanilha();
	}
}