#!/usr/bin/env python3
"""Convierte el dump MySQL de sistemarenta a PostgreSQL."""

import re
from pathlib import Path

SRC = Path(__file__).parent / "sistemarenta .sql"
DST = Path(__file__).parent / "sistemarenta_postgresql.sql"

ENUMS = {
    "tipo_cliente_enum": ("Particular", "Empresa"),
    "estado_cliente_enum": ("Activo", "Inactivo", "Prospecto"),
    "estado_cotizacion_enum": ("Borrador", "Enviada", "Aceptada", "Rechazada", "Vencida"),
    "estado_devolucion_enum": ("Pendiente", "EnProcesoInspeccion", "Completa", "IncompletaConProblemas"),
    "estado_entrega_enum": ("Programada", "EnTransito", "Entregada", "Fallida"),
    "estado_item_enum": ("Disponible", "Rentado", "EnMantenimiento", "DeBaja"),
    "tipo_mantenimiento_enum": ("Preventivo", "Correctivo", "Mejora"),
    "estado_proveedor_enum": ("Activo", "Inactivo"),
    "estado_renta_enum": ("Programada", "EnCurso", "Finalizada", "Retrasada", "Cancelada"),
    "estado_solicitud_enum": ("Nueva", "EnProceso", "Atendida", "Cancelada"),
    "estado_usuario_enum": ("Activo", "Inactivo", "Bloqueado"),
}

ENUM_REPLACEMENTS = {
    "enum('Particular','Empresa')": "tipo_cliente_enum",
    "enum('Activo','Inactivo','Prospecto')": "estado_cliente_enum",
    "enum('Borrador','Enviada','Aceptada','Rechazada','Vencida')": "estado_cotizacion_enum",
    "enum('Pendiente','EnProcesoInspeccion','Completa','IncompletaConProblemas')": "estado_devolucion_enum",
    "enum('Programada','EnTransito','Entregada','Fallida')": "estado_entrega_enum",
    "enum('Disponible','Rentado','EnMantenimiento','DeBaja')": "estado_item_enum",
    "enum('Preventivo','Correctivo','Mejora')": "tipo_mantenimiento_enum",
    "enum('Activo','Inactivo')": "estado_proveedor_enum",
    "enum('Programada','EnCurso','Finalizada','Retrasada','Cancelada')": "estado_renta_enum",
    "enum('Nueva','EnProceso','Atendida','Cancelada')": "estado_solicitud_enum",
    "enum('Activo','Inactivo','Bloqueado')": "estado_usuario_enum",
}

AUTO_INCREMENT_MAP = {
    "auditoria_inventario": 8,
    "categoria": 7,
    "cliente": 7,
    "cotizacion": 29,
    "detalle_cotizacion": 37,
    "devolucion": 2,
    "direccion": 7,
    "entrega": 2,
    "inventario_item": 43,
    "mantenimiento": 30,
    "producto": 36,
    "proveedor": 5,
    "renta": 29,
    "rol": 5,
    "solicitud": 29,
    "subcategoria": 11,
    "usuario": 12,
}

TRIGGER_FUNCTIONS = """
-- Función genérica para updated_at (equivalente a ON UPDATE CURRENT_TIMESTAMP)
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION trg_inventario_item_after_update_fn()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.estado_item = 'EnMantenimiento' AND OLD.estado_item IS DISTINCT FROM 'EnMantenimiento' THEN
        INSERT INTO mantenimiento (
            inventario_item_id,
            fecha_inicio,
            fecha_fin_prevista,
            tipo_mantenimiento,
            descripcion_problema,
            estado_mantenimiento,
            responsable,
            created_at,
            updated_at
        ) VALUES (
            NEW.id,
            CURRENT_TIMESTAMP,
            (CURRENT_TIMESTAMP + INTERVAL '3 days')::date,
            'Correctivo',
            'Mantenimiento generado automáticamente por cambio de estado',
            'PROGRAMADO',
            'Sistema',
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventario_item_after_update
    AFTER UPDATE ON inventario_item
    FOR EACH ROW
    EXECUTE FUNCTION trg_inventario_item_after_update_fn();

CREATE OR REPLACE FUNCTION trg_mantenimiento_after_delete_fn()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE inventario_item
    SET estado_item = 'Disponible'
    WHERE id = OLD.inventario_item_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_mantenimiento_after_delete
    AFTER DELETE ON mantenimiento
    FOR EACH ROW
    EXECUTE FUNCTION trg_mantenimiento_after_delete_fn();

CREATE OR REPLACE FUNCTION trg_mantenimiento_after_update_fn()
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.estado_mantenimiento IN ('Finalizado', 'COMPLETADO'))
       AND (OLD.estado_mantenimiento NOT IN ('Finalizado', 'COMPLETADO')) THEN
        UPDATE inventario_item
        SET estado_item = 'Disponible'
        WHERE id = NEW.inventario_item_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_mantenimiento_after_update
    AFTER UPDATE ON mantenimiento
    FOR EACH ROW
    EXECUTE FUNCTION trg_mantenimiento_after_update_fn();

CREATE OR REPLACE FUNCTION trg_producto_after_insert_fn()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO inventario_item (
        producto_id,
        numero_serie,
        estado_item,
        fecha_adquisicion,
        costo_adquisicion,
        ubicacion_fisica,
        notas,
        created_at,
        updated_at
    ) VALUES (
        NEW.id,
        NEW.numero_serie,
        CASE
            WHEN NEW.estado = 'disponible' THEN 'Disponible'::estado_item_enum
            WHEN NEW.estado = 'rentado' THEN 'Rentado'::estado_item_enum
            WHEN NEW.estado = 'mantenimiento' THEN 'EnMantenimiento'::estado_item_enum
            WHEN NEW.estado = 'baja' THEN 'DeBaja'::estado_item_enum
            ELSE 'Disponible'::estado_item_enum
        END,
        NEW.fecha_compra,
        NEW.precio_compra,
        NEW.ubicacion,
        'Item creado automáticamente desde producto: ' || NEW.nombre,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_after_insert
    AFTER INSERT ON producto
    FOR EACH ROW
    EXECUTE FUNCTION trg_producto_after_insert_fn();

CREATE OR REPLACE FUNCTION trg_producto_after_update_fn()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.estado IS DISTINCT FROM NEW.estado
       OR OLD.ubicacion IS DISTINCT FROM NEW.ubicacion
       OR OLD.numero_serie IS DISTINCT FROM NEW.numero_serie
       OR OLD.precio_compra IS DISTINCT FROM NEW.precio_compra
       OR OLD.fecha_compra IS DISTINCT FROM NEW.fecha_compra THEN
        UPDATE inventario_item
        SET
            numero_serie = NEW.numero_serie,
            estado_item = CASE
                WHEN NEW.estado = 'disponible' THEN 'Disponible'::estado_item_enum
                WHEN NEW.estado = 'rentado' THEN 'Rentado'::estado_item_enum
                WHEN NEW.estado = 'mantenimiento' THEN 'EnMantenimiento'::estado_item_enum
                WHEN NEW.estado = 'baja' THEN 'DeBaja'::estado_item_enum
                ELSE estado_item
            END,
            fecha_adquisicion = NEW.fecha_compra,
            costo_adquisicion = NEW.precio_compra,
            ubicacion_fisica = NEW.ubicacion,
            notas = 'Actualizado desde producto: ' || NEW.nombre || ' - ' || CURRENT_TIMESTAMP,
            updated_at = CURRENT_TIMESTAMP
        WHERE producto_id = NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_producto_after_update
    AFTER UPDATE ON producto
    FOR EACH ROW
    EXECUTE FUNCTION trg_producto_after_update_fn();

CREATE OR REPLACE FUNCTION trg_renta_after_insert_fn()
RETURNS TRIGGER AS $$
DECLARE
    rec RECORD;
BEGIN
    IF NEW.cotizacion_id IS NOT NULL THEN
        FOR rec IN
            SELECT dc.producto_id, dc.cantidad, dc.precio_unitario
            FROM detalle_cotizacion dc
            WHERE dc.cotizacion_id = NEW.cotizacion_id
        LOOP
            INSERT INTO renta_inventario_item (
                renta_id,
                inventario_item_id,
                precio_renta_item,
                condicion_salida,
                notas
            )
            SELECT
                NEW.id,
                ii.id,
                rec.precio_unitario,
                'Buena',
                'Asignado automáticamente desde renta ID: ' || NEW.id
            FROM inventario_item ii
            WHERE ii.producto_id = rec.producto_id
              AND ii.estado_item = 'Disponible'
            LIMIT rec.cantidad;

            UPDATE inventario_item ii
            SET estado_item = 'Rentado',
                updated_at = CURRENT_TIMESTAMP
            WHERE ii.id IN (
                SELECT rii.inventario_item_id
                FROM renta_inventario_item rii
                WHERE rii.renta_id = NEW.id
                  AND rii.inventario_item_id IN (
                      SELECT id FROM inventario_item
                      WHERE producto_id = rec.producto_id
                  )
            );
        END LOOP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_renta_after_insert
    AFTER INSERT ON renta
    FOR EACH ROW
    EXECUTE FUNCTION trg_renta_after_insert_fn();

CREATE OR REPLACE FUNCTION trg_renta_after_update_fn()
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.estado_renta IN ('Finalizada', 'Cancelada'))
       AND (OLD.estado_renta NOT IN ('Finalizada', 'Cancelada')) THEN
        UPDATE inventario_item ii
        SET estado_item = 'Disponible',
            updated_at = CURRENT_TIMESTAMP
        WHERE ii.id IN (
            SELECT rii.inventario_item_id
            FROM renta_inventario_item rii
            WHERE rii.renta_id = NEW.id
        );
    END IF;

    IF (NEW.estado_renta = 'EnCurso') AND (OLD.estado_renta = 'Programada') THEN
        UPDATE inventario_item ii
        SET estado_item = 'Rentado',
            updated_at = CURRENT_TIMESTAMP
        WHERE ii.id IN (
            SELECT rii.inventario_item_id
            FROM renta_inventario_item rii
            WHERE rii.renta_id = NEW.id
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_renta_after_update
    AFTER UPDATE ON renta
    FOR EACH ROW
    EXECUTE FUNCTION trg_renta_after_update_fn();
"""

UPDATED_AT_TABLES = [
    "categoria", "cliente", "cotizacion", "detalle_cotizacion", "devolucion",
    "direccion", "entrega", "inventario_item", "mantenimiento", "producto",
    "proveedor", "renta", "rol", "solicitud", "subcategoria", "usuario",
]

VIEW_SQL = """
CREATE OR REPLACE VIEW vista_clientes_completa AS
SELECT
    c.id,
    c.nombre,
    c.nombre2,
    c.apellido1,
    c.apellido2,
    c.rfc,
    c.telefono_principal,
    c.correo_electronico,
    c.tipo_cliente,
    c.estado_cliente,
    d.calle,
    d.numero_exterior,
    d.numero_interior,
    d.colonia,
    d.ciudad,
    d.estado_provincia,
    d.codigo_postal,
    d.pais,
    d.referencias,
    c.created_at,
    c.updated_at
FROM cliente c
LEFT JOIN cliente_direccion cd
    ON c.id = cd.cliente_id AND cd.es_principal = TRUE
LEFT JOIN direccion d
    ON cd.direccion_id = d.id;
"""


def strip_mysql_noise(content: str) -> str:
    content = re.sub(
        r"DELIMITER \$\$[\s\S]*?DELIMITER ;",
        "",
        content,
        flags=re.I,
    )
    content = re.sub(
        r"CREATE TABLE `vista_clientes_completa`[\s\S]*?\);\s*",
        "",
        content,
        flags=re.I,
    )
    content = re.sub(r"DROP TABLE IF EXISTS `vista_clientes_completa`;\s*", "", content, flags=re.I)
    content = re.sub(
        r"CREATE ALGORITHM=UNDEFINED[\s\S]*?;",
        "",
        content,
        flags=re.I,
    )
    content = re.sub(
        r"-- AUTO_INCREMENT de las tablas volcadas[\s\S]*?(?=-- Restricciones|-- Filtros)",
        "",
        content,
        flags=re.I,
    )
    skip = [
        r"^-- phpMyAdmin.*",
        r"^SET SQL_MODE.*",
        r"^START TRANSACTION.*",
        r"^SET time_zone.*",
        r"^/\*!40101.*",
        r"^COMMIT.*",
        r"^USE `sistemarenta`.*",
        r"^CREATE DATABASE IF NOT EXISTS.*",
        r"^DROP DATABASE IF EXISTS.*",
    ]
    lines = []
    for line in content.splitlines():
        if any(re.match(p, line.strip()) for p in skip):
            continue
        lines.append(line)
    return "\n".join(lines)


def convert_syntax(content: str) -> str:
    content = re.sub(r"`([^`]+)`", r"\1", content)
    content = re.sub(r"tinyint\(1\)", "BOOLEAN", content, flags=re.I)
    content = re.sub(r"int\(\d+\)\s+UNSIGNED", "INTEGER", content, flags=re.I)
    content = re.sub(r"int\(\d+\)", "INTEGER", content, flags=re.I)
    content = re.sub(r"DEFAULT 0(?!\d)", "DEFAULT FALSE", content)
    content = re.sub(r"current_timestamp\(\)", "CURRENT_TIMESTAMP", content, flags=re.I)
    content = re.sub(r" ON UPDATE CURRENT_TIMESTAMP", "", content, flags=re.I)
    content = re.sub(
        r"TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK \(json_valid\(especificaciones\)\)",
        "JSONB DEFAULT NULL",
        content,
        flags=re.I,
    )
    content = re.sub(r"\)\s*ENGINE=\w+[^;]*;", ");", content, flags=re.I)
    for mysql_enum, pg_enum in ENUM_REPLACEMENTS.items():
        content = content.replace(mysql_enum, pg_enum)
    content = re.sub(
        r"ALTER TABLE (\w+)\s+ADD PRIMARY KEY \(",
        r"ALTER TABLE ONLY \1 ADD PRIMARY KEY (",
        content,
        flags=re.I,
    )
    content = re.sub(
        r"ALTER TABLE (\w+)\s+ADD CONSTRAINT (\w+) FOREIGN KEY",
        r"ALTER TABLE ONLY \1 ADD CONSTRAINT \2 FOREIGN KEY",
        content,
        flags=re.I,
    )
    content = convert_index_alterations(content)
    return content


def convert_index_alterations(content: str) -> str:
    """Convierte bloques ALTER TABLE ... ADD KEY de MySQL a sintaxis PostgreSQL."""
    pattern = re.compile(
        r"ALTER TABLE ONLY (\w+) ADD PRIMARY KEY \(([^)]+)\)(.*?);",
        re.I | re.S,
    )

    def repl(match: re.Match) -> str:
        table = match.group(1)
        pk_cols = match.group(2)
        tail = match.group(3)
        lines = [f"ALTER TABLE ONLY {table} ADD PRIMARY KEY ({pk_cols});"]

        for uq in re.finditer(r",\s*ADD UNIQUE KEY (\w+) \(([^)]+)\)", tail, re.I):
            name, cols = uq.groups()
            lines.append(
                f"ALTER TABLE ONLY {table} ADD CONSTRAINT {name} UNIQUE ({cols});"
            )

        for idx in re.finditer(r",\s*ADD KEY (\w+) \(([^)]+)\)", tail, re.I):
            name, cols = idx.groups()
            lines.append(f"CREATE INDEX IF NOT EXISTS {name} ON {table} ({cols});")

        return "\n".join(lines)

    return pattern.sub(repl, content)


def build_sequences() -> str:
    lines = []
    for table, next_val in AUTO_INCREMENT_MAP.items():
        seq = f"{table}_id_seq"
        lines.append(f"CREATE SEQUENCE IF NOT EXISTS {seq};")
        lines.append(f"ALTER TABLE {table} ALTER COLUMN id SET DEFAULT nextval('{seq}');")
        lines.append(f"SELECT setval('{seq}', {next_val - 1}, true);")
    return "\n".join(lines)


def build_updated_at_triggers() -> str:
    return "\n".join(
        f"CREATE TRIGGER trg_{table}_updated_at BEFORE UPDATE ON {table} "
        f"FOR EACH ROW EXECUTE FUNCTION set_updated_at();"
        for table in UPDATED_AT_TABLES
    )


def main() -> None:
    raw = SRC.read_text(encoding="utf-8", errors="replace")
    body = strip_mysql_noise(raw)
    body = convert_syntax(body)

    enum_defs = [
        f"CREATE TYPE {name} AS ENUM ({', '.join(repr(v) for v in values)});"
        for name, values in ENUMS.items()
    ]

    header = """-- PostgreSQL dump convertido desde MySQL/MariaDB
-- Base de datos: sistemarenta
-- Origen: backend/sql/sistemarenta .sql
--
-- Instalación:
--   sudo -u postgres createdb sistemarenta
--   sudo -u postgres psql -d sistemarenta -f sistemarenta_postgresql.sql

\\set ON_ERROR_STOP on
BEGIN;

-- Extensiones opcionales
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tipos ENUM (PostgreSQL no tiene ENUM inline como MySQL)
"""

    footer = f"""
-- Vista
{VIEW_SQL}

-- Secuencias equivalentes a AUTO_INCREMENT
{build_sequences()}

-- Triggers de negocio
{TRIGGER_FUNCTIONS}

-- Triggers updated_at
{build_updated_at_triggers()}

COMMIT;
"""

    output = header + "\n".join(enum_defs) + "\n\n" + body + footer
    output = re.sub(r"\n{3,}", "\n\n", output)
    DST.write_text(output, encoding="utf-8")
    print(f"Escrito: {DST} ({len(output.splitlines())} líneas)")


if __name__ == "__main__":
    main()
