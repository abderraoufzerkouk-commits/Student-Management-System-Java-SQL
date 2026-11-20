# Academic Management System

A desktop-based academic management application developed with Java and Microsoft SQL Server. The system provides a centralized platform for managing students, teachers, academic modules, registrations, grades, examination sessions, academic results, and diploma eligibility.

The application follows a layered architecture to separate the graphical interface, business logic, and database operations.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Application Architecture](#application-architecture)
- [Technologies](#technologies)
- [Database Design](#database-design)
- [Academic Calculation Logic](#academic-calculation-logic)
- [Project Structure](#project-structure)
- [Installation and Configuration](#installation-and-configuration)
- [Database Configuration](#database-configuration)
- [Running the Application](#running-the-application)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

## Overview

The Academic Management System is designed to simplify and centralize the management of academic information.

It provides a graphical interface through which users can manage academic records and perform operations related to the entire academic process.

The system covers several interconnected areas:

- Student information management.
- Teacher information management.
- Academic module management.
- Student registration in modules.
- Grade recording and modification.
- Examination and resit management.
- Automatic calculation of academic averages.
- Identification of students requiring resit examinations.
- Diploma eligibility verification.

The application uses Java Swing for the desktop interface and Microsoft SQL Server for persistent data storage.

---

## Key Features

### 1. Student Management

Provides tools to manage student records.

- Add new students.
- Update existing student information.
- Delete student records.
- Display student information in a structured table.
- Store names, dates of birth, and email addresses.
- Validate input data before database operations.

### 2. Teacher Management

Provides functionality for managing teachers.

- Add new teachers.
- Update teacher information.
- Delete teachers.
- Display teacher records.
- Manage teacher names and specializations.
- Maintain relationships between teachers and their assigned modules.

### 3. Module Management

Allows users to manage academic modules.

- Create and update modules.
- Delete modules.
- Assign teachers to modules.
- Define module coefficients.
- Specify teaching hours.
- Display module information and assigned teachers.
- Validate coefficients and teaching hours.

### 4. Student Registration

Manages the relationship between students and academic modules.

- Register students in modules.
- Remove students from modules.
- Display registered modules for each student.
- Prevent duplicate registrations.
- Maintain registration dates.
- Enforce database relationships between students and modules.

### 5. Grade Management

Provides tools for recording and managing student grades.

Supported assessment types:

- Continuous Assessment (CC).
- Examination.
- Resit Examination.

Supported examination sessions:

- Normal Session.
- Resit Session.

Features include:

- Add grades.
- Update existing grades.
- Filter grades by student and examination session.
- Associate grades with the correct student and module.
- Validate grade values between 0 and 20.
- Prevent duplicate grades for the same assessment type and session.

### 6. Academic Results

The application provides several operations for analyzing academic performance:

- Calculate module averages before resit examinations.
- Calculate final module averages after resit examinations.
- Identify students who require resit examinations.
- Display detailed grade information.
- Calculate weighted overall averages.
- Identify students who have validated all their modules.

### 7. Diploma Eligibility

The system includes a diploma validation mechanism.

A student is considered eligible for a diploma when all registered modules have a final average greater than or equal to 10/20.

The system automatically checks the results of each registered module before determining diploma eligibility.

---

## Application Architecture

The application follows a layered architecture that separates responsibilities into five main packages.

### Connection Layer

Responsible for establishing the connection between the Java application and Microsoft SQL Server.

Main class:

- `DatabaseConnection`

Responsibilities:

- Load the Microsoft JDBC driver.
- Establish database connections.
- Provide a reusable connection method.

### DAO Layer (Data Access Object)

Responsible for communication with the database.

Main classes:

- `GestionEtudiants`
- `GestionEnseignants`
- `GestionModules`
- `GestionInscription`
- `GestionNote`

Responsibilities:

- Insert records.
- Retrieve records.
- Update existing records.
- Delete records.
- Execute specialized SQL queries.
- Manage database operations using JDBC and PreparedStatement.

### Model Layer

Contains the main entities used by the application.

Main classes:

- `Etudiants`
- `Enseignants`
- `Modules`
- `Inscription`
- `Note`

These classes represent the application's data entities and contain their attributes, constructors, getters, setters, and related methods.

### Service Layer

Contains business logic that requires coordination between different data access operations.

Main classes:

- `Resultat`
- `Diplome`

Responsibilities:

- Calculate academic averages.
- Identify students requiring resit examinations.
- Determine whether modules are validated.
- Verify diploma eligibility.

### GUI Layer

Provides the graphical user interface using Java Swing.

Main components:

- `MainFrame`
- `EtudiantPanel`
- `EnseignantPanel`
- `ModulePanel`
- `InscriptionPanel`
- `NotePanel`
- `ResultatPanel`

The main window uses `CardLayout` to navigate between the application's different functional panels.

It also includes a global refresh mechanism to synchronize displayed information after database operations.

---

## Technologies

| Technology                | Purpose                                    |
| ------------------------- | ------------------------------------------ |
| Java 17                   | Application development and business logic |
| Java Swing                | Desktop graphical interface                |
| Microsoft SQL Server 2022 | Relational database                        |
| JDBC                      | Java-to-database connectivity              |
| Microsoft JDBC Driver     | SQL Server connection                      |
| Eclipse IDE               | Development environment                    |

---

## Database Design

The application uses a relational database named `GestionUniversitaire`.

It contains five main tables.

### Main Entities

| Table         | Description                          |
| ------------- | ------------------------------------ |
| `Etudiant`    | Stores student information           |
| `Enseignant`  | Stores teacher information           |
| `Module`      | Stores academic module information   |
| `Inscription` | Manages student-module registrations |
| `Note`        | Stores assessment grades             |

### Database Relationships

- A teacher can teach multiple modules.
- Each module is assigned to one teacher.
- A student can register for multiple modules.
- A module can have multiple registered students.
- Each registration connects one student to one module.
- Grades are associated with a registered student-module pair.

### Integrity Constraints

The database uses several constraints to maintain data consistency:

- Primary keys.
- Foreign keys.
- Composite primary key for student registrations.
- Unique email addresses.
- Unique grade combinations for each student, module, assessment type, and session.
- Check constraints for coefficients, teaching hours, grade values, and assessment types.
- Cascading deletion for registration relationships.
- Indexes to improve query performance.

### SQL Views

The database includes four views for academic calculations:

| View                     | Purpose                                              |
| ------------------------ | ---------------------------------------------------- |
| `v_NotesAvantRattrapage` | Calculates module averages before resit examinations |
| `v_EtudiantsRattrapage`  | Identifies students requiring resit examinations     |
| `v_NotesApresRattrapage` | Calculates final averages after resit examinations   |
| `v_Diplomes`             | Identifies students who have validated all modules   |

### Stored Procedures

Two stored procedures calculate weighted overall student averages:

- `sp_MoyenneGeneraleAvantRattrapage`
- `sp_MoyenneGeneraleApresRattrapage`

Both procedures receive a student ID as a parameter.

---

## Academic Calculation Logic

The system uses a weighted calculation to determine module averages.

### Before Resit Examination

The module average is calculated using:

```text
Module Average = (CC × 0.4) + (Exam × 0.6)
```

### After Resit Examination

When a resit examination is available, the system keeps the better examination grade:

```text
Final Average = (CC × 0.4) + (MAX(Normal Exam, Resit Exam) × 0.6)
```

A module is considered validated when:

```text
Final Average >= 10/20
```

### Overall Average

The overall average is calculated using module coefficients:

```text
Overall Average =
Σ(Module Average × Module Coefficient)
/
Σ(Module Coefficient)
```

### Diploma Validation

A student is eligible for a diploma only when every registered module has a final average of at least 10/20.

---

## Project Structure

```text
Student-Management-System-Java-SQL/
│
├── src/
│   ├── Connection/
│   │   └── DatabaseConnection.java
│   │
│   ├── DAO/
│   │   ├── GestionEnseignants.java
│   │   ├── GestionEtudiants.java
│   │   ├── GestionInscription.java
│   │   ├── GestionModules.java
│   │   └── GestionNote.java
│   │
│   ├── GUI/
│   │   ├── DialogAjouterEnseignant.java
│   │   ├── DialogAjouterEtudiant.java
│   │   ├── DialogAjouterModule.java
│   │   ├── DialogAjouterNote.java
│   │   ├── DialogInscrireEtudiant.java
│   │   ├── EnseignantPanel.java
│   │   ├── EtudiantPanel.java
│   │   ├── InscriptionPanel.java
│   │   ├── MainFrame.java
│   │   ├── ModulePanel.java
│   │   ├── NotePanel.java
│   │   └── ResultatPanel.java
│   │
│   ├── model/
│   │   ├── Enseignants.java
│   │   ├── Etudiants.java
│   │   ├── Inscription.java
│   │   ├── Modules.java
│   │   └── Note.java
│   │
│   └── Service/
│       ├── Diplome.java
│       └── Resultat.java
│
├── SQLQuery1.sql(University).sql
├── .gitignore
└── README.md
```

---

## Installation and Configuration

### Prerequisites

Before running the application, make sure you have installed:

- Java Development Kit (JDK 17).
- Microsoft SQL Server 2022.
- SQL Server Management Studio (SSMS).
- Eclipse IDE.
- Microsoft JDBC Driver for SQL Server.

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/Student-Management-System-Java-SQL.git
```

Navigate to the project directory:

```bash
cd Student-Management-System-Java-SQL
```

### 2. Create the Database

1. Open SQL Server Management Studio.
2. Connect to your SQL Server instance.
3. Open the SQL script included in the repository.
4. Execute the script to create the database and its database objects.

### 3. Configure the JDBC Driver

Download the Microsoft JDBC Driver for SQL Server.

The application requires a driver JAR file named similarly to:

```text
mssql-jdbc-*.jar
```

In Eclipse:

1. Right-click the project.
2. Select **Build Path**.
3. Select **Add External JARs...**.
4. Choose the downloaded JDBC driver.
5. Apply the changes.

### 4. Configure the Database Connection

Open:

```text
src/Connection/DatabaseConnection.java
```

Configure the connection URL, SQL Server instance, database name, username, and password according to your local environment.

Make sure that your SQL Server instance is running and that the database is accessible.

**Security note:** Do not commit real database passwords or private credentials to GitHub.

### 5. Run the Application

Open the project in Eclipse, ensure the JDBC driver is included in the Build Path, and run the main application class:

```text
MainFrame.java
```

---

## Future Improvements

Potential enhancements include:

- Export academic transcripts and reports to PDF using Apache PDFBox.
- User authentication and role-based access control.
- Graphical statistics and academic performance dashboards using JFreeChart.
- A web-based version using Spring Boot.
- Multi-user access and centralized deployment.

---

## Author

**ZERKOUK Abderraouf**
