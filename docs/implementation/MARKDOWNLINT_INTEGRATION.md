# MarkdownLint Integration Guide

## Overview

This project now includes MarkdownLint integration to ensure consistent, high-quality Markdown documentation.
MarkdownLint is a linter for Markdown files that checks for common formatting issues and enforces style
consistency across all documentation.

## Why MarkdownLint?

MarkdownLint helps maintain:

- **Consistency**: Enforces uniform formatting across all documentation
- **Readability**: Ensures documents follow best practices for clarity
- **Quality**: Catches common formatting mistakes before they're committed
- **Maintainability**: Makes documentation easier to update and collaborate on

## Configuration

The project uses a `.markdownlint.json` configuration file in the repository root with the following settings:

```json
{
  "default": true,
  "MD013": {
    "line_length": 120,
    "heading_line_length": 120,
    "code_block_line_length": 120,
    "code_blocks": true,
    "tables": true,
    "headings": true,
    "strict": false,
    "stern": false
  },
  "MD033": {
    "allowed_elements": ["a", "br", "img"]
  },
  "MD041": false
}
```

### Configuration Explanation

- **default: true** - Enables all default MarkdownLint rules
- **MD013** - Line length configuration:
  - Maximum line length: 120 characters (increased from default 80)
  - Applies to headings, code blocks, and tables
- **MD033** - Allows specific HTML elements (`<a>`, `<br>`, `<img>`)
- **MD041** - Disabled (allows documents without a first-line heading)

## How to Use MarkdownLint

### Prerequisites

MarkdownLint can be run using `npx` without installation:

```bash
npx markdownlint-cli --version
```

### Basic Commands

#### Check All Markdown Files

```bash
npx markdownlint-cli "**/*.md" --ignore node_modules
```

#### Check a Specific File

```bash
npx markdownlint-cli path/to/file.md
```

#### Auto-Fix Issues

Many MarkdownLint issues can be automatically fixed:

```bash
npx markdownlint-cli "**/*.md" --fix --ignore node_modules
```

#### Check Specific Directory

```bash
npx markdownlint-cli "docs/**/*.md"
```

## Common MarkdownLint Rules

Here are the most important rules enforced by MarkdownLint:

| Rule  | Description                                     | Example                              |
|-------|------------------------------------------------ |--------------------------------------|
| MD001 | Heading levels should only increment by one     | Don't skip from `#` to `###`         |
| MD012 | No multiple consecutive blank lines             | Max one blank line between content   |
| MD013 | Line length limit (120 chars)                   | Break long lines or use references   |
| MD022 | Headings should be surrounded by blank lines    | Add blank lines before/after heading |
| MD031 | Fenced code blocks need blank lines around them | Add blank lines before/after code    |
| MD032 | Lists should be surrounded by blank lines       | Add blank lines before/after lists   |
| MD040 | Fenced code blocks should have a language       | Use ` ```bash ` instead of ` ``` `   |
| MD060 | Table columns should have consistent spacing    | Add spaces around table pipes        |

## Workflow

When creating or modifying Markdown documentation:

1. **Create/Edit** - Write your Markdown content
2. **Auto-Fix** - Run `npx markdownlint-cli "**/*.md" --fix --ignore node_modules`
3. **Check** - Run `npx markdownlint-cli path/to/file.md` to verify
4. **Manual Fixes** - Address any remaining issues that couldn't be auto-fixed
5. **Verify** - Ensure no errors remain
6. **Commit** - Commit your changes

## Tips for Writing Markdown

### Code Blocks

Always specify the language for code blocks:

Good - language specified:

```bash
npm install
```

Bad - no language specified:

```text
npm install
```

### Line Length

Keep lines under 120 characters. For long URLs, use reference-style links:

```markdown
This is a [link to documentation][doc-link] that keeps lines short.

[doc-link]: https://very-long-url.example.com/path/to/documentation
```

### Blank Lines

Always add blank lines:

- Before and after headings
- Before and after lists
- Before and after code blocks
- Before and after tables

### Tables

Ensure consistent spacing around table pipes:

```markdown
| Column 1 | Column 2 | Column 3 |
|----------|----------|----------|
| Value 1  | Value 2  | Value 3  |
```

## Handling MarkdownLint Errors

### Common Issues and Solutions

#### MD013: Line Too Long

**Problem**: Line exceeds 120 characters

**Solutions**:

- Break the line into multiple lines
- Use reference-style links for long URLs
- For tables, abbreviate content or restructure
- For code blocks, this is acceptable (code is excluded from strict enforcement)

#### MD040: Missing Code Block Language

**Problem**: Fenced code block without language specification

**Solution**: Add language identifier:

````markdown
```bash
command here
```
````

#### MD031/MD032: Missing Blank Lines

**Problem**: Missing blank lines around code blocks or lists

**Solution**: Add blank lines before and after:

````markdown
This is text.

```bash
code here
```

More text.
````

## Integration with GitHub Copilot

The GitHub Copilot instructions (`.github/copilot-instructions.md`) have been updated to include MarkdownLint
requirements. When Copilot generates or modifies Markdown files, it should:

1. Follow MarkdownLint rules
2. Run MarkdownLint to check for errors
3. Fix any issues before committing
4. Include MarkdownLint verification in the workflow

## Future Enhancements

Potential improvements to consider:

- **CI/CD Integration**: Add MarkdownLint to GitHub Actions workflow
- **Pre-commit Hook**: Auto-run MarkdownLint before commits
- **VS Code Integration**: Configure MarkdownLint extension for real-time feedback
- **Custom Rules**: Add project-specific MarkdownLint rules as needed

## Resources

- [MarkdownLint Rules Documentation](https://github.com/DavidAnson/markdownlint/blob/main/doc/Rules.md)
- [MarkdownLint CLI](https://github.com/igorshubovych/markdownlint-cli)
- [Markdown Guide](https://www.markdownguide.org/)
- [GitHub Flavored Markdown Spec](https://github.github.com/gfm/)

## Troubleshooting

### MarkdownLint Not Found

If `npx markdownlint-cli` doesn't work, ensure you have Node.js and npm installed:

```bash
node --version
npm --version
```

### Too Many Errors

If you have many files with errors, fix them incrementally:

1. Start with auto-fix: `npx markdownlint-cli "**/*.md" --fix --ignore node_modules`
2. Fix one directory at a time: `npx markdownlint-cli "docs/api/*.md" --fix`
3. Address remaining issues manually

### False Positives

If MarkdownLint flags something that shouldn't be an error:

1. Check if the rule can be configured in `.markdownlint.json`
2. Add inline comments to disable specific rules for specific lines (use sparingly)
3. Update the configuration file if the rule doesn't fit the project

## Summary

MarkdownLint integration ensures consistent, high-quality documentation throughout the project. By following
the guidelines in this document and using the provided tools, you can maintain professional documentation
that is easy to read, maintain, and collaborate on.
