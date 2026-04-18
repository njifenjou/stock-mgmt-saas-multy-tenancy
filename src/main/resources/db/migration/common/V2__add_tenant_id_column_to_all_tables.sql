-- categories
alter table public.categories
    add tenant_id varchar(255) not null;

comment on column public.categories.tenant_id is 'Tenant ID';

-- products

alter table public.products
    add tenant_id varchar(255) not null;

comment on column public.products.tenant_id is 'Tenant ID';

-- stocks_mvts

alter table public.stocks_mvts
    add tenant_id varchar(255) not null;

comment on column public.stocks_mvts.tenant_id is 'Tenant ID';


