-- 1. ADMIN (Password: password)
INSERT INTO utilisateurs (email, mot_de_passe_hash, role, actif, date_creation) VALUES ('admin@food.mg', '$2a$10$XFMpt6u.QO08rK5uCg78f.n97D4GvW2p/U788/mH/qI30Lq5y92M2', 'ADMIN', true, CURRENT_TIMESTAMP);

-- 2. CLIENTS (Password: password)
INSERT INTO utilisateurs (email, mot_de_passe_hash, role, actif, date_creation) VALUES ('client1@gmail.com', '$2a$10$XFMpt6u.QO08rK5uCg78f.n97D4GvW2p/U788/mH/qI30Lq5y92M2', 'CLIENT', true, CURRENT_TIMESTAMP);
INSERT INTO utilisateurs (email, mot_de_passe_hash, role, actif, date_creation) VALUES ('client2@gmail.com', '$2a$10$XFMpt6u.QO08rK5uCg78f.n97D4GvW2p/U788/mH/qI30Lq5y92M2', 'CLIENT', true, CURRENT_TIMESTAMP);

-- 3. RESTAURANTS (Password: password)
INSERT INTO utilisateurs (email, mot_de_passe_hash, role, actif, date_creation) VALUES ('contact@burgershop.com', '$2a$10$XFMpt6u.QO08rK5uCg78f.n97D4GvW2p/U788/mH/qI30Lq5y92M2', 'RESTAURANT', true, CURRENT_TIMESTAMP);
INSERT INTO utilisateurs (email, mot_de_passe_hash, role, actif, date_creation) VALUES ('manager@pizzahub.com', '$2a$10$XFMpt6u.QO08rK5uCg78f.n97D4GvW2p/U788/mH/qI30Lq5y92M2', 'RESTAURANT', true, CURRENT_TIMESTAMP);

-- 4. LIVREURS (Password: password)
INSERT INTO utilisateurs (email, mot_de_passe_hash, role, actif, date_creation) VALUES ('livreur1@express.mg', '$2a$10$XFMpt6u.QO08rK5uCg78f.n97D4GvW2p/U788/mH/qI30Lq5y92M2', 'LIVREUR', true, CURRENT_TIMESTAMP);
INSERT INTO utilisateurs (email, mot_de_passe_hash, role, actif, date_creation) VALUES ('livreur2@express.mg', '$2a$10$XFMpt6u.QO08rK5uCg78f.n97D4GvW2p/U788/mH/qI30Lq5y92M2', 'LIVREUR', true, CURRENT_TIMESTAMP);

-- Clients
INSERT INTO clients (utilisateur_id, nom, prenom, telephone) VALUES (2, 'RAKOTO', 'Jean', '0341122233');
INSERT INTO clients (utilisateur_id, nom, prenom, telephone) VALUES (3, 'RANDRIA', 'Marie', '0324455566');

-- Restaurants
INSERT INTO restaurants (utilisateur_id, nom, description, telephone, adresse, ville, latitude, longitude, ouvert, note_moyenne) VALUES (4, 'Burger Shop', 'Les meilleurs burgers de la ville', '0202233344', '12 Rue de France', 'Antananarivo', -18.8792, 47.5079, true, 4.5);
INSERT INTO restaurants (utilisateur_id, nom, description, telephone, adresse, ville, latitude, longitude, ouvert, note_moyenne) VALUES (5, 'Pizza Hub', 'Pizzas artisanales au feu de bois', '0202255566', '45 Avenue de l Indépendance', 'Antananarivo', -18.8750, 47.5050, true, 4.2);

-- Plats
INSERT INTO plats (restaurant_id, nom, description, prix, categorie, disponible) VALUES (1, 'Cheese Burger', 'Pain brioché, boeuf, cheddar, salade, tomate', 15000.0, 'Burger', true);
INSERT INTO plats (restaurant_id, nom, description, prix, categorie, disponible) VALUES (1, 'Bacon Burger', 'Pain brioché, boeuf, bacon, oignons caramélisés', 18000.0, 'Burger', true);
INSERT INTO plats (restaurant_id, nom, description, prix, categorie, disponible) VALUES (2, 'Pizza Margherita', 'Sauce tomate, mozzarella, basilic frais', 12000.0, 'Pizza', true);
INSERT INTO plats (restaurant_id, nom, description, prix, categorie, disponible) VALUES (2, 'Pizza Reine', 'Sauce tomate, mozzarella, jambon, champignons', 16000.0, 'Pizza', true);

-- Livreurs
INSERT INTO livreurs (utilisateur_id, nom, prenom, telephone, statut) VALUES (6, 'ANDRIANINA', 'Paul', '0331122334', 'DISPONIBLE');
INSERT INTO livreurs (utilisateur_id, nom, prenom, telephone, statut) VALUES (7, 'SOAVA', 'Lova', '0345566778', 'EN_LIVRAISON');

-- Statuts Commande
INSERT INTO statuts_commande (libelle, rang) VALUES ('ANNULER', 0);
INSERT INTO statuts_commande (libelle, rang) VALUES ('CREER', 1);
INSERT INTO statuts_commande (libelle, rang) VALUES ('ACCEPTEE_RESTAURANT', 2);
INSERT INTO statuts_commande (libelle, rang) VALUES ('EN_PREPARATION', 3);
INSERT INTO statuts_commande (libelle, rang) VALUES ('PRET', 4);
INSERT INTO statuts_commande (libelle, rang) VALUES ('EN_LIVRAISON', 5);
INSERT INTO statuts_commande (libelle, rang) VALUES ('LIVREE', 6);

-- Statuts Paiement
INSERT INTO statuts_paiement (libelle, rang) VALUES ('INITIALISE', 1);
INSERT INTO statuts_paiement (libelle, rang) VALUES ('SUCCES', 2);
INSERT INTO statuts_paiement (libelle, rang) VALUES ('ECHEC', 0);

-- Statuts Livraison
INSERT INTO statuts_livraison (libelle, rang) VALUES ('EN_ATTENTE', 1);
INSERT INTO statuts_livraison (libelle, rang) VALUES ('ACCEPTEE_LIVREUR', 2);
INSERT INTO statuts_livraison (libelle, rang) VALUES ('EN_COURS', 3);
INSERT INTO statuts_livraison (libelle, rang) VALUES ('TERMINEE', 4);

-- Commandes initiales
INSERT INTO commandes (client_id, restaurant_id, statut_commande_id, montant_total, mode_paiement, date_creation) VALUES (1, 1, 7, 33000.0, 'CARTE', '2026-03-27 12:00:00');
INSERT INTO commandes (client_id, restaurant_id, statut_commande_id, montant_total, mode_paiement, date_creation) VALUES (2, 2, 7, 28000.0, 'CASH', '2026-03-28 14:00:00');
