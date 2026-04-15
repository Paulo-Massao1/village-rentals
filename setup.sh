#!/bin/bash
# Downloads the SQLite JDBC driver into the lib/ folder.
# Run this once after cloning the repo.

echo "Downloading SQLite JDBC driver..."
mkdir -p lib
curl -L -o lib/sqlite-jdbc-3.45.1.0.jar \
  "https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar"

if [ -s lib/sqlite-jdbc-3.45.1.0.jar ]; then
  echo "Done! SQLite JDBC driver saved to lib/sqlite-jdbc-3.45.1.0.jar"
else
  echo "Download failed. Please download manually from:"
  echo "https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar"
  echo "and place it in the lib/ folder."
fi
