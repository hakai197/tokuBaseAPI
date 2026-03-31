# TokuBase API

REST API for a Tokusatsu metadata database covering **Kamen Rider** and **Super Sentai** series, their characters, transformation forms, and episode listings.

## Tech Stack

- **Java 17** / **Spring Boot 3.2.5**
- **Spring Data JPA** with **MySQL**
- **Lombok** for boilerplate reduction
- **Springdoc OpenAPI 2.5** (Swagger UI)
- **Maven** build

## Data Model

```
Series (RIDER | SENTAI)
 ├── Characters (MAIN | SECONDARY | SIXTH | EXTRA)
 │    └── Forms (transformations, final forms)
 └── Episodes
```

| Entity      | Key Fields                                                        |
|-------------|-------------------------------------------------------------------|
| **Series**  | name, type, yearStart, yearEnd, description, images               |
| **Character** | name, series, role, color, images                               |
| **Form**    | name, character, powerType, isFinalForm, images                   |
| **Episode** | series, episodeNumber, title, airDate, images                     |

## API Endpoints

All endpoints are served under `http://localhost:8080`.

### Series — `/api/series`

| Method   | Path                              | Description                              |
|----------|-----------------------------------|------------------------------------------|
| `GET`    | `/api/series`                     | List all series                          |
| `GET`    | `/api/series/paged`               | List series with pagination              |
| `GET`    | `/api/series/{id}`                | Get a series by ID                       |
| `GET`    | `/api/series/{id}/detail`         | Get series with full character & episode lists |
| `GET`    | `/api/series/type/{type}`         | Filter by type (`RIDER` or `SENTAI`)     |
| `GET`    | `/api/series/type/{type}/paged`   | Filter by type, paginated                |
| `GET`    | `/api/series/{id}/characters`     | Characters belonging to a series         |
| `GET`    | `/api/series/{id}/characters/summary` | Lightweight character summaries      |
| `GET`    | `/api/series/{id}/episodes`       | Episodes belonging to a series           |
| `GET`    | `/api/series/search`              | Search by name, type, year range         |
| `POST`   | `/api/series`                     | Create a new series                      |
| `PUT`    | `/api/series/{id}`                | Update a series                          |
| `DELETE` | `/api/series/{id}`                | Delete a series (cascades)               |

### Characters — `/api/characters`

| Method   | Path                                        | Description                              |
|----------|---------------------------------------------|------------------------------------------|
| `GET`    | `/api/characters`                           | List all characters                      |
| `GET`    | `/api/characters/{id}`                      | Get a character by ID                    |
| `GET`    | `/api/characters/series/{seriesId}`         | Characters for a series                  |
| `GET`    | `/api/characters/series/{seriesId}/role/{role}` | Filter by series and role            |
| `GET`    | `/api/characters/role/{role}`               | Filter by role                           |
| `GET`    | `/api/characters/{id}/forms`                | All forms for a character                |
| `GET`    | `/api/characters/{id}/forms/final`          | Final forms only                         |
| `GET`    | `/api/characters/search`                    | Search by name, role, color, series      |
| `POST`   | `/api/characters`                           | Create a character                       |
| `PUT`    | `/api/characters/{id}`                      | Update a character                       |
| `DELETE` | `/api/characters/{id}`                      | Delete a character (cascades to forms)   |

### Forms — `/api/forms`

| Method   | Path                                    | Description                    |
|----------|-----------------------------------------|--------------------------------|
| `GET`    | `/api/forms/{id}`                       | Get a form by ID               |
| `GET`    | `/api/forms/character/{characterId}`    | All forms for a character      |
| `GET`    | `/api/forms/character/{characterId}/final` | Final forms only            |
| `POST`   | `/api/forms`                            | Create a form                  |
| `PUT`    | `/api/forms/{id}`                       | Update a form                  |
| `DELETE` | `/api/forms/{id}`                       | Delete a form                  |

### Episodes — `/api/episodes`

| Method   | Path                                              | Description                          |
|----------|----------------------------------------------------|--------------------------------------|
| `GET`    | `/api/episodes/{id}`                               | Get an episode by ID                 |
| `GET`    | `/api/episodes/{id}/detail`                        | Episode with embedded series info    |
| `GET`    | `/api/episodes/series/{seriesId}`                  | All episodes for a series            |
| `GET`    | `/api/episodes/series/{seriesId}/number/{num}`     | Episode by series and episode number |
| `GET`    | `/api/episodes/search`                             | Search by series, title, date range  |
| `POST`   | `/api/episodes`                                    | Create an episode                    |
| `PUT`    | `/api/episodes/{id}`                               | Update an episode                    |
| `DELETE` | `/api/episodes/{id}`                               | Delete an episode                    |

### Global Search — `/api/search`

| Method | Path           | Description                                              |
|--------|----------------|----------------------------------------------------------|
| `GET`  | `/api/search?q=&limit=` | Cross-entity search across series, characters, and episodes |

### Wiki Sync (Admin) — `/api/admin/wiki`

Import data from Fandom wikis ([Kamen Rider Wiki](https://kamenrider.fandom.com), [RangerWiki](https://powerrangers.fandom.com)).

| Method | Path                                        | Description                              |
|--------|---------------------------------------------|------------------------------------------|
| `POST` | `/api/admin/wiki/sync/rider`               | Import Kamen Rider series                |
| `POST` | `/api/admin/wiki/sync/sentai`              | Import Super Sentai series               |
| `POST` | `/api/admin/wiki/sync/all`                 | Import both                              |
| `POST` | `/api/admin/wiki/sync/characters/{seriesId}` | Import characters for a specific series |

> Imported data is licensed under [CC-BY-SA 3.0](https://creativecommons.org/licenses/by-sa/3.0/) by the respective Fandom wiki communities.

## Prerequisites

- **Java 17+**
- **MySQL** running on `localhost:3306`

## Getting Started

1. **Create the database** (or let Hibernate auto-create it):

   ```sql
   CREATE DATABASE tokubase_db;
   ```

2. **Configure credentials** in `src/application.properties` (defaults to `root` / `Postgres1`).

3. **Build and run**:

   ```bash
   mvn spring-boot:run
   ```

4. **Explore the API** at:
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - OpenAPI spec: http://localhost:8080/api-docs

## Pagination

All list endpoints support Spring Data pagination via query parameters:

```
?page=0&size=20&sort=name,asc
```
