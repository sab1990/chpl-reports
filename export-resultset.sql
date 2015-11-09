--Connect to the database
\connect openchpl

DROP TABLE IF EXISTS openchpl.certified_product_pdf_store;

CREATE TABLE openchpl.certified_product_pdf_store
(
   certified_product_id bigint NOT NULL references openchpl.certified_product(certified_product_id),
   chpl_product_number character varying(250) NOT NULL,
   report_url character varying(255) NOT NULL,
   report_store bytea,
   date_added timestamp without time zone NOT NULL
) 
WITH (
  OIDS = TRUE
);

-- move data to the openchpl schemasl
insert into openchpl.certified_product_pdf_store (certified_product_id, chpl_product_number, report_url, report_store, date_added) (
	select c.certified_product_id, p.chapel_id, p.report_url, p.report_store, p.date_added
	from chapel_pdf.chapel_pdf_store p
	inner join openchpl.certified_product c on c.chpl_product_number = p.chapel_id
	left join openchpl.certified_product_pdf_store s on p.chapel_id = s.chpl_product_number
	where s.report_url <> p.report_url or s.report_url is null
);