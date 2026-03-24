# TokuBase API Frontend Guide

This React app is an interactive companion for your Spring backend. It helps developers learn and test the API quickly with:

- Endpoint catalog grouped by feature area
- Pre-filled request samples based on backend DTOs
- Editable path/query/body inputs
- Live request execution and response preview
- Copyable curl command generation
- One-click database bootstrap from wiki admin sync endpoints

## Run locally

1. Start the Spring API first (default expected URL: http://localhost:8080).
2. Start this frontend:

```bash
cd api-guide
npm install
npm run dev
```

3. Open the app at http://localhost:5173.
4. In the UI, adjust API Base URL if your backend runs on another port/host.

## Build

```bash
cd api-guide
npm run build
npm run preview
```

## Notes

- Swagger shortcuts in the hero section link to `/swagger-ui.html` and `/api-docs` for the current base URL.
- Database Bootstrap in the hero section can populate data by calling:
	- `POST /api/admin/wiki/sync/all`
	- `POST /api/admin/wiki/sync/characters/{seriesId}` for each discovered series
- If requests fail, verify the backend is running and reachable from the browser.
