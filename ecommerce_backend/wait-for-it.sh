#!/bin/bash
set -e

HOST="$1"
PORT="$2"
shift 2
CMD="$@"

if [ -z "$HOST" ] || [ -z "$PORT" ]; then
  echo "Usage: $0 host port -- command"
  exit 2
fi

echo "Waiting for $HOST:$PORT ..."
until bash -c "cat < /dev/tcp/$HOST/$PORT" >/dev/null 2>&1; do
  sleep 1
done

echo "$HOST:$PORT is reachable — running command:"
echo "$CMD"
exec $CMD
