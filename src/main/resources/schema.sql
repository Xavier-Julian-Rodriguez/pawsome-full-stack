-- Database: pawsomeDB

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    user_first_name VARCHAR(50) NOT NULL,
    user_last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS pets (
    pet_id SERIAL PRIMARY KEY,
    pet_name VARCHAR(50) NOT NULL,
    pet_age DECIMAL(3,1) NOT NULL,
    pet_species VARCHAR(50) NOT NULL,
    pet_image BYTEA,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_user
    FOREIGN KEY(user_id)
    REFERENCES users(user_id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS recipes (
    recipe_id SERIAL PRIMARY KEY,
    recipe_title VARCHAR(50) NOT NULL,
    recipe_ingredients TEXT,
    recipe_instructions TEXT,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pet_id INTEGER NOT NULL,
    CONSTRAINT fk_pet
    FOREIGN KEY(pet_id)
    REFERENCES pets(pet_id)
    ON DELETE CASCADE
    );