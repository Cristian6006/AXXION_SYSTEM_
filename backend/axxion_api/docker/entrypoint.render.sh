#!/bin/sh
set -e

PORT="${PORT:-10000}"

sed "s/__PORT__/${PORT}/g" /etc/nginx/http.d/default.conf.template > /etc/nginx/http.d/default.conf

echo "Esperando base de datos..."
until php artisan db:show --no-interaction >/dev/null 2>&1; do
  sleep 2
done

if [ -z "$APP_KEY" ]; then
  php artisan key:generate --force
fi

if [ -z "$JWT_SECRET" ]; then
  php artisan jwt:secret --force
fi

php artisan migrate --force --no-interaction || true
php artisan config:clear
php artisan route:clear

exec /usr/bin/supervisord -c /etc/supervisor/conf.d/supervisord.conf
