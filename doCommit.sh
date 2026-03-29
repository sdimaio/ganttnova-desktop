#!/usr/bin/env bash

set -euo pipefail

# Resolve the repository root first so the script behaves consistently even if
# it is launched from a nested directory.
repo_root="$(git rev-parse --show-toplevel)"
cd "$repo_root"

# Keep GitHub transport configuration repository-local as well. The helper is
# intentionally opinionated for this repository, but the values remain
# overridable through environment variables when needed.
project_slug="${GANTTNOVA_GITHUB_SLUG:-sdimaio/ganttnova-desktop}"
origin_url="${GANTTNOVA_GITHUB_REMOTE:-git@github.com:${project_slug}.git}"
ssh_key_path="${GANTTNOVA_GITHUB_SSH_KEY:-$HOME/.ssh/id_ed25519_github_sdimaio}"
ssh_command="ssh -i ${ssh_key_path} -o IdentitiesOnly=yes"

if [[ ! -f "$ssh_key_path" ]]; then
    echo "Missing SSH key: ${ssh_key_path}" >&2
    exit 1
fi

# Keep the author identity local to this repository. A helper script must not
# rewrite the user's global Git identity because that would leak into unrelated
# worktrees.
git config --local user.name "sdimaio"
git config --local user.email "simmaco.dimaio@gmail.com"
git config --local core.sshCommand "$ssh_command"

if git remote get-url origin >/dev/null 2>&1; then
    git remote set-url origin "$origin_url"
else
    git remote add origin "$origin_url"
fi

current_branch="$(git branch --show-current)"
if [[ -z "$current_branch" ]]; then
    echo "Refusing to run on a detached HEAD." >&2
    exit 1
fi

# Accept either a quoted multi-word message or fall back to a timestamp. The
# timestamp default is only a last resort so the script remains usable during
# quick personal commits.
if [[ $# -eq 0 ]]; then
    now="$(date +"%Y-%m-%d %H:%M:%S")"
    commit_message="Update ${now}"
else
    commit_message="$*"
fi

git status --short
git add -A

if git diff --cached --quiet; then
    echo "Nothing to commit."
    exit 0
fi

# Fetch before pushing so we can fail early if the local branch is behind the
# remote one. This is safer than an implicit pull because it avoids surprise
# merges or rebases inside an automation helper.
git fetch origin --prune

if git show-ref --verify --quiet "refs/remotes/origin/${current_branch}"; then
    local_head="$(git rev-parse "${current_branch}")"
    remote_head="$(git rev-parse "origin/${current_branch}")"
    merge_base="$(git merge-base "${current_branch}" "origin/${current_branch}")"

    if [[ "$local_head" != "$remote_head" && "$local_head" == "$merge_base" ]]; then
        echo "Local branch is behind origin/${current_branch}. Pull or rebase first." >&2
        exit 1
    fi
fi

git commit -m "$commit_message"
git push -u origin "$current_branch"
git status --short
