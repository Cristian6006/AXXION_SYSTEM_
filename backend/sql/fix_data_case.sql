USE sistemarenta;

-- Normalizar datos existentes a MAYÚSCULAS para cumplir con la validación de la API
UPDATE `mantenimiento` 
SET 
    `estado_mantenimiento` = UPPER(`estado_mantenimiento`),
    `tipo_mantenimiento` = UPPER(`tipo_mantenimiento`);
