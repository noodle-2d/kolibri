alter type financial_asset_type add value 'option';
alter table financial_asset add column option_asset_id bigint;
