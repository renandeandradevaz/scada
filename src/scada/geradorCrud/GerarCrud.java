package scada.geradorCrud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import scada.util.Util;
import scada.util.UtilReflection;

public class GerarCrud {

	private static final String HASH_PRIMEIRA_LETRA_MAIUSCULA = "OIAHPSODFIH349823OISHFD";
	private static final String HASH_PRIMEIRA_LETRA_MINUSCULA = "mohoidfgoih98745oihdog";
	private static List<String> tiposAceitaveis;

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		tiposAceitaveis = new ArrayList<String>();
		tiposAceitaveis.add("integer");
		tiposAceitaveis.add("string");
		tiposAceitaveis.add("bigdecimal");
		tiposAceitaveis.add("boolean");
		tiposAceitaveis.add("gregoriancalendar");

		Scanner scanner = new Scanner(System.in);

		System.out.print("Nome da entidade: ");
		String nomeEntidade = scanner.nextLine();
		nomeEntidade = nomeEntidade.replace(" ", "");

		System.out.println();

		System.out.println("Informe os atributos, separando-os por vírgula, de acordo com o padrão: 'nomeAtributo:Tipo'. Ex: codigo:String,preco:BigDecimal");
		System.out.println("Tipos aceitáveis: Integer, String, BigDecimal, Boolean, GregorianCalendar");
		String atributos = scanner.nextLine();
		atributos = atributos.replace(" ", "");

		gerarArquivos(nomeEntidade, atributos);

	}

	private static void gerarArquivos(String nomeEntidade, String atributosString) {

		if (validarAtributos(atributosString)) {

			String nomeEntidadePrimeiraLetraMaiuscula = nomeEntidade.substring(0, 1).toUpperCase() + nomeEntidade.substring(1);
			String nomeEntidadePrimeiraLetraMinuscula = nomeEntidade.substring(0, 1).toLowerCase() + nomeEntidade.substring(1);

			String pacotePrincipal = new UtilReflection().obterPacotePrincipal();

			List<String> atributos = new ArrayList<String>();
			String[] atributosArray = atributosString.split(",");

			for (int i = 0; i < atributosArray.length; i++) {

				atributos.add(atributosArray[i]);
			}

			gerarModelo(nomeEntidadePrimeiraLetraMaiuscula, nomeEntidadePrimeiraLetraMinuscula, pacotePrincipal, atributos);
			gerarController(nomeEntidadePrimeiraLetraMaiuscula, nomeEntidadePrimeiraLetraMinuscula, pacotePrincipal);
			gerarJspListar(nomeEntidadePrimeiraLetraMaiuscula, nomeEntidadePrimeiraLetraMinuscula, pacotePrincipal, atributos);
			gerarJspCriarEditar(nomeEntidadePrimeiraLetraMaiuscula, nomeEntidadePrimeiraLetraMinuscula, pacotePrincipal, atributos);

			System.out.println("Arquivos gerados com sucesso! Por favor, atualize o seu projeto para sincronizar as modificações");
		}
	}

	private static Boolean validarAtributos(String atributos) {

		if (Util.preenchido(atributos)) {

			String[] atributosArray = atributos.split(",");

			for (int i = 0; i < atributosArray.length; i++) {

				String atributo = atributosArray[i];

				if (!atributo.contains(":")) {

					System.out.println("O atributo " + atributo + " não está no padrão correto");
					return false;
				}

				String tipo = atributo.split(":")[1];
				if (Util.vazio(tipo) || !tiposAceitaveis.contains(tipo.toLowerCase())) {

					System.out.println("O tipo " + tipo + " não está na lista de tipos aceitáveis");
					return false;
				}
			}

			return true;
		}

		else {

			System.out.println("Informe os atributos!");

			return false;
		}

	}

	private static void gerarModelo(String nomeEntidadePrimeiraLetraMaiuscula, String nomeEntidadePrimeiraLetraMinuscula, String pacotePrincipal, List<String> atributos) {

		StringBuffer stringBufferAtributos = new StringBuffer();
		StringBuffer stringBufferImports = new StringBuffer();
		StringBuffer stringBufferSettersAndGetters = new StringBuffer();

		for (String atributo : atributos) {

			String tipo = atributo.split(":")[1];
			tipo = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);

			String nomeAtributo = atributo.split(":")[0];
			nomeAtributo = nomeAtributo.substring(0, 1).toLowerCase() + nomeAtributo.substring(1);

			String nomeAtributoPrimeiraLetraMaiuscula = nomeAtributo.substring(0, 1).toUpperCase() + nomeAtributo.substring(1);

			if (tipo.toLowerCase().equals("gregoriancalendar")) {

				if (!stringBufferImports.toString().contains("import java.util.GregorianCalendar;")) {

					stringBufferImports.append("\nimport java.util.GregorianCalendar;");
				}

				stringBufferAtributos.append("    private GregorianCalendar " + nomeAtributo + ";\n");

				stringBufferSettersAndGetters.append("\n\n    public GregorianCalendar get" + nomeAtributoPrimeiraLetraMaiuscula + "() {\n        return " + nomeAtributo + ";\n    }");
				stringBufferSettersAndGetters.append("\n\n    public void set" + nomeAtributoPrimeiraLetraMaiuscula + "(GregorianCalendar " + nomeAtributo + "){\n        this." + nomeAtributo + " = " + nomeAtributo + ";\n    }");

			}

			else if (tipo.toLowerCase().equals("bigdecimal")) {

				if (!stringBufferImports.toString().contains("import java.math.BigDecimal;")) {

					stringBufferImports.append("\nimport java.math.BigDecimal;");
				}

				stringBufferAtributos.append("    private BigDecimal " + nomeAtributo + ";\n");

				stringBufferSettersAndGetters.append("\n\n    public BigDecimal get" + nomeAtributoPrimeiraLetraMaiuscula + "() {\n        return " + nomeAtributo + ";\n    }");
				stringBufferSettersAndGetters.append("\n\n    public void set" + nomeAtributoPrimeiraLetraMaiuscula + "(BigDecimal " + nomeAtributo + "){\n        this." + nomeAtributo + " = " + nomeAtributo + ";\n    }");
			}

			else {

				stringBufferAtributos.append("    private " + tipo + " " + nomeAtributo + ";\n");

				stringBufferSettersAndGetters.append("\n\n    public " + tipo + " get" + nomeAtributoPrimeiraLetraMaiuscula + "() {\n        return " + nomeAtributo + ";\n    }");
				stringBufferSettersAndGetters.append("\n\n    public void set" + nomeAtributoPrimeiraLetraMaiuscula + "(" + tipo + " " + nomeAtributo + "){\n        this." + nomeAtributo + " = " + nomeAtributo + ";\n    }");
			}

		}

		BufferedReader reader = null;
		PrintWriter writer = null;
		String line = null;

		try {
			reader = new BufferedReader(new FileReader("src/" + pacotePrincipal + "/geradorCrud/modelo/" + HASH_PRIMEIRA_LETRA_MAIUSCULA + ".java"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		try {
			writer = new PrintWriter(new FileWriter("src/" + pacotePrincipal + "/modelo/" + nomeEntidadePrimeiraLetraMaiuscula + ".java"));
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			while ((line = reader.readLine()) != null) {

				line = line.replaceAll(HASH_PRIMEIRA_LETRA_MAIUSCULA, nomeEntidadePrimeiraLetraMaiuscula);
				line = line.replaceAll(HASH_PRIMEIRA_LETRA_MINUSCULA, nomeEntidadePrimeiraLetraMinuscula);
				line = line.replaceAll("geradorCrud.", "");
				line = line.replaceAll("// Imports aqui", stringBufferImports.toString());
				line = line.replaceAll("// Atributos aqui", stringBufferAtributos.toString());
				line = line.replaceAll("// Getters and setters aqui", stringBufferSettersAndGetters.toString());

				writer.println(line);
			}
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		writer.close();
	}

	private static void gerarController(String nomeEntidadePrimeiraLetraMaiuscula, String nomeEntidadePrimeiraLetraMinuscula, String pacotePrincipal) {

		BufferedReader reader = null;
		PrintWriter writer = null;
		String line = null;

		try {
			reader = new BufferedReader(new FileReader("src/" + pacotePrincipal + "/geradorCrud/controller/" + HASH_PRIMEIRA_LETRA_MAIUSCULA + "Controller.java"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		try {
			writer = new PrintWriter(new FileWriter("src/" + pacotePrincipal + "/controller/" + nomeEntidadePrimeiraLetraMaiuscula + "Controller.java"));
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			while ((line = reader.readLine()) != null) {

				line = line.replaceAll(HASH_PRIMEIRA_LETRA_MAIUSCULA, nomeEntidadePrimeiraLetraMaiuscula);
				line = line.replaceAll(HASH_PRIMEIRA_LETRA_MINUSCULA, nomeEntidadePrimeiraLetraMinuscula);
				line = line.replaceAll("geradorCrud.", "");
				writer.println(line);
			}
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		writer.close();
	}

	private static void gerarJspListar(String nomeEntidadePrimeiraLetraMaiuscula, String nomeEntidadePrimeiraLetraMinuscula, String pacotePrincipal, List<String> atributos) {

		StringBuffer stringBufferCamposPesquisa = new StringBuffer();
		StringBuffer stringBufferThLabel = new StringBuffer();
		StringBuffer stringBufferTdValor = new StringBuffer();

		for (String atributo : atributos) {

			String tipo = atributo.split(":")[1];
			tipo = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);

			String nomeAtributo = atributo.split(":")[0];
			nomeAtributo = nomeAtributo.substring(0, 1).toLowerCase() + nomeAtributo.substring(1);

			String nomeAtributoPrimeiraLetraMaiuscula = nomeAtributo.substring(0, 1).toUpperCase() + nomeAtributo.substring(1);

			String input = "    <input type=\"text\" class=\"input-small\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"\\${sessaoGeral.valor.get('" + nomeEntidadePrimeiraLetraMinuscula + "')." + nomeAtributo + "}\" placeholder=\"" + nomeAtributoPrimeiraLetraMaiuscula + "\">\n";

			if (tipo.toLowerCase().equals("boolean")) {

				input = "    <input type=\"checkbox\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"\\${sessaoGeral.valor.get('" + nomeEntidadePrimeiraLetraMinuscula + "')." + nomeAtributo + "}\" >\n";
			}

			if (tipo.toLowerCase().equals("integer")) {

				input = "    <input type=\"text\" class=\"input-small numero-inteiro\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"\\${sessaoGeral.valor.get('" + nomeEntidadePrimeiraLetraMinuscula + "')." + nomeAtributo + "}\" placeholder=\"" + nomeAtributoPrimeiraLetraMaiuscula + "\">\n";
			}

			if (tipo.toLowerCase().equals("bigdecimal")) {

				input = "    <input type=\"text\" class=\"input-small numero-decimal\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"\\${sessaoGeral.valor.get('" + nomeEntidadePrimeiraLetraMinuscula + "')." + nomeAtributo + "}\" placeholder=\"" + nomeAtributoPrimeiraLetraMaiuscula + "\">\n";
			}

			if (tipo.toLowerCase().equals("gregoriancalendar")) {

				input = "    <input type=\"text\" class=\"input-small data\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"<fmt:formatDate value=\"\\${sessaoGeral.valor.get('" + nomeEntidadePrimeiraLetraMinuscula + "')." + nomeAtributo + ".time}\" />\" placeholder=\"" + nomeAtributoPrimeiraLetraMaiuscula + "\">\n";
			}

			stringBufferCamposPesquisa.append(input);

			stringBufferThLabel.append("\n                    <th> " + nomeAtributoPrimeiraLetraMaiuscula + " </th>");

			String td = "\n                        <td> \\${item." + nomeAtributo + "} </td>";

			if (tipo.toLowerCase().equals("gregoriancalendar")) {

				td = "\n                        <td> <fmt:formatDate value=\"\\${item." + nomeAtributo + ".time}\" /> </td>";
			}

			if (tipo.toLowerCase().equals("bigdecimal")) {

				td = "\n                        <td> <fmt:formatNumber value=\"\\${item." + nomeAtributo + "}\" /> </td>";
			}

			if (tipo.toLowerCase().equals("boolean")) {

				td = "\n                        <td> <tags:simNao valor=\"\\${item." + nomeAtributo + "}\" /> </td>";
			}

			stringBufferTdValor.append(td);

		}

		BufferedReader reader = null;
		PrintWriter writer = null;
		String line = null;

		try {
			reader = new BufferedReader(new FileReader("src/" + pacotePrincipal + "/geradorCrud/listar" + HASH_PRIMEIRA_LETRA_MAIUSCULA + "s.jsp"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		String pasta = "WebContent/WEB-INF/jsp/" + nomeEntidadePrimeiraLetraMinuscula;
		File file = new File(pasta);
		file.mkdir();

		try {
			writer = new PrintWriter(new FileWriter(pasta + "/listar" + nomeEntidadePrimeiraLetraMaiuscula + "s.jsp"));
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			while ((line = reader.readLine()) != null) {

				line = line.replaceAll(HASH_PRIMEIRA_LETRA_MAIUSCULA, nomeEntidadePrimeiraLetraMaiuscula);
				line = line.replaceAll(HASH_PRIMEIRA_LETRA_MINUSCULA, nomeEntidadePrimeiraLetraMinuscula);
				line = line.replaceAll("<!-- Campos de pesquisa aqui -->", stringBufferCamposPesquisa.toString());
				line = line.replaceAll("<!-- Th label aqui -->", stringBufferThLabel.toString().substring(2));
				line = line.replaceAll("<!-- Td valor aqui -->", stringBufferTdValor.toString().substring(2));

				writer.println(line);
			}
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		writer.close();
	}

	private static void gerarJspCriarEditar(String nomeEntidadePrimeiraLetraMaiuscula, String nomeEntidadePrimeiraLetraMinuscula, String pacotePrincipal, List<String> atributos) {

		StringBuffer stringBufferInputs = new StringBuffer();

		for (String atributo : atributos) {

			String tipo = atributo.split(":")[1];
			tipo = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);

			String nomeAtributo = atributo.split(":")[0];
			nomeAtributo = nomeAtributo.substring(0, 1).toLowerCase() + nomeAtributo.substring(1);

			String nomeAtributoPrimeiraLetraMaiuscula = nomeAtributo.substring(0, 1).toUpperCase() + nomeAtributo.substring(1);

			String input = "        <input type=\"text\" class=\"input-xlarge\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"" + "\\${" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "}\">\n";

			if (tipo.toLowerCase().equals("boolean")) {

				input = "        <input type=\"checkbox\" <c:if test=\"\\${" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "}\"> checked=\"checked\" </c:if> class=\"input-xlarge\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\">\n";
			}

			if (tipo.toLowerCase().equals("integer")) {

				input = "        <input type=\"text\" class=\"input-xlarge numero-inteiro\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"" + "\\${" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "}\">\n";
			}

			if (tipo.toLowerCase().equals("bigdecimal")) {

				input = "        <input type=\"text\" class=\"input-xlarge numero-decimal\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"<fmt:formatNumber value=\"\\${" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "}\"/>\">\n";
			}

			if (tipo.toLowerCase().equals("gregoriancalendar")) {

				input = "        <input type=\"text\" class=\"input-xlarge data\" name=\"" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + "\" value=\"<fmt:formatDate value=\"" + "\\${" + nomeEntidadePrimeiraLetraMinuscula + "." + nomeAtributo + ".time}\"/>\">\n";
			}

			stringBufferInputs.append("    <div class=\"control-group\">\n");
			stringBufferInputs.append("      <label class=\"control-label\">" + nomeAtributoPrimeiraLetraMaiuscula + "</label>\n");
			stringBufferInputs.append("      <div class=\"controls\">\n");
			stringBufferInputs.append(input);
			stringBufferInputs.append("      </div>\n");
			stringBufferInputs.append("    </div>\n");
		}

		BufferedReader reader = null;
		PrintWriter writer = null;
		String line = null;

		try {
			reader = new BufferedReader(new FileReader("src/" + pacotePrincipal + "/geradorCrud/criarEditar" + HASH_PRIMEIRA_LETRA_MAIUSCULA + ".jsp"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		String pasta = "WebContent/WEB-INF/jsp/" + nomeEntidadePrimeiraLetraMinuscula;

		try {
			writer = new PrintWriter(new FileWriter(pasta + "/criarEditar" + nomeEntidadePrimeiraLetraMaiuscula + ".jsp"));
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			while ((line = reader.readLine()) != null) {

				line = line.replaceAll(HASH_PRIMEIRA_LETRA_MAIUSCULA, nomeEntidadePrimeiraLetraMaiuscula);
				line = line.replaceAll(HASH_PRIMEIRA_LETRA_MINUSCULA, nomeEntidadePrimeiraLetraMinuscula);
				line = line.replaceAll("<!-- Campos de input aqui -->", stringBufferInputs.toString());

				writer.println(line);
			}
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		writer.close();
	}

}
