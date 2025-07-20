#!/bin/bash

# Post-test commit hook - backup commit after tests pass
# Simple fallback in case Claude didn't commit changes manually

if [ $? -eq 0 ]; then
    if git rev-parse --git-dir > /dev/null 2>&1; then
        # Check if there are any changes to commit
        if ! git diff --quiet || ! git diff --cached --quiet || [ -n "$(git ls-files --others --exclude-standard)" ]; then
            git add -A
            git commit --author="Claude <claude@pannous.com>" -m "chore: backup commit after tests passed"
            echo "✓ Backup commit created (Claude should have committed manually)"
        else
            echo "✓ No changes to commit after tests"
        fi
    else
        echo "✓ Not in a git repository, skipping post-test commit"
    fi
else
    echo "✗ Tests failed, skipping commit"
fi