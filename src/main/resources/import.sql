-- Le mot de passe hashé ci-dessous correspond à "admin123"
INSERT INTO utilisateurs (email, mot_de_passe_hash, role, actif, date_creation)
VALUES ('admin@itu.mg', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00dmxs.TVuHOnu', 'ADMIN', true, CURRENT_TIMESTAMP);