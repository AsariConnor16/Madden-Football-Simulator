# NFL Head Coach Simulation Engine

An interactive, text-based Java simulation modeling an 8-game NFL regular season. The engine simulates play-calling strategy, clock management, situational field position, and probabilistic drive outcomes for both offense and defense.

---

### Key Technical Features

* **Stochastic Outcome Modeling:** Designed probabilistic models using pseudo-random distributions to simulate play yardage across multiple play categories (inside run, outside run, short/long pass, play action) with realistic negative-play variances.
* **Game State Architecture:** Managed interdependent state variables—including game clock decay, down and distance, field position, and score tracking—across continuous offensive and defensive possessions.
* **Situational Probability Mechanics:** Modeled defensive possession outcomes (touchdown, field goal, punt, turnover) using discrete probability splits and dynamic clock burn based on drive length.
* **Distance-Weighted Field Goal Engine:** Implemented success-rate probability thresholds calculated directly from field-goal distance (automatic under 48 yards, 50% split from 48–68 yards, and 0% beyond 68 yards).
* **Data Ingestion & Schedule Generation:** Built an automated team schedule generator that reads league rosters via file I/O (`Scanner`, `ArrayList`), validates user input case-insensitively, and ensures a non-repeating 8-game opponent schedule.

---

### Technologies Used

* **Language:** Java (JDK 8+)
* **Concepts:** Procedural programming, stochastic simulations, file I/O, error handling, state-machine logic

---

### How to Run

1. Clone or download this repository.
2. Ensure `nflteams` is in the same directory as the Java source file.
3. Compile and run:
   ```bash
   javac MaddenSimulator.java
   java MaddenSimulator
