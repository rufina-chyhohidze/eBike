#!/bin/bash

# function that takes in a string parameter and a block of code to execute, it prints the string, executes the command while printing dots, and shows if command was successful or not...
run_with_progress() {
  # Store the first argument as the description label
  local description="$1"
  shift  # Remove the description from the argument list

  # Temporary file to store any error output (stderr)
  local tmp_err="/tmp/error_log_$$.txt"

  # Print the description without a newline so we can add dots on the same line
  echo -n "$description"

  {
    # Run the provided command(s), suppressing stdout
    "$@" > /dev/null
  } 2> "$tmp_err" &  # Redirect stderr to a temp file, run in background

  local pid=$!  # Store the PID of the background process

  # Show a dot every second while the process is still running
  while kill -0 "$pid" 2>/dev/null; do
    printf '.'
    sleep 1
  done

  # Wait for the process to complete and capture the exit code
  wait $pid
  local exit_code=$?

  if [ $exit_code -ne 0 ]; then
    # If the command failed (non-zero exit), show ❌ and print the error
    echo " ❌ Failed"
    echo "Error:"
    cat "$tmp_err"  # Show the contents of the error log
    rm -f "$tmp_err"  # Clean up the temp file
    exit $exit_code  # Exit the script with the same failure code
  else
    # If the command succeeded, show ✅ and clean up
    echo " ✅ Done"
    rm -f "$tmp_err"
  fi
}