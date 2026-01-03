# Sample Export API

The Export API allows you to export sample data in various formats with optional compression.

## Endpoint

**POST** `/sample/export`

## Features

- Export samples in multiple formats: XLSX, CSV, XML, JSON
- Optional ZIP compression of exported files
- Datetime-prefixed filenames for easy identification
- Support for filtered exports using search criteria
- Automatic content-type headers and file downloads

## Request Format

```json
{
  "format": "xlsx|csv|xml|json",
  "zip": true|false,
  "searchRequest": {
    // Optional: Same as search API request to filter samples
    "page": 0,
    "pageSize": 100,
    "sortModel": [],
    "filterModel": {
      "items": [],
      "logicOperator": "and"
    }
  }
}
```

### Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `format` | string | Yes | Export format: `xlsx`, `csv`, `xml`, or `json` |
| `zip` | boolean | No | Whether to compress the file as ZIP (default: false) |
| `searchRequest` | object | No | Search criteria to filter samples (see SEARCH_API.md) |

## Response

The API returns a binary file with appropriate headers:

- **Content-Type**: Varies based on format and compression
  - `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` (XLSX)
  - `text/csv` (CSV)
  - `application/xml` (XML)
  - `application/json` (JSON)
  - `application/zip` (when compressed)
- **Content-Disposition**: `attachment; filename="{timestamp}_samples.{ext}"`

### Filename Format

All exported files follow this naming convention:
```
yyyyMMdd_HHmmss_samples.{format}
```

Examples:
- `20260103_145230_samples.xlsx`
- `20260103_145230_samples.csv`
- `20260103_145230_samples.zip` (when compressed)

## Export Formats

### XLSX (Excel)

Excel spreadsheet with formatted headers:
- Bold header row
- Auto-sized columns
- All sample fields included

### CSV

Comma-separated values file:
- Header row with field names
- Quoted values for proper escaping
- Compatible with Excel and other tools

### XML

XML document with structured data:
```xml
<SamplesWrapper>
  <samples>
    <samples>
      <id>1</id>
      <name>Sample1</name>
      <description>Description</description>
      <active>true</active>
      <createdAt>2026-01-03T08:53:01.409Z</createdAt>
      <createdBy>user</createdBy>
      <updatedAt>2026-01-03T08:53:01.409Z</updatedAt>
      <updatedBy>user</updatedBy>
    </samples>
  </samples>
</SamplesWrapper>
```

### JSON

Pretty-printed JSON array:
```json
[
  {
    "id": 1,
    "name": "Sample1",
    "description": "Description",
    "active": true,
    "createdAt": "2026-01-03T08:53:01.409Z",
    "createdBy": "user",
    "updatedAt": "2026-01-03T08:53:01.409Z",
    "updatedBy": "user"
  }
]
```

## Examples

### Export all samples as JSON

```bash
curl -X POST http://localhost:8080/sample/export \
  -H "Content-Type: application/json" \
  -d '{
    "format": "json",
    "zip": false
  }' \
  --output samples.json
```

### Export filtered samples as XLSX

```bash
curl -X POST http://localhost:8080/sample/export \
  -H "Content-Type: application/json" \
  -d '{
    "format": "xlsx",
    "zip": false,
    "searchRequest": {
      "page": 0,
      "pageSize": 1000,
      "filterModel": {
        "items": [
          {
            "field": "active",
            "operator": "equals",
            "value": true
          }
        ],
        "logicOperator": "and"
      }
    }
  }' \
  --output samples.xlsx
```

### Export as CSV with ZIP compression

```bash
curl -X POST http://localhost:8080/sample/export \
  -H "Content-Type: application/json" \
  -d '{
    "format": "csv",
    "zip": true
  }' \
  --output samples.zip
```

## Error Handling

### 400 Bad Request

Invalid format or validation errors:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Format must be one of: xlsx, csv, xml, json",
  "timestamp": "2026-01-03T08:53:01.409Z",
  "path": "/sample/export"
}
```

### 500 Internal Server Error

Export processing errors:

```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Failed to export samples: [error details]",
  "timestamp": "2026-01-03T08:53:01.409Z",
  "path": "/sample/export"
}
```

## Integration with Frontend

### JavaScript/TypeScript Example

```javascript
async function exportSamples(format, zip = false, filters = null) {
  const response = await fetch('/sample/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      format,
      zip,
      searchRequest: filters
    })
  });

  if (!response.ok) {
    throw new Error('Export failed');
  }

  // Get filename from Content-Disposition header
  const contentDisposition = response.headers.get('Content-Disposition');
  const filename = contentDisposition
    ?.split('filename=')[1]
    ?.replace(/"/g, '') || `samples.${format}`;

  // Create blob and download
  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  a.click();
  window.URL.revokeObjectURL(url);
}

// Usage
exportSamples('xlsx', false);
exportSamples('json', true);
exportSamples('csv', false, {
  page: 0,
  pageSize: 1000,
  filterModel: {
    items: [{ field: 'active', operator: 'equals', value: true }],
    logicOperator: 'and'
  }
});
```

## Notes

- Exports default to 10,000 records if no pageSize is specified in searchRequest
- Large exports are limited to maximum 100,000 records for memory safety
- If you need to export more than 100,000 records, use filtered exports with specific criteria
- Filtered exports use the same search criteria as the search API (see SEARCH_API.md)
- ZIP compression is useful for large exports or when transmitting over slow networks
- All exports include all sample fields: id, name, description, active, createdAt, createdBy, updatedAt, updatedBy
