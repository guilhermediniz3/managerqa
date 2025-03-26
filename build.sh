#!/bin/bash

# Instala o Nixpacks localmente
curl -sSL https://nixpacks.com/install.sh | bash -s -- --local

# Executa o build usando o nixpacks local
~/.nixpacks/bin/nixpacks build

# Build do frontend e cópia para o backend
cd testerqa && yarn install && yarn build
cd ..
mkdir -p backend/src/main/resources/static
cp -R testerqa/dist/* backend/src/main/resources/static/

# Build do backend
cd backend && ./mvnw package -DskipTests