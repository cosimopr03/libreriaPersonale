# libreriaPersonale
# Gestore di Libreria Personale

Un’applicazione desktop in Java per organizzare e tenere traccia della tua collezione di libri.  
Permette di aggiungere, rimuovere, modificare e cercare volumi, con persistenza su CSV e interfaccia grafica Swing.

---

## 📋 Caratteristiche

- **CRUD completo**: aggiunta, rimozione, modifica (stato di lettura e valutazione).
- **Ricerca** per titolo (case-insensitive, ricerca parziale).
- **Filtri** per stato di lettura e valutazione.
- **Ordinamento** per autore e per titolo (Strategy Pattern).
- **Persistenza** su file CSV tramite OpenCSV.
- **Architettura modulare**:
    - Interfaccia `ArchivioLibri`
    - Abstract Class `AbstractArchivioLibriFile`
    - Implementazione `ArchivioLibriFileCsv`
- **Pattern**:
    - Strategy (ordinamento, filtri)
    - Singleton/Facade (`LibreriaUiFacade`)
    - Bridge (layer UI ↔ archivio)
- **GUI** in Swing con:
    - `JTable` per la visualizzazione
    - `JDialog` personalizzati per aggiunta/modifica

---

## 🛠️ Prerequisiti

- JDK 21+
- Maven (o altro build tool a piacere)
- Plugin PlantUML (per generazione diagrammi)

---

## 🚀 Installazione

1. Clona il repository:
   ```bash
   git clone https://github.com/tuo-username/gestore-libreria-personale.git
   cd gestore-libreria-personale

