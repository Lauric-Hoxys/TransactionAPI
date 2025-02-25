package fr.server.capitaliste;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class TransactionLibrary {

    /**
     * Couleur de DEBUG TERMINAL
     */
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    /**
     * Variable du serveur
     */
    private static final String SERVER_URL = "http://54.38.191.181:4300/";
    private static final String EVENT_REQUEST = "bank:transaction:request";
    private static Socket socket = null;

    /**
     * Initialise la connexion WebSocket.
     */
    private static void initializeSocket() {
        if (socket == null) {
            try {
                socket = IO.socket(SERVER_URL);
                socket.connect();
                System.out.println(GREEN + "Connecté au serveur WebSocket : " + SERVER_URL + RESET);
            } catch (URISyntaxException e) {
                System.out.println(RED + "Erreur de connexion WebSocket : " + e.getMessage() + RESET);
            }
        }
    }

    /**
     * Crée un objet JSON transaction.
     * @param id UUID de la transaction
     * @param sender UUID de l'expéditeur
     * @param recipient UUID du destinataire
     * @param ribSender RIB de l'expéditeur
     * @param ribRecipient RIB du destinataire
     * @param amount Montant de la transaction
     * @return JSONObject représentant la transaction
     */
    public static JSONObject createTransaction(String id, String sender, String recipient, String ribSender, String ribRecipient, int amount) {
        JSONObject message = new JSONObject();
        try {
            message.put("id", id);

            JSONObject uuid = new JSONObject();
            uuid.put("sender", sender);
            uuid.put("recipient", recipient);

            message.put("uuid", uuid);
            message.put("sender", ribSender);
            message.put("recipient", ribRecipient);
            message.put("amount", amount);
        } catch (JSONException e) {
            System.out.println(RED + "Erreur JSON : " + e.getMessage() + RED);
        }
        return message;
    }

    /**
     * Envoie une transaction au serveur WebSocket avec un callback personnalisé.
     * @param transaction Objet JSON contenant les informations de la transaction.
     * @param callback Fonction de callback pour gérer la réponse.
     */
    public static void sendTransaction(JSONObject transaction, Callback callback) {
        initializeSocket();
        if (socket == null) {
            System.out.println(RED + "WebSocket non initialisé !" + RESET);
            return;
        }

        if (!socket.connected()) {
            System.out.println(RED + "En attente de connexion WebSocket..." + RESET);
            socket.on(Socket.EVENT_CONNECT, args -> {
                System.out.println(GREEN + "Connexion établie, envoi de la transaction..." + RESET);
                emitTransaction(transaction, callback);
            });
        } else {
            emitTransaction(transaction, callback);
        }
    }

    private static void emitTransaction(JSONObject transaction, Callback callback) {
        socket.emit(EVENT_REQUEST, new Object[]{transaction}, (Object... args) -> {
            if (callback != null) {
                callback.call(args);
            }
            disconnect();
        });

        System.out.println(GREEN + "Transaction envoyée : " + transaction.toString() + RESET);
    }

    /**
     * Ferme la connexion WebSocket.
     */
    private static void disconnect() {
        if (socket != null) {
            socket.disconnect();
            socket.close();
            socket = null;
            System.out.println(RED + "Connexion WebSocket fermée." + RESET);
        }
    }
}
