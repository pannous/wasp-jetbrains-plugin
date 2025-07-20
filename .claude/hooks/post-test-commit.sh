#!/bin/bash

# Post-test commit hook - commits changes after tests pass
# Only runs if the previous command (tests) succeeded

if [ $? -eq 0 ]; then
    if git rev-parse --git-dir > /dev/null 2>&1; then
        # Check if there are any changes to commit
        if ! git diff --quiet || ! git diff --cached --quiet || [ -n "$(git ls-files --others --exclude-standard)" ]; then
            git add -A
            git commit -m "Changes complete - tests passing - $(date '+%Y-%m-%d %H:%M:%S')"
            echo "✓ Changes committed after successful tests"
        else
            echo "✓ No changes to commit after tests"
        fi
    else
        echo "✓ Not in a git repository, skipping post-test commit"
    fi
else
    echo "✗ Tests failed, skipping commit"
fi