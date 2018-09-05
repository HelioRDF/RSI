package tratamentos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import br.com.rsi.domain.usuarios.Usuario;

public class GetWebService {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		GetWebService http = new GetWebService();
		Gson g = new Gson();
		Usuario u = new Usuario();
		// Type usuarioType = new TypeToken<Usuario>() {
		// }.getType();

		// u.setLogin("programatche");
		// u.setEmail("aaa.com");
		// u.setSenha("4312");
		// u.setPerfil("Admin");
		// String json = g.toJson(u, usuarioType);
		String url = "http://localhost:9000/api/projects/search";
		// String result = http.sendGet(url,"GET");
		System.out.println("\nSaída:"+http.sendGet(url, "GET").toString());

		// http.sendPost(url, json, "PUT");

	}

	// HTTP GET request
	private String sendGet(String url, String method) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod(method);

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();

	}

}
