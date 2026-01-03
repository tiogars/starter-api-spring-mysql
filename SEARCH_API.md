# Sample Search Service

## Overview

The Sample Search Service provides a comprehensive search API for Sample entities, fully compatible with MUI X DataGrid v7. It supports:

- **Pagination**: Control page number and page size
- **Sorting**: Sort by any Sample field in ascending or descending order
- **Filtering**: Filter by any Sample field with multiple operators
- **Field Support**: All Sample entity fields are searchable

## Endpoint

```
POST /sample/search
```

## Request Model

The request follows the MUI X DataGrid v7 structure:

```json
{
  "page": 0,
  "pageSize": 10,
  "sortModel": [
    {
      "field": "name",
      "sort": "asc"
    }
  ],
  "filterModel": {
    "items": [
      {
        "field": "name",
        "operator": "contains",
        "value": "test"
      }
    ],
    "logicOperator": "and"
  }
}
```

### Request Fields

- **page** (integer): Zero-based page number (default: 0)
- **pageSize** (integer): Number of items per page (default: 10)
- **sortModel** (array): List of sort criteria
  - **field** (string): Field name to sort by
  - **sort** (string): Sort direction ("asc" or "desc")
- **filterModel** (object): Filtering criteria
  - **items** (array): List of filter items
    - **field** (string): Field name to filter
    - **operator** (string): Filter operator
    - **value** (any): Filter value
  - **logicOperator** (string): "and" or "or" to combine filters

## Response Model

```json
{
  "rows": [
    {
      "id": 1,
      "name": "Sample 1",
      "description": "Description",
      "active": true,
      "createdAt": "2024-01-01T00:00:00.000Z",
      "createdBy": "user1",
      "updatedAt": "2024-01-01T00:00:00.000Z",
      "updatedBy": "user1"
    }
  ],
  "rowCount": 100
}
```

### Response Fields

- **rows** (array): List of Sample objects for the current page
- **rowCount** (number): Total number of rows matching the search criteria

## Searchable Fields

All Sample entity fields are searchable:

| Field | Type | Operators |
|-------|------|-----------|
| id | Long | =, !=, >, >=, <, <=, equals, not, greaterThan, greaterThanOrEqual, lessThan, lessThanOrEqual |
| name | String | contains, equals, startsWith, endsWith, isEmpty, isNotEmpty |
| description | String | contains, equals, startsWith, endsWith, isEmpty, isNotEmpty |
| active | Boolean | equals |
| createdAt | Date | is, not, after, before, onOrAfter, onOrBefore, =, !=, >, >=, <, <= |
| updatedAt | Date | is, not, after, before, onOrAfter, onOrBefore, =, !=, >, >=, <, <= |
| createdBy | String | contains, equals, startsWith, endsWith, isEmpty, isNotEmpty |
| updatedBy | String | contains, equals, startsWith, endsWith, isEmpty, isNotEmpty |

## Examples

### Example 1: Basic Pagination

Request all samples with pagination:

```json
{
  "page": 0,
  "pageSize": 25
}
```

### Example 2: Sorting

Sort by name ascending:

```json
{
  "page": 0,
  "pageSize": 10,
  "sortModel": [
    {
      "field": "name",
      "sort": "asc"
    }
  ]
}
```

Sort by multiple fields:

```json
{
  "page": 0,
  "pageSize": 10,
  "sortModel": [
    {
      "field": "active",
      "sort": "desc"
    },
    {
      "field": "name",
      "sort": "asc"
    }
  ]
}
```

### Example 3: Filtering

Filter by name containing "test":

```json
{
  "page": 0,
  "pageSize": 10,
  "filterModel": {
    "items": [
      {
        "field": "name",
        "operator": "contains",
        "value": "test"
      }
    ],
    "logicOperator": "and"
  }
}
```

Filter active samples:

```json
{
  "page": 0,
  "pageSize": 10,
  "filterModel": {
    "items": [
      {
        "field": "active",
        "operator": "equals",
        "value": true
      }
    ]
  }
}
```

Filter by ID range:

```json
{
  "page": 0,
  "pageSize": 10,
  "filterModel": {
    "items": [
      {
        "field": "id",
        "operator": ">=",
        "value": 10
      },
      {
        "field": "id",
        "operator": "<=",
        "value": 100
      }
    ],
    "logicOperator": "and"
  }
}
```

### Example 4: Multiple Filters with OR Logic

Find samples where name contains "test" OR description contains "example":

```json
{
  "page": 0,
  "pageSize": 10,
  "filterModel": {
    "items": [
      {
        "field": "name",
        "operator": "contains",
        "value": "test"
      },
      {
        "field": "description",
        "operator": "contains",
        "value": "example"
      }
    ],
    "logicOperator": "or"
  }
}
```

### Example 5: Complex Search

Search with sorting, filtering, and pagination:

```json
{
  "page": 1,
  "pageSize": 20,
  "sortModel": [
    {
      "field": "createdAt",
      "sort": "desc"
    }
  ],
  "filterModel": {
    "items": [
      {
        "field": "active",
        "operator": "equals",
        "value": true
      },
      {
        "field": "name",
        "operator": "contains",
        "value": "sample"
      }
    ],
    "logicOperator": "and"
  }
}
```

### Example 6: Date Filtering

Filter by creation date (timestamp in milliseconds):

```json
{
  "page": 0,
  "pageSize": 10,
  "filterModel": {
    "items": [
      {
        "field": "createdAt",
        "operator": "after",
        "value": 1704067200000
      }
    ]
  }
}
```

## Integration with MUI X DataGrid v7

This API is designed to work seamlessly with MUI X DataGrid v7. Here's a sample React component:

```typescript
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { useState } from 'react';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 90 },
  { field: 'name', headerName: 'Name', width: 150 },
  { field: 'description', headerName: 'Description', width: 200 },
  { field: 'active', headerName: 'Active', type: 'boolean', width: 100 },
  { field: 'createdBy', headerName: 'Created By', width: 150 },
  { field: 'createdAt', headerName: 'Created At', type: 'dateTime', width: 180 },
];

function SampleDataGrid() {
  const [rows, setRows] = useState([]);
  const [rowCount, setRowCount] = useState(0);
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 25,
  });
  const [sortModel, setSortModel] = useState([]);
  const [filterModel, setFilterModel] = useState({ items: [] });
  const [loading, setLoading] = useState(false);

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await fetch('/sample/search', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          page: paginationModel.page,
          pageSize: paginationModel.pageSize,
          sortModel,
          filterModel,
        }),
      });
      const data = await response.json();
      setRows(data.rows);
      setRowCount(data.rowCount);
    } finally {
      setLoading(false);
    }
  };

  // Fetch data when pagination, sort, or filter changes
  useEffect(() => {
    fetchData();
  }, [paginationModel, sortModel, filterModel]);

  return (
    <DataGrid
      rows={rows}
      columns={columns}
      rowCount={rowCount}
      loading={loading}
      pageSizeOptions={[10, 25, 50, 100]}
      paginationModel={paginationModel}
      paginationMode="server"
      onPaginationModelChange={setPaginationModel}
      sortingMode="server"
      sortModel={sortModel}
      onSortModelChange={setSortModel}
      filterMode="server"
      filterModel={filterModel}
      onFilterModelChange={setFilterModel}
    />
  );
}
```

## Notes

- The service uses case-insensitive string matching for string filters
- Date values should be provided as timestamps (milliseconds since epoch)
- Empty requests (no filters, no sorting) will return all results with default sorting by ID descending
- The `logicOperator` field defaults to "and" if not specified
- Invalid or unsupported field names/operators are silently ignored
