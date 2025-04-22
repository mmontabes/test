-- Crear la tabla de salas
CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- Crear la tabla de usuarios
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    dni VARCHAR(15) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    room_id INTEGER NOT NULL,
    CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL
);

-- Insertar datos iniciales en salas
INSERT INTO rooms (name) VALUES
('Sala 1'),
('Sala 2');

-- Insertar datos iniciales en usuarios
INSERT INTO users (name, email, dni, phone, role, room_id) VALUES
('Juan Pérez', 'juan@example.com', '12345678W', '600123456', 'ADMIN', 1),
('María López', 'maria@example.com', '87654321X', '611987654', 'USER', 2);
