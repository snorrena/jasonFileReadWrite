# JsonFileReadWrite.java Summary

A Spark-based Java web server that persists a list of `User` objects to a local `users.json` file, exposing endpoints to read and append users. Uses Gson for JSON (de)serialization and Lombok/`@Slf4j` for logging.

## Fields

- `userList` — `Collections.synchronizedList` wrapping an `ArrayList<User>`, the in-memory copy of all users.
- `gson` — shared `Gson` instance for all JSON conversion.
- `USER_JSON_FILE` — constant `"users.json"`, the on-disk data file.

## `main(String[] args)`

- Configures Spark's thread pool (`maxThreads=32`, `minThreads=2`, `timeOutMillis=30000`).
- Registers an `after` filter adding CORS headers (`Access-Control-Allow-Origin: *`, `Access-Control-Allow-Methods: GET, POST`).
- Routes:
  - `GET /get-users` → returns the contents of `getUsers()`.
  - `POST /post-user` → parses the request body into a `User` via Gson, appends it to `userList` (inside a `synchronized` block), writes the updated list to file, and echoes the new user back as JSON.
- If `users.json` does not already exist, seeds `userList` via `resetUserList()` and writes it to file.
- Reads `users.json` back from disk into `userList` using a `TypeToken<ArrayList<User>>` for proper generic deserialization; falls back to an empty synchronized list on `IOException` or if the parsed result is `null`.
- Logs every loaded user as JSON in a `forEach` loop.

## `writeUserListToFile()` (private)

- Serializes `userList` to JSON via Gson and writes it to `USER_JSON_FILE` using a `BufferedWriter`/`FileWriter`.
- Logs the pre-write list size; logs (but does not rethrow) any `IOException`.

## `getUsers()` (private)

- Reads `USER_JSON_FILE` as a string via `Files.readString`.
- Defaults to `"[]"` if the read fails, logging the `IOException`.
- Returns the raw JSON string (used directly as the `GET /get-users` response body).

## `resetUserList()` (private)

- Clears `userList` and repopulates it with four hardcoded `User` seed records (Scott, Tammy, Creighton, Mystery), each with an id, name, and email.

## `User` class (package-private, same file)

- Plain data class with `id` (int), `name` (String), `email` (String).
- Annotated with Lombok `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` for generated getters/setters/equals/hashCode/toString and both constructors.

## Notes

- No file locking around `users.json`: `writeUserListToFile()` and `getUsers()` both touch the file independently, and only in-memory mutations of `userList` are `synchronized` — concurrent read/write under load could race at the filesystem level.
- `getUsers()` reads straight from disk rather than serializing the in-memory `userList`, so it reflects the last successful write rather than any pending in-memory state.
- API base path and port are Spark defaults (not shown here); CORS is fully open (`*`), suitable for local/dev use only.