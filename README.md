# Extract Certification PDF files

## Restore backup database with PDF collection

```sh
psql openchpl < chapel_pdf_schema
```

## Get data from backup and extract it in the chpl-data-model
```sh
$ psql -Upostgres -f export-resultset.sql openchpl
```
