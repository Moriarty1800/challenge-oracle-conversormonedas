import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Main {
    static void main(String[] args) {
        //Variables
        int opcion = 1;
        String monedaA = "";
        String monedaB = "";
        double AMOUNT = 0.0;
        String apiKey = "4b173fba272f4c4bb82b19b4";



        Scanner input = new Scanner(System.in);

        while (opcion != 3) {
            try {
                HttpClient client = HttpClient.newHttpClient();


                System.out.println("\nSistema conversor de monedas.");
                System.out.println("\nElige una opción.");
                System.out.println("\n1 - Tipos de cambio disponibles.");
                System.out.println("2 - Conversor de monedas.");
                System.out.println("3 - Salir.");
                opcion = input.nextInt();
                input.nextLine();

                if (opcion == 3) {
                    System.out.println("Adios.");
                    input.close();
                    break;

                } else if (opcion == 2) {
                    System.out.println("\nIngresa los tres caracteres de la moneda a convertir.");
                    monedaA = input.nextLine().trim().toUpperCase();
                    System.out.println("Ingresa los tres caracteres de la moneda a la que quieres convertir los "+ monedaA +".");
                    monedaB = input.nextLine().trim().toUpperCase();
                    System.out.println("¿Cuántos " + monedaA + " quieres convertir a " + monedaB + "?");
                    AMOUNT = input.nextDouble();
                    String direccion = "https://v6.exchangerate-api.com/v6/"+apiKey+"/pair/"+monedaA+"/"+monedaB+"/"+AMOUNT;
                    String direccionEnriquecidaB = "https://v6.exchangerate-api.com/v6/"+apiKey+"/enriched/"+monedaA+"/"+monedaB;
                    String direccionEnriquecidaA = "https://v6.exchangerate-api.com/v6/"+apiKey+"/enriched/"+monedaB+"/"+monedaA;

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(direccion))
                            .build();
                    HttpResponse<String> response = client
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    String json = response.body();
                    Gson gson = new Gson();

                    respuestasAPI datos = gson.fromJson(json, respuestasAPI.class);

                    HttpRequest request2 = HttpRequest.newBuilder()
                            .uri(URI.create(direccionEnriquecidaB))
                            .build();

                    HttpResponse<String> response2 = client
                            .send(request2, HttpResponse.BodyHandlers.ofString());

                    String json2 = response2.body();

                    infoMoneda datosB = gson.fromJson(json2, infoMoneda.class);

                    HttpRequest request3 = HttpRequest.newBuilder()
                            .uri(URI.create(direccionEnriquecidaA))
                            .build();

                    HttpResponse<String> response3 = client
                            .send(request3, HttpResponse.BodyHandlers.ofString());

                    String json3 = response3.body();

                    infoMoneda datosA = gson.fromJson(json3, infoMoneda.class);


                    System.out.println("\nSiendo las: "+ datos.time_last_update_utc);
                    System.out.println("Información de monedas a cambiar:");
                    System.out.println("El tipo de cambio solicitado es: " + datos.conversion_rate +" "+monedaB+ " por cada "
                            + monedaA);
                    System.out.println("La conversión solicitada es: " + datos.conversion_rate*AMOUNT + monedaB + " por cada "
                            + monedaA); //Multiplicacion tipo de cambio corregida
                    System.out.println("Gracias por utilizar nuestro servicio.");

                } else if (opcion == 1) {
                    List<Map.Entry<String, String>> entradas =
                            new ArrayList<>(Monedas.nombreMoneda.entrySet());
                    entradas.sort(Map.Entry.comparingByKey());
                    int columnas = 4;
                    int anchoColumnas = 35;
                    int contador = 0;
                    System.out.println("\nMonedas soportadas: \n");
                    for (Map.Entry<String, String> entry : entradas) {
                        String codigoMoneda = entry.getKey();
                        String nombre = entry.getValue();
                        System.out.printf("%-5s %-"+(anchoColumnas-5)+"s", codigoMoneda, nombre);
                        contador++;

                        if (contador % columnas == 0) {
                            System.out.println();
                        }
                    }


                }
            } catch (InputMismatchException einput) {
                System.out.println("Error: " + einput.getClass().getSimpleName());
                System.out.println("Debes de escribir un número válido.");
                input.nextLine();

            } catch (NullPointerException npe) {
                System.out.println("Error interno: " + npe.getClass().getSimpleName());
            } catch (Exception eclient) {
                System.out.println("Error al conectarse al servidor: " + eclient.getClass().getSimpleName());

            }
        }
    }
}