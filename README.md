# Manuel d'utilisation de l'application de chat

L'application de chat est une application de messagerie simple qui permet aux utilisateurs de se connecter à un serveur, d'échanger des messages et d'utiliser des commandes pour effectuer différentes actions.

## Prérequis

Avant d'utiliser l'application, assurez-vous d'avoir Java installé sur votre système. De plus, vous devez avoir JavaFX installé et définir le chemin vers JavaFX lors de l'exécution des commandes.

## Installation

1. Clonez le référentiel depuis GitHub.
2. Compilez les fichiers Java en utilisant la commande suivante dans le répertoire du projet :
    ```bash
    javac -d bin/ --module-path 'chemin_vers_javafx' --add-modules javafx.controls src/*.java
    ```
3. Lancez le serveur en utilisant la commande suivante dans le répertoire du projet :
    ```bash
    java -cp bin/ Server
    ```
4. Lancez autant de clients que nécessaire en utilisant la commande suivante dans le répertoire du projet :
    ```bash
    java -cp bin/ --module-path 'chemin_vers_javafx' --add-modules javafx.controls ChatApplication
    ```

## Utilisation de l'application

1. Lorsque vous lancez un client, vous devez entrer votre nom. Si c'est votre première connexion, le serveur créera un nouveau compte pour vous.

2. Une fois connecté, vous pouvez utiliser les commandes suivantes dans la zone de texte en bas de la fenêtre du client :
   - `/nick <nouveau_nom>` : Changez votre nom d'utilisateur.
   - `/list` : Affiche la liste des utilisateurs connectés.
   - `/follow <nom_utilisateur>` : Permet de suivre un autre utilisateur.
   - `/unfollow <nom_utilisateur>` : Permet de ne plus suivre un utilisateur.
   - `/like <id_message>` : Likez un message (non implémenté).
   - `/unlike <id_message>` : Unlikez un message (non implémenté).
   - `/delete <id_message>` : Supprimez un de vos messages (non implémenté).
   - `/profil` : Affiche votre profil (nombre de followers, nombre de followings).
   - `/voirProfil <nom_utilisateur>` : Affiche le profil d'un utilisateur (nombre de followers, nombre de followings).
   - `/quit` : Quittez le chat.

3. Vous pouvez également simplement envoyer des messages normaux qui seront visibles par tous les utilisateurs qui vous suivent.

## Notes importantes

- Les fonctionnalités `/like`, `/unlike`, `/delete` ne sont pas implémentées.
- Le serveur fonctionne en écoutant sur le port 9999. Assurez-vous qu'aucun autre service n'utilise ce port sur votre machine.
- En cas de déconnexion inattendue, le serveur et le client tenteront de se reconnecter automatiquement après un délai de 5 secondes.
