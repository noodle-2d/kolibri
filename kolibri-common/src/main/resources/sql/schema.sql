create type currency as enum (
    'usd',
    'eur',
    'rub',
    'cny',
    'gbr',
    'czk',
    'btc'
);

create type financial_asset_type as enum (
    'stock',
    'bond',
    'fund',
    'option'
);

create type account_type as enum (
    'cash',
    'deposit',
    'dept',
    'debit_card',
    'credit_card',
    'financial_asset',
    'broker',
    'crypto'
);

create type transaction_type as enum (
    'income',
    'expense',
    'transfer',
    'currency_conversion',
    'financial_asset_purchase',
    'financial_asset_sale'
);

create type external_transaction_category as enum (
    'active_income',
    'passive_income',
    'gift_income',
    'financial_expense',
    'non_financial_expense',
    'commission',
    'fine'
);

create table currency_price (
    id bigserial primary key,
    currency currency not null,
    to_currency currency not null,
    price numeric not null,
    date date not null
);

create table financial_asset (
    id bigserial primary key,
    name varchar(100) not null,
    company_name varchar(100) not null,
    type financial_asset_type not null,
    currency currency not null,
    option_asset_id bigint
);

create table financial_asset_price (
    id bigserial primary key,
    financial_asset_id bigint not null references financial_asset(id),
    price numeric not null,
    date date not null
);

create table account (
    id bigserial primary key,
    name varchar(200) not null,
    type account_type not null,
    currency currency,
    financial_asset_id bigint references financial_asset(id),
    create_date date not null,
    close_date date
);

create table transaction (
    id bigserial primary key,
    account_id bigint not null references account(id),
    type transaction_type not null,
    external_transaction_category external_transaction_category,
    amount numeric not null,
    date date not null,
    comment varchar(200),
    associated_transaction_id bigint,
    exact_financial_asset_price numeric,
    exact_sold_currency_ratio_part numeric,
    exact_bought_currency_ratio_part numeric
);

create table telegram_integration (
    last_update_id bigint not null
);
