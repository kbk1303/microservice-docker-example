#!/bin/bash

set -euo pipefail

CONTAINER_NAME="mariadb-db"
MYSQL_ROOT_USER="root"
MYSQL_ROOT_PASSWORD="Harlov1234!"

if [ $# -eq 0 ]; then
    echo "Fejl: Mangler microservice navn"
    echo "Brug: $0 <MS-NAVN>"
    exit 1
fi

MS_NAME="$1"
DB_NAME="${MS_NAME}_DB"
DB_USER="$MS_NAME"
DB_PASSWORD="$MS_NAME"

echo "Opretter database og bruger for: $MS_NAME"

# Opret database
docker exec "$CONTAINER_NAME" mariadb -u"$MYSQL_ROOT_USER" -p"$MYSQL_ROOT_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS \`$DB_NAME\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Opret bruger for både localhost og % (vigtigt!)
docker exec "$CONTAINER_NAME" mariadb -u"$MYSQL_ROOT_USER" -p"$MYSQL_ROOT_PASSWORD" -e "
CREATE USER IF NOT EXISTS '$DB_USER'@'localhost' IDENTIFIED BY '$DB_PASSWORD';
CREATE USER IF NOT EXISTS '$DB_USER'@'%' IDENTIFIED BY '$DB_PASSWORD';
GRANT ALL PRIVILEGES ON \`$DB_NAME\`.* TO '$DB_USER'@'localhost';
GRANT ALL PRIVILEGES ON \`$DB_NAME\`.* TO '$DB_USER'@'%';
FLUSH PRIVILEGES;"

# Vent et sekund
sleep 1

# Test forbindelse fra både localhost og external
echo "Tester docker exec forbindelse..."
if docker exec "$CONTAINER_NAME" mariadb -u"$DB_USER" -p"$DB_PASSWORD" -e "USE \`$DB_NAME\`; SELECT 1;" >/dev/null 2>&1; then
    echo "✓ Docker exec forbindelse OK"
else
    echo "✗ Docker exec forbindelse fejlede"
    exit 1
fi

echo "Tester ekstern forbindelse..."
if docker exec "$CONTAINER_NAME" mariadb -u"$DB_USER" -p"$DB_PASSWORD" -h127.0.0.1 -e "USE \`$DB_NAME\`; SELECT 1;" >/dev/null 2>&1; then
    echo "✓ Ekstern forbindelse OK"
else
    echo "✗ Ekstern forbindelse fejlede"
    exit 1
fi

echo "✓ Success: $DB_NAME og $DB_USER oprettet med fuld adgang"

echo ""
echo "=== OPRETTET SUCCESFULDT ==="
echo "Database: $DB_NAME"
echo "Bruger: $DB_USER"
echo "Password: $DB_PASSWORD"
echo "Forbindelsesstreng: mariadb -h localhost -P 3306 -u$DB_USER -p$DB_PASSWORD $DB_NAME"
echo "Docker forbindelse: docker exec -it $CONTAINER_NAME mariadb -u$DB_USER -p$DB_PASSWORD $DB_NAME"
echo ""

# Vis tabeller (skal være tom)
echo "Tabeller i $DB_NAME:"
docker exec "$CONTAINER_NAME" mariadb -u"$DB_USER" -p"$DB_PASSWORD" -e "USE \`$DB_NAME\`; SHOW TABLES;"

echo "Script fuldført!"