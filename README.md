# Village Rentals System
CPSY 200 Final Project — Part B Prototype

## Setup (do this once)

1. Install **Java 21+** from https://adoptium.net/

2. Install **Extension Pack for Java** in VS Code

3. Download **JavaFX SDK 21** from https://gluonhq.com/products/javafx/ and extract it somewhere (e.g. `C:\javafx-sdk-21`)

4. Download **SQLite JDBC driver** from https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar and put it in the `lib/` folder

5. Open the `village-rentals` folder in VS Code

6. Open `.vscode/settings.json` and change the JavaFX path to where you extracted it:
   ```
   "C:/javafx-sdk-21/lib/**/*.jar"  →  your actual path
   ```

7. Do the same in `.vscode/launch.json` (update the `--module-path` to your JavaFX path)

8. Open `App.java` and hit **Run** (or F5). A window should open. The database is created automatically on first run.

## For building GUI screens

Your screens go in the `ui/` folder. To interact with the database, use the DAO classes:

```java
EquipmentDAO equipmentDAO = new EquipmentDAO();

// Get all
List<Equipment> all = equipmentDAO.getAll();

// Add
equipmentDAO.add(new Equipment(102, 10, "Drill Press", "Floor standing drill", 35.99));

// Delete
equipmentDAO.delete(101);
```

Same pattern for `CustomerDAO` and `RentalDAO`. No SQL needed, the DAOs handle everything.

## Project Structure

- `model/` — Data classes (Equipment, Customer, Category, Rental)
- `dao/` — Database operations (add, delete, getAll, getById)
- `ui/` — GUI screens (Person 2, 3, 4 build here)
- `util/` — SampleDataLoader (loads sample data on first run)
- `App.java` — Main entry point
