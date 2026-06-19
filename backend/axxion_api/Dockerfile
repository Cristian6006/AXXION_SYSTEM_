FROM php:8.2-fpm-alpine

# Install system dependencies
RUN apk add --no-cache \
    zip \
    libzip-dev \
    libpng-dev \
    libjpeg-turbo-dev \
    freetype-dev \
    oniguruma-dev \
    && docker-php-ext-configure gd --with-freetype --with-jpeg \
    && docker-php-ext-install -j$(nproc) \
        pdo_mysql \
        mbstring \
        zip \
        gd

# Install Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

WORKDIR /var/www/laravel

# Copy application files
COPY . .

# Copy composer files first for layer caching
COPY composer.json composer.lock /var/www/laravel/

# Install dependencies (no dev for production, but we use --optimize-autoloader)
RUN composer install --optimize-autoloader --no-dev --no-interaction --no-progress

# Set permissions for Laravel
RUN chown -R www-data:www-data /var/www/laravel/storage \
    && chown -R www-data:www-data /var/www/laravel/bootstrap/cache \
    && chmod -R 775 /var/www/laravel/storage \
    && chmod -R 775 /var/www/laravel/bootstrap/cache

EXPOSE 9000

CMD ["php-fpm"]