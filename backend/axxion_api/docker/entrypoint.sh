#!/bin/sh
set -e

echo "Esperando base de datos en ${DB_HOST}..."
until mysqladmin ping -h"$DB_HOST" -u"$DB_USERNAME" -p"$DB_PASSWORD" --silent 2>/dev/null; do
  sleep 2
done

if [ -z "$APP_KEY" ]; then
  php artisan key:generate --force
fi

if [ -z "$JWT_SECRET" ]; then
  php artisan jwt:secret --force
fi

php artisan migrate --force --no-interaction

php artisan config:clear
php artisan route:clear

exec "$@"
