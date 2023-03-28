import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        
        // fazer uma conexão HTTP e buscar os top 250 filmes
		String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/MostPopularMovies.json";
		URI endereco = URI.create(url);
		var client = HttpClient.newHttpClient();
		var request = HttpRequest.newBuilder(endereco).GET().build();
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		String body = response.body();
		System.out.println(body);
		
		// extrair somente os dados que interessam (titulo, poster, nota)
		var parser = new JsonParser();
		List<Map<String, String>> listaDeFilmes = parser.parse(body);
		
		// exibir os dados
		var geradora = new GeradoraDeFigurinhas();
		var diretorio = new File("figurinhas/");
		diretorio.mkdir();
		for (Map<String, String> filme : listaDeFilmes) {
			
			
			String urlImagem = filme.get("image");
			String titulo = filme.get("title");
			titulo = titulo.replace(":", " -");
			double classificacao = Double.parseDouble(filme.get("imDbRating"));
			InputStream inputStream = new URL(urlImagem).openStream();
			String nomeArquivo = "figurinhas/" + titulo + ".png";
			int numeroEstrelinhas = (int) classificacao;
			String textoFigurinha;
			InputStream imagemFabio;
			if (classificacao >= 8.0){
				textoFigurinha = "TOPZERA";
				imagemFabio = new FileInputStream(new File("fotos/like.jpg"));
			}else{
				textoFigurinha = "Hmmm...";
				imagemFabio = new FileInputStream(new File("fotos/dislike.jpg"));
			}

			geradora.cria(inputStream, nomeArquivo, textoFigurinha, imagemFabio);

			System.out.println("\u001b[1mTitulo:\u001b[m " + filme.get("title"));
			System.out.println("\u001b[1mImagem:\u001b[m " + filme.get("image"));
			System.out.println("\u001b[1mClassificacao:\u001b[m " + filme.get("imDbRating"));
			
			for (int n = 0; n <= numeroEstrelinhas; n++) {
				System.out.print("⭐");
			}
			System.out.println("\n");
		}
	}

}