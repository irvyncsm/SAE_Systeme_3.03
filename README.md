# Réseau Social Client-Serveur

*SAÉ 3.03 2023-2024*
> COURSIMAULT Irvyn & TOURE Kris


## Description du Projet
Le projet vise à développer une application client-serveur pour un réseau social, permettant aux utilisateurs de publier des messages et de suivre d'autres utilisateurs pour consulter leurs publications.

L'application se divise en deux parties principales :
- le client, chargé d'afficher les messages et de gérer les actions des utilisateurs,
- et le serveur, responsable de la réception et de la redistribution des messages.

### 1.1 Client
- Le client se connecte au serveur en spécifiant l'adresse IP (ou le nom) et le nom d'utilisateur.
- Affiche les messages des utilisateurs suivis dans l'ordre chronologique, avec une limite de messages affichés.
- Permet de créer un compte s'il n'est pas enregistré sur le serveur.
- Actions disponibles via des commandes :
  - `/follow <nom_utilisateur>` : s'abonner à un utilisateur.
  - `/unfollow <nom_utilisateur>` : se désabonner.
  - `/like <id_message>` : aimer un message.
  - `/delete <id_message>` : supprimer un de ses messages.

### 1.2 Serveur
- Accepte les connexions de nouveaux clients.
- Reçoit et redistribue les messages aux utilisateurs appropriés.
- Gère plusieurs messages simultanément en parallèle.
- Commandes disponibles en ligne de commande :
  - `/delete <id_message>` : supprime un message.
  - `/remove <nom_utilisateur>` : supprime un utilisateur et ses messages.

### 1.3 Extensions
#### 1.3.1 Persistance
- Assure la persistance du serveur pour reprendre son état après un redémarrage.
- Optionnelle : le client peut mémoriser le serveur et le nom d'utilisateur.

#### 1.3.2 Interface Graphique
- Développe une interface graphique pour le client.

## Compétences Visées
- Administrer des systèmes informatiques communicants complexes.
- Travailler dans une équipe informatique.

## Livrables
- Code source commenté.
- Diagramme de classes.
- Rapport et Manuel utilisateur.
- Documentation Javadoc.
