FROM node:18-alpine AS builder
WORKDIR /app

# Install pnpm globally
RUN npm install -g pnpm

# Copy package files first for caching
COPY package.json pnpm-lock.yaml* ./

# Install dependencies (pnpm)
RUN pnpm install --frozen-lockfile --prefer-offline

# Copy source
COPY . .

# Build production bundle
RUN pnpm run build

# Production image serving static files
FROM node:18-alpine AS runner
WORKDIR /app

# Install a tiny static server
RUN npm install -g serve

# Copy build from builder
COPY --from=builder /app/build ./build

EXPOSE 3000

# Serve the build folder on port 3000
CMD ["serve", "-s", "build", "-l", "3000"]
