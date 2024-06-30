package com.currencyconverter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/648262cef3da757b6c0e4645/latest/USD"; // Reemplaza con la URL de tu API
    private static final String API_KEY = "648262cef3da757b6c0e4645"; // Reemplaza con tu clave de API
    private static List<String> historial = new ArrayList<>();

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                System.out.println("Response JSON: " + jsonResponse); // Imprimir la respuesta JSON para depuración
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

                if (jsonObject.has("conversion_rates")) {
                    JsonObject ratesObject = jsonObject.get("conversion_rates").getAsJsonObject();

                    // Obtener las tasas de cambio
                    double arsRate = ratesObject.has("ARS") ? ratesObject.get("ARS").getAsDouble() : 0.0;
                    double bobRate = ratesObject.has("BOB") ? ratesObject.get("BOB").getAsDouble() : 0.0;
                    double brlRate = ratesObject.has("BRL") ? ratesObject.get("BRL").getAsDouble() : 0.0;
                    double clpRate = ratesObject.has("CLP") ? ratesObject.get("CLP").getAsDouble() : 0.0;
                    double copRate = ratesObject.has("COP") ? ratesObject.get("COP").getAsDouble() : 0.0;
                    double usdRate = ratesObject.has("USD") ? ratesObject.get("USD").getAsDouble() : 0.0;
                    double mxnRate = ratesObject.has("MXN") ? ratesObject.get("MXN").getAsDouble() : 0.0;
                    double eurRate = ratesObject.has("EUR") ? ratesObject.get("EUR").getAsDouble() : 0.0;
                    double gbpRate = ratesObject.has("GBP") ? ratesObject.get("GBP").getAsDouble() : 0.0;
                    double jpyRate = ratesObject.has("JPY") ? ratesObject.get("JPY").getAsDouble() : 0.0;

                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        System.out.println("Seleccione la moneda a convertir:");
                        System.out.println("1. ARS (Peso argentino)");
                        System.out.println("2. BOB (Boliviano boliviano)");
                        System.out.println("3. BRL (Real brasileño)");
                        System.out.println("4. CLP (Peso chileno)");
                        System.out.println("5. COP (Peso colombiano)");
                        System.out.println("6. USD (Dólar estadounidense)");
                        System.out.println("7. MXN (Peso mexicano)");
                        System.out.println("8. EUR (Euro)");
                        System.out.println("9. GBP (Libra esterlina)");
                        System.out.println("10. JPY (Yen japonés)");
                        System.out.println("11. Ver historial de conversiones");
                        System.out.println("12. Salir");
                        int opcion = scanner.nextInt();

                        if (opcion == 12) {
                            break;
                        }

                        if (opcion == 11) {
                            mostrarHistorial();
                            continue;
                        }

                        System.out.println("Ingrese la cantidad a convertir:");
                        double cantidad = scanner.nextDouble();
                        double resultado = 0.0;
                        String moneda = "";

                        switch (opcion) {
                            case 1:
                                resultado = convertirMoneda(cantidad, arsRate);
                                moneda = "ARS";
                                break;
                            case 2:
                                resultado = convertirMoneda(cantidad, bobRate);
                                moneda = "BOB";
                                break;
                            case 3:
                                resultado = convertirMoneda(cantidad, brlRate);
                                moneda = "BRL";
                                break;
                            case 4:
                                resultado = convertirMoneda(cantidad, clpRate);
                                moneda = "CLP";
                                break;
                            case 5:
                                resultado = convertirMoneda(cantidad, copRate);
                                moneda = "COP";
                                break;
                            case 6:
                                resultado = convertirMoneda(cantidad, usdRate);
                                moneda = "USD";
                                break;
                            case 7:
                                resultado = convertirMoneda(cantidad, mxnRate);
                                moneda = "MXN";
                                break;
                            case 8:
                                resultado = convertirMoneda(cantidad, eurRate);
                                moneda = "EUR";
                                break;
                            case 9:
                                resultado = convertirMoneda(cantidad, gbpRate);
                                moneda = "GBP";
                                break;
                            case 10:
                                resultado = convertirMoneda(cantidad, jpyRate);
                                moneda = "JPY";
                                break;
                            default:
                                System.out.println("Opción inválida");
                                continue;
                        }

                        guardarEnHistorial(cantidad, resultado, moneda);
                        System.out.println("Resultado: " + resultado + " " + moneda);
                    }
                    scanner.close();
                } else {
                    System.out.println("Error: la respuesta JSON no contiene la clave 'conversion_rates'");
                }
            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static double convertirMoneda(double cantidad, double tasaCambio) {
        return cantidad * tasaCambio;
    }

    private static void guardarEnHistorial(double cantidad, double resultado, String moneda) {
        historial.add("Convertido " + cantidad + " USD a " + resultado + " " + moneda);
    }

    private static void mostrarHistorial() {
        if (historial.isEmpty()) {
            System.out.println("No hay conversiones en el historial.");
        } else {
            System.out.println("Historial de conversiones:");
            for (String conversion : historial) {
                System.out.println(conversion);
            }
        }
    }
}
