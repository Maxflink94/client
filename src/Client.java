import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Set;

public class Client {
    public static void main(String[] args) {

        //Initierar alla objekt. Satta till null för att bli initierade till "något"
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        Scanner sc = new Scanner(System.in);

        //Starta klienten
        try {
            //Alla portar över 1024 är (oftast) fria
            //Initierar Socket med specifik port
            socket = new Socket("localhost", 8080);

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            //Initierar Reader och Writer och kopplar dem till socket
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            while (true) {

                //Meddelandet som ska skickas till servern
                String message = userInput();

                //Skicka meddelande till server
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String resp = bufferedReader.readLine();

                //Anropa openRespons metod med server respons
                JSONObject persons = openRespons(resp);

                String val = sc.nextLine();
                int number = Integer.parseInt(val);
                Set<String> keys = persons.keySet();

                if (number != 1 && number <= 4){
                String p = "p" + number;
                JSONObject person = (JSONObject) persons.get(p);
                System.out.println(person.get("name") + ", " + person.get("favoriteColor") + ", " + person.get("age"));

                } else if (number == 1){
                    for (String x : keys) {
                        JSONObject person = (JSONObject) persons.get(x);

                        //Skriv ut namn på person
                        System.out.println(person.get("name"));
                }
                }
                //Avsluta om användaren skriver quit
                if (message.equalsIgnoreCase("Exit"))
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            //Stäng kopplingar
            try {
                if (socket != null) {
                    socket.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Klienten Avslutas!");
            }
        }
    }

    static String userInput(){
        //Steg 1. Skriv ut en meny för användaren
        System.out.println("1. Hämta data om alla personer");
        System.out.println("2. Hämta data om person ett");
        System.out.println("3. Hämta data om person två");
        System.out.println("4. Hämta data om person tre");

        //Steg 2. Låta användaren göra ett val
        System.out.print("Skriv in ditt menyval: ");

        //Skapa JSON objekt för att hämta data om alla personer. Stringifiera objektet och returnerar det
        JSONObject jsonReturn = new JSONObject();
        jsonReturn.put("httpURL", "allPersons");
        jsonReturn.put("httpMethod", "get");

        //Returnera JSON objekt
        return jsonReturn.toJSONString();

    }

    static JSONObject openRespons(String resp) throws ParseException {

        //Init parser för att parsa till JSON
        JSONParser parser = new JSONParser();

        JSONObject serverRespons = (JSONObject) parser.parse(resp);

        //Kollar om respons lyckas
        if ("200".equals(serverRespons.get("httpStatusCode").toString())){

            //Bygger upp ett JSON-objekt av den returnerade data
            JSONObject data = (JSONObject) parser.parse((String) serverRespons.get("data"));

            return data;
        }
        return null;
    }
}