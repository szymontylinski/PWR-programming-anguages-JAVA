# 🟦 Projekty z Javy – Języki Programowania (PWr)

Repozytorium zawiera zestaw **6 projektów w języku Java**, zrealizowanych w ramach przedmiotu "Języki programowania" na Politechnice Wrocławskiej (rok akademicki 2024/25).  
Każdy projekt znajduje się w osobnym folderze i prezentuje inne aspekty programowania w Javie – od podstawowej logiki, przez GUI, aż po programowanie współbieżne i komunikację sieciową.

---

## 📂 Struktura repozytorium
projekt1/ # Kompilacja IDE/CLI
projekt2/ # Pętle, instrukcje warunkowe, wyjątki
projekt3/ # Kontenery danych
projekt4/ # GUI (Swing, JavaFX)
projekt5/ # Programowanie współbieżne
projekt6/ # Gniazda TCP/IP + RMI

---

## 📘 Opis projektów

### 2. Pętle, instrukcje warunkowe, wyjątki
**Temat:** inteligentny system wspomagający konferencje branżowe.  
- Implementacja metaheurystycznego algorytmu przydziału zasobów.  
- Zastosowanie **algorytmu genetycznego** do przypisywania uczestnikom konferencji innych osób o podobnych zainteresowaniach.  
- Obsługa wyjątków i walidacja danych wejściowych.  

---

### 3. Kontenery danych
**Temat:** uproszczony system przepływu feedbacku w firmie.  
- Implementacja logiki biznesowej oddzielonej od interfejsu tekstowego (separacja warstw).  
- Walidacja danych i obsługa błędów.  
- Aplikacja kompilowana i uruchamiana jako **plik JAR**.  

---

### 4. GUI (Swing, JavaFX)
**Temat:** aplikacja typu *Fitness Tracker*.  
- Graficzny interfejs użytkownika w Swing/JavaFX.  
- Rejestracja wyników treningów oraz ich prezentacja na wykresach.  
- Eksport jako wykonywalny **plik JAR**.  

---

### 5. Programowanie współbieżne
**Temat:** wizualizacja gry **Game of Life** Johna Conway’a.  
- Synchronizacja wątków przy użyciu **CountDownLatch**.  
- Implementacja z dwiema strukturami danych:  
  - pierwsza do odczytu stanu planszy,  
  - druga do aktualizacji.  
- Zamiana struktur przy synchronizacji wątków.  

---

### 6. Gniazda TCP/IP + RMI
**Temat:** gra w kółko i krzyżyk w architekturze klient-serwer.  
- Wykorzystanie **RMI** do połączenia graczy.  
- Serwer odpowiada za logikę gry (parowanie, inicjalizacja stanu, wymiana ruchów, walidacja I/O).  
- Obsługa wielu graczy jednocześnie (np. poprzez pokoje gry lub tokeny sesji).  
- Statystyki graczy (wygrane, remisy, porażki).  
- Logowanie w konsoli kluczowych zdarzeń (parowanie, błędy, koniec gry).  

---

## ⚙️ Technologie
- Java 17  
- Swing, JavaFX  
- Wątki i synchronizacja (CountDownLatch)  
- TCP/IP, RMI  

---

## 📄 Licencja
Projekt edukacyjny. Można używać do celów nauki i demonstracji.  
