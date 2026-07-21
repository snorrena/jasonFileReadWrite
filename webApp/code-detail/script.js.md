# script.js Summary

Client-side JavaScript for a form-based UI that talks to a Java Spark API for creating and listing users. Wrapped in an IIFE to avoid polluting the global scope.

## Event wiring

- Clicking `#submit_button` triggers `postFormData()`.
- Pressing `Enter` inside `#form1` prevents the default submit and also triggers `postFormData()`.

## Functions

### `postFormData()`
- Validates form input via `validFormData()`.
- If valid, builds a JSON payload from `#form1` using `FormData` → `Object.fromEntries` → `JSON.stringify`.
- POSTs the JSON string to `http://127.0.0.1:4567/post-user`.
- On response, clears the `#name_input` and `#email_input` fields and reloads the user list via `loadUserData()`.

### `validFormData()`
- Reads `#name_input` and `#email_input` values.
- Returns `true` only if name is non-empty and email passes `validateEmail()`.

### `validateEmail(email)`
- Tests the email against a permissive regex (`^[^\s@]+@[^\s@]+\.[^\s@]+$`) requiring a local part, an `@`, a domain, and a dot — designed to catch typos without rejecting unusual-but-valid addresses.

### `loadUserData()` (async)
- GETs `http://127.0.0.1:4567/get-users`.
- On a successful (`res.ok`) response, parses the JSON body and passes it to `printDataToDom()`.
- Logs a message if the fetch is not successful.
- Called once immediately when the script loads (to populate the list on page load) and again after every successful `postFormData()` call.

### `printDataToDom(data)`
- Takes the user array returned from the API.
- Uses `Array.reduce` to find the user with the highest `id` and pre-populates the `#id` input with `max.id + 1`, so the next entry gets an incremented id.
- Builds an HTML string of `<li>` elements (id, name, email) for every user and injects it into `#user_data_ul` via `innerHTML`.

## Notes

- API base URL (`http://127.0.0.1:4567`) is hardcoded in two places (`postFormData`, `loadUserData`).
- `printDataToDom` will throw if `userArray` is empty, since `Array.reduce` without an initial value fails on an empty array.