# TransactionLibrary

TransactionLibrary est une bibliothèque Java permettant d'envoyer des transactions via un serveur WebSocket. Elle facilite la communication avec un serveur distant pour effectuer des transactions en temps réel.

## Fonctionnalités

- Connexion automatique au serveur WebSocket
- Création et envoi de transactions sous forme de JSON
- Gestion des réponses via un callback personnalisé
- Fermeture automatique de la connexion après l'envoi


## Utilisation

Créer une transaction
```Java
import fr.server.capitaliste.TransactionLibrary;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        JSONObject transaction = TransactionLibrary.createTransaction(
            "12345",                      // ID de la transaction
            "sender-uuid",                // UUID de l'expéditeur
            "recipient-uuid",             // UUID du destinataire
            "FR7612345678901234567890123", // RIB de l'expéditeur
            "FR7612345678909876543210987", // RIB du destinataire
            1000                           // Montant de la transaction
        );

        TransactionLibrary.sendTransaction(transaction, status -> {
            if (status) {
                System.out.println("✅ Transaction réussie !");
            } else {
                System.out.println("❌ Transaction échouée.");
            }
        });
    }
}
```

## Configuration

La bibliothèque utilise un serveur WebSocket défini par SERVER_URL :
```Java
private static final String SERVER_URL = "http://yourserver";
```

## Dépendances de la librairie

socket.io-client (pour la connexion WebSocket)
org.json (pour la manipulation des objets JSON)
(déjà implémenter dans l'API)

## Licence

Ce projet est sous licence MIT. Tu es libre de l'utiliser et de le modifier selon tes besoins.
