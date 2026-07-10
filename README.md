# Expense Tracker

> **Status: In Progress** — Currently a Java console app. Actively being migrated to a full web application with AI-powered features. See [Roadmap](#roadmap) below.

A personal expense tracking application that lets users log expenses by category, view spending history, and get a summary of where their money is going. Built as a learning project to go from a basic console app to a full-stack web application with real-world features like spending forecasts and AI-generated insights.

## Why this project?

Most expense trackers are either too simple (just a list of numbers) or too complex to build as a learning exercise. This project is meant to grow in stages — starting with core Java fundamentals, then layering on data-driven features (forecasting, budget tracking) and AI integration, and finally converting it into a proper web app. The commit history reflects that progression intentionally.

## Current Features (Console App)

- Add an expense with category, amount, date, and a note
- Choose from predefined categories: Food, Travel, Bills, Shopping, Entertainment, Extra
- View all logged expenses
- View a summary of expenses

## Planned Features

### Data & Insights
- [ ] **Next month's forecast** — predicts likely spending per category based on a moving average of the last 3 months
- [ ] **Burn rate warning** — tracks daily spend against a monthly budget and warns if you're on track to exceed it before month-end

### AI Integration (Claude API)
- [ ] **Natural-language monthly summary** — a written breakdown of spending patterns, not just raw numbers
- [ ] **Personalized savings tips** — 2–3 suggestions based on top spending categories
- [ ] **Auto-categorization** — infer an expense's category from a free-text note instead of manual selection

### Web App Migration
- [ ] Convert core logic (`Expense`, `ExpenseManager`) into a Spring Boot REST API
- [ ] Add persistent storage (starting with H2, potentially SQLite/Postgres later)
- [ ] Build a browser-based frontend (HTML/CSS/JS) to add, view, and summarize expenses
- [ ] Wire up AI features as backend endpoints called from the frontend

## Tech Stack

**Current:**
- Java
- Console I/O (`Scanner`)

**Planned:**
- Spring Boot (REST API)
- H2 / SQLite (persistence)
- HTML, CSS, JavaScript (frontend)
- Anthropic Claude API (AI features)

## Project Structure

```
expense-tracker/
├── Main.java          # Entry point, console menu loop
├── Expense.java        # Expense model (category, amount, date, note)
└── ExpenseManager.java # Core logic: add, view, summarize expenses
```
*(Structure will expand as the Spring Boot migration progresses.)*

## Getting Started (Console Version)

```bash
javac Main.java Expense.java ExpenseManager.java
java Main
```

Follow the on-screen menu to add expenses, view them, or see a summary.

## What I'm Learning

This project is as much about the process as the output:
- Core Java & OOP design (separating concerns between `Expense`, `ExpenseManager`, and I/O)
- REST API design and HTTP fundamentals
- Integrating third-party APIs (Anthropic's Claude API)
- Full-stack thinking — connecting a Java backend to a browser frontend
- Iterative development — shipping a working console app first, then evolving it rather than over-engineering from day one

## License

MIT License — feel free to use, modify, or learn from this code.
