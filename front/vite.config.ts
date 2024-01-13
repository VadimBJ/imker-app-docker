import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    watch: {
      usePolling: true,
    },
    host: true,
    strictPort: true,
    port: 5173,
    open: false,
    proxy: {
      "/api": {
        target: "http://172.20.0.12:8080",
      },
    },
  },
  build: {
    outDir: "build",
    sourcemap: true,
  },
  base: "/imker",
});
